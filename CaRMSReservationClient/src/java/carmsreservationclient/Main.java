package carmsreservationclient;

import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalReservationEntitySessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB(name = "ModelEntitySessionBeanRemote")
    private static ModelEntitySessionBeanRemote modelEntitySessionBean;

    @EJB(name = "CategoryEntitySessionBeanRemote")
    private static CategoryEntitySessionBeanRemote categoryEntitySessionBean;

    @EJB(name = "RentalReservationEntitySessionBeanRemote")
    private static RentalReservationEntitySessionBeanRemote rentalReservationEntitySessionBeanRemote;

    @EJB(name = "OutletEntitySessionBeanRemote")
    private static OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    
    @EJB(name = "ReservationSessionBeanRemote")
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    @EJB(name = "CustomerEntitySessionBeanRemote")
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(categoryEntitySessionBean, customerEntitySessionBeanRemote, modelEntitySessionBean, reservationSessionBeanRemote, outletEntitySessionBeanRemote, rentalReservationEntitySessionBeanRemote);
        mainApp.runApp();
        
    }
    
}
