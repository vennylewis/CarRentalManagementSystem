package ejb.session.stateless;

import entity.CarEntity;
import entity.ModelEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarNotFoundException;
import util.exception.ModelNotFoundException;


@Stateless
@Local(CarEntitySessionBeanLocal.class)
@Remote(CarEntitySessionBeanRemote.class)

public class CarEntitySessionBean implements CarEntitySessionBeanRemote, CarEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @EJB
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;

    @Override
    public CarEntity createNewCarEntity(CarEntity newCarEntity, Long modelId) throws ModelNotFoundException {
        try {
            ModelEntity modelEntity = modelEntitySessionBeanLocal.retrieveModelEntityByModelId(modelId);
            newCarEntity.setModelEntity(modelEntity);
            modelEntity.setCarEntity(newCarEntity);

            em.persist(newCarEntity);
            em.flush();

            return newCarEntity;
        } catch (ModelNotFoundException ex) {
            throw new ModelNotFoundException("Model Not Found");
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
        em.remove(carEntityToRemove);
        em.flush();
    }

    
}
