/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;


public class CustomerServiceModule {
     
    private EmployeeEntity currentEmployee;
    
    public CustomerServiceModule() {
    }

    public CustomerServiceModule(EmployeeEntity currentEmployee) {
        this();
        
        this.currentEmployee = currentEmployee;
    }
    
    public void menuCustomerServiceModule() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management Client :: Customer Service Module ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Logout\n");
            //TODO: Do we want to put log out in each module, or attached to MainApp
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    System.out.println("Pickup Car\n");
                    
                }
                else if (response == 2) {
                    System.out.println("Return Car\n");

                }
                else if (response == 3) {
                    //Should do log out instead
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 3) {
                //should not be break but log out?
                break;
            }
        }
        
    }  
}
