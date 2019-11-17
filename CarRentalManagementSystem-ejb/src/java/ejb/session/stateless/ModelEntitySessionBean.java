package ejb.session.stateless;

import entity.CarEntity;
import entity.CategoryEntity;
import entity.ModelEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.StatusEnum;
import util.exception.CarNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.ModelNotFoundException;

@Stateless
@Local(ModelEntitySessionBeanLocal.class)
@Remote(ModelEntitySessionBeanRemote.class)

public class ModelEntitySessionBean implements ModelEntitySessionBeanRemote, ModelEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;

    @Override
    public ModelEntity createNewModelEntity(ModelEntity newModelEntity, Long categoryId) throws CategoryNotFoundException {
        try {
            CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryEntityByCategoryId(categoryId);
            newModelEntity.setCategoryEntity(categoryEntity);
            categoryEntity.getModelEntities().add(newModelEntity);

            em.persist(newModelEntity);
            em.flush();

            return newModelEntity;
        } catch (CategoryNotFoundException ex) {
            throw new CategoryNotFoundException("Category Not Found");
        }
    }

    @Override
    public List<ModelEntity> retrieveAllModels() {
        Query query = em.createQuery("SELECT m FROM ModelEntity m");

        return query.getResultList();
    }

    @Override
    public ModelEntity retrieveModelEntityByModelId(Long modelId) throws ModelNotFoundException {
        ModelEntity modelEntity = em.find(ModelEntity.class, modelId);
        if (modelEntity != null) {
            modelEntity.getRentalReservationEntities().size();
            modelEntity.getCarEntities().size();
            return modelEntity;
        } else {
            throw new ModelNotFoundException("Model ID " + modelId + " does not exist!");
        }
    }

    @Override
    public void updateModel(ModelEntity modelEntity) {
        em.merge(modelEntity);
        em.flush();
    }

    @Override
    public void deleteModel(Long modelId) throws ModelNotFoundException {
        ModelEntity modelEntityToRemove = retrieveModelEntityByModelId(modelId);
        modelEntityToRemove.setCategoryEntity(null);
        modelEntityToRemove.getCategoryEntity().getModelEntities().remove(modelEntityToRemove);

        // delete all cars of this model (or set disabled, if alr used)
        for (CarEntity carEntity : modelEntityToRemove.getCarEntities()) {
            try {
                carEntitySessionBeanLocal.deleteCar(carEntity.getCarId());
            } catch (CarNotFoundException ex) {
                System.out.println("Error deleting car ID " + carEntity.getCarId() + ": " + ex.getMessage());
            }
        }
        
        // delete this model (or set disabled, if alr used)
        if (modelEntityToRemove.getModelStatus() == StatusEnum.USED) {
            modelEntityToRemove.setModelStatus(StatusEnum.DISABLED);
        } else {
            em.remove(modelEntityToRemove);
        }
        
        em.flush();
    }
}
