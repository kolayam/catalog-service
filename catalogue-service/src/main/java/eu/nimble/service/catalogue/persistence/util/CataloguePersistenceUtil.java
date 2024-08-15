package eu.nimble.service.catalogue.persistence.util;

import eu.nimble.service.catalogue.model.catalogue.CatalogueLineSortOptions;
import eu.nimble.service.catalogue.model.catalogue.CatalogueIDResponse;
import eu.nimble.service.catalogue.model.catalogue.CataloguePaginationResponse;
import eu.nimble.service.catalogue.model.catalogue.ProductStatus;
import eu.nimble.service.catalogue.util.SpringBridge;
import eu.nimble.service.model.ubl.catalogue.CatalogueType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.CatalogueLineType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.ClauseType;
import eu.nimble.utility.ExecutionContext;
import eu.nimble.utility.persistence.JPARepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by suat on 31-Dec-18.
 */
public class CataloguePersistenceUtil {
    public static final String CATALOGUE_SHOPPING_CART_ID = "SHOPPING_CART";

    private static final String QUERY_GET_ALL_CATALOGUES = "SELECT catalogue FROM CatalogueType catalogue";
    private static final String QUERY_GET_ALL_CATALOGUES_EXCEPT_CART = "SELECT catalogue FROM CatalogueType catalogue " +
            " WHERE catalogue.ID <> '" + CATALOGUE_SHOPPING_CART_ID + "'";
    private static final String QUERY_GET_ALL_CATALOGUES_FOR_PARTY = "SELECT catalogue FROM CatalogueType as catalogue "
            + " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification"
            + " WHERE partyIdentification.ID = :partyId";
    private static final String QUERY_GET_ALL_PRODUCT_CATALOGUES_FOR_PARTY = "SELECT catalogue FROM CatalogueType as catalogue "
            + " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification"
            + " WHERE partyIdentification.ID = :partyId"
            + " AND catalogue.ID <> '" + CATALOGUE_SHOPPING_CART_ID + "'";
    private static final String QUERY_GET_BY_UUID = "SELECT catalogue FROM CatalogueType catalogue WHERE catalogue.UUID = :uuid";
    private static final String QUERY_GET_FOR_PARTY = "SELECT catalogue FROM CatalogueType as catalogue "
            + " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification"
            + " WHERE catalogue.ID = :catalogueId"
            + " AND partyIdentification.ID = :partyId";
    private static final String QUERY_GET_CLAUSES_BY_UUID = "SELECT catalogue.clause FROM CatalogueType catalogue WHERE catalogue.UUID = :uuid";
    private static final String QUERY_CHECK_EXISTENCE_BY_ID = "SELECT COUNT(catalogue) FROM CatalogueType catalogue"
            + " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification"
            + " WHERE catalogue.ID = :catalogueId and partyIdentification.ID = :partyId";
    private static final String QUERY_CHECK_EXISTENCE_BY_UUID = "SELECT COUNT(catalogue) FROM CatalogueType catalogue"
            + " WHERE catalogue.UUID = :catalogueUuid";
    private static final String QUERY_GET_CATALOGUE_HJID = "SELECT catalogue.hjid FROM CatalogueType catalogue"
            + " WHERE catalogue.UUID = :catalogueUuid";
    private static final String QUERY_GET_CATALOGUE_IDLIST_FOR_PARTY = "SELECT catalogue.ID FROM CatalogueType as catalogue" +
            " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification" +
            " WHERE partyIdentification.ID = :partyId";
    private static final String QUERY_GET_CATALOGUE_ID_AND_NAME_LIST_FOR_PARTY = "SELECT catalogue.ID,catalogue.UUID FROM CatalogueType as catalogue" +
            " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification" +
            " WHERE partyIdentification.ID = :partyId";
    private static final String QUERY_GET_IDS_BY_UUIDS =
            "SELECT catalogue.UUID, catalogue.ID FROM CatalogueType catalogue" +
                    " WHERE catalogue.UUID in :uuids";
    private static final String QUERY_GET_CATALOGUE_LINES_BY_IDS =
            "SELECT catalogueLine FROM CatalogueType cat" +
                    " JOIN cat.catalogueLine catalogueLine" +
                    " JOIN cat.providerParty party" +
                    " JOIN party.partyIdentification partyId" +
                    " WHERE partyId.ID = :partyId AND catalogueLine.hjid in :catalogueLineHjids";
    private static final String QUERY_GET_COMMODITY_CLASSIFICATION_URIS_OF_CATALOGUE_LINES = "SELECT DISTINCT itemClassificationCode.URI FROM CatalogueType as catalogue " +
            " JOIN catalogue.catalogueLine catalogueLine JOIN catalogueLine.goodsItem.item.commodityClassification commodityClassification JOIN commodityClassification.itemClassificationCode itemClassificationCode " +
            " WHERE catalogue.UUID = :catalogueUuid AND itemClassificationCode.URI IS NOT NULL";
    private static final String QUERY_GET_CATALOGUE_LINE_HJIDS_FOR_PARTY = "SELECT catalogueLine.hjid FROM CatalogueType as catalogue "
            + " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification JOIN catalogue.catalogueLine catalogueLine"
            + " WHERE partyIdentification.ID = :partyId";
    private static final String QUERY_GET_COMMODITY_CLASSIFICATION_URIS_FOR_PARTY_CATALOGUES = "SELECT DISTINCT itemClassificationCode.URI FROM CatalogueType as catalogue " +
            " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification JOIN catalogue.catalogueLine catalogueLine " +
            " JOIN catalogueLine.goodsItem.item.commodityClassification commodityClassification JOIN commodityClassification.itemClassificationCode itemClassificationCode " +
            " WHERE partyIdentification.ID = :partyId AND itemClassificationCode.URI IS NOT NULL";
    private static final String QUERY_GET_CATALOGUE_LINE_HJIDS_WITH_CATEGORY_NAME_FOR_PARTY = "SELECT catalogueLine.hjid FROM CatalogueType as catalogue "
            + " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification JOIN catalogue.catalogueLine catalogueLine "
            + " JOIN catalogueLine.goodsItem.item.commodityClassification commodityClassification JOIN commodityClassification.itemClassificationCode itemClassificationCode "
            + " WHERE partyIdentification.ID = :partyId"
            + " AND itemClassificationCode.URI in :categoryUri";
    private static final String QUERY_GET_CATALOGUE_UUID_FOR_PARTY = "SELECT catalogue.UUID FROM CatalogueType as catalogue "
            + " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification"
            + " WHERE catalogue.ID = :catalogueId"
            + " AND partyIdentification.ID = :partyId";
    private static final String QUERY_GET_PROVIDER_PARTY_ID_FOR_CATALOGUE_UUID = "SELECT partyIdentification.ID FROM CatalogueType as catalogue "
            + " JOIN catalogue.providerParty as catalogue_provider_party JOIN catalogue_provider_party.partyIdentification partyIdentification"
            + " WHERE catalogue.UUID = :catalogueUuid";
    private static final String QUERY_IS_PRICE_HIDDEN_FOR_CATALOG = "SELECT count(catalogueLine) FROM CatalogueType catalogue join catalogue.catalogueLine catalogueLine WHERE catalogue.UUID = :uuid AND catalogueLine.priceHidden = true";
    private static final String QUERY_GET_PERMITTED_PARTIES_FOR_CATALOG = "SELECT permittedPartyIDs.item FROM CatalogueType catalogue join catalogue.permittedPartyIDItems permittedPartyIDs WHERE catalogue.UUID = :uuid";
    private static final String QUERY_GET_RESTRICTED_PARTIES_FOR_CATALOG = "SELECT restrictedPartyIDs.item FROM CatalogueType catalogue join catalogue.restrictedPartyIDItems restrictedPartyIDs WHERE catalogue.UUID = :uuid";
    // native queries
    private static final String QUERY_GET_CATALOGUE_LINE_HJIDS_WITH_CATEGORY_NAME_AND_SEARCH_TEXT_FOR_PARTY = "select catalogueLine.hjid from catalogue_type catalogue join party_type party on (catalogue.provider_party_catalogue_typ_0 = party.hjid)" +
            " join party_identification_type party_identification on (party_identification.party_identification_party_t_0 = party.hjid)" +
            " join catalogue_line_type catalogueLine on (catalogueLine.catalogue_line_catalogue_typ_0 = catalogue.hjid)" +
            " join goods_item_type goods_item on (catalogueLine.goods_item_catalogue_line_ty_0 = goods_item.hjid)" +
            " join item_type item_type on (goods_item.item_goods_item_type_hjid = item_type.hjid)" +
            " join text_type text_type on (text_type.name__item_type_hjid = item_type.hjid or text_type.description_item_type_hjid = item_type.hjid)" +
            " join commodity_classification_type commodity_classification on (commodity_classification.commodity_classification_ite_0 = item_type.hjid)" +
            " join code_type code_type on (code_type.hjid = commodity_classification.item_classification_code_com_0)" +
            " join item_location_quantity_type item_location on (catalogueLine.required_item_location_quant_1 = item_location.hjid)" +
            " join price_type price_type on (item_location.price_item_location_quantity_0 = price_type.hjid)" +
            " join amount_type amount_type on (price_type.price_amount_price_type_hjid = amount_type.hjid)" +
            " where party_identification.id = :partyId and code_type.uri = :categoryUri and text_type.language_id = :languageId and text_type.value_ in (" +
            " SELECT text_type.value_ FROM text_type, plainto_tsquery(:searchText) AS q WHERE (value_ @@ q)" +
            ")";

