package carmsreservationclient;

import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalReservationEntitySessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.RentalRateEntity;
import entity.RentalReservationEntity;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import util.comparator.SortRentalFee;
import util.enumeration.PaymentStatusEnum;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoRentalRateApplicableException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

public class MainApp {

    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RentalReservationEntitySessionBeanRemote rentalReservationEntitySessionBeanRemote;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private CustomerEntity currentCustomer;

    public MainApp() {
        currentCustomer = null;
    }

    public MainApp(CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ModelEntitySessionBeanRemote modelEntitySessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, RentalReservationEntitySessionBeanRemote rentalReservationEntitySessionBeanRemote) {
        this();

        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.rentalReservationEntitySessionBeanRemote = rentalReservationEntitySessionBeanRemote;
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
                        try {
                            createNewCustomer();
                            runCustomerMenu();
                        } catch (CustomerExistsException ex) {
                            System.out.println(ex.getMessage());
                        }

                    }

                } else if (response == 2) {
                    try {
                        doLogin();
                        System.out.println();
                        System.out.println("Login successful as " + currentCustomer.getName() + "!\n");

                        runCustomerMenu();
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

        while (true) {
            System.out.println("*** You are logged in as " + currentCustomer.getName() + " ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: View All My Reservation");
            System.out.println("3: View Reservation Details");
            System.out.println("4: Customer Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    searchCar();
                } else if (response == 2) {
                    viewAllMyReservations();
                } else if (response == 3) {
                    viewReservationDetails();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }

            if (response == 4) {
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
        Long pickupOutletId = 1l;
        Long returnOutletId = 1l;
        boolean searchSuccess = true;
        double rentalFee = 0;

        boolean validDate = false;
        while (!validDate) {
            try {
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

                if (rentalEnd.after(rentalStart)) {
                    validDate = true;
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Outlet Locations options: ");
        System.out.printf("%8s%30s%15s%15s\n", "Outlet ID", "Outlet Name", "Opening Hour", "Closing Hour");
        for (OutletEntity outlet : outletEntitySessionBeanRemote.retrieveAllOutlets()) {
            System.out.printf("%8s%30s%15s%15s\n", outlet.getOutletId(), outlet.getName(), outlet.getOpeningHour(), outlet.getClosingHour());

        }

        boolean worked = false;
        while (!worked) {
            System.out.print("Enter pickup outlet ID> ");
            pickupOutletId = scanner.nextLong();
            System.out.print("Enter return outlet ID> ");
            returnOutletId = scanner.nextLong();
            scanner.nextLine();
            System.out.println();

            try {
                OutletEntity pickupOutlet = outletEntitySessionBeanRemote.retrieveOutletEntityByOutletId(pickupOutletId);
                OutletEntity returnOutlet = outletEntitySessionBeanRemote.retrieveOutletEntityByOutletId(returnOutletId);

                //check for opening hours if needed
//                if(returnOutlet.getOpeningHour().substring(0, 2))
                
                List<ModelEntity> availableModels = reservationSessionBeanRemote.searchModels(rentalStart, rentalEnd, pickupOutlet, returnOutlet);
                List<CategoryEntity> availableCategories = reservationSessionBeanRemote.searchCategories(rentalStart, rentalEnd, pickupOutlet, returnOutlet);

                if (availableModels.isEmpty()) {
                    searchSuccess = false;
                    System.out.println("Sorry, but no cars are available for the dates and location you have chosen!");
                } else {
                    System.out.println("Available categories are shown below:");
                    System.out.printf("%11s%20s%20s\n", "Category ID", "Category Name", "Rental Fee ($)");
                    for (CategoryEntity category : availableCategories) {

                        try {
                            rentalFee = calculateRentalFee(category.getCategoryId(), rentalStart, rentalEnd);
                        } catch (NoRentalRateApplicableException ex) {
                            System.out.println(ex.getMessage());
                        }
                        System.out.printf("%11s%20s%20s\n", category.getCategoryId(), category.getCategoryName(), rentalFee);
                    }

                    System.out.println();

                    System.out.println("Available models are shown below:");
                    System.out.printf("%8s%20s%20s%20s%15s\n", "Model ID", "Category", "Make Name", "Model Name", "Rental Fee ($)");
                    for (ModelEntity model : availableModels) {
                        try {
                            rentalFee = calculateRentalFee(model.getCategoryEntity().getCategoryId(), rentalStart, rentalEnd);
                        } catch (NoRentalRateApplicableException ex) {
                            System.out.println(ex.getMessage());
                        }
                        System.out.printf("%8s%20s%20s%20s%15s\n", model.getModelId(), model.getCategoryEntity().getCategoryName(), model.getMake(), model.getModel(), rentalFee);
                    }
                }
                worked = true;
            } catch (OutletNotFoundException ex) {
                System.out.println("Incorrect outlet! Try again!");
            }
        }

        if (searchSuccess) {
            System.out.print("If you want to reserve a car, enter any character (otherwise, leave blank)> ");
            String reserveResponse = scanner.nextLine().trim();
            if (!reserveResponse.isEmpty()) {
                Long modelIdLong = 1l;
                Long categoryIdLong = 1l;
                System.out.println();
                System.out.println("***CaRMS Reservation System :: Reserve a Car***");

                boolean validChoice = false;
                while (validChoice == false) {
                    System.out.print("If you want to reserve based on car category, enter car category ID (otherwise, leave blank)> ");
                    String categoryId = scanner.nextLine().trim();
                    System.out.print("If you want to reserve based on specific car model, enter car model ID (otherwise, leave blank)> ");
                    String modelId = scanner.nextLine().trim();
                    if (categoryId.isEmpty()) {
                        categoryIdLong = null;
                        if (!modelId.isEmpty()) {
                            modelIdLong = Long.parseLong(modelId);
                            validChoice = true;
                        }
                    } else if (!categoryId.isEmpty()) {
                        categoryIdLong = Long.parseLong(categoryId);
                        modelIdLong = null;
                        validChoice = true;
                    }
                }

                reserveCar(modelIdLong, categoryIdLong, pickupOutletId, returnOutletId, rentalStart, rentalEnd, rentalFee);
            }
        }
    }

    private void reserveCar(Long modelId, Long categoryId, Long pickupOutletId, Long returnOutletId, Date rentalStart, Date rentalEnd, double rentalFee) {
        Scanner sc = new Scanner(System.in);
        Long ccNum = 1l;
        if (currentCustomer == null) {
            System.out.println();
            System.out.println("You have to login first!");
            System.out.println("1. Register Customer");
            System.out.println("2. Customer Login\n");
            System.out.print("> ");
            Integer response = sc.nextInt();
            if (response == 1) {
                try {
                    createNewCustomer();
                    System.out.println("*** You are logged in as " + currentCustomer.getName() + " ***\n");
                    System.out.println();
                } catch (CustomerExistsException ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (response == 2) {
                try {
                    doLogin();
                    System.out.println("Login successful as " + currentCustomer.getName() + "!\n");
                    System.out.println();
                } catch (InvalidLoginCredentialException ex) {
                    System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                }
            }
        }

        System.out.println("Reserved Category ID " + categoryId);
        System.out.println("Reserved Model ID " + modelId);

        if (currentCustomer != null) {
            System.out.print("Enter Credit Card Number> ");
            ccNum = sc.nextLong();
            sc.nextLine();
            System.out.print("Do you want to pay now? (Reply with any character, otherwise leave blank) >");
            String payment = sc.nextLine().trim();

            try {
                RentalReservationEntity rentalReservationEntity = rentalReservationEntitySessionBeanRemote.createRentalReservationEntity(new RentalReservationEntity(rentalStart, rentalEnd, ccNum), currentCustomer.getCustomerId(), returnOutletId, pickupOutletId);
                rentalReservationEntity.setAmount(rentalFee);
                Long rentalReservationEntityId = rentalReservationEntity.getRentalReservationId();
                if (categoryId != null) {
                    System.out.println("Reserved Category ID" + categoryId);
                    rentalReservationEntitySessionBeanRemote.setCategory(rentalReservationEntityId, categoryId);
                } else if (modelId != null) {
                    System.out.println("Reserved Model ID " + modelId);
                    rentalReservationEntitySessionBeanRemote.setModel(rentalReservationEntityId, modelId);
                }

                if (!payment.isEmpty()) {
                    rentalReservationEntity.setPaymentStatus(PaymentStatusEnum.PAID);
                    System.out.println("You have successfully paid for the reservation");
                } else {
                    System.out.println("You will have to make payment of $" + rentalFee + " at the time of pickup.");
                }
                rentalReservationEntitySessionBeanRemote.updateRentalReservation(rentalReservationEntity);
                System.out.println("You have successfully reserved a car!");
            } catch (OutletNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private double calculateRentalFee(Long categoryId, Date rentalStart, Date rentalEnd) throws NoRentalRateApplicableException {
        double rentalFee = 0;
        try {
            CategoryEntity category = categoryEntitySessionBeanRemote.retrieveCategoryEntityByCategoryId(categoryId);
            List<RentalRateEntity> availableRentalRates = category.getRentalRateEntities();
            List<RentalRateEntity> applicableRentalRates = new ArrayList<>();
            Date currentDate = rentalStart;
            if (!availableRentalRates.isEmpty()) {
                // for each day, find the cheapest rental rate and add to total fee
                while (currentDate.getDate() <= rentalEnd.getDate()) {
                    applicableRentalRates = new ArrayList<>();
                    //for debugging
                    //System.out.println(availableRentalRates.size() + " available");
                    for (RentalRateEntity rentalRateEntity : availableRentalRates) {
                        if (rentalRateEntity.getValidityStartDate() == null && rentalRateEntity.getValidityEndDate() == null) {
                            applicableRentalRates.add(rentalRateEntity);
                        } else if (rentalRateEntity.getValidityStartDate().compareTo(currentDate) <= 0 && rentalRateEntity.getValidityEndDate().compareTo(currentDate) >= 0) {
                            applicableRentalRates.add(rentalRateEntity);
                        }
                    }
                    //for debugging
//                    System.out.println(applicableRentalRates.size() + " applicable");
                    if (!applicableRentalRates.isEmpty()) {
                        applicableRentalRates.sort(new SortRentalFee());
                        rentalFee += applicableRentalRates.get(0).getRatePerDay();
                    } else {
                        throw new NoRentalRateApplicableException("No rental rate applicable! Try reserving another date.");
                    }
                    currentDate.setDate(currentDate.getDate() + 1);
                }
            }
        } catch (CategoryNotFoundException ex) {
            // what do i do ah
        }
        return rentalFee;
    }

    private void viewAllMyReservations() {
        System.out.println("*** CaRMS Reservation System :: View All My Reservations ***\n");
        Scanner sc = new Scanner(System.in);

        currentCustomer = customerEntitySessionBeanRemote.retrieveCustomerEntityByCustomerId(currentCustomer.getCustomerId());
        List<RentalReservationEntity> rentalReservationEntities = currentCustomer.getRentalReservationEntities();
        if (!rentalReservationEntities.isEmpty()) {
            String carName = "";
            String categoryName = "";
            String makeName = "";
            String modelName = "";
            System.out.printf("%20s%35s%35s%20s%20s%20s%20s%20s%20s%15s\n", "Rental Reservation ID", "Rental Start Date/Time", "Rental End Date/Time", "Pickup Outlet", "Return outlet", "License Plate No", "Category", "Make Name", "Model Name", "Rental Fee ($)");

            for (RentalReservationEntity rentalReservationEntity : rentalReservationEntities) {
                if (rentalReservationEntity.getCategoryEntity() != null) {
                    categoryName = rentalReservationEntity.getCategoryEntity().getCategoryName().toString();
                }

                if (rentalReservationEntity.getModelEntity() != null) {
                    categoryName = rentalReservationEntity.getModelEntity().getCategoryEntity().getCategoryName().toString();
                    makeName = rentalReservationEntity.getModelEntity().getMake();
                    modelName = rentalReservationEntity.getModelEntity().getModel();
                }
                if (rentalReservationEntity.getCarEntity() != null) {
                    carName = rentalReservationEntity.getCarEntity().getLicensePlateNo();
                    categoryName = rentalReservationEntity.getCarEntity().getModelEntity().getCategoryEntity().getCategoryName().toString();
                    makeName = rentalReservationEntity.getModelEntity().getMake();
                    modelName = rentalReservationEntity.getModelEntity().getModel();
                }
                System.out.printf("%20s%35s%35s%20s%20s%20s%20s%20s%20s%15s\n", rentalReservationEntity.getRentalReservationId(), rentalReservationEntity.getRentalStartTime().toString(), rentalReservationEntity.getRentalEndTime().toString(), rentalReservationEntity.getPickupOutletEntity().getName(), rentalReservationEntity.getReturnOutletEntity().getName(), carName, categoryName, makeName, modelName, rentalReservationEntity.getAmount());
            }
        } else {
            System.out.println("You have not reserved any cars!");
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    private void viewReservationDetails() {
        System.out.println("*** CaRMS Reservation System :: View Reservation Details ***\n");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Rental Reservation ID> ");

        Long rentalReservationId = sc.nextLong();
        String carName = "";
        String categoryName = "";
        String makeName = "";
        String modelName = "";
        try {
            RentalReservationEntity rentalReservationEntity = rentalReservationEntitySessionBeanRemote.retrieveRentalReservationEntityByRentalReservationId(rentalReservationId);
            if (rentalReservationEntity.getCategoryEntity() != null) {
                categoryName = rentalReservationEntity.getCategoryEntity().getCategoryName().toString();
            }

            if (rentalReservationEntity.getModelEntity() != null) {
                categoryName = rentalReservationEntity.getModelEntity().getCategoryEntity().getCategoryName().toString();
                makeName = rentalReservationEntity.getModelEntity().getMake();
                modelName = rentalReservationEntity.getModelEntity().getModel();
            }
            if (rentalReservationEntity.getCarEntity() != null) {
                carName = rentalReservationEntity.getCarEntity().getLicensePlateNo();
                categoryName = rentalReservationEntity.getCarEntity().getModelEntity().getCategoryEntity().getCategoryName().toString();
                makeName = rentalReservationEntity.getModelEntity().getMake();
                modelName = rentalReservationEntity.getModelEntity().getModel();
            }

            System.out.printf("%35s%35s%20s%20s%20s%20s%20s%20s%15s\n", "Rental Start Date/Time", "Rental End Date/Time", "Pickup Outlet", "Return outlet", "License Plate No", "Category", "Make Name", "Model Name", "Rental Fee ($)");
            System.out.printf("%35s%35s%20s%20s%20s%20s%20s%20s%15s\n", rentalReservationEntity.getRentalStartTime().toString(), rentalReservationEntity.getRentalEndTime().toString(), rentalReservationEntity.getPickupOutletEntity().getName(), rentalReservationEntity.getReturnOutletEntity().getName(), carName, categoryName, makeName, modelName, "Rental Fee");

            System.out.println("------------------------");
            System.out.println("1: Cancel Reservation Rental Rate");
            System.out.println("2: Back\n");
            System.out.print("> ");
            int response = sc.nextInt();

            if (response == 1) {
                doCancelReservation(rentalReservationEntity);
            }
        } catch (RentalReservationNotFoundException ex) {
            System.out.println("An error has occurred while retrieving rental reservation: " + ex.getMessage() + "\n");
        }
    }

    private void doCancelReservation(RentalReservationEntity rentalReservationEntity) {
        System.out.println("*** CaRMS Reservation System :: View Reservation Details :: Cancel Reservation ***\n");
        Scanner sc = new Scanner(System.in);

        System.out.printf("Confirm Cancel Rental Reservation (Rental Reservation ID: %d) (Enter 'Y' to Delete)> ", rentalReservationEntity.getRentalReservationId());
        String input = sc.nextLine().trim();

        if (input.equals("Y")) {
            double penaltyFee;
            // get current date, by LocalDate
            LocalDate today = java.time.LocalDate.now();
            Date currentDate = new Date(today.getYear() - 1900, today.getMonthValue() - 1, today.getDayOfMonth());

            // calculate penalty fee
            Long timeDiff = rentalReservationEntity.getRentalStartTime().getTime() - currentDate.getTime();
            Long timeDiffInDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
            if (timeDiffInDays >= 14) {
                penaltyFee = 0;
            } else if (timeDiffInDays >= 7) {
                penaltyFee = rentalReservationEntity.getAmount() * 0.2;
            } else if (timeDiffInDays >= 3) {
                penaltyFee = rentalReservationEntity.getAmount() * 0.5;
            } else {
                penaltyFee = rentalReservationEntity.getAmount() * 0.7;
            }
            DecimalFormat df = new DecimalFormat("0.00");
            System.out.println("Penalty Fee: $" + df.format(penaltyFee));

            // charge penalty based on whether customer has paid or not
            if (rentalReservationEntity.getPaymentStatus() == PaymentStatusEnum.PAID) {
                System.out.println("As you have already made payment, you will be refunded the remainder after deducting the penalty fee: $" + df.format(rentalReservationEntity.getAmount() - penaltyFee));
            } else {
                System.out.println("As you have yet to make payment, $" + penaltyFee + " will be charged to your credit card " + rentalReservationEntity.getCcNum());
            }
        } else {
            System.out.println("Reservation not cancelled!");
        }

        // delete reservation from system
        try {
            rentalReservationEntitySessionBeanRemote.deleteRentalReservation(rentalReservationEntity.getRentalReservationId());
            System.out.println("Rental reservation cancelled successfully!\n");
        } catch (RentalReservationNotFoundException ex) {
            System.out.println("An error has occurred while cancelling the rental reservation: " + ex.getMessage() + "\n");
        }
    }
}
