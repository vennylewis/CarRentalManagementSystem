package ejb.session.stateless;

import entity.CategoryEntity;
import entity.ModelEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        em.remove(modelEntityToRemove);
        em.flush();
    }

}
