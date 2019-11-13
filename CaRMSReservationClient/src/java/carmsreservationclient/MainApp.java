package carmsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CustomerExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;

public class MainApp {

    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private CustomerEntity currentCustomer;

    public MainApp() {
        currentCustomer = null;
    }

    public MainApp(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote) {
        this();

        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to CaRMS Reservation Client for Car Reservation Management System ***\n");
            System.out.println("1: Register Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Car");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    if (currentCustomer == null) {
                    //what if creation doesn't work? because the same record already exists? or other cases
                    
//                    runCustomerMenu();
                        try {
                            createNewCustomer();
                        } catch (CustomerExistsException ex) {
                            System.out.println(ex.getMessage());
                        }

                    }

                } else if (response == 2) {
                    try {
                        doLogin();
                        System.out.println();
                        System.out.println("Login successful as " + currentCustomer.getName() + "!\n");

//                            runCustomerMenu();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }

                } else if (response == 3) {
                    searchCar();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }

    }

    private void createNewCustomer() throws CustomerExistsException {

        Scanner scanner = new Scanner(System.in);
        String name = "";
        String email = "";
        String number = "";
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
        CustomerEntity existingCustomer = null;
        
        try {
            existingCustomer = customerEntitySessionBeanRemote.retrieveCustomerEntitybyPassportNumber(passportNumber);
        } catch (CustomerNotFoundException ex) {
        }
        if (existingCustomer != null) {
            throw new CustomerExistsException("Dear customer, you already have an account with Merlion Car Rental.\n");
        } else {
            currentCustomer = customerEntitySessionBeanRemote.createCustomerEntity(new CustomerEntity(name, email, Integer.parseInt(phoneNumber), passportNumber));
            System.out.println("Customer " + name + " created successfully!\n");
        } 
    }

    private void doLogin() throws InvalidLoginCredentialException {

        Scanner scanner = new Scanner(System.in);
        String name = "";
        String passportNumber = "";

        System.out.println("*** CaRMS Reservation System :: Login Customer ***\n");
        System.out.print("Enter name> ");
        name = scanner.nextLine().trim();
        System.out.print("Enter passport number> ");
        passportNumber = scanner.nextLine().trim();

        if (name.length() > 0 && passportNumber.length() > 0) {
            currentCustomer = customerEntitySessionBeanRemote.login(name, passportNumber);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }

    }

    private void runCustomerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** You are logged in as " + currentCustomer.getName() + " ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: View Reservation Details");
            System.out.println("3: View All My Reservation");
            System.out.println("4: Customer Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                if(response == 1) {
                    searchCar();
                } else if(response == 2) {
                    break;
                } else if(response == 3) {
                    break;
                } else if(response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
            
            if(response == 4) {
                break;
            }
        }
    }
    

    private void searchCar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation System :: Search Car ***\n");
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date rentalStart = new Date();
        Date rentalEnd = new Date();
        OutletEntity pickupOutlet = new OutletEntity();
        OutletEntity returnOutlet = new OutletEntity();
        
        boolean validDate = false;
        while(!validDate){
            try{ 
                System.out.print("Enter start date (DD-MM-YYYY)> ");
                String rentalStartDate = scanner.nextLine().trim();
                System.out.print("Enter start time (HH:MM)> ");
                String rentalStartTime = scanner.nextLine().trim();
                System.out.print("Enter return date (DD-MM-YYYY)> ");
                String rentalReturnDate = scanner.nextLine().trim();
                System.out.print("Enter return time (HH:MM)> ");
                String rentalReturnTime = scanner.nextLine().trim();
                rentalStart = simpleDateFormat.parse(rentalStartDate + " " + rentalStartTime);
                rentalEnd = simpleDateFormat.parse(rentalReturnDate + " " + rentalReturnTime);
                
                if(rentalEnd.after(rentalStart)) {
                    validDate = true;
                }
            } catch(ParseException ex) {
                ex.printStackTrace();
            }          
        }
 
        System.out.println("Outlet Locations options: ");
        System.out.printf("%8s%30s%15s%15s\n", "Outlet ID", "Address", "Start Time", "End Time");
        for (OutletEntity outlet: outletEntitySessionBeanRemote.retrieveAllOutlets()) {
            System.out.printf("%8s%30s%15s%15s\n", outlet.getOutletId(), outlet.getOutletName(), outlet.getOpeningHour(), outlet.getClosingHour());

        }

        boolean worked = false;
        while (!worked) {
            System.out.print("Enter pickup outlet ID> ");
            String pickupOutletId = scanner.nextLine().trim();
            System.out.print("Enter return outlet ID> ");
            String returnOutletId = scanner.nextLine().trim();

            try {
                pickupOutlet = outletEntitySessionBeanRemote.retrieveOutletEntityByOutletId(Long.parseLong(pickupOutletId));
                returnOutlet = outletEntitySessionBeanRemote.retrieveOutletEntityByOutletId(Long.parseLong(returnOutletId));

                //check for opening hours if needed
                List<ModelEntity> availableModels = reservationSessionBeanRemote.searchCars(rentalStart, rentalEnd, pickupOutlet, returnOutlet);
                System.out.printf("%8s%20s%20s%15s\n", "ID", "Category", "Model Name", "Rental Fee ($)");

                for (ModelEntity model : availableModels) {
                    System.out.printf("%8s%20s%20s%15s\n", model.getModelId(), model.getCategoryEntity().getCategoryName(), model.getName(), "Price ($)");
                    //might need to print the available catgory, with any as the model Name
                }
                worked = true;
            } catch(OutletNotFoundException ex) {
                
            }

        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
        //need to include question to ask whether they want to reserve car or not
    }
}