    private static final String QUERY_GET_CATALOGUE_LINE_HJIDS_WITH_SEARCH_TEXT_FOR_PARTY = "select catalogueLine.hjid from catalogue_type catalogue join party_type party on (catalogue.provider_party_catalogue_typ_0 = party.hjid)" +
            " join party_identification_type party_identification on (party_identification.party_identification_party_t_0 = party.hjid)" +
            " join catalogue_line_type catalogueLine on (catalogueLine.catalogue_line_catalogue_typ_0 = catalogue.hjid)" +
            " join goods_item_type goods_item on (catalogueLine.goods_item_catalogue_line_ty_0 = goods_item.hjid)" +
            " join item_type item_type on (goods_item.item_goods_item_type_hjid = item_type.hjid)" +
            " join text_type text_type on (text_type.name__item_type_hjid = item_type.hjid or text_type.description_item_type_hjid = item_type.hjid)" +
            " join item_location_quantity_type item_location on (catalogueLine.required_item_location_quant_1 = item_location.hjid)" +
            " join price_type price_type on (item_location.price_item_location_quantity_0 = price_type.hjid)" +
            " join amount_type amount_type on (price_type.price_amount_price_type_hjid = amount_type.hjid) " +
            " where party_identification.id = :partyId and text_type.language_id = :languageId and text_type.value_ in (" +
            " SELECT text_type.value_ FROM text_type, plainto_tsquery(:searchText) AS q WHERE (value_ @@ q)" +
            ")";

