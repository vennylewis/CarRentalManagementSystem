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
    private String address;
    @Column(nullable = false, length = 16)
    private String openingHours;
    
    //(mappedBy = "OutletEntity") together with @JoinColumn annotation will make sure that there is no extra outletentity_employeeentity table
    @OneToMany (mappedBy = "outletEntity")
    private List<EmployeeEntity> employeeEntities;

    public OutletEntity() {
        employeeEntities = new ArrayList<>();
    }

    public OutletEntity(String address, String openingHours) {
        this();
        
        this.address = address;
        this.openingHours = openingHours; 
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
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
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
}
