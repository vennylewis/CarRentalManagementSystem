package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.StatusEnum;

@Entity
public class ModelEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;
    private String name;
    private StatusEnum modelStatus;
    
    @ManyToOne
    private CategoryEntity categoryEntity;
    
    @OneToOne(mappedBy = "modelEntity")
    private CarEntity carEntity;
    
    public ModelEntity() {
        this.modelStatus = StatusEnum.UNUSED; // unused by default
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StatusEnum getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(StatusEnum modelStatus) {
        this.modelStatus = modelStatus;
    }
    
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
    
    public CarEntity getCarEntity() {
        return carEntity;
    }

    public void setCarEntity(CarEntity carEntity) {
        this.carEntity = carEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelId != null ? modelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the modelId fields are not set
        if (!(object instanceof ModelEntity)) {
            return false;
        }
        ModelEntity other = (ModelEntity) object;
        if ((this.modelId == null && other.modelId != null) || (this.modelId != null && !this.modelId.equals(other.modelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ModelEntity[ id=" + modelId + " ]";
    }


 }