    /*
     Queries for lazy getters
     */
    public static final String QUERY_GET_CATALOGUE_WITH_LINES =
            "SELECT catalogue FROM CatalogueType catalogue" +
                    " LEFT JOIN FETCH catalogue.catalogueLine cl WHERE catalogue.UUID = :uuid";
    public static final String QUERY_GET_CATALOGUE_BY_PARTY_ID_CATALOGUE_ID_WITH_LINES = "SELECT catalogue FROM CatalogueType catalogue" +
            " LEFT JOIN FETCH catalogue.catalogueLine cl" +
            " JOIN catalogue.providerParty as catalogue_provider_party" +
            " JOIN catalogue_provider_party.partyIdentification partyIdentification" +
            " WHERE catalogue.ID = :catalogueId AND partyIdentification.ID = :partyId";

    public static List<CatalogueType> getAllCatalogues() {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_ALL_CATALOGUES);
    }

    /**
     * Gets all catalogues except shopping carts
     *
     * @return
     */
    public static List<CatalogueType> getAllProductCatalogues() {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_ALL_CATALOGUES_EXCEPT_CART);
    }

    public static Boolean isPriceHidden(String catalogueUuid) {
        long priceHidden = new JPARepositoryFactory().forCatalogueRepository(true).getSingleEntity(QUERY_IS_PRICE_HIDDEN_FOR_CATALOG, new String[]{"uuid"}, new Object[]{catalogueUuid});
        return priceHidden > 0;
    }

    public static List<String> getPermittedParties(String catalogueUuid) {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_PERMITTED_PARTIES_FOR_CATALOG, new String[]{"uuid"}, new Object[]{catalogueUuid});
    }

    public static List<String> getRestrictedParties(String catalogueUuid) {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_RESTRICTED_PARTIES_FOR_CATALOG, new String[]{"uuid"}, new Object[]{catalogueUuid});
    }

    public static List<CatalogueType> getAllCataloguesForParty(String partyId) {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_ALL_CATALOGUES_FOR_PARTY, new String[]{"partyId"}, new Object[]{partyId});
    }

    /**
     * Gets all catalogues except shopping carts for the given party
     *
     * @param partyId
     * @return
     */
    public static List<CatalogueType> getAllProductCataloguesForParty(String partyId) {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_ALL_PRODUCT_CATALOGUES_FOR_PARTY, new String[]{"partyId"}, new Object[]{partyId});
    }

    /**
     * Checks whether the user, whose data is encapsulated in the execution context, is allowed to access catalogue specified with the catalogue id and provider id.
     * */
    public static boolean checkCatalogueForWhiteBlackList(String catalogueId, String cataloguePartyId, ExecutionContext executionContext) {
        String catalogueUuid = getCatalogueUUid(catalogueId, cataloguePartyId);
        return checkCatalogueForWhiteBlackList(catalogueUuid, executionContext);
    }

    /**
     * Checks whether the user, whose data is encapsulated in the execution context, is allowed to access specified catalogue.
     * */
    public static boolean checkCatalogueForWhiteBlackList(String catalogueUuid, ExecutionContext executionContext) {
        // retrieve the catalogue provider id
        String catalogueProviderId = getCatalogueProviderId(catalogueUuid);
        System.out.println("catalogueProviderId:" + catalogueProviderId);
        System.out.println("executionContext:" + executionContext);
        // the users who own the catalogue can access it
        // skip this check for the anonymous users because they do not have any company id
        if(executionContext.getCompanyId() != null && catalogueProviderId.contentEquals(executionContext.getCompanyId())){
            return true;
        }
        // for the others, check the catalogue whitelist/blacklist for the given vat number
        String vatNumber = executionContext.getVatNumber();
        List<String> permittedParties = getPermittedParties(catalogueUuid);
        List<String> restrictedParties = getRestrictedParties(catalogueUuid);
        if (permittedParties.size() > 0) {
            if (vatNumber != null) {
                return permittedParties.contains(vatNumber);
            }
            return false;
        } else if (restrictedParties.size() > 0) {
            if (vatNumber != null) {
                return !restrictedParties.contains(vatNumber);
            }
        }
        return true;
    }

    public static CataloguePaginationResponse getCatalogueLinesForParty(String catalogueId, String partyId, String categoryUri,
                                                                        String searchText, String languageId, CatalogueLineSortOptions sortOption,
                                                                        ProductStatus productStatus,int limit, int offset) {

        String catalogueUuid = "";
        long size = 0;
        List<String> categoryUris = new ArrayList<>();
        List<CatalogueLineType> catalogueLines = new ArrayList<>();
        List<Long> catalogueLineHjids;
        String getCatalogueLinesQuery = null;
        QueryData queryData = null;
        if (catalogueId.equals("all")) {
            categoryUris = new JPARepositoryFactory().forCatalogueRepository().getEntities(QUERY_GET_COMMODITY_CLASSIFICATION_URIS_FOR_PARTY_CATALOGUES, new String[]{"partyId"}, new Object[]{partyId});
            // get the query
            queryData = getQuery(null, partyId, searchText, languageId, categoryUri, sortOption, null,productStatus);
        }
        else {
            // get catalogue uuid
            catalogueUuid = new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(QUERY_GET_CATALOGUE_UUID_FOR_PARTY, new String[]{"catalogueId", "partyId"}, new Object[]{catalogueId, partyId});

            if (catalogueUuid != null) {
                // get uris of the categories for all catalogue lines which the catalogue contains
                categoryUris = new JPARepositoryFactory().forCatalogueRepository().getEntities(QUERY_GET_COMMODITY_CLASSIFICATION_URIS_OF_CATALOGUE_LINES, new String[]{"catalogueUuid"}, new Object[]{catalogueUuid});
                // if limit is equal to 0,then no catalogue lines are returned
                // get the query
                queryData = getQuery(catalogueId, partyId, searchText, languageId, categoryUri, sortOption, catalogueUuid,productStatus);
            }
        }

        if (limit != 0) {
            // get all catalogue line ids
            // map repository response to Long to make sure that the line hjids have Long data type for both native and non-native queries
            catalogueLineHjids = new JPARepositoryFactory().forCatalogueRepository().getEntities(queryData.query, queryData.parameterNames.toArray(new String[0]), queryData.parameterValues.toArray(), null, null, queryData.isNativeQuery)
                    .stream().map(value -> Long.parseLong(String.valueOf(value))).collect(Collectors.toList());
            // set the size of catalogue lines
            size = catalogueLineHjids.size();
            // update catalogue line ids according to the offset
            if (offset < catalogueLineHjids.size()) {
                catalogueLineHjids = catalogueLineHjids.subList(offset, catalogueLineHjids.size());
            }
            // update catalogue line ids according to the limit
            if (catalogueLineHjids.size() > limit) {
                catalogueLineHjids = catalogueLineHjids.subList(0, limit);
            }

            // although we use the sort option to create the query in getQuery function
            // we also have to use this option to sort catalogue lines while getting them since the result can be something arbitrary without this option
            getCatalogueLinesQuery = QUERY_GET_CATALOGUE_LINES_BY_IDS;
            if (sortOption != null) {
                switch (sortOption) {
                    case PRICE_HIGH_TO_LOW:
                        getCatalogueLinesQuery += " ORDER BY catalogueLine.requiredItemLocationQuantity.price.priceAmount.value DESC NULLS LAST";
                        break;
                    case PRICE_LOW_TO_HIGH:
                        getCatalogueLinesQuery += " ORDER BY catalogueLine.requiredItemLocationQuantity.price.priceAmount.value ASC NULLS LAST";
                        break;
                }
            }

            if (catalogueLineHjids.size() != 0) {
                catalogueLines = new JPARepositoryFactory().forCatalogueRepository(true)
                        .getEntities(getCatalogueLinesQuery, new String[]{"partyId", "catalogueLineHjids"}, new Object[]{partyId, catalogueLineHjids});
            }
        }

        // created CataloguePaginationResponse
        CataloguePaginationResponse cataloguePaginationResponse = new CataloguePaginationResponse();
        cataloguePaginationResponse.setSize(size);
        cataloguePaginationResponse.setCatalogueLines(catalogueLines);
        cataloguePaginationResponse.setCatalogueUuid(catalogueUuid);
        cataloguePaginationResponse.setCategoryUris(categoryUris);
        cataloguePaginationResponse.setCatalogueId(catalogueId);
        if (catalogueUuid != null && !catalogueUuid.contentEquals("")) {
            cataloguePaginationResponse.setPermittedParties(getPermittedParties(catalogueUuid));
            cataloguePaginationResponse.setRestrictedParties(getRestrictedParties(catalogueUuid));
            cataloguePaginationResponse.setPriceHidden(isPriceHidden(catalogueUuid));
        }
        return cataloguePaginationResponse;
    }

    public static CatalogueType getCatalogueByUuid(String catalogueUuid) {
        return getCatalogueByUuid(catalogueUuid, false);
    }

    public static CatalogueType getCatalogueByUuid(String catalogueUuid, boolean byPassCache) {
        CatalogueType catalogue;
        if (byPassCache) {
            catalogue = new JPARepositoryFactory().forCatalogueRepository(true).getSingleEntity(QUERY_GET_BY_UUID, new String[]{"uuid"}, new Object[]{catalogueUuid});

        } else {
            // retrieve catalog from cache
            catalogue = (CatalogueType) SpringBridge.getInstance().getCacheHelper().getCatalog(catalogueUuid);
            // it does not exist in the cache, therefore, retrieve it from database and cache it
            if (catalogue == null) {
                catalogue = new JPARepositoryFactory().forCatalogueRepository(true).getSingleEntity(QUERY_GET_BY_UUID, new String[]{"uuid"}, new Object[]{catalogueUuid});
            }
        }
        return catalogue;
    }

    public static CatalogueType getCatalogueWithLinesInitialized(String catalogueUuid) {
        return new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(
                CataloguePersistenceUtil.QUERY_GET_CATALOGUE_WITH_LINES,
                new String[]{"uuid"},
                new Object[]{catalogueUuid});
    }

    public static CatalogueType getCatalogueForParty(String catalogueId, String partyId) {
        return getCatalogueForParty(catalogueId, partyId, true);
    }

    public static CatalogueType getCatalogueForParty(String catalogueId, String partyId, boolean lazyDisabled) {
        return new JPARepositoryFactory().forCatalogueRepository(lazyDisabled).getSingleEntity(QUERY_GET_FOR_PARTY, new String[]{"catalogueId", "partyId"}, new Object[]{catalogueId, partyId});
    }

    public static CatalogueType getCatalogueWithLinesInitialized(String partyId, String catalogueId) {
        return new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(
                CataloguePersistenceUtil.QUERY_GET_CATALOGUE_BY_PARTY_ID_CATALOGUE_ID_WITH_LINES,
                new String[]{"catalogueId", "partyId"},
                new Object[]{catalogueId, partyId});
    }

    public static String getCatalogueUUid(String catalogueId, String partyId) {
        return new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(QUERY_GET_CATALOGUE_UUID_FOR_PARTY, new String[]{"catalogueId", "partyId"}, new Object[]{catalogueId, partyId});
    }

    public static Long getCatalogueHjid(String catalogueUuid) {
        return new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(QUERY_GET_CATALOGUE_HJID, new String[]{"catalogueUuid"}, new Object[]{catalogueUuid});
    }

    public static String getCatalogueProviderId(String catalogueUuid) {
        return new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(QUERY_GET_PROVIDER_PARTY_ID_FOR_CATALOGUE_UUID, new String[]{"catalogueUuid"}, new Object[]{catalogueUuid});
    }

    public static List<CatalogueIDResponse> getCatalogueNames(List<String> uuids) {
        if (uuids == null || uuids.size() == 0) {
            return new ArrayList<>();
        } else {
            List<Object> dbResponse = new JPARepositoryFactory().forCatalogueRepository().getEntities(QUERY_GET_IDS_BY_UUIDS, new String[]{"uuids"}, new Object[]{uuids});
            List<CatalogueIDResponse> results = new ArrayList<>();
            for (Object resp : dbResponse) {
                Object[] respElements = (Object[]) resp;
                results.add(new CatalogueIDResponse((String) respElements[0], (String) respElements[1]));
            }
            return results;
        }
    }

    public static List<ClauseType> getClausesForCatalogue(String uuid) {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_CLAUSES_BY_UUID, new String[]{"uuid"}, new Object[]{uuid});
    }

    public static Boolean checkCatalogueExistenceById(String catalogueId, String partyId) {
        long catalogueExists = new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(QUERY_CHECK_EXISTENCE_BY_ID, new String[]{"catalogueId", "partyId"}, new Object[]{catalogueId, partyId});
        return catalogueExists == 1 ? true : false;
    }

    public static Boolean checkCatalogueExistenceByUuid(String catalogueUuid) {
        long catalogueExists = new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(QUERY_CHECK_EXISTENCE_BY_UUID, new String[]{"catalogueUuid"}, new Object[]{catalogueUuid});
        return catalogueExists == 1;
    }

    public static List<String> getCatalogueIdListsForParty(String partyId) {
        return new JPARepositoryFactory().forCatalogueRepository().getEntities(QUERY_GET_CATALOGUE_IDLIST_FOR_PARTY, new String[]{"partyId"}, new Object[]{partyId});
    }

    public static List<Object[]> getCatalogueIdAndNameListsForParty(String partyId) {
        return new JPARepositoryFactory().forCatalogueRepository().getEntities(QUERY_GET_CATALOGUE_ID_AND_NAME_LIST_FOR_PARTY, new String[]{"partyId"}, new Object[]{partyId});
    }

    private static QueryData getQuery(String catalogueId, String partyId, String searchText, String languageId, String categoryUri,
                                      CatalogueLineSortOptions sortOption, String catalogueUUID, ProductStatus productStatus) {
        QueryData queryData = new QueryData();
        String addQuery = "";
        if (catalogueId != null && catalogueUUID == null) {
            // catalogue id and party id are the common parameters for all queries
            queryData.parameterNames.add("catalogueId");
            queryData.parameterValues.add(catalogueId);
            addQuery = " AND catalogue.ID = :catalogueId";
        }

        if (catalogueUUID != null) {
            // catalogue uuid is the common parameters for all queries
            queryData.parameterNames.add("catalogueUUId");
            queryData.parameterValues.add(catalogueUUID);
            addQuery = " AND catalogue.UUID = :catalogueUUId";

        }

        queryData.parameterNames.add("partyId");
        queryData.parameterValues.add(partyId);

        // no category uri filtering and search text filtering
        if (categoryUri == null && searchText == null) {
            queryData.query = QUERY_GET_CATALOGUE_LINE_HJIDS_FOR_PARTY + addQuery;
            queryData.isNativeQuery = false;
        }
        // category uri filtering and search text filtering
        else if (categoryUri != null && searchText != null) {
            queryData.parameterNames.add("languageId");
            queryData.parameterValues.add(languageId);

            queryData.parameterNames.add("searchText");
            queryData.parameterValues.add(searchText);

            queryData.parameterNames.add("categoryUri");
            queryData.parameterValues.add(categoryUri);

            queryData.query = QUERY_GET_CATALOGUE_LINE_HJIDS_WITH_CATEGORY_NAME_AND_SEARCH_TEXT_FOR_PARTY + addQuery;
            queryData.isNativeQuery = true;
        }
        // category uri filtering
        else if (categoryUri != null) {
            queryData.parameterNames.add("categoryUri");
            queryData.parameterValues.add(categoryUri);

            queryData.query = QUERY_GET_CATALOGUE_LINE_HJIDS_WITH_CATEGORY_NAME_FOR_PARTY + addQuery;
            queryData.isNativeQuery = false;
        }
        // search text filtering
        else {
            queryData.parameterNames.add("languageId");
            queryData.parameterValues.add(languageId);

            queryData.parameterNames.add("searchText");
            queryData.parameterValues.add(searchText);

            queryData.query = QUERY_GET_CATALOGUE_LINE_HJIDS_WITH_SEARCH_TEXT_FOR_PARTY + addQuery;
            queryData.isNativeQuery = true;
        }

        if(productStatus != null){
            queryData.parameterNames.add("productStatus");
            queryData.parameterValues.add(productStatus.toString());
            queryData.query += String.format(" AND catalogueLine.%s = :productStatus",queryData.isNativeQuery ? "product_status_type" :"productStatusType");
        }

        if (sortOption != null) {
            if (queryData.isNativeQuery) {
                switch (sortOption) {
                    case PRICE_HIGH_TO_LOW:
                        queryData.query += " ORDER BY amount_type.value_ DESC NULLS LAST";
                        break;
                    case PRICE_LOW_TO_HIGH:
                        queryData.query += " ORDER BY amount_type.value_ ASC NULLS LAST";
                        break;
                }
            } else {
                switch (sortOption) {
                    case PRICE_HIGH_TO_LOW:
                        queryData.query += " ORDER BY catalogueLine.requiredItemLocationQuantity.price.priceAmount.value DESC NULLS LAST";
                        break;
                    case PRICE_LOW_TO_HIGH:
                        queryData.query += " ORDER BY catalogueLine.requiredItemLocationQuantity.price.priceAmount.value ASC NULLS LAST";
                        break;
                }
            }
        }

        return queryData;
    }

    private static class QueryData {
        private String query;
        private List<String> parameterNames = new ArrayList<>();
        private List<Object> parameterValues = new ArrayList<>();
        boolean isNativeQuery;
    }
}
