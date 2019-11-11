package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.RentalStatusEnum;
import util.enumeration.StatusEnum;

@Entity
public class CarEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(nullable = false, length = 8, unique = true)
    private String licensePlateNo;
    @Column(nullable = false, length = 32)
    private String colour;
    @Column(nullable = false)
    private RentalStatusEnum rentalStatus;
    @Column(nullable = false)
    private StatusEnum carStatus;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private OutletEntity outletEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ModelEntity modelEntity;
 
    public CarEntity() {
        this.carStatus = StatusEnum.UNUSED; // unused by default
        this.rentalStatus = RentalStatusEnum.IN_OUTLET; // in outlet by default
    }

    public CarEntity(String licensePlateNo, String colour) {
        this();
        
        this.licensePlateNo = licensePlateNo;
        this.colour = colour;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
    
    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
    
    public RentalStatusEnum getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(RentalStatusEnum rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public StatusEnum getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(StatusEnum carStatus) {
        this.carStatus = carStatus;
    }
    
    public OutletEntity getOutletEntity() {
        return outletEntity;
    }

    public void setOutletEntity(OutletEntity outletEntity) {
        this.outletEntity = outletEntity;
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public void setModelEntity(ModelEntity modelEntity) {
        this.modelEntity = modelEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof CarEntity)) {
            return false;
        }
        CarEntity other = (CarEntity) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarEntity[ id=" + carId + " ]";
    }

}
