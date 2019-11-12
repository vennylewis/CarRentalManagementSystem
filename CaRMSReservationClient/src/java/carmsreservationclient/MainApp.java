/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import entity.CustomerEntity;
import java.util.Date;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
 
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    
    private CustomerEntity currentCustomer;
    
    
    public MainApp() 
    {
        currentCustomer = null;
    }

    
    
    public MainApp(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote) 
    {
        this();

        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
    }



    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to CaRMS Reservation Client for Car Reservation Management System ***\n");
            System.out.println("1: Register Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Car");
            System.out.println("4: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    if (currentCustomer == null) {
                    createNewCustomer();
                    //what if creation doesn't work? because the same record already exists? or other cases
                    }
                    
                }
                else if (response == 2) {
                    try
                        {
                            doLogin();
                            System.out.println();
                            System.out.println("Login successful as " + currentCustomer.getName() + "!\n");   
                            
//                            runCustomerMenu();
                        }
                        catch(InvalidLoginCredentialException ex) 
                        {
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }

                }
                else if (response == 3) {
                    searchCar();
                }
                else if (response == 4) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 4) {
                break;
            }
        }
        
    }
    
    private void createNewCustomer() {
        
        Scanner scanner = new Scanner(System.in);
        String name = "";
        String email = "";
        String phoneNumber = "";
        String passportNumber = "";

        
        System.out.println("*** CaRMS Reservation Client :: Create New Customer ***\n");
        System.out.print("Enter name> ");
        name = scanner.nextLine().trim();
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter phone number(8 digit)> ");
        phoneNumber = scanner.nextLine().trim();
        System.out.print("Enter passport number(10 digit)> ");
        passportNumber = scanner.nextLine().trim();
        
        customerEntitySessionBeanRemote.createCustomerEntity(new CustomerEntity(name, email, Integer.parseInt(phoneNumber), passportNumber));

        System.out.println("Customer " + name + " created successfully!\n");  
    }
    
    private void doLogin() throws InvalidLoginCredentialException 
    {
        
        Scanner scanner = new Scanner(System.in);
        String name = "";
        String passportNumber = "";
        
        System.out.println("*** CaRMS Reservation System :: Login Customer ***\n");
        System.out.print("Enter name> ");
        name = scanner.nextLine().trim();
        System.out.print("Enter passport number> ");
        passportNumber = scanner.nextLine().trim();
        
        if(name.length() > 0 && passportNumber.length() > 0)
        {
            currentCustomer = customerEntitySessionBeanRemote.login(name, passportNumber);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
        
    }
    
    private void searchCar() {
        Scanner scanner = new Scanner(System.in);
        String pickupOutlet = "";
        String returnOutlet = "";
        
        System.out.println("*** CaRMS Reservation System :: Search Car ***\n");
        System.out.print("Enter start date (DDMMYYYY)> ");
//        Chop the string up, to plonk as date and time
//        pickupDate = scanner.nextLine().trim();
        System.out.print("Enter start time (2400 notation)> ");
        System.out.print("Enter return date (DDMMYYYY)> ");
//        returnDate = scanner.nextLine().trim();
//        int startDay = scanner.nextInt();
//        int startMonth = scanner.nextInt();
//        int startYear = scanner.nextInt();
//        Date returnDate = new Date(startYear, startMonth-1, startDay));
        System.out.print("Enter pickup outlet> ");
        pickupOutlet = scanner.nextLine().trim();
        System.out.print("Enter return outlet> ");
        returnOutlet = scanner.nextLine().trim();
//        System.out.println("Choose one car category");
//        System.out.println("1: Create New Model");
//        System.out.println("2: View All Models");
//        System.out.println("3: View Model Details");
//        System.out.println("4: Create New Car");
//        returnOutlet = scanner.nextLine().trim();
//        System.out.print("Enter return outlet> ");
//        returnOutlet = scanner.nextLine().trim();
        
        
//        List<CarEntity> carEntities = carEntitySessionBeanRemote.retrieveAllCars();
//        System.out.printf("%8s%15s%20s%15s%15s\n", "Car ID", "Model", "License Plate No", "Colour", "Status");
//
//        for (CarEntity carEntity : carEntities) {
//            System.out.printf("%8s%15s%20s%15s%15s\n", carEntity.getCarId().toString(), carEntity.getModelEntity().getName(), carEntity.getLicensePlateNo(), carEntity.getColour(), carEntity.getCarStatus());
//        }
//
//        System.out.print("Press any key to continue...> ");
//        sc.nextLine();
        
    }
}

