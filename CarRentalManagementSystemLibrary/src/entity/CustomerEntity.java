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
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 64)
    private String email;
    @Column(nullable = false, length = 8)
    private Integer phoneNumber;
    @Column(nullable = false, length = 10, unique = true)
    private String passportNumber;
    //not sure why the same passportNumber can be persisted, even with unique = true
    //the same with the nullable = false

    @OneToMany (mappedBy = "customerEntity")
    private List<RentalReservationEntity> rentalReservationEntities;
    
    public CustomerEntity() {
        rentalReservationEntities = new ArrayList<>();
    }

    public CustomerEntity(String name, String email, Integer phoneNumber, String passportNumber) {
        this();
        
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
    }

    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + customerId + " ]";
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public List<RentalReservationEntity> getRentalReservationEntities() {
        return rentalReservationEntities;
    }

    public void setRentalReservationEntities(List<RentalReservationEntity> rentalReservationEntities) {
        this.rentalReservationEntities = rentalReservationEntities;
    }
    
}
