package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CategoryNameEnum;
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
    
    public List<CategoryEntity> retrieveAllCategories() {
        Query query = em.createQuery("SELECT c FROM CategoryEntity c");

        return query.getResultList();
    }
    
    @Override
    public CategoryEntity retrieveCategoryEntityByCategoryId(Long categoryId) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
        
        if (categoryEntity != null) {
            categoryEntity.getModelEntities().size();
            categoryEntity.getRentalRateEntities().size();
            categoryEntity.getRentalReservationEntities().size();
            return categoryEntity;
        } else {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }
    }
    
    @Override
    public CategoryNameEnum getCategoryNamebyCategoryId(Long categoryId) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
        
        if (categoryEntity != null) {
            return categoryEntity.getCategoryName();
        } else {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }
    }
   
}
