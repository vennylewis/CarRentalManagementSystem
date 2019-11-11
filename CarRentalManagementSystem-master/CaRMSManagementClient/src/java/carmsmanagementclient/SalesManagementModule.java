package carmsmanagementclient;

import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import entity.EmployeeEntity;
import entity.RentalRateEntity;
import java.util.Date;
import java.util.Scanner;
import util.enumeration.EmployeeTypeEnum;
import util.exception.CategoryNotFoundException;
import util.exception.InvalidLoginCredentialException;

public class SalesManagementModule {

    private EmployeeEntity currentEmployee;
    private RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    
    public SalesManagementModule() {
    }

    public SalesManagementModule(EmployeeEntity currentEmployee, RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote) {
        this();
        
        this.currentEmployee = currentEmployee;
        this.rentalRateEntitySessionBeanRemote = rentalRateEntitySessionBeanRemote;
    }
    
    public void menuSalesManagementModule() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management Client :: Sales Management Module ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("6: Logout\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    System.out.println("Create Rental Rate\n");
                    doCreateRentalRate();
                }
                else if (response == 2) {
                    System.out.println("View All Rental Rates\n");
                    // doViewAllRentalRates();
                }
                else if (response == 3) {
                    System.out.println("View Rental Rate Details\n");

                }
                else if (response == 4) {
                    System.out.println("Update Rental Rate\n");

                }
                else if (response == 5) {
                    System.out.println("Delete Rental Rate\n");

                }
                else if (response == 6) {
                    // do logout
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 6) {
                //should not be break but log out?
                break;
            }
        }
        
    }

    private void doCreateRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: Create Rental Rate ***\n");
        String name = "Rental Rate 1";
        double ratePerDay = 10.0;
        Date validityStartDate = new Date(2019, 10, 25);
        Date validityEndDate = new Date(2019, 10, 31);
        RentalRateEntity newRentalRateEntity = new RentalRateEntity(name, ratePerDay, validityStartDate, validityEndDate);
        long categoryId = 1;
        try {
            rentalRateEntitySessionBeanRemote.createRentalRateEntity(newRentalRateEntity, categoryId);
        } catch (CategoryNotFoundException ex) {
            System.out.println("Invalid category id: " + ex.getMessage() + "\n");
        }
    }
}
