package carmsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import javax.ejb.EJB;
import util.exception.CategoryNotFoundException;

public class Main {

    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    @EJB
    private static RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    
    public static void main(String[] args){
        MainApp mainApp = new MainApp(employeeEntitySessionBeanRemote, rentalRateEntitySessionBeanRemote);
        mainApp.runApp();
    }
    
}
