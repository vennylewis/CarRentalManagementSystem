package carmsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeTypeEnum;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
 
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    private EmployeeEntity currentEmployee;
    
    private SalesManagementModule salesManagementModule;
    private OperationsManagementModule operationsManagementModule;
    private CustomerServiceModule customerServiceModule;
    
    
    public MainApp() 
    {
        currentEmployee = null;
    }

    
    
    public MainApp(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote) 
    {
        this();

        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.rentalRateEntitySessionBeanRemote = rentalRateEntitySessionBeanRemote;
    }



    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to CaRMS Management Client for Car Reservation Management System ***\n");
            System.out.println("1: Employee Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {

                    if (currentEmployee == null) {
                        try
                        {
                            doLogin();
                            System.out.println();
                            System.out.println("Login " + currentEmployee.getName() + " successful as " + currentEmployee.getEmployeeType() + "!\n");                                                
                        }
                        catch(InvalidLoginCredentialException ex) 
                        {
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }
                    }
                    
                    if(currentEmployee != null) {
                        System.out.println("You are already logged in as " + currentEmployee.getEmployeeType()+ "!\n");
                        
                        if(currentEmployee.getEmployeeType().equals(EmployeeTypeEnum.SALESMANAGER)) {
                        salesManagementModule = new SalesManagementModule(currentEmployee, rentalRateEntitySessionBeanRemote);
                        salesManagementModule.menuSalesManagementModule();
                        }
                        else if(currentEmployee.getEmployeeType().equals(EmployeeTypeEnum.OPSMANAGER)) {
                            operationsManagementModule = new OperationsManagementModule(currentEmployee);
                            operationsManagementModule.menuOperationsManagementModule();
                        }
                        else if(currentEmployee.getEmployeeType().equals(EmployeeTypeEnum.CUSTOMERSERVICEEXEC)) {
                            customerServiceModule = new CustomerServiceModule(currentEmployee);
                            customerServiceModule.menuCustomerServiceModule();
                        }
                    }
                    
                    currentEmployee = null;
                }
                else if (response == 2) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 2) {
                break;
            }
        }
        
    }
    
    private void doLogin() throws InvalidLoginCredentialException 
    {
        
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** CaRMS Reservation System :: Login Customer ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployee = employeeEntitySessionBeanRemote.login(username, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
        
    }

}