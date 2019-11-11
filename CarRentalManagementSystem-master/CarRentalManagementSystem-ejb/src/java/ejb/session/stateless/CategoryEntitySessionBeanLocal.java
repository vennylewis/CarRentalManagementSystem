package ejb.session.stateless;

import entity.CategoryEntity;
import util.exception.CategoryNotFoundException;


public interface CategoryEntitySessionBeanLocal {

    public CategoryEntity createCategoryEntity(CategoryEntity newCategoryEntity);
    public CategoryEntity retrieveCategoryEntityByCategoryId(Long categoryId) throws CategoryNotFoundException;
}
