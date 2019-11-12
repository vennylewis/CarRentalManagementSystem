/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalReservationEntity;
import java.util.List;
import util.exception.CarNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;


public interface RentalReservationEntitySessionBeanLocal {

    public RentalReservationEntity createRentalReservationEntity(RentalReservationEntity newRentalReservationEntity, Long customerId, Long returnOutletId, Long pickupOutletId) throws OutletNotFoundException;

    public void deleteRentalReservation(Long rentalReservationId) throws RentalReservationNotFoundException;

    public void allocateCar(Long rentalReservationId, Long carEntityId) throws RentalReservationNotFoundException, CarNotFoundException;

    public RentalReservationEntity retrieveRentalReservationEntityByRentalReservationId(Long rentalReservationId) throws RentalReservationNotFoundException;

    public List<RentalReservationEntity> retrieveAllReservations();
    
}
