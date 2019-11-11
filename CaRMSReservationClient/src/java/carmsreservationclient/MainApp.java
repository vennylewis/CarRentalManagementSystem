/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import entity.CustomerEntity;
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
            System.out.println("3: Exit\n");
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
                        }
                        catch(InvalidLoginCredentialException ex) 
                        {
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }

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
}

