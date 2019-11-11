package ejb.session.stateless;

import entity.CategoryEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CategoryNotFoundException;

@Stateless
@Local(CategoryEntitySessionBeanLocal.class)
@Remote(CategoryEntitySessionBeanRemote.class)
public class CategoryEntitySessionBean implements CategoryEntitySessionBeanRemote, CategoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

   @Override
    public CategoryEntity createCategoryEntity(CategoryEntity newCategoryEntity) {
        em.persist(newCategoryEntity);
        em.flush();
        
        return newCategoryEntity;
    }
    
    @Override
    public CategoryEntity retrieveCategoryEntityByCategoryId(Long categoryId) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
        
        if (categoryEntity != null) {
            return categoryEntity;
        } else {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }
    }
   
}
