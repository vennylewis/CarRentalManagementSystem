package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import util.enumeration.CategoryNameEnum;

@Entity
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(nullable = false)
    private CategoryNameEnum categoryName;
    
    @OneToMany(mappedBy = "categoryEntity")
    private List<RentalRateEntity> rentalRateEntities;
    @OneToMany(mappedBy = "categoryEntity")
    private List<ModelEntity> modelEntities;
    @OneToMany(mappedBy = "categoryEntity")
    private List<RentalReservationEntity> rentalReservationEntities;
    
    public CategoryEntity() {
        rentalRateEntities = new ArrayList<> ();
        rentalReservationEntities = new ArrayList<>();
        modelEntities = new ArrayList<>();
    }

    public CategoryEntity(CategoryNameEnum categoryName) {
        this();
        
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public CategoryNameEnum getCategoryName() {
        return categoryName;
    }
    
    public List<RentalRateEntity> getRentalRateEntities() {
        return rentalRateEntities;
    }
    
    public List<ModelEntity> getModelEntities() {
        return modelEntities;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryId fields are not set
        if (!(object instanceof CategoryEntity)) {
            return false;
        }
        CategoryEntity other = (CategoryEntity) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CategoryEntity[ id=" + categoryId + " ]";
    }

    public List<RentalReservationEntity> getRentalReservationEntities() {
        return rentalReservationEntities;
    }

    public void setRentalReservationEntities(List<RentalReservationEntity> rentalReservationEntities) {
        this.rentalReservationEntities = rentalReservationEntities;
    }
    
}
