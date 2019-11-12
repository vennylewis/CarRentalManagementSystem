/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import entity.CarEntity;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.PaymentStatusEnum;
import util.enumeration.RentalStatusEnum;
import util.exception.CarNotFoundException;


public class CustomerServiceModule {
     
    private EmployeeEntity currentEmployee;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    
    public CustomerServiceModule() {
    }

    public CustomerServiceModule(EmployeeEntity currentEmployee, CarEntitySessionBeanRemote carEntitySessionBeanRemote) {
        this();
        
        this.currentEmployee = currentEmployee;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
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
                    doPickupCar();
                }
                else if (response == 2) {
                    System.out.println("Return Car\n");
                    doReturnCar();
                }
                else if (response == 3) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 3) {
                break;
            }
        }
    }  
    
    public void doPickupCar() {
        System.out.println("*** CaRMS Management Client :: Customer Servie Module :: Pickup Car ***\n");
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter license plate no. of car to be picked up> ");
        long pickupCarId = sc.nextLong();
        try {
            CarEntity pickupCar = carEntitySessionBeanRemote.retrieveCarEntityByCarId(pickupCarId);
            if (pickupCar.getRentalReservationEntity().getPaymentStatus() == PaymentStatusEnum.DEFERRED) {
                // need to pay first
            }
            pickupCar.setRentalStatus(RentalStatusEnum.ON_RENTAL);
            pickupCar.setOutletEntity(null);
            carEntitySessionBeanRemote.updateCar(pickupCar);
        } catch (CarNotFoundException ex) {
            System.out.println("Error finding car: " + ex.getMessage());
        }
    }
    
    public void doReturnCar() {
        System.out.println("*** CaRMS Management Client :: Customer Servie Module :: Return Car ***\n");
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter license plate no. of car to be returned> ");
        long returnCarId = sc.nextLong();
        try {
            CarEntity returnCar = carEntitySessionBeanRemote.retrieveCarEntityByCarId(returnCarId);
            returnCar.setRentalStatus(RentalStatusEnum.IN_OUTLET);
            returnCar.setOutletEntity(returnCar.getRentalReservationEntity().getReturnOutletEntity());
            carEntitySessionBeanRemote.updateCar(returnCar);
        } catch (CarNotFoundException ex) {
            System.out.println("Error finding car: " + ex.getMessage());
        }   
    }
}
