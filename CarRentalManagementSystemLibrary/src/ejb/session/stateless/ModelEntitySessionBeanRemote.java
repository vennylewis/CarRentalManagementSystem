package ejb.session.stateless;

import entity.CategoryEntity;
import entity.ModelEntity;
import java.util.List;
import util.exception.CategoryNotFoundException;
import util.exception.ModelNotFoundException;


public interface ModelEntitySessionBeanRemote {
    
    public ModelEntity createNewModelEntity(ModelEntity newModelEntity, Long categoryId) throws CategoryNotFoundException;
    public List<ModelEntity> retrieveAllModels();
    public ModelEntity retrieveModelEntityByModelId(Long modelId) throws ModelNotFoundException;
    public void updateModel(ModelEntity modelEntity);
    public void deleteModel(Long modelId) throws ModelNotFoundException;
    public Long getCategoryIdByModelId(Long modelId) throws ModelNotFoundException;
    
}
