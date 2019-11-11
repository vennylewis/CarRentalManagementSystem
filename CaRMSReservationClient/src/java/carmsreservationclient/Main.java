package carmsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static CustomerEntitySessionBeanRemote customerEntitySessionBean;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerEntitySessionBean);
        mainApp.runApp();
        
    }
    
}
