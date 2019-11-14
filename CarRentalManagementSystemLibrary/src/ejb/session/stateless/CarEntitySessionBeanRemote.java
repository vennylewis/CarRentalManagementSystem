package ejb.session.stateless;

import entity.CarEntity;
import java.util.List;
import util.exception.CarNotFoundException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;


public interface CarEntitySessionBeanRemote {
    
    public CarEntity createNewCarEntity(CarEntity newCarEntity, Long modelId, Long outletId) throws ModelNotFoundException, OutletNotFoundException;
    public List<CarEntity> retrieveAllCars();
    public CarEntity retrieveCarEntityByCarId(Long carId) throws CarNotFoundException;
    public void updateCar(CarEntity carEntity);
    public void deleteCar(Long carId) throws CarNotFoundException;
    
}
