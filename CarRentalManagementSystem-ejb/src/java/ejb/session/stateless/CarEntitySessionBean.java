package ejb.session.stateless;

import entity.CarEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.StatusEnum;
import util.exception.CarNotFoundException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;

@Stateless
@Local(CarEntitySessionBeanLocal.class)
@Remote(CarEntitySessionBeanRemote.class)

public class CarEntitySessionBean implements CarEntitySessionBeanRemote, CarEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @EJB
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;
    
    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    @Override
    public CarEntity createNewCarEntity(CarEntity newCarEntity, Long modelId, Long outletId) throws ModelNotFoundException, OutletNotFoundException {
        try {
            OutletEntity outletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(outletId);
            ModelEntity modelEntity = modelEntitySessionBeanLocal.retrieveModelEntityByModelId(modelId);
            if (modelEntity.getModelStatus() == StatusEnum.DISABLED) {
                throw new ModelNotFoundException("Model Not Found");
            } else {
                newCarEntity.setModelEntity(modelEntity);
                modelEntity.getCarEntities().add(newCarEntity);
                
                newCarEntity.setOutletEntity(outletEntity);
                outletEntity.getCarEntities().add(newCarEntity);
                
                em.persist(newCarEntity);
                em.flush();

                return newCarEntity;
            }
        } catch (OutletNotFoundException | ModelNotFoundException ex) {
            throw new ModelNotFoundException("Outlet and/or Model Not Found");
        }
    }

    @Override
    public List<CarEntity> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM CarEntity c");

        return query.getResultList();
    }

    @Override
    public CarEntity retrieveCarEntityByCarId(Long carId) throws CarNotFoundException {
        CarEntity carEntity = em.find(CarEntity.class, carId);
        if (carEntity != null) {
            return carEntity;
        } else {
            throw new CarNotFoundException("Car ID " + carId + " does not exist!");
        }
    }

    @Override
    public void updateCar(CarEntity carEntity) {
        em.merge(carEntity);
        em.flush();
    }

    @Override
    public void deleteCar(Long carId) throws CarNotFoundException {
        CarEntity carEntityToRemove = retrieveCarEntityByCarId(carId);
        carEntityToRemove.setModelEntity(null);
        carEntityToRemove.getModelEntity().getCarEntities().remove(carEntityToRemove);
        
        carEntityToRemove.setOutletEntity(null);
        if (carEntityToRemove.getOutletEntity() != null) {
            carEntityToRemove.getOutletEntity().getCarEntities().remove(carEntityToRemove);
        }

        if (carEntityToRemove.getCarStatus() == StatusEnum.USED) {
            carEntityToRemove.setCarStatus(StatusEnum.DISABLED);
        } else {
            em.remove(carEntityToRemove);
        }

        em.flush();
    }

}
