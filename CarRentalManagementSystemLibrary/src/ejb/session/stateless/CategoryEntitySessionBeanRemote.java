package ejb.session.stateless;

import entity.CategoryEntity;
import util.exception.CategoryNotFoundException;


public interface CategoryEntitySessionBeanRemote {

    public CategoryEntity createCategoryEntity(CategoryEntity newCategoryEntity);
    public CategoryEntity retrieveCategoryEntityByCategoryId(Long categoryId) throws CategoryNotFoundException;
}
