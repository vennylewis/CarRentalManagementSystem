package carmsmanagementclient;

import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import entity.EmployeeEntity;
import entity.RentalRateEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.comparator.SortRentalRate;
import util.enumeration.EmployeeTypeEnum;
import util.exception.CategoryNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RentalRateNotFoundException;

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
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
//                    System.out.println("Create Rental Rate\n");
                    doCreateRentalRate();
                }
                else if (response == 2) {
//                    System.out.println("View All Rental Rates\n");
                    doViewAllRentalRates();
                }
                else if (response == 3) {
//                    System.out.println("View Rental Rate Details\n");
                    doViewRentalRateDetails();
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

    private void doCreateRentalRate() {
        Scanner sc = new Scanner(System.in);
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date validityStart = new Date();
        Date validityEnd = new Date();
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: Create Rental Rate ***\n");
        RentalRateEntity newRentalRateEntity = new RentalRateEntity();
        
        System.out.print("Enter Category ID> ");
        long categoryId = sc.nextLong();
        
        System.out.print("Enter Name> ");
        sc.nextLine();
        newRentalRateEntity.setName(sc.nextLine().trim());
        
        System.out.print("Enter Rate Per Day> ");
        newRentalRateEntity.setRatePerDay(sc.nextDouble());
        sc.nextLine();
        
        boolean validDate = false;
        while(!validDate){
            try{ 
                System.out.print("Enter start date (DD-MM-YYYY) (leave blank if always valid)> ");
                String validityStartDate = sc.nextLine().trim();
                //Help me fix this weird bug
                System.out.print("Enter start time (HH:MM)(leave blank if always valid)> ");
                String validityStartTime = sc.nextLine().trim();
                System.out.print("Enter return date (DD-MM-YYYY)(leave blank if always valid)> ");
                String validityEndDate = sc.nextLine().trim();
                System.out.print("Enter return time (HH:MM)(leave blank if always valid)> ");
                String validityEndTime = sc.nextLine().trim();
//                System.out.println(validityStartDate + "I'm trying to see");
                
                if (validityStartDate.isEmpty() && validityStartTime.isEmpty() && validityEndDate.isEmpty() && validityEndTime.isEmpty()) {
//                    System.out.println("I'm at empty");
                    validityStart = null;
                    validityEnd = null;
                    validDate = true;
                } else {
//                    System.out.println("I'm trying to parse");
                    validityStart = simpleDateFormat.parse(validityStartDate + " " + validityStartTime);
                    validityEnd = simpleDateFormat.parse(validityEndDate + " " + validityEndTime);
                    if(validityEnd.after(validityStart)) {
                        validDate = true;
                    } else {
                        System.out.println("Invalid date and time input! Try again!");
                    }   
                } 
            } catch(ParseException ex) {
                ex.printStackTrace();
            }          
        }
        
        newRentalRateEntity.setValidityStartDate(validityStart);
        newRentalRateEntity.setValidityEndDate(validityEnd);
        try {
            rentalRateEntitySessionBeanRemote.createRentalRateEntity(newRentalRateEntity, categoryId);
        } catch (CategoryNotFoundException ex) {
            System.out.println("Invalid category id: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllRentalRates() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View All Rental Rates ***\n");
        List<RentalRateEntity> rentalRateEntities = rentalRateEntitySessionBeanRemote.retrieveAllRentalRates();
        System.out.printf("%14s%15s%20s%20s%30s%30s%15s\n", "Rental Rate ID", "Category", "Name", "Rate Per Day", "Validity Start Date", "Validity End Date", "Status");
        
        rentalRateEntities.sort(new SortRentalRate());
        
        for (RentalRateEntity rentalRateEntity: rentalRateEntities) {
            System.out.printf("%14s%15s%20s%20s%30s%30s%15s\n", rentalRateEntity.getRentalRateId().toString(), rentalRateEntity.getCategoryEntity().getCategoryName(), rentalRateEntity.getName(), rentalRateEntity.getRatePerDay(), rentalRateEntity.getValidityStartDate(), rentalRateEntity.getValidityEndDate(), rentalRateEntity.getRentalRateStatus());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private void doViewRentalRateDetails() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Rental Rate Details ***\n");
        System.out.print("Enter Rental Rate ID> ");
        
        Long rentalRateId = sc.nextLong();
        try {
            RentalRateEntity rentalRateEntity = rentalRateEntitySessionBeanRemote.retrieveRentalRateEntityByRentalRateId(rentalRateId);
            System.out.printf("%14s%15s%20s%20s%30s%30s%15s\n", "Rental Rate ID", "Category", "Name", "Rate Per Day", "Validity Start Date", "Validity End Date", "Status");
            System.out.printf("%14s%15s%20s%20s%30s%30s%15s\n", rentalRateEntity.getRentalRateId().toString(), rentalRateEntity.getCategoryEntity().getCategoryName(), rentalRateEntity.getName(), rentalRateEntity.getRatePerDay(), rentalRateEntity.getValidityStartDate(), rentalRateEntity.getValidityEndDate(), rentalRateEntity.getRentalRateStatus());
            System.out.println("------------------------");
            System.out.println("1: Update Rental Rate");
            System.out.println("2: Delete Rental Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            int response = sc.nextInt();
            
            if (response == 1) {
                doUpdateRentalRate(rentalRateEntity);
            } else if (response == 2) {
                doDeleteRentalRate(rentalRateEntity);
            }
        } catch (RentalRateNotFoundException ex) {
            System.out.println("An error has occurred while retrieving rental rate: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateRentalRate(RentalRateEntity rentalRateEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Rental Rate Details :: Update Rental Rate ***\n");
        
        System.out.print("Enter Name (blank if no change)> ");
        String input = sc.nextLine().trim();
        if (input.length() > 0) {
            rentalRateEntity.setName(input);
        }
        
        System.out.print("Enter Rate Per Day (0 if no change)> ");
        double changedRate = sc.nextDouble(); // need input validation?
        if (changedRate != 0) {
            rentalRateEntity.setRatePerDay(changedRate);
        }

        // need to add functionality for updating other attributes (Dates etc)
        
        rentalRateEntitySessionBeanRemote.updateRentalRate(rentalRateEntity);
    }
    
    private void doDeleteRentalRate(RentalRateEntity rentalRateEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Rental Rate Details :: Delete Rental Rate ***\n");
        System.out.printf("Confirm Delete Rental Rate %s (Rental Rate ID: %d) (Enter 'Y' to Delete)> ", rentalRateEntity.getName(), rentalRateEntity.getRentalRateId());
        String input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                rentalRateEntitySessionBeanRemote.deleteRentalRate(rentalRateEntity.getRentalRateId());
                System.out.println("Rental rate deleted successfully!\n");
            }
            catch (RentalRateNotFoundException ex) 
            {
                System.out.println("An error has occurred while deleting the rental rate: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Rental Rate NOT deleted!\n");
        }
    }
}
