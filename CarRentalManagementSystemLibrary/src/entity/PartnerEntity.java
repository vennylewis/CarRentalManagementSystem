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
public class PartnerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(nullable = false, unique = true, length = 32)
    private String name;
    @Column(nullable = false, unique = true, length = 32)
    private String email;
    @Column(nullable = false, length = 32)
    private String password;

    //(mappedBy = "partnerEntity")
    @OneToMany
    private List<RentalReservationEntity> rentalReservationEntities;
    

    @OneToMany(mappedBy = "partnerEntity")
    private List<CustomerEntity> partnerCustomers;
    
    public PartnerEntity() {
        rentalReservationEntities = new ArrayList<>();
        partnerCustomers = new ArrayList<>();
    }

    public PartnerEntity(String name, String email, String password) {
        this();
        
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    
    

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof PartnerEntity)) {
            return false;
        }
        PartnerEntity other = (PartnerEntity) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PartnerEntity[ id=" + partnerId + " ]";
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<RentalReservationEntity> getRentalReservationEntities() {
        return rentalReservationEntities;
    }

    public void setRentalReservationEntities(List<RentalReservationEntity> rentalReservationEntities) {
        this.rentalReservationEntities = rentalReservationEntities;
    }

    public List<CustomerEntity> getPartnerCustomers() {
        return partnerCustomers;
    }

    public void setPartnerCustomers(List<CustomerEntity> partnerCustomers) {
        this.partnerCustomers = partnerCustomers;
    }


}
