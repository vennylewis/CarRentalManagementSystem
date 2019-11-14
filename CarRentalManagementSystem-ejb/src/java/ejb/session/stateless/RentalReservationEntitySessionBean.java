/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.RentalReservationEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

@Stateless
@Remote(RentalReservationEntitySessionBeanRemote.class)
@Local(RentalReservationEntitySessionBeanLocal.class)
public class RentalReservationEntitySessionBean implements RentalReservationEntitySessionBeanRemote, RentalReservationEntitySessionBeanLocal {
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "CarEntitySessionBeanLocal")
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;
    
    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    
    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    
    @EJB(name = "ModelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    
    
    
    @Override
    public RentalReservationEntity createRentalReservationEntity(RentalReservationEntity newRentalReservationEntity, Long customerId, Long returnOutletId, Long pickupOutletId) throws OutletNotFoundException{
        try {
            CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerEntityByCustomerId(customerId);
            OutletEntity pickupOutletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(pickupOutletId);
            newRentalReservationEntity.setCustomerEntity(customerEntity);
            customerEntity.getRentalReservationEntities().add(newRentalReservationEntity);
            newRentalReservationEntity.setPickupOutletEntity(pickupOutletEntity);
            pickupOutletEntity.getPickupRentalReservationEntities().add(newRentalReservationEntity);
            
            OutletEntity returnOutletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(returnOutletId);
            newRentalReservationEntity.setReturnOutletEntity(returnOutletEntity);
            returnOutletEntity.getPickupRentalReservationEntities().add(newRentalReservationEntity);
            em.persist(newRentalReservationEntity);
            //trying to see whether this is how you should associate something
            em.merge(customerEntity);
            em.merge(pickupOutletEntity);
            em.merge(returnOutletEntity);
            em.flush();

            return newRentalReservationEntity;
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet Not Found");
        }
    }

    @Override
    public void setCategory(Long rentalReservationEntityId, Long categoryEntityId) {
        try {
            RentalReservationEntity rentalReservationEntity = retrieveRentalReservationEntityByRentalReservationId(rentalReservationEntityId);
            CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryEntityByCategoryId(categoryEntityId);
            rentalReservationEntity.setCategoryEntity(categoryEntity);
            categoryEntity.getRentalReservationEntities().add(rentalReservationEntity);
            em.merge(rentalReservationEntity);
            em.merge(categoryEntity);
            em.flush();
            
        } catch(CategoryNotFoundException | RentalReservationNotFoundException ex) {
            System.out.println("Error in setting category to rental reservation!");
        }
    }
    
    @Override
    public void setModel(Long rentalReservationEntityId, Long modelEntityId) {
        try {
            RentalReservationEntity rentalReservationEntity = retrieveRentalReservationEntityByRentalReservationId(rentalReservationEntityId);
            ModelEntity modelEntity = modelEntitySessionBeanLocal.retrieveModelEntityByModelId(modelEntityId);
            rentalReservationEntity.setModelEntity(modelEntity);
            modelEntity.getRentalReservationEntities().add(rentalReservationEntity);
            em.merge(rentalReservationEntity);
            em.merge(modelEntity);
            em.flush();
            
        } catch(ModelNotFoundException | RentalReservationNotFoundException ex) {
            System.out.println("Error in setting model to rental reservation!");
        }
    }
    
    @Override
    public List<RentalReservationEntity> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM RentalReservationEntity r");
        //call a .size() from associated class?
        return query.getResultList();
    }

    @Override
    public RentalReservationEntity retrieveRentalReservationEntityByRentalReservationId(Long rentalReservationId) throws RentalReservationNotFoundException {
        RentalReservationEntity rentalReservationEntity = em.find(RentalReservationEntity.class, rentalReservationId);
        rentalReservationEntity.getCategoryEntity();
        rentalReservationEntity.getModelEntity();
        if (rentalReservationEntity != null) {
            return rentalReservationEntity;
        } else {
            throw new RentalReservationNotFoundException("Rental Reservation ID " + rentalReservationId + " does not exist!");
        }
    }
    

    @Override
    public void allocateCar(Long rentalReservationId, Long carEntityId) throws RentalReservationNotFoundException, CarNotFoundException {
        try{
            RentalReservationEntity rentalReservationEntity = retrieveRentalReservationEntityByRentalReservationId(rentalReservationId);
            CarEntity carEntity = carEntitySessionBeanLocal.retrieveCarEntityByCarId(carEntityId);
            rentalReservationEntity.setCarEntity(carEntity);
            carEntity.setRentalReservationEntity(rentalReservationEntity);
        
            em.merge(rentalReservationEntity);
            em.flush();
        } catch(RentalReservationNotFoundException | CarNotFoundException ex) {
            System.out.println("Error in allocating car");
        }
    }
    
    public void updateRentalReservation(RentalReservationEntity rentalReservationEntity) {
        em.merge(rentalReservationEntity);
        em.flush();
    }
    
    @Override
    public void deleteRentalReservation(Long rentalReservationId) throws RentalReservationNotFoundException {
        RentalReservationEntity rentalReservationEntityToRemove = retrieveRentalReservationEntityByRentalReservationId(rentalReservationId);
        
        rentalReservationEntityToRemove.getCustomerEntity().getRentalReservationEntities().remove(rentalReservationEntityToRemove);
        rentalReservationEntityToRemove.getPickupOutletEntity().getPickupRentalReservationEntities().remove(rentalReservationEntityToRemove);
        rentalReservationEntityToRemove.getReturnOutletEntity().getReturnRentalReservationEntities().remove(rentalReservationEntityToRemove);
        
        if(rentalReservationEntityToRemove.getCategoryEntity() != null){
            rentalReservationEntityToRemove.getCategoryEntity().getRentalReservationEntities().remove(rentalReservationEntityToRemove);  
            rentalReservationEntityToRemove.setCategoryEntity(null);            
        }
        
        if(rentalReservationEntityToRemove.getModelEntity() != null){
            rentalReservationEntityToRemove.getModelEntity().getRentalReservationEntities().remove(rentalReservationEntityToRemove); 
            rentalReservationEntityToRemove.setModelEntity(null);
        }
        
        if(rentalReservationEntityToRemove.getCarEntity() != null){
            rentalReservationEntityToRemove.getCarEntity().setRentalReservationEntity(null);     
            rentalReservationEntityToRemove.setCarEntity(null);
        }
        
//        rentalReservationEntityToRemove.setPickupOutletEntity(null);
//        rentalReservationEntityToRemove.setReturnOutletEntity(null);
//        rentalReservationEntityToRemove.setCustomerEntity(null);
        em.remove(rentalReservationEntityToRemove);
        em.flush();
    }
}
