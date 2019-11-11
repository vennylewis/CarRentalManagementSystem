package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;

@Stateless
@Remote(CustomerEntitySessionBeanRemote.class)
@Local(CustomerEntitySessionBeanLocal.class)
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
 
    
    @Override
    public CustomerEntity createCustomerEntity(CustomerEntity newCustomerEntity) {
        em.persist(newCustomerEntity);
        em.flush();
        
        return newCustomerEntity;
    }
    
    @Override
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) {
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);
        
        return customerEntity;
    }
    
    @Override
    public CustomerEntity login(String name, String passportNumber) throws InvalidLoginCredentialException
    {
        try
        {
            Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.passportNumber = :inPassportNumber");
            query.setParameter("inPassportNumber", passportNumber);
            CustomerEntity customer = (CustomerEntity)query.getSingleResult();

            if(customer.getName().equals(name) && customer.getPassportNumber().equals(passportNumber))
            {
                return customer;
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
}
