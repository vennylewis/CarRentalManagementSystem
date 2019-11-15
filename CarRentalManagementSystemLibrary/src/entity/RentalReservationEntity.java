package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.PaymentStatusEnum;
import util.enumeration.StatusEnum;

@Entity
public class RentalReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalReservationId;
    @Column(nullable = false)
    private Date rentalStartTime;
    @Column(nullable = false)
    private Date rentalEndTime;
    @Column(nullable = false)
    private PaymentStatusEnum paymentStatus;
    @Column(length = 16)
    private Long ccNum;
    //@Column(precision = ? nullable = false)
    private Double amount;
            
//    @ManyToOne (optional = true)
//    @JoinColumn (nullable = true)
    @ManyToOne
    @JoinColumn
    private CategoryEntity categoryEntity;
    
    @ManyToOne
    @JoinColumn
    private ModelEntity modelEntity;
    
    @OneToOne
    private CarEntity carEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private OutletEntity returnOutletEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private OutletEntity pickupOutletEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customerEntity;
    
    //need to create more associations for partner, and transitDriverDispatchRecord
//    Decided to make this the unidirectional end to the association
//    @ManyToOne(optional = true)
//    @JoinColumn(nullable = true)
//    private PartnerEntity partnerEntity;
    
    
    public RentalReservationEntity() {
        this.paymentStatus = PaymentStatusEnum.DEFERRED; // deferred by default
        
    }

    public RentalReservationEntity(Date rentalStartTime, Date rentalEndTime, Long ccNum) {
        this();
        
        this.rentalStartTime = rentalStartTime;
        this.rentalEndTime = rentalEndTime;
        this.ccNum = ccNum;
    }

    public Long getRentalReservationId() {
        return rentalReservationId;
    }

    public void setRentalReservationId(Long rentalReservationId) {
        this.rentalReservationId = rentalReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalReservationId != null ? rentalReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalReservationId fields are not set
        if (!(object instanceof RentalReservationEntity)) {
            return false;
        }
        RentalReservationEntity other = (RentalReservationEntity) object;
        if ((this.rentalReservationId == null && other.rentalReservationId != null) || (this.rentalReservationId != null && !this.rentalReservationId.equals(other.rentalReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalReservationEntity[ id=" + rentalReservationId + " ]";
    }
    
    
    public Date getRentalStartTime() {
        return rentalStartTime;
    }

    public void setRentalStartTime(Date rentalStartTime) {
        this.rentalStartTime = rentalStartTime;
    }

    public Date getRentalEndTime() {
        return rentalEndTime;
    }

    public void setRentalEndTime(Date rentalEndTime) {
        this.rentalEndTime = rentalEndTime;
    }

    public PaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getCcNum() {
        return ccNum;
    }

    public void setCcNum(Long ccNum) {
        this.ccNum = ccNum;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public CarEntity getCarEntity() {
        return carEntity;
    }

    public void setCarEntity(CarEntity carEntity) {
        this.carEntity = carEntity;
    }

    public OutletEntity getReturnOutletEntity() {
        return returnOutletEntity;
    }

    public void setReturnOutletEntity(OutletEntity returnOutletEntity) {
        this.returnOutletEntity = returnOutletEntity;
    }

    public OutletEntity getPickupOutletEntity() {
        return pickupOutletEntity;
    }

    public void setPickupOutletEntity(OutletEntity pickupOutletEntity) {
        this.pickupOutletEntity = pickupOutletEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
    
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public void setModelEntity(ModelEntity modelEntity) {
        this.modelEntity = modelEntity;
    }

//    public PartnerEntity getPartnerEntity() {
//        return partnerEntity;
//    }
//
//    public void setPartnerEntity(PartnerEntity partnerEntity) {
//        this.partnerEntity = partnerEntity;
//    }

}
