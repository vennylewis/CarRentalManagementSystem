package ejb.session.stateless;

import entity.CustomerEntity;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

public interface CustomerEntitySessionBeanRemote {
    
    public CustomerEntity createCustomerEntity(CustomerEntity newCustomerEntity);
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId);
    public CustomerEntity retrieveCustomerEntitybyPassportNumber(String passportNumber) throws CustomerNotFoundException;
    public CustomerEntity login(String name, String passportNumber) throws InvalidLoginCredentialException;
    
}
