/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Venny
 */
@Entity
public class TransitDriverDispatchRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverDispatchRecordId;
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private boolean completion; 
    @Column(nullable = false)
    private String outletToPickUp;
    
    @ManyToOne(optional = false)
    @JoinColumn (nullable = false)
    private OutletEntity outletEntity;
    
    @OneToOne
    private RentalReservationEntity rentalReservationEntity;
    
    @ManyToOne
    private EmployeeEntity employeeEntity;

    public TransitDriverDispatchRecordEntity() {
        completion = false;
        employeeEntity = null;
    }

    public TransitDriverDispatchRecordEntity(Date date, String outletToPickUp) {
        this();
        
        this.date = date;
        this.outletToPickUp = outletToPickUp;
    }
    
    

    public Long getTransitDriverDispatchRecordId() {
        return transitDriverDispatchRecordId;
    }

    public void setTransitDriverDispatchRecordId(Long transitDriverDispatchRecordId) {
        this.transitDriverDispatchRecordId = transitDriverDispatchRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchRecordId != null ? transitDriverDispatchRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverDispatchRecordId fields are not set
        if (!(object instanceof TransitDriverDispatchRecordEntity)) {
            return false;
        }
        TransitDriverDispatchRecordEntity other = (TransitDriverDispatchRecordEntity) object;
        if ((this.transitDriverDispatchRecordId == null && other.transitDriverDispatchRecordId != null) || (this.transitDriverDispatchRecordId != null && !this.transitDriverDispatchRecordId.equals(other.transitDriverDispatchRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatchRecordEntity[ id=" + transitDriverDispatchRecordId + " ]";
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCompletion() {
        return completion;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }
    
    public OutletEntity getOutletEntity() {
        return outletEntity;
    }

    public void setOutletEntity(OutletEntity outletEntity) {
        this.outletEntity = outletEntity;
    }

    public RentalReservationEntity getRentalReservationEntity() {
        return rentalReservationEntity;
    }

    public void setRentalReservationEntity(RentalReservationEntity rentalReservationEntity) {
        this.rentalReservationEntity = rentalReservationEntity;
    }

    public EmployeeEntity getEmployeeEntity() {
        return employeeEntity;
    }

    public void setEmployeeEntity(EmployeeEntity employeeEntity) {
        this.employeeEntity = employeeEntity;
    }

    public String getOutletToPickUp() {
        return outletToPickUp;
    }

    public void setOutletToPickUp(String outletToPickUp) {
        this.outletToPickUp = outletToPickUp;
    }
    
}
