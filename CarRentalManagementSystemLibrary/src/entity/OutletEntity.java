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


@Entity
public class OutletEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 64)
    private String address;
    @Column(length = 5)
    private String openingHour;
    @Column(length = 5)
    private String closingHour;
    //do we need an address tho
    
    //(mappedBy = "OutletEntity") together with @JoinColumn annotation will make sure that there is no extra outletentity_employeeentity table
    @OneToMany (mappedBy = "outletEntity")
    private List<EmployeeEntity> employeeEntities;
    
    @OneToMany (mappedBy = "pickupOutletEntity")
    private List<RentalReservationEntity> pickupRentalReservationEntities;

    @OneToMany (mappedBy = "returnOutletEntity")
    private List<RentalReservationEntity> returnRentalReservationEntities;
    
    @OneToMany(mappedBy = "outletEntity")
    private List<CarEntity> carEntities;

    public OutletEntity() {
        employeeEntities = new ArrayList<>();
        carEntities = new ArrayList<> ();
    }

    public OutletEntity(String name, String address, String startHours, String endHours) {
        this();
        
        this.name = name;
        this.address = address;
        this.openingHour = startHours; 
        this.closingHour = endHours; 
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof OutletEntity)) {
            return false;
        }
        OutletEntity other = (OutletEntity) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OutletEntity[ id=" + outletId + " ]";
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }
    
    public List<EmployeeEntity> getEmployeeEntities() {
        return employeeEntities;
    }

    public void setEmployeeEntities(List<EmployeeEntity> employeeEntities) {
        this.employeeEntities = employeeEntities;
    }
    
    public void addEmployeeEntity(EmployeeEntity newEmployeeEntity)
    {
        if(!this.employeeEntities.contains(newEmployeeEntity))
        {
            this.employeeEntities.add(newEmployeeEntity);
        }
    }
    
    public void removeEmployeeEntity(EmployeeEntity employeeEntity)
    {
        if(this.employeeEntities.contains(employeeEntity))
        {
            this.employeeEntities.remove(employeeEntity);
        }
    }

    public List<RentalReservationEntity> getPickupRentalReservationEntities() {
        return pickupRentalReservationEntities;
    }

    public void setPickupRentalReservationEntities(List<RentalReservationEntity> pickupRentalReservationEntities) {
        this.pickupRentalReservationEntities = pickupRentalReservationEntities;
    }

    public List<RentalReservationEntity> getReturnRentalReservationEntities() {
        return returnRentalReservationEntities;
    }

    public void setReturnRentalReservationEntities(List<RentalReservationEntity> returnRentalReservationEntities) {
        this.returnRentalReservationEntities = returnRentalReservationEntities;
    }

    public List<CarEntity> getCarEntities() {
        return carEntities;
    }

    public void setCarEntities(List<CarEntity> carEntities) {
        this.carEntities = carEntities;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
