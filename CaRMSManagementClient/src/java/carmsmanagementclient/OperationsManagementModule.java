package carmsmanagementclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.CarEntity;
import entity.EmployeeEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.TransitDriverDispatchRecordEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.StatusEnum;
import util.comparator.SortCar;
import util.comparator.SortModel;
import util.comparator.SortRentalRate;
import util.enumeration.RentalStatusEnum;
import util.enumeration.StatusEnum;

import util.exception.CarNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;

public class OperationsManagementModule {

    private EmployeeEntity currentEmployee;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private Date date;
    
    public OperationsManagementModule() {
        this.date = new Date();
    }

    public OperationsManagementModule(EmployeeEntity currentEmployee, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, ModelEntitySessionBeanRemote modelEntitySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote) {
        this();

        this.currentEmployee = currentEmployee;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.modelEntitySessionBeanRemote = modelEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
    }

    public void menuOperationsManagementModule() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS Management Client :: Operations Management Module ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: View Model Details");
            System.out.println("4: Create New Car");
            System.out.println("5: View All Cars");
            System.out.println("6: View Car Details");
            System.out.println("7: View Transit Driver Dispatch Records for Current Day Reservations");
//            System.out.println("8: Assign Transit Driver");
//            System.out.println("9: Update Transit As Completed");
            System.out.println("8: Logout\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    System.out.println("Create New Model\n");
                    doCreateNewModel();
                } else if (response == 2) {
                    System.out.println("View All Models\n");
                    doViewAllModels();
                } else if (response == 3) {
                    System.out.println("View Model Details\n");
                    doViewModelDetails();
                } else if (response == 4) {
                    System.out.println("Create New Car\n");
                    doCreateNewCar();
                } else if (response == 5) {
                    System.out.println("View All Cars\n");
                    doViewAllCars();
                } else if (response == 6) {
                    System.out.println("View Car Details\n");
                    doViewCarDetails();
                } else if (response == 7) {
                    System.out.println("View Transit Driver Dispatch Records for Current Day Reservations\n");
                    doViewTransitDriverDispatchRecordForTheDay();
                } else if (response == 8) {
//                    System.out.println("Assign Transit Drivers\n");
//                    doAssignTransitDriverDispatch();
//                } else if (response == 9) {
//                    System.out.println("Update Transit as Completed \n");
//                    doUpdateTransit();
//                } else if (response == 10) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 8) {
                break;
            }
        }

    }

    public void doCreateNewModel() {
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: Create New Model ***\n");
        Scanner sc = new Scanner(System.in);

        ModelEntity newModelEntity = new ModelEntity();
        System.out.print("Enter Make Name> ");
        newModelEntity.setMake(sc.nextLine().trim());
        System.out.print("Enter Model Name> ");
        newModelEntity.setModel(sc.nextLine().trim());
        System.out.print("Enter Category ID> ");
        long categoryId = sc.nextLong();

        try {
            modelEntitySessionBeanRemote.createNewModelEntity(newModelEntity, categoryId);
        } catch (CategoryNotFoundException ex) {
            System.out.println("Invalid category id: " + ex.getMessage() + "\n");
        }
    }

    public void doViewAllModels() {
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: View All Models ***\n");
        Scanner sc = new Scanner(System.in);

        List<ModelEntity> modelEntities = modelEntitySessionBeanRemote.retrieveAllModels();
        System.out.printf("%8s%15s%30s%30s%15s\n", "Model ID", "Category", "Make Name", "Model Name", "Status");

        modelEntities.sort(new SortModel());

        for (ModelEntity modelEntity : modelEntities) {
            System.out.printf("%8s%15s%30s%30s%15s\n", modelEntity.getModelId().toString(), modelEntity.getCategoryEntity().getCategoryName(), modelEntity.getMake(), modelEntity.getModel(), modelEntity.getModelStatus());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    public void doViewModelDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Model Details ***\n");
        System.out.print("Enter Model ID> ");

        Long modelId = sc.nextLong();
        try {
            ModelEntity modelEntity = modelEntitySessionBeanRemote.retrieveModelEntityByModelId(modelId);
            System.out.printf("%8s%15s%30s%15s\n", "Model ID", "Category", "Name", "Status");
            System.out.printf("%8s%15s%30s%15s\n", modelEntity.getModelId().toString(), modelEntity.getCategoryEntity().getCategoryName(), modelEntity.getMake(), modelEntity.getModelStatus());
            System.out.println("------------------------");
            System.out.println("1: Update Model");
            System.out.println("2: Delete Model");
            System.out.println("3: Back\n");
            System.out.print("> ");
            int response = sc.nextInt();

            if (response == 1) {
                doUpdateModel(modelEntity);
            } else if (response == 2) {
                doDeleteModel(modelEntity);
            }
        } catch (ModelNotFoundException ex) {
            System.out.println("An error has occurred while retrieving model: " + ex.getMessage() + "\n");
        }
    }

    public void doUpdateModel(ModelEntity modelEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Model Details :: Update Model ***\n");

        System.out.print("Enter Name (blank if no change)> ");
        String input = sc.nextLine().trim();
        if (input.length() > 0) {
            modelEntity.setMake(input);
        }

        // I didn't include update functionality for status cos I think it should be updated in other use cases
        modelEntitySessionBeanRemote.updateModel(modelEntity);
    }

    public void doDeleteModel(ModelEntity modelEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Model Details :: Delete Model ***\n");
        System.out.printf("Confirm Delete Model %s (Model ID: %d) (Enter 'Y' to Delete)> ", modelEntity.getMake(), modelEntity.getModelId());
        String input = sc.nextLine().trim();

        if (input.equals("Y")) {
            try {
                modelEntitySessionBeanRemote.deleteModel(modelEntity.getModelId());
                System.out.println("Model deleted successfully!\n");
            } catch (ModelNotFoundException ex) {
                System.out.println("An error has occurred while deleting the model: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Model NOT deleted!\n");
        }
    }

    public void doCreateNewCar() {
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: Create New Car ***\n");
        Scanner sc = new Scanner(System.in);

        CarEntity newCarEntity = new CarEntity();
        System.out.print("Enter Car License Plate Number> ");
        newCarEntity.setLicensePlateNo(sc.nextLine().trim());
        System.out.print("Enter Car Colour> ");
        newCarEntity.setColour(sc.nextLine().trim());
        System.out.print("Enter Model ID> ");
        long modelId = sc.nextLong();
        System.out.print("Enter Outlet ID> ");
        long outletId = sc.nextLong();

        try {
            CarEntity carEntity = carEntitySessionBeanRemote.createNewCarEntity(newCarEntity, modelId, outletId);
        } catch (ModelNotFoundException | OutletNotFoundException ex) {
            System.out.println("Invalid model id: " + ex.getMessage() + "\n");
        }
    }

    public void doViewAllCars() {
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: View All Cars ***\n");
        Scanner sc = new Scanner(System.in);

        List<CarEntity> carEntities = carEntitySessionBeanRemote.retrieveAllCars();
        System.out.printf("%8s%15s%20s%15s%15s\n", "Car ID", "Model", "License Plate No", "Colour", "Status");

        carEntities.sort(new SortCar());
        for (CarEntity carEntity : carEntities) {
            System.out.printf("%8s%15s%20s%15s%15s\n", carEntity.getCarId().toString(), carEntity.getModelEntity().getMake(), carEntity.getLicensePlateNo(), carEntity.getColour(), carEntity.getCarStatus());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    public void doViewCarDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** CaRMS Management Client :: Operations Management Module :: View Car Details ***\n");
        System.out.print("Enter Car ID> ");

        Long carId = sc.nextLong();
        try {
            CarEntity carEntity = carEntitySessionBeanRemote.retrieveCarEntityByCarId(carId);
            System.out.printf("%8s%15s%20s%15s%15s\n", "Car ID", "Model", "License Plate No", "Colour", "Status");
            System.out.printf("%8s%15s%20s%15s%15s\n", carEntity.getCarId().toString(), carEntity.getModelEntity().getMake(), carEntity.getLicensePlateNo(), carEntity.getColour(), carEntity.getCarStatus());
            System.out.println("------------------------");
            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            int response = sc.nextInt();

            if (response == 1) {
                doUpdateCar(carEntity);
            } else if (response == 2) {
                doDeleteCar(carEntity);
            }
        } catch (CarNotFoundException ex) {
            System.out.println("An error has occurred while retrieving car: " + ex.getMessage() + "\n");
        }
    }

    public void doUpdateCar(CarEntity carEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: View Car Details :: Update Car ***\n");

        System.out.print("Enter License Plate No (blank if no change)> ");
        String input = sc.nextLine().trim();
        if (input.length() > 0) {
            carEntity.setLicensePlateNo(input);
        }

        System.out.print("Enter Colour (blank if no change)> ");
        String colour = sc.nextLine().trim();
        if (colour.length() > 0) {
            carEntity.setColour(colour);
        }

        System.out.print("Enter Status: \"In Outlet\", \"On Rental\", or \"Repair\", blank if no change)> ");
        String status = sc.nextLine().trim();
        if (status.equals("In Outlet")) {
            carEntity.setRentalStatus(RentalStatusEnum.IN_OUTLET);
        } else if (status.equals("On Rental")) {
            carEntity.setRentalStatus(RentalStatusEnum.ON_RENTAL);
        } else if (status.equals("Repair")) {
            carEntity.setRentalStatus(RentalStatusEnum.REPAIR);
        }

        carEntitySessionBeanRemote.updateCar(carEntity);
    }

    public void doDeleteCar(CarEntity carEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: View Car Details :: Delete Car ***\n");
        System.out.printf("Confirm Delete Car %s (Car ID: %d) (Enter 'Y' to Delete)> ", carEntity.getLicensePlateNo(), carEntity.getCarId());
        String input = sc.nextLine().trim();

        if (input.equals("Y")) {
            try {
                carEntitySessionBeanRemote.deleteCar(carEntity.getCarId());
                System.out.println("Car deleted successfully!\n");
            } catch (CarNotFoundException ex) {
                System.out.println("An error has occurred while deleting the car: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Car NOT deleted!\n");
        }

    }
    
    public void doViewTransitDriverDispatchRecordForTheDay() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: View Transit Driver Dispatch Record for Current Day ***\n");
        String pattern = "DD-mm-YYYY";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        boolean validDate = false;
        while (!validDate) {
            try {
                System.out.print("Enter today's date(DD-MM-YYYY)> ");
                String today = sc.nextLine().trim();
                date = simpleDateFormat.parse(today);
                this.date = date;
                validDate = true;
            } catch (ParseException ex) {
                System.out.println("Invalid date and time entry");
            }
        }
        
        Long outletId = currentEmployee.getOutletEntity().getOutletId();
        
        List<TransitDriverDispatchRecordEntity> transitRecords = transitDriverDispatchRecordSessionBeanRemote.retrieveAllTransitDriverDispatchRecordEntityByOutletAndDate(outletId, date);
        
        //consider including completion status
        if(transitRecords.isEmpty()) {
            System.out.println("There is no transit driver dispatch record for today!");
        } else {
            System.out.printf("%20s%10s%10s%15s%20s%15s\n", "Transit Driver Dispatch Record ID", "License Plate", "Car Make", "Outlet To Pickup", "Reservation Time", "Employee Assigned");
            for(TransitDriverDispatchRecordEntity transitRecord: transitRecords) {
                Date rentalStart = transitRecord.getRentalReservationEntity().getRentalStartTime();
                CarEntity car = transitRecord.getRentalReservationEntity().getCarEntity();
                
                System.out.printf("%20s%10s%10s%15s%20s%15s\n", transitRecord.getTransitDriverDispatchRecordId(), car.getLicensePlateNo(), car.getModelEntity().getMake(), transitRecord.getOutletToPickUp(), rentalStart, transitRecord.getEmployeeEntity());
                
            }
            System.out.println("------------------------");
            System.out.println("1: Assign Transit Driver");
            System.out.println("2: Update Transit As Completed");
            System.out.println("3: Back\n");
            System.out.print("> ");
            int response = sc.nextInt();
            
            if (response == 1) {
                doAssignTransitDriverDispatch();
            } else if (response == 2) {
                doUpdateTransit();
            }
        }
    }
    
    public void doAssignTransitDriverDispatch() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: Assign Transit Driver Dispatch ***\n");
        Long transitDriverDispatchId = 1l;
        
        boolean validChoice = false;
        while (!validChoice) {
            validChoice = true;
            System.out.print("Enter transit driver dispatch ID> ");
            transitDriverDispatchId = sc.nextLong();
            sc.nextLine();
            
            TransitDriverDispatchRecordEntity transitRecord = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitDriverDispatchRecordByTransitDriverRecordId(transitDriverDispatchId);
            
            if(!transitRecord.getOutletEntity().equals(currentEmployee.getOutletEntity())) {
                validChoice = false;
            }
            
            if(!transitRecord.getDate().equals(date)) {
                validChoice = false;
            }
            
            if(transitRecord.getEmployeeEntity() != null) {
                validChoice = false;   
            }  
            
        }
        
        boolean validEmployee = false;
        while (!validEmployee) {
            System.out.println("Remember to input the employee that is working in " + currentEmployee.getOutletEntity().getOutletId());
            System.out.print("Enter employee ID> ");
            Long employeeId = sc.nextLong();
            sc.nextLine();
            try {
                EmployeeEntity employeeAssigned = employeeEntitySessionBeanRemote.retrieveEmployeeEntityByEmployeeId(employeeId);
                if (employeeAssigned.getOutletEntity().equals(currentEmployee.getOutletEntity())) {
                    validEmployee = true;
                    transitDriverDispatchRecordSessionBeanRemote.allocateEmployee(transitDriverDispatchId, employeeId);
                    System.out.println("Employee " + employeeAssigned.getName() + " successfully assigned!");
                }
            } catch (EmployeeNotFoundException ex) {
                System.out.println("Employee not found!");
            }
        }
                
    }
    
    public void doUpdateTransit() {
        Scanner sc = new Scanner(System.in);
        Long transitDriverDispatchId = 1l;
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: Update Transit's Completion Status ***\n");
        boolean validChoice = false;
        while (!validChoice) {
            validChoice = true;
            System.out.println("Remember to input the transit driver dispatch ID that is in this outlet and has today'a date!");
            System.out.print("Enter transit driver dispatch ID> ");
            transitDriverDispatchId = sc.nextLong();
            sc.nextLine();
            
            TransitDriverDispatchRecordEntity transitRecord = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitDriverDispatchRecordByTransitDriverRecordId(transitDriverDispatchId);
            
            if(!transitRecord.getOutletEntity().equals(currentEmployee.getOutletEntity())) {
                validChoice = false;
            }
            
            if(!transitRecord.getDate().equals(date)) {
                validChoice = false;
            }
            
            if(transitRecord.getEmployeeEntity() != null) {
                validChoice = false;   
            }          
        }
        
        
        TransitDriverDispatchRecordEntity transitRecord = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitDriverDispatchRecordByTransitDriverRecordId(transitDriverDispatchId);
        transitRecord.setCompletion(true);
        transitDriverDispatchRecordSessionBeanRemote.updateTransitDriverDispatchRecord(transitRecord);
    }
}
