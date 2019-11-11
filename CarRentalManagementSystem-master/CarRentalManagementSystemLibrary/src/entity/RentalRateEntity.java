package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import util.enumeration.StatusEnum;


@Entity
public class RentalRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    private String name;
    private double ratePerDay;
    private Date validityStartDate;
    private Date validityEndDate;
    private StatusEnum rentalRateStatus;
    
    @ManyToOne
    private CategoryEntity categoryEntity;

    public RentalRateEntity() {
    }

    public RentalRateEntity(String name, double ratePerDay, Date validityStartDate, Date validityEndDate) {
        this();
        
        this.name = name;
        this.ratePerDay = ratePerDay;
        this.validityStartDate = validityStartDate;
        this.validityEndDate = validityEndDate;
        this.rentalRateStatus = StatusEnum.UNUSED; // unused by default
    }
    
    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public double getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public Date getValidityStartDate() {
        return validityStartDate;
    }

    public void setValidityStartDate(Date validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    public Date getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(Date validityEndDate) {
        this.validityEndDate = validityEndDate;
    }
    
    public StatusEnum getRentalRateStatus() {
        return rentalRateStatus;
    }

    public void setRentalRateStatus(StatusEnum rentalRateStatus) {
        this.rentalRateStatus = rentalRateStatus;
    }
    
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        if(this.categoryEntity != null)
        {
            this.categoryEntity.getRentalRateEntities().remove(this);
        }
        
        this.categoryEntity = categoryEntity;
        
        if(this.categoryEntity != null)
        {
            if(!this.categoryEntity.getRentalRateEntities().contains(this))
            {
                this.categoryEntity.getRentalRateEntities().add(this);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRateEntity)) {
            return false;
        }
        RentalRateEntity other = (RentalRateEntity) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRateEntity[ id=" + rentalRateId + " ]";
    }
    
    
}
