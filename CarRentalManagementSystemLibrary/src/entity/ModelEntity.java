package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.StatusEnum;

@Entity
public class ModelEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;
    @Column(nullable = false, length = 32, unique = true)
    private String make;
    @Column(nullable = false, length = 32, unique = true)
    private String model;
    @Column(nullable = false)
    private StatusEnum modelStatus;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CategoryEntity categoryEntity;
    
    @OneToMany(mappedBy = "modelEntity")
    private List<CarEntity> carEntities;
    @OneToMany(mappedBy = "modelEntity")
    private List<RentalReservationEntity> rentalReservationEntities;
  
    public ModelEntity() {
        this.modelStatus = StatusEnum.UNUSED; // unused by default
        carEntities = new ArrayList<>();
        rentalReservationEntities = new ArrayList<>();
    }
    
    public ModelEntity(String make, String model) {
        this();
        
        this.make = make;
        this.model = model;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }
    
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
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
    
    public List<CarEntity> getCarEntities() {
        return carEntities;
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

    @XmlTransient
    public List<RentalReservationEntity> getRentalReservationEntities() {
        return rentalReservationEntities;
    }

    public void setRentalReservationEntities(List<RentalReservationEntity> rentalReservationEntities) {
        this.rentalReservationEntities = rentalReservationEntities;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


 }
