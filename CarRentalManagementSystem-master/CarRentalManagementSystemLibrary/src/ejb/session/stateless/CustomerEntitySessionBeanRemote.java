package ejb.session.stateless;

import entity.CustomerEntity;
import util.exception.InvalidLoginCredentialException;

public interface CustomerEntitySessionBeanRemote {
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId);

    public CustomerEntity createCustomerEntity(CustomerEntity newCustomerEntity);
    
    public CustomerEntity login(String name, String passportNumber) throws InvalidLoginCredentialException;
}
