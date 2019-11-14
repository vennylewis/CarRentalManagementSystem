package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import util.exception.CategoryNotFoundException;


public interface CategoryEntitySessionBeanRemote {
    public CategoryEntity createCategoryEntity(CategoryEntity newCategoryEntity);
    public CategoryEntity retrieveCategoryEntityByCategoryId(Long categoryId) throws CategoryNotFoundException;
    public List<CategoryEntity> retrieveAllCategories();
}
