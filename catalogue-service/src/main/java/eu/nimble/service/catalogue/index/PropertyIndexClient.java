package eu.nimble.service.catalogue.index;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.nimble.common.rest.indexing.IIndexingServiceClient;
import eu.nimble.service.catalogue.model.category.Property;
import eu.nimble.service.catalogue.util.CredentialsUtil;
import eu.nimble.service.catalogue.util.SpringBridge;
import eu.nimble.service.model.solr.SearchResult;
import eu.nimble.service.model.solr.owl.ClassType;
import eu.nimble.service.model.solr.owl.PropertyType;
import eu.nimble.utility.JsonSerializationUtility;
import feign.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by suat on 01-Feb-19.
 */
@Component
public class PropertyIndexClient {
    private static final Logger logger = LoggerFactory.getLogger(PropertyIndexClient.class);

    @Autowired
    private CredentialsUtil credentialsUtil;
    @Autowired
    IndexingClientController indexingClientController;

    public boolean indexProperty(Property property, Set<String> associatedCategoryUris) {
        try {
            String propertyJson;
            try {
                PropertyType indexProperty = IndexingWrapper.toIndexProperty(property, associatedCategoryUris);
                propertyJson = JsonSerializationUtility.getObjectMapper().writeValueAsString(indexProperty);

            } catch (Exception e) {
                String serializedProperty = JsonSerializationUtility.serializeEntitySilently(property);
                String msg = String.format("Failed to transform Property to PropertyType. \n property: %s", serializedProperty);
                logger.error(msg, e);
                return false;
            }

            List<IIndexingServiceClient> clients = indexingClientController.getClients();
            boolean indexComplete = false;
            for (IIndexingServiceClient client : clients) {
                Response response = client.setProperty(credentialsUtil.getBearerToken(),propertyJson);
                if (response.status() == HttpStatus.OK.value()) {
                    logger.info("Indexed property successfully. property uri: {}", property.getUri());
                    indexComplete =  true;

                } else {
                    String msg = String.format("Failed to index property. uri: %s, indexing call status: %d, message: %s", property.getUri(), response.status(), IOUtils.toString(response.body().asInputStream()));
                    logger.error(msg);
                    indexComplete = false;
                }
            }
            return indexComplete;

        } catch (Exception e) {
            String msg = String.format("Failed to index property. uri: %s", property.getUri());
            logger.error(msg, e);
            return false;
        }
    }

