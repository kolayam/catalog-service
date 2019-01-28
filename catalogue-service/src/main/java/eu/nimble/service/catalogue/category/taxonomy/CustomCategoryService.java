package eu.nimble.service.catalogue.category.taxonomy;

import eu.nimble.service.catalogue.category.ProductCategoryService;
import eu.nimble.service.catalogue.model.category.Category;
import eu.nimble.service.catalogue.model.category.CategoryTreeResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suat on 03-Apr-18.
 */
@Component
public class CustomCategoryService implements ProductCategoryService {
    @Override
    public Category getCategory(String categoryId) {
        Category category = new Category();
        category.setTaxonomyId("Custom");
        category.setId(categoryId);
        category.setPreferredName(categoryId);
        category.setProperties(new ArrayList<>());
        return category;
    }

    @Override
    public List<Category> getProductCategories(String categoryName) {
        return new ArrayList<>();
    }

    @Override
    public List<Category> getProductCategories(String categoryName, boolean forLogistics) {
        return new ArrayList<>();
    }

    @Override
    public CategoryTreeResponse getCategoryTree(String categoryId) {
        return new CategoryTreeResponse();
    }

    @Override
    public List<Category> getParentCategories(String categoryId) {
        return new ArrayList<>();
    }

    @Override
    public List<Category> getRootCategories() {
        return new ArrayList<>();
    }

    @Override
    public List<Category> getChildrenCategories(String categoryId) {
        return new ArrayList<>();
    }
}