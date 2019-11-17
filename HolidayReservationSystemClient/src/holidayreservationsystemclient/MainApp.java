package holidayreservationsystemclient;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.CategoryEntity;
import ws.client.CategoryNameEnum;
import ws.client.CategoryNotFoundException_Exception;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginCredentialException;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.ModelEntity;
import ws.client.ModelNotFoundException_Exception;
import ws.client.NoRentalRateApplicableException_Exception;
import ws.client.OutletEntity;
import ws.client.OutletNotFoundException_Exception;
import ws.client.PartnerEntity;
import ws.client.PaymentStatusEnum;
import ws.client.RentalReservationEntity;
import ws.client.RentalReservationNotFoundException_Exception;

public class MainApp {

    private PartnerEntity currentPartner;
    private CustomerEntity currentCustomer;

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Web-based CaRMS Reservation Client for Holiday Reservation System ***\n");
            System.out.println("1: Partner Login");
            System.out.println("2: Partner Search Car");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful as " + currentPartner.getName() + "!\n");
                        System.out.println();
                        
                        runCustomerMenu();
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option. Try again!");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException_Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS Reservation System :: Login Customer ***\n");
        System.out.print("Enter email> ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();

        //throws InvalidLoginCredentialException
        if (email.length() > 0 && password.length() > 0) {
            currentPartner = login(email, password);
        } else {
            throw new InvalidLoginCredentialException_Exception("Missing login credential!", new InvalidLoginCredentialException());
        }

    }

    private void runCustomerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** You are logged in as " + currentPartner.getName() + " ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: View All Partner Reservations");
            System.out.println("3: View Reservation Details");
            System.out.println("4: Customer Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    searchCar();
                } else if (response == 2) {
                    viewAllPartnerReservations();
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
        String timePattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timePattern);
        Date rentalBeginTime = new Date();
        Date rentalEndTime = new Date();
        Date rentalStart = new Date();
        Date rentalEnd = new Date();
        Long pickupOutletId = 1l;
        Long returnOutletId = 1l;
