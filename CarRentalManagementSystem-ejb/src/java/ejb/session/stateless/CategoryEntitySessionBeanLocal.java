package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import util.enumeration.CategoryNameEnum;
import util.exception.CategoryNotFoundException;


public interface CategoryEntitySessionBeanLocal {

    public CategoryEntity createCategoryEntity(CategoryEntity newCategoryEntity);
    public CategoryEntity retrieveCategoryEntityByCategoryId(Long categoryId) throws CategoryNotFoundException;
    public List<CategoryEntity> retrieveAllCategories();
    public CategoryNameEnum getCategoryNamebyCategoryId(Long categoryId) throws CategoryNotFoundException;
    
}
