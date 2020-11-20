package eu.nimble.service.catalogue.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import eu.nimble.common.rest.identity.IdentityClientTypedMockConfig;
import eu.nimble.service.catalogue.model.category.Category;
import eu.nimble.utility.JsonSerializationUtility;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class Test02_ProductCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test1_getAvailableTaxonomies() throws Exception {
        MockHttpServletRequestBuilder request = get("/taxonomies/id")
                .header("Authorization", IdentityClientTypedMockConfig.sellerPersonID);
        MvcResult result = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = JsonSerializationUtility.getObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(result.getResponse().getContentAsString());
        ArrayNode taxonomies = mapper.readTree(parser);
        Assert.assertEquals(2, taxonomies.size());
    }

    @Test
    public void test2_getCategoriesByName() throws Exception {
        MockHttpServletRequestBuilder request = get("/taxonomies/eClass/categories").param("name", "die")
                .header("Authorization", IdentityClientTypedMockConfig.sellerPersonID);
        MvcResult result = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = JsonSerializationUtility.getObjectMapper();
        List<Category> categories = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Category>>() {});

        // check existence duplicate categories
        Set<String> categoryNames = new HashSet<>();
        categories.stream().forEach(category -> categoryNames.add(category.getId()));
        Assert.assertEquals("Duplicates results in the retrieved categories", categoryNames.size(), categories.size());
    }

    @Test
    public void test3_getCategoriesByName() throws Exception {
        // get logistic categories for warehouse
        MockHttpServletRequestBuilder request = get("/taxonomies/eClass/categories")
                .header("Authorization", IdentityClientTypedMockConfig.sellerPersonID)
                .param("name", "warehouse")
                .param("forLogistics","true");
        MvcResult result = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = JsonSerializationUtility.getObjectMapper();
        List<Category> categories = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Category>>() {});

        // check the categories size
        Assert.assertEquals(2,categories.size());

        // get logistic categories for mdf
        request = get("/taxonomies/eClass/categories")
                .header("Authorization", IdentityClientTypedMockConfig.sellerPersonID)
                .param("name", "mdf")
                .param("forLogistics","true");
        result = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();
        categories = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Category>>() {});

        // check the categories size
        Assert.assertEquals(0,categories.size());
    }

    @Test
    public void test4_getCategoriesByIds() throws Exception {
        MockHttpServletRequestBuilder request = get("/categories")
                .header("Authorization", IdentityClientTypedMockConfig.sellerPersonID)
                .param("categoryIds", "0173-1#01-BAA975#013")
                .param("taxonomyIds", "eClass");
        MvcResult result = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = JsonSerializationUtility.getObjectMapper();
        List<Category> categories = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Category>>() {});
        Assert.assertEquals("Die-cutter and stamping machines (post press)", categories.get(0).getPreferredName().get(0).getValue());
    }
}