//        boolean searchSuccess = true;
        double rentalFee = 0;
        double[] rentalRatePerCategory = new double[10];
        List<ModelEntity> availableModels = new ArrayList<>();
        List<CategoryEntity> availableCategories = new ArrayList<>();
        ArrayList<Double> rentalFeeperCategory;

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
                System.out.println("Invalid date and time entry");
            }
        }

        System.out.println("Outlet Locations options: ");
        System.out.printf("%8s%30s%15s%15s\n", "Outlet ID", "Outlet Name", "Opening Hour", "Closing Hour");
        List<OutletEntity> allOutlets = retrieveAllOutlets();
        for (OutletEntity outlet : allOutlets) {
            System.out.printf("%8s%30s%15s%15s\n", outlet.getOutletId(), outlet.getName(), outlet.getOpeningHour(), outlet.getClosingHour());

        }

        //check whether outlet entered is valid and between operating hours. If not, prompts user to enter a valid outlet again
        boolean validOutlet = true;
        while (validOutlet) {
            System.out.println("Remember to choose the outlet with the appropriate operating hours for your booking!");
            System.out.print("Enter pickup outlet ID> ");
            pickupOutletId = scanner.nextLong();
            System.out.print("Enter return outlet ID> ");
            returnOutletId = scanner.nextLong();
            scanner.nextLine();
            System.out.println();

            try {
                OutletEntity pickupOutlet = retrieveOutletEntityByOutletId(pickupOutletId);
                OutletEntity returnOutlet = retrieveOutletEntityByOutletId(returnOutletId);

                if (pickupOutlet.getOpeningHour() != null) {
                    Date rentalPickupOpeningHour = simpleTimeFormat.parse(pickupOutlet.getOpeningHour());
                    Date rentalPickupClosingHour = simpleTimeFormat.parse(pickupOutlet.getClosingHour());
                    if (!checkTimeIsBetweenTimePeriod(rentalBeginTime, rentalPickupOpeningHour, rentalPickupClosingHour)) {
                        validOutlet = false;
                    }
                }
                if (returnOutlet.getOpeningHour() != null) {
                    Date rentalReturnOpeningHour = simpleTimeFormat.parse(returnOutlet.getOpeningHour());
                    Date rentalReturnClosingHour = simpleTimeFormat.parse(returnOutlet.getClosingHour());
                    if (!checkTimeIsBetweenTimePeriod(rentalEndTime, rentalReturnOpeningHour, rentalReturnClosingHour)) {
                        validOutlet = false;
                    }
                }

            } catch (OutletNotFoundException_Exception | ParseException ex) {
                System.out.println("Outlet entered is not within operating hours, or does not exist!");
            }
        }

        try {
            OutletEntity pickupOutlet = retrieveOutletEntityByOutletId(pickupOutletId);
            OutletEntity returnOutlet = retrieveOutletEntityByOutletId(returnOutletId);

            try {
                // convert dates to XMLGregorianCalendar before using them
                XMLGregorianCalendar rentalStartGC = convertDateToXMLGregorianCalendar(rentalStart);
                XMLGregorianCalendar rentalEndGC = convertDateToXMLGregorianCalendar(rentalEnd);

                //Call searchModels because it will also populate the category list
                availableModels = searchModels(rentalStartGC, rentalEndGC, pickupOutlet, returnOutlet);
                availableCategories = searchCategories(rentalStartGC, rentalEndGC, pickupOutlet, returnOutlet);
            } catch (DatatypeConfigurationException ex) {
                ex.printStackTrace();
            }

            rentalRatePerCategory = new double[availableCategories.size()];

            if (availableModels.isEmpty()) {
                System.out.println("Sorry, but no cars are available for the dates and location you have chosen!");
            } else {
                System.out.println("Available categories are shown below:");
                System.out.printf("%11s%20s%20s\n", "Category ID", "Category Name", "Rental Fee ($)");
                for (CategoryEntity category : availableCategories) {

                    try {
                        try {
                            // convert dates to XMLGregorianCalendar before using them
                            XMLGregorianCalendar rentalStartGC = convertDateToXMLGregorianCalendar(rentalStart);
                            XMLGregorianCalendar rentalEndGC = convertDateToXMLGregorianCalendar(rentalEnd);

                            rentalFee = calculateRentalFee(category, rentalStartGC, rentalEndGC);
                        } catch (DatatypeConfigurationException ex) {
                            ex.printStackTrace();
                        }

                        rentalRatePerCategory[availableCategories.indexOf(category)] = rentalFee;
                        try {
                            System.out.printf("%11s%20s%20s\n", category.getCategoryId(), getCategoryNamebyCategoryId(category.getCategoryId()).toString(), rentalFee);
                        } catch (CategoryNotFoundException_Exception ex) { // shouldn't have this exception
                            System.out.println("Category not found!");
                        }
                    } catch (NoRentalRateApplicableException_Exception ex) {
                        System.out.println("No rental rate found for that category");
                    }
                }
                System.out.println();

                System.out.println("Available models are shown below:");
                System.out.printf("%8s%20s%20s%20s%15s\n", "Model ID", "Category", "Make Name", "Model Name", "Rental Fee ($)");
                for (ModelEntity model : availableModels) {
                    rentalFee = rentalRatePerCategory[availableCategories.indexOf(model.getCategoryEntity())];
                    try {
                        System.out.printf("%8s%20s%20s%20s%15s\n", model.getModelId(), getCategoryNamebyCategoryId(model.getCategoryEntity().getCategoryId()).toString(), model.getMake(), model.getModel(), rentalFee);
                    } catch (CategoryNotFoundException_Exception ex) { // shouldn't have this exception
                        System.out.println("Category not found!");
                    }
                }

                //reserve car prompts
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
                                try {
                                    modelIdLong = Long.parseLong(modelId);
                                    rentalFee = rentalRatePerCategory[availableCategories.indexOf(retrieveModelEntityByModelId(modelIdLong).getCategoryEntity())];
                                    validChoice = true;
                                } catch (ModelNotFoundException_Exception ex) {
                                    System.out.println("Model Not Found");
                                }
                            }
                        } else if (!categoryId.isEmpty()) {
                            try {
                                categoryIdLong = Long.parseLong(categoryId);
                                rentalFee = rentalRatePerCategory[availableCategories.indexOf(retrieveCategoryEntityByCategoryId(categoryIdLong))];
                                modelIdLong = null;
                                validChoice = true;
                            } catch (CategoryNotFoundException_Exception ex) {
                                System.out.println("Category Not Found");
                            }
                        }
                    }

                    reserveCar(modelIdLong, categoryIdLong, pickupOutletId, returnOutletId, rentalStart, rentalEnd, rentalFee);
                }
            }
        } catch (OutletNotFoundException_Exception ex) {
            System.out.println("Incorrect outlet! Try again!");
        }
