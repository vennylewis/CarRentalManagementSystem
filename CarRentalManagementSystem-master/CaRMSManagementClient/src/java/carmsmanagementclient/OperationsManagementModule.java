/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;

/**
 *
 * @author Venny
 */
public class OperationsManagementModule {
    private EmployeeEntity currentEmployee;
    
    public OperationsManagementModule() {
    }

    public OperationsManagementModule(EmployeeEntity currentEmployee) {
        this();
        
        this.currentEmployee = currentEmployee;
    }
    
    public void menuOperationsManagementModule() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management Client :: Operations Management Module ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("10: Voew Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign Transit Driver");
            System.out.println("12: Update Transit As Completed");
            System.out.println("13: Logout\n");
            //TODO: Do we want to put log out in each module, or attached to MainApp
            response = 0;
            
            while(response < 1 || response > 13)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    System.out.println("Create New Model\n");
                    
                }
                else if (response == 2) {
                    System.out.println("View All Models\n");

                }
                else if (response == 3) {
                    System.out.println("Update Model\n");

                }
                else if (response == 4) {
                    System.out.println("Delete Model\n");

                }
                else if (response == 5) {
                    System.out.println("Create New Car\n");

                }
                else if (response == 6) {
                    System.out.println("View All Cars\n");

                }
                else if (response == 7) {
                    System.out.println("View Car Details\n");

                }
                else if (response == 8) {
                    System.out.println("Update Car\n");

                }
                else if (response == 9) {
                    System.out.println("Delete Car\n");

                }
                else if (response == 10) {
                    System.out.println("View Transit Driver Dispatch Records for Current Day Reservations\n");

                }
                else if (response == 11) {
                    System.out.println("Assign Transit Drivers\n");

                }
                else if (response == 12) {
                    System.out.println("Update Transit as Completed \n");

                }
                else if (response == 13) {
                    //Should do log out instead
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 13) {
                //should not be break but log out?
                break;
            }
        }
        
    }
}
