package ejb.session.stateless;

import entity.CustomerEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;


@Stateless
@Remote(EmployeeEntitySessionBeanRemote.class)
@Local(EmployeeEntitySessionBeanLocal.class)
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    
    @Override
    public EmployeeEntity createEmployeeEntity(EmployeeEntity newEmployeeEntity, Long outletId) throws OutletNotFoundException {
//        em.persist(newEmployeeEntity);
//        em.flush();
//        
//        return newEmployeeEntity;
        
        try {
            OutletEntity outletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(outletId);
            newEmployeeEntity.setOutletEntity(outletEntity);
            outletEntity.getEmployeeEntities().add(newEmployeeEntity);
            

            em.persist(newEmployeeEntity);
            em.flush();

            return newEmployeeEntity;
        }
        catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet Not Found");
        }
    }
    
    @Override
    public List<EmployeeEntity> retrieveAllEmployees()
    {
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e");
        
        return query.getResultList();
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(Long employeeId) throws EmployeeNotFoundException {
        EmployeeEntity employeeEntity = em.find(EmployeeEntity.class, employeeId);
        employeeEntity.getTransitDriverDispatchRecordEntities().size();
        
        return employeeEntity;
    }

    @Override
    public EmployeeEntity login(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.username = :inUsername");
            query.setParameter("inUsername", username);
            EmployeeEntity employee = (EmployeeEntity)query.getSingleResult();

            if(employee.getUsername().equals(username) && employee.getPassword().equals(password))
            {
                return employee;
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
