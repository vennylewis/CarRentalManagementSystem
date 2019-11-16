package ejb.session.ws;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalReservationEntitySessionBeanLocal;
import ejb.session.stateful.ReservationSessionBeanRemote;
import entity.RentalReservationEntity;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CustomerExistsException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

@WebService(serviceName = "CaRMSWebService")
@Stateless()
public class CaRMSWebService {

    @EJB
    private RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBean;
    
    //shouldn't the below use local interface as well?
    @EJB
    private static OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    @EJB
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    
    @WebMethod
    public RentalReservationEntity createRentalReservationEntity(RentalReservationEntity newRentalReservationEntity, Long customerId, Long returnOutletId, Long pickupOutletId) throws OutletNotFoundException {
        return rentalReservationEntitySessionBean.createRentalReservationEntity(newRentalReservationEntity, customerId, returnOutletId, pickupOutletId);
    }
}