//        
//        if (searchSuccess) {
//            
//            }
//        }
    }

    private void reserveCar(Long modelId, Long categoryId, Long pickupOutletId, Long returnOutletId, Date rentalStart, Date rentalEnd, double rentalFee) {
        Scanner sc = new Scanner(System.in);
        Long ccNum = 1l;
        if (currentPartner == null) {
            System.out.println();
            System.out.println("You have to login first!");
            try {
                doLogin();
                System.out.println("Login successful as " + currentPartner.getName() + "!\n");
                System.out.println();
            } catch (InvalidLoginCredentialException_Exception ex) {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
            }
        }

        System.out.println("Reserved Category ID " + categoryId);
        System.out.println("Reserved Model ID " + modelId);

        System.out.println("*** Enter Customer Details ***");
        System.out.print("Enter name> ");
        String name = sc.nextLine().trim();
        System.out.print("Enter email> ");
        String email = sc.nextLine().trim();
        System.out.print("Enter phone number(8 digit)> ");
        String phoneNumber = sc.nextLine().trim();
        System.out.print("Enter passport number(10 digit)> ");
        String passportNumber = sc.nextLine().trim();

        try {
            currentCustomer = retrieveCustomerEntitybyPassportNumber(passportNumber);
        } catch (CustomerNotFoundException_Exception ex) {
            currentCustomer = new CustomerEntity();
            currentCustomer.setName(name);
            currentCustomer.setEmail(name);
            currentCustomer.setPhoneNumber(Integer.parseInt(phoneNumber));
            currentCustomer.setPassportNumber(name);
            currentCustomer = createCustomerEntity(currentCustomer);
        }

        if (currentCustomer != null) {
            System.out.print("Enter Credit Card Number> ");
            ccNum = sc.nextLong();
            sc.nextLine();
            System.out.print("Do you want to pay now? (Reply with any character, otherwise leave blank) >");
            String payment = sc.nextLine().trim();

            try {
                RentalReservationEntity rentalReservationEntity = new RentalReservationEntity();
                rentalReservationEntity.setCcNum(ccNum);
                try {
                    // convert dates to XMLGregorianCalendar before using them
                    XMLGregorianCalendar rentalStartGC = convertDateToXMLGregorianCalendar(rentalStart);
                    XMLGregorianCalendar rentalEndGC = convertDateToXMLGregorianCalendar(rentalEnd);

                    rentalReservationEntity.setRentalStartTime(rentalStartGC);
                    rentalReservationEntity.setRentalEndTime(rentalEndGC);
                } catch (DatatypeConfigurationException ex) {
                    ex.printStackTrace();
                }

                rentalReservationEntity = createRentalReservationEntity(rentalReservationEntity, currentCustomer.getCustomerId(), returnOutletId, pickupOutletId);
                rentalReservationEntity.setAmount(rentalFee);
                Long rentalReservationEntityId = rentalReservationEntity.getRentalReservationId();

                if (!payment.isEmpty()) {
                    rentalReservationEntity.setPaymentStatus(PaymentStatusEnum.PAID);
                    System.out.println("You have successfully paid for the reservation");
                } else {
                    System.out.println("You will have to make payment of $" + rentalFee + " at the time of pickup.");
                }
                updateRentalReservation(rentalReservationEntity);

                if (categoryId != null) {
                    System.out.println("Reserved Category ID " + categoryId);
                    setCategory(rentalReservationEntityId, categoryId);
                } else if (modelId != null) {
                    System.out.println("Reserved Model ID " + modelId);
                    setModel(rentalReservationEntityId, modelId);
                }
                System.out.println("You have successfully reserved a car!");
            } catch (OutletNotFoundException_Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean checkTimeIsBetweenTimePeriod(Date toCheck, Date start, Date end) {
        boolean check = false;

        if (start.before(end)) { //Time period is from morning to night
            if (toCheck.compareTo(start) >= 0 && toCheck.compareTo(end) <= 0) {
                check = true;
            }
        } else { //Time period is from night to morning
            if (toCheck.compareTo(end) >= 0 && toCheck.compareTo(start) <= 0) {
                check = true;
            }
        }

        return check;
    }

    private void viewAllPartnerReservations() {
        System.out.println("*** CaRMS Reservation System :: View All My Reservations ***\n");
        Scanner sc = new Scanner(System.in);

        currentCustomer = retrieveCustomerEntityByCustomerId(currentCustomer.getCustomerId());
        List<RentalReservationEntity> rentalReservationEntities = currentCustomer.getRentalReservationEntities();
        if (!rentalReservationEntities.isEmpty()) {
            System.out.printf("%20s%35s%35s%20s%20s%20s%20s%20s%20s%15s\n", "Rental Reservation ID", "Rental Start Date/Time", "Rental End Date/Time", "Pickup Outlet", "Return outlet", "License Plate No", "Category", "Make Name", "Model Name", "Rental Fee ($)");

            for (RentalReservationEntity rentalReservationEntity : rentalReservationEntities) {
                String carName = "";
                String categoryName = "";
                String makeName = "";
                String modelName = "";
                if (rentalReservationEntity.getCategoryEntity() != null) {
                    try {
                        categoryName = getCategoryNamebyCategoryId(rentalReservationEntity.getCategoryEntity().getCategoryId()).toString();
                    } catch (CategoryNotFoundException_Exception ex) { // shouldn't have this exception
                        System.out.println("Category not found!");
                    }
                }

                if (rentalReservationEntity.getModelEntity() != null) {
                    try {
                        categoryName = getCategoryNamebyCategoryId(rentalReservationEntity.getCategoryEntity().getCategoryId()).toString();
                    } catch (CategoryNotFoundException_Exception ex) { // shouldn't have this exception
                        System.out.println("Category not found!");
                    }
                    makeName = rentalReservationEntity.getModelEntity().getMake();
                    modelName = rentalReservationEntity.getModelEntity().getModel();
                }
                if (rentalReservationEntity.getCarEntity() != null) {
                    carName = rentalReservationEntity.getCarEntity().getLicensePlateNo();
                    try {
                        categoryName = getCategoryNamebyCategoryId(rentalReservationEntity.getCategoryEntity().getCategoryId()).toString();
                    } catch (CategoryNotFoundException_Exception ex) { // shouldn't have this exception
                        System.out.println("Category not found!");
                    }
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
            RentalReservationEntity rentalReservationEntity = retrieveRentalReservationEntityByRentalReservationId(rentalReservationId);
            if (rentalReservationEntity.getCategoryEntity() != null) {
                try {
                    categoryName = getCategoryNamebyCategoryId(rentalReservationEntity.getCategoryEntity().getCategoryId()).toString();
                } catch (CategoryNotFoundException_Exception ex) { // shouldn't have this exception
                    System.out.println("Category not found!");
                }
            }

            if (rentalReservationEntity.getModelEntity() != null) {
                try {
                    categoryName = getCategoryNamebyCategoryId(rentalReservationEntity.getCategoryEntity().getCategoryId()).toString();
                } catch (CategoryNotFoundException_Exception ex) { // shouldn't have this exception
                    System.out.println("Category not found!");
                }
                makeName = rentalReservationEntity.getModelEntity().getMake();
                modelName = rentalReservationEntity.getModelEntity().getModel();
            }
            if (rentalReservationEntity.getCarEntity() != null) {
                carName = rentalReservationEntity.getCarEntity().getLicensePlateNo();
                try {
                    categoryName = getCategoryNamebyCategoryId(rentalReservationEntity.getCategoryEntity().getCategoryId()).toString();
                } catch (CategoryNotFoundException_Exception ex) { // shouldn't have this exception
                    System.out.println("Category not found!");
                }
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
        } catch (RentalReservationNotFoundException_Exception ex) {
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
            Long timeDiff = rentalReservationEntity.getRentalStartTime().toGregorianCalendar().getTime().getTime() - currentDate.getTime();
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
            deleteRentalReservation(rentalReservationEntity.getRentalReservationId());
            System.out.println("Rental reservation cancelled successfully!\n");
        } catch (RentalReservationNotFoundException_Exception ex) {
            System.out.println("An error has occurred while cancelling the rental reservation: " + ex.getMessage() + "\n");
        }
    }

    private XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        XMLGregorianCalendar xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        return xmlgc;
    }

    private static PartnerEntity login(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.login(arg0, arg1);
    }

    private static java.util.List<ws.client.OutletEntity> retrieveAllOutlets() {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveAllOutlets();
    }

    private static OutletEntity retrieveOutletEntityByOutletId(java.lang.Long arg0) throws OutletNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveOutletEntityByOutletId(arg0);
    }

    private static java.util.List<ws.client.ModelEntity> searchModels(javax.xml.datatype.XMLGregorianCalendar arg0, javax.xml.datatype.XMLGregorianCalendar arg1, ws.client.OutletEntity arg2, ws.client.OutletEntity arg3) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.searchModels(arg0, arg1, arg2, arg3);
    }

    private static java.util.List<ws.client.CategoryEntity> searchCategories(javax.xml.datatype.XMLGregorianCalendar arg0, javax.xml.datatype.XMLGregorianCalendar arg1, ws.client.OutletEntity arg2, ws.client.OutletEntity arg3) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.searchCategories(arg0, arg1, arg2, arg3);
    }

    private static double calculateRentalFee(ws.client.CategoryEntity arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2) throws NoRentalRateApplicableException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.calculateRentalFee(arg0, arg1, arg2);
    }

    private static CategoryNameEnum getCategoryNamebyCategoryId(java.lang.Long arg0) throws CategoryNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.getCategoryNamebyCategoryId(arg0);
    }

    private static ModelEntity retrieveModelEntityByModelId(java.lang.Long arg0) throws ModelNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveModelEntityByModelId(arg0);
    }

    private static CategoryEntity retrieveCategoryEntityByCategoryId(java.lang.Long arg0) throws CategoryNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveCategoryEntityByCategoryId(arg0);
    }

    private static CustomerEntity retrieveCustomerEntitybyPassportNumber(java.lang.String arg0) throws CustomerNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveCustomerEntitybyPassportNumber(arg0);
    }

    private static CustomerEntity createCustomerEntity(ws.client.CustomerEntity arg0) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.createCustomerEntity(arg0);
    }

    private static RentalReservationEntity createRentalReservationEntity(ws.client.RentalReservationEntity arg0, java.lang.Long arg1, java.lang.Long arg2, java.lang.Long arg3) throws OutletNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.createRentalReservationEntity(arg0, arg1, arg2, arg3);
    }

    private static void updateRentalReservation(ws.client.RentalReservationEntity arg0) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        port.updateRentalReservation(arg0);
    }

    private static void setCategory(java.lang.Long arg0, java.lang.Long arg1) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        port.setCategory(arg0, arg1);
    }

    private static void setModel(java.lang.Long arg0, java.lang.Long arg1) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        port.setModel(arg0, arg1);
    }

    private static CustomerEntity retrieveCustomerEntityByCustomerId(java.lang.Long arg0) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveCustomerEntityByCustomerId(arg0);
    }

    private static RentalReservationEntity retrieveRentalReservationEntityByRentalReservationId(java.lang.Long arg0) throws RentalReservationNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveRentalReservationEntityByRentalReservationId(arg0);
    }

    private static void deleteRentalReservation(java.lang.Long arg0) throws RentalReservationNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        port.deleteRentalReservation(arg0);
    }
}
