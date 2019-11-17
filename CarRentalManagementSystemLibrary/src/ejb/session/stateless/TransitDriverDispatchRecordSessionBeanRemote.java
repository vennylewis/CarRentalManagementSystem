/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecordEntity;
import java.util.Date;
import java.util.List;
import util.exception.EmployeeNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

public interface TransitDriverDispatchRecordSessionBeanRemote {
    public List<TransitDriverDispatchRecordEntity> retrieveAllTransitDriverDispatchRecordEntityByOutletAndDate(Long outletId, Date date);

    public void allocateEmployee(Long transitDriverDispatchRecordId, Long employeeId) throws EmployeeNotFoundException;

    public void updateTransitDriverDispatchRecord(TransitDriverDispatchRecordEntity transitDriverDisparchRecordEntity);

    public TransitDriverDispatchRecordEntity retrieveTransitDriverDispatchRecordByTransitDriverRecordId(Long transitDriverDispatchRecordId);

    public List<TransitDriverDispatchRecordEntity> retrieveAllTransitDriverDispatchRecordByOutlet();

    public TransitDriverDispatchRecordEntity createNewModelEntity(TransitDriverDispatchRecordEntity newTransitDriverDispatchRecordEntity, Long rentalReservationId, Long outletId) throws RentalReservationNotFoundException, OutletNotFoundException;
     
}
