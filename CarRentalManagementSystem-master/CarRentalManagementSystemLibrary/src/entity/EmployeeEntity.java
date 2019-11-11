package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import util.enumeration.EmployeeTypeEnum;

@Entity
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false)
    private EmployeeTypeEnum employeeType;
    @Column(nullable = false, length = 64, unique = true)
    private String username;
    @Column(nullable = false, length = 32)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private OutletEntity outletEntity;
    
    public EmployeeEntity() {
    }

    public EmployeeEntity(String name, EmployeeTypeEnum employeeType, String username, String password) {
        this();
        
        this.name = name;
        this.employeeType = employeeType;
        this.username = username;
        this.password = password;
    }

    
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof EmployeeEntity)) {
            return false;
        }
        EmployeeEntity other = (EmployeeEntity) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EmployeeEntity[ id=" + employeeId + " ]";
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeTypeEnum getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeTypeEnum employeeType) {
        this.employeeType = employeeType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OutletEntity getOutletEntity() {
        return outletEntity;
    }

    public void setOutletEntity(OutletEntity outletEntity) {
        if(this.outletEntity != null)
        {
            this.outletEntity.getEmployeeEntities().remove(this);
        }
        
        this.outletEntity = outletEntity;
        
        if(this.outletEntity != null)
        {
            if(!this.outletEntity.getEmployeeEntities().contains(this))
            {
                this.outletEntity.getEmployeeEntities().add(this);
            }
        }
    }
}