    public List<PropertyType> getProperties(Set<String> uris) {
        Response response;
        try {
            response = indexingClientController.getNimbleIndexClient().getProperties(credentialsUtil.getBearerToken(),uris,null);

            if (response.status() == HttpStatus.OK.value()) {
                List<PropertyType> properties = extractIndexPropertiesFromSearchResults(response, uris.toString());
                logger.debug("Retrieved properties for uris: {}", uris);
                return properties;

            } else {
                String msg = String.format("Failed to retrieve properties. uris: %s, indexing call status: %d, message: %s", uris, response.status(), IOUtils.toString(response.body().asInputStream()));
                logger.error(msg);
                throw new RuntimeException(msg);
            }

        } catch (Exception e) {
            String msg = String.format("Failed to retrieve properties for uris. uris: %s", uris);
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public List<PropertyType> getIndexPropertiesForCategory(String categoryUri) {
        return getIndexPropertiesForCategories(Arrays.asList(categoryUri));
    }

    public List<PropertyType> getIndexPropertiesForCategories(List<String> categoryUris) {
        try {
            Set<String> urisSet = new HashSet<>(categoryUris);
            Response response = indexingClientController.getNimbleIndexClient().getProperties(credentialsUtil.getBearerToken(),null,urisSet);

            if (response.status() == HttpStatus.OK.value()) {
                List<PropertyType> properties = extractIndexPropertiesFromSearchResults(response, categoryUris.toString());
                logger.debug("Retrieved properties for categories: {}", categoryUris);
                return properties;

            } else {
                String msg = String.format("Failed to retrieve properties for categories: %s, indexing call status: %d, message: %s", categoryUris, response.status(), IOUtils.toString(response.body().asInputStream()));
                logger.error(msg);
                throw new RuntimeException(msg);
            }

        } catch (Exception e) {
            String msg = String.format("Failed to retrieve properties for categories: %s", categoryUris);
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public Map<String, List<Property>> getIndexPropertiesForIndexCategories(List<ClassType> indexCategories) {
        Map<String, Collection<String>> categoryPropertyMap = new HashMap<>();
        // aggregate the properties to be fetched and keep the mapping between categories and properties
        for(ClassType indexCategory : indexCategories) {
            Collection<String> categoryProperties = indexCategory.getProperties();
            if(categoryProperties != null) {
                categoryPropertyMap.put(indexCategory.getUri(), categoryProperties);
            }
        }

        // fetch properties for bottom-most categories
        List<String> bottomMostCategories = getBottomMostCategories(indexCategories);
        List<PropertyType> indexProperties = getIndexPropertiesForCategories(bottomMostCategories);
        List<Property> properties = IndexingWrapper.toProperties(indexProperties);

        // put properties into a map for easy access
        Map<String, Property> propertyMap = new HashMap<>(); // property uri -> property map
        for(Property property : properties) {
            propertyMap.put(property.getUri(), property);
        }

        // populate the category->property map
        Map<String, List<Property>> categoryProperties = new HashMap<>();
        for(ClassType indexCategory : indexCategories) {
            String categoryUri = indexCategory.getUri();
            Collection<String> catPropCol = categoryPropertyMap.get(categoryUri);
            List<Property> catPropList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(catPropCol)) {
                for (String propUri : catPropCol) {
                    // skip the object properties that are not retrieved from the index
                    if(propertyMap.get(propUri) != null) {
                        catPropList.add(propertyMap.get(propUri));
                    }
                }
            }
            categoryProperties.put(categoryUri, catPropList);
        }
        return categoryProperties;
    }

    /**
     * Identifies only the bottom-most categories among the given list
     * @param indexCategories
     * @return
     */
    private List<String> getBottomMostCategories(List<ClassType> indexCategories) {
        List<String> remainingCategories = new ArrayList<>();
        for(ClassType category : indexCategories) {
            boolean isParent = false;
            for(ClassType category2 : indexCategories) {
                // if category2 is included in the children of the category, then, category is a parent
                if(category.getAllChildren() != null && category.getAllChildren().contains(category2.getUri())) {
                    isParent = true;
                    break;
                }
            }
            if(!isParent) {
                remainingCategories.add(category.getUri());
            }
        }
        return remainingCategories;
    }

    private List<PropertyType> extractIndexPropertiesFromSearchResults(Response response, String query) {
        ObjectMapper mapper = JsonSerializationUtility.getObjectMapper();
        SearchResult<PropertyType> searchResult;
        List<PropertyType> indexProperties;

        String responseBody;
        try {
            responseBody = IOUtils.toString(response.body().asInputStream());
        }
        catch (IOException e){
            String msg = String.format("Failed to get response body for query: %s", query);
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        try {
            System.out.println("extractIndexPropertiesFromSearchResults:"+responseBody);
            searchResult = mapper.readValue(responseBody, new TypeReference<SearchResult<PropertyType>>() {});
            indexProperties = searchResult.getResult();
            System.out.println("extractIndexPropertiesFromSearchResults-indexProperties:"+new ObjectMapper().writeValueAsString(indexProperties));
            // filter properties so that only datatype properties and properties that are visible (PropertyType.isVisible) on the UI are included
            indexProperties = indexProperties.stream()
                    .filter(indexProperty ->
                            (indexProperty.getPropertyType().contentEquals("DatatypeProperty") || indexProperty.getPropertyType().contentEquals("FunctionalProperty")) )
                    .collect(Collectors.toList());
            return indexProperties;

        } catch (IOException e) {
            String msg = String.format("Failed to parse SearchResult while getting properties. query: %s, serialized results: %s", query, responseBody);
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
