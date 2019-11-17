package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.RentalReservationEntity;
import entity.TransitDriverDispatchRecordEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

@Stateless
@Remote(TransitDriverDispatchRecordSessionBeanRemote.class)
@Local(TransitDriverDispatchRecordSessionBeanLocal.class)
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    @EJB(name = "EmployeeEntitySessionBeanLocal")
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    @EJB(name = "RentalReservationEntitySessionBeanLocal")
    private RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBeanLocal;

    @Override
    public TransitDriverDispatchRecordEntity createNewModelEntity(TransitDriverDispatchRecordEntity newTransitDriverDispatchRecordEntity, Long rentalReservationId, Long outletId) throws RentalReservationNotFoundException, OutletNotFoundException {
        try {
            OutletEntity outletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(outletId);
            RentalReservationEntity rentalReservationEntity = rentalReservationEntitySessionBeanLocal.retrieveRentalReservationEntityByRentalReservationId(rentalReservationId);
            newTransitDriverDispatchRecordEntity.setOutletEntity(outletEntity);
            newTransitDriverDispatchRecordEntity.setRentalReservationEntity(rentalReservationEntity);
            outletEntity.getTransitDriverDispatchRecordEntities().add(newTransitDriverDispatchRecordEntity);
            rentalReservationEntity.setTransitDriverDispatchRecordEntity(newTransitDriverDispatchRecordEntity);
            
            em.persist(newTransitDriverDispatchRecordEntity);
            em.flush();

            return newTransitDriverDispatchRecordEntity;
        } catch (RentalReservationNotFoundException | OutletNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation or Outlet Not Found");
        }
    }

    @Override
    public List<TransitDriverDispatchRecordEntity> retrieveAllTransitDriverDispatchRecordByOutlet(){
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecordEntity t");

        return query.getResultList();
    }

    @Override
    public TransitDriverDispatchRecordEntity retrieveTransitDriverDispatchRecordByTransitDriverRecordId(Long transitDriverDispatchRecordId) {
        TransitDriverDispatchRecordEntity transitDriverDispatchRecordEntity = em.find(TransitDriverDispatchRecordEntity.class, transitDriverDispatchRecordId);

        return transitDriverDispatchRecordEntity;

    }

    @Override
    public void updateTransitDriverDispatchRecord(TransitDriverDispatchRecordEntity transitDriverDisparchRecordEntity) {
        em.merge(transitDriverDisparchRecordEntity);
        em.flush();
    }

    @Override
    public void allocateEmployee(Long transitDriverDispatchRecordId, Long employeeId) throws EmployeeNotFoundException { 
        EmployeeEntity employeeEntity = employeeEntitySessionBeanLocal.retrieveEmployeeEntityByEmployeeId(employeeId);
        TransitDriverDispatchRecordEntity transitDriverDispatchRecordEntity = retrieveTransitDriverDispatchRecordByTransitDriverRecordId(transitDriverDispatchRecordId);
        employeeEntity.getTransitDriverDispatchRecordEntities().add(transitDriverDispatchRecordEntity);
        transitDriverDispatchRecordEntity.setEmployeeEntity(employeeEntity);
        
        em.merge(transitDriverDispatchRecordEntity);
        em.flush();
    }
    //If it cause error, just take the method out honestly
    @Override
    public List<TransitDriverDispatchRecordEntity> retrieveAllTransitDriverDispatchRecordEntityByOutletAndDate(Long outletId, Date date) {
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecordEntity t WHERE t.date = :inDate AND t.outletEntity = :inOutletEntity");
        try {
            query.setParameter("inDate", date);
            query.setParameter("inOutletEntity", outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(outletId));
            
        } catch(OutletNotFoundException ex) {
            ex.printStackTrace();
        }
        return query.getResultList();
    }
    

}
