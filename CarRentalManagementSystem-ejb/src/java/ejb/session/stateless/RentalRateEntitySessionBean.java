package ejb.session.stateless;

import entity.CategoryEntity;
import entity.RentalRateEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CategoryNotFoundException;
import util.exception.RentalRateNotFoundException;

@Stateless
@Local(RentalRateEntitySessionBeanLocal.class)
@Remote(RentalRateEntitySessionBeanRemote.class)

public class RentalRateEntitySessionBean implements RentalRateEntitySessionBeanRemote, RentalRateEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @Override
    public RentalRateEntity createRentalRateEntity(RentalRateEntity newRentalRateEntity, Long categoryId) throws CategoryNotFoundException {
        try {
            CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryEntityByCategoryId(categoryId);
            newRentalRateEntity.setCategoryEntity(categoryEntity);
            categoryEntity.getRentalRateEntities().add(newRentalRateEntity);
            
            em.persist(newRentalRateEntity);
            em.flush();
            
            return newRentalRateEntity;
        }
        catch (CategoryNotFoundException ex) {
            throw new CategoryNotFoundException("Category Not Found");
        }
    }
    
    @Override
    public List<RentalRateEntity> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT e FROM RentalRateEntity e");
        
        return query.getResultList();
    }
    
    @Override
    public RentalRateEntity retrieveRentalRateEntityByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, rentalRateId);
        
        if(rentalRateEntity != null) {
            return rentalRateEntity;
        }
        else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }     
    }
    
    // check update and delete methods are correct
    @Override
    public void updateRentalRate(RentalRateEntity rentalRateEntity) {
        em.merge(rentalRateEntity);
        em.flush();
    }
    
    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRateEntity rentalRateEntityToRemove = retrieveRentalRateEntityByRentalRateId(rentalRateId);
        em.remove(rentalRateEntityToRemove);
        em.flush();
    }
}
