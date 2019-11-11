package ejb.session.stateless;

import entity.OutletEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.OutletNotFoundException;
//import util.exception.StaffNotFoundException;
//import util.exception.StaffUsernameExistException;
//import util.exception.UnknownPersistenceException;

@Stateless
@Remote(OutletEntitySessionBeanRemote.class)
@Local(OutletEntitySessionBeanLocal.class)
public class OutletEntitySessionBean implements OutletEntitySessionBeanRemote, OutletEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewOutlet(OutletEntity newOutletEntity)
    {
        em.persist(newOutletEntity);
        em.flush();

        return newOutletEntity.getOutletId();
        //return type is Long because the outletId will be used to initialise the EmployeeEntity
    }
    
    
    @Override
    public List<OutletEntity> retrieveAllOutlets()
    {
        Query query = em.createQuery("SELECT o FROM OutletEntity o");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public OutletEntity retrieveOutletEntityByOutletId(Long outletId) throws OutletNotFoundException
    {
        OutletEntity outletEntity = em.find(OutletEntity.class, outletId);
        
        if(outletEntity != null)
        {
            return outletEntity;
        }
        else
        {
            throw new OutletNotFoundException("Outlet not found");
        }
    }

}
