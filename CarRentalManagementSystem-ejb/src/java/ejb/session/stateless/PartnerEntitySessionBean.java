package ejb.session.stateless;

import entity.CustomerEntity;
import entity.PartnerEntity;
import entity.RentalReservationEntity;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.RentalReservationNotFoundException;

@Stateless
@Remote(PartnerEntitySessionBeanRemote.class)
@Local(PartnerEntitySessionBeanLocal.class)
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanLocal;

    @EJB(name = "RentalReservationEntitySessionBeanLocal")
    private RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBeanLocal;

    
    @Override
    public PartnerEntity createPartnerEntity(PartnerEntity newPartnerEntity) {
        em.persist(newPartnerEntity);
        em.flush();
        
        return newPartnerEntity;
    }
    
    @Override
    public PartnerEntity retrievePartnerEntityByPartnerId(Long partnerId) {
        PartnerEntity partnerEntity = em.find(PartnerEntity.class, partnerId);
//      unsure whether need to do lazy loading for web service 
        partnerEntity.getRentalReservationEntities().size();
        return partnerEntity;
    }
    
    @Override
    public PartnerEntity retrievePartnerEntitybyEmail(String email) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p from PartnerEntity p WHERE p.email = :inEmail");
        query.setParameter("inEmail", email);
    
        try {
            return (PartnerEntity)query.getSingleResult();
        }
        catch(NoResultException ex) {
            throw new PartnerNotFoundException("Partner with email " + email + " does not exist!");
        }
    }
//    
    @Override
    public PartnerEntity login(String email, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Query query = em.createQuery("SELECT p FROM PartnerEntity p WHERE p.email = :inEmail");
            query.setParameter("inEmail", email);
            PartnerEntity partner = (PartnerEntity)query.getSingleResult();
//            The above should be the same as the line below
//            PartnerEntity partner = retrievePartnerEntitybyEmail(email);
            
            if(partner.getEmail().equals(email) && partner.getPassword().equals(password))
            {
                return partner;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
        }
        catch(NoResultException ex)
        {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }        
    }
    
    @Override
    public void updatePartner(PartnerEntity partnerEntity) {
        em.merge(partnerEntity);
        em.flush();
    }
    
    @Override
    public void cancelReservation(Long partnerId, Long rentalReservationEntityId) {
        try {
            PartnerEntity partner = retrievePartnerEntityByPartnerId(partnerId);
            RentalReservationEntity rentalReservation = rentalReservationEntitySessionBeanLocal.retrieveRentalReservationEntityByRentalReservationId(rentalReservationEntityId);
            
            partner.getRentalReservationEntities().remove(rentalReservation);
            
            em.merge(partner);
            em.flush();
        } catch(RentalReservationNotFoundException ex) {
            System.out.println("Unable to find rental reservation");
        }
    }
    
    @Override
    public void reservePartnerCustomerReservation(Long partnerId, Long rentalReservationEntityId, Long customerId) {
        try {
            PartnerEntity partner = retrievePartnerEntityByPartnerId(partnerId);
            RentalReservationEntity rentalReservation = rentalReservationEntitySessionBeanLocal.retrieveRentalReservationEntityByRentalReservationId(rentalReservationEntityId);
            CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerEntityByCustomerId(customerId);

            partner.getPartnerCustomers().add(customer);
            customer.setPartnerEntity(partner);
            em.merge(partner);

//          decided to make rentalReservation and partner have a unidirectional relationship instead
//            rentalReservation.setPartnerEntity(partner);
            partner.getRentalReservationEntities().add(rentalReservation);
//            em.merge(rentalReservation);

            em.merge(partner);
            em.flush();
        } catch(RentalReservationNotFoundException ex) {
            System.out.println("Unable to find the rental reservation");
        }
    }
}
