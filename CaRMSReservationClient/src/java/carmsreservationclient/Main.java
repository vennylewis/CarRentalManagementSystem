package carmsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalReservationEntitySessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB(name = "RentalReservationEntitySessionBeanRemote")
    private static RentalReservationEntitySessionBeanRemote rentalReservationEntitySessionBeanRemote;

    @EJB(name = "OutletEntitySessionBeanRemote")
    private static OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;

    @EJB(name = "ReservationSessionBeanRemote")
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    @EJB(name = "CustomerEntitySessionBeanRemote")
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerEntitySessionBeanRemote, reservationSessionBeanRemote, outletEntitySessionBeanRemote, rentalReservationEntitySessionBeanRemote);
        mainApp.runApp();
        
    }
    
}
