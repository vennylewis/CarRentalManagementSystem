package carmsmanagementclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import javax.ejb.EJB;
import util.exception.CategoryNotFoundException;

public class Main {

    @EJB
    private static OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    @EJB
    private static RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    @EJB
    private static ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    @EJB
    private static CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    
    
    
    public static void main(String[] args){
        MainApp mainApp = new MainApp(employeeEntitySessionBeanRemote, rentalRateEntitySessionBeanRemote, modelEntitySessionBeanRemote, carEntitySessionBeanRemote, outletEntitySessionBeanRemote);
        mainApp.runApp();
    }
    
}
