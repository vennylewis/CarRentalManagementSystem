package carmsmanagementclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import entity.CarEntity;
import entity.EmployeeEntity;
import entity.ModelEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.comparator.SortCar;
import util.comparator.SortModel;
import util.comparator.SortRentalRate;
import util.enumeration.StatusEnum;
import util.exception.CarNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.ModelNotFoundException;

public class OperationsManagementModule {

    private EmployeeEntity currentEmployee;
    private ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;

    public OperationsManagementModule() {
    }

    public OperationsManagementModule(EmployeeEntity currentEmployee, ModelEntitySessionBeanRemote modelEntitySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote) {
        this();

        this.currentEmployee = currentEmployee;
        this.modelEntitySessionBeanRemote = modelEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
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
            System.out.println("7: Voew Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("8: Assign Transit Driver");
            System.out.println("9: Update Transit As Completed");
            System.out.println("10: Logout\n");
            response = 0;

            while (response < 1 || response > 10) {
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

                } else if (response == 8) {
                    System.out.println("Assign Transit Drivers\n");

                } else if (response == 9) {
                    System.out.println("Update Transit as Completed \n");

                } else if (response == 10) {
                    //Should do log out instead
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 10) {
                //should not be break but log out?
                break;
            }
        }

    }

    public void doCreateNewModel() {
        System.out.println("*** CaRMS Management Client :: Operations Management Module :: Create New Model ***\n");
        Scanner sc = new Scanner(System.in);

        ModelEntity newModelEntity = new ModelEntity();
        System.out.print("Enter Model Name> ");
        newModelEntity.setName(sc.nextLine().trim());
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
        System.out.printf("%8s%15s%30s%15s\n", "Model ID", "Category", "Name", "Status");
        
        modelEntities.sort(new SortModel());
        
        for (ModelEntity modelEntity : modelEntities) {
            System.out.printf("%8s%15s%30s%15s\n", modelEntity.getModelId().toString(), modelEntity.getCategoryEntity().getCategoryName(), modelEntity.getName(), modelEntity.getModelStatus());
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
            System.out.printf("%8s%15s%30s%15s\n", modelEntity.getModelId().toString(), modelEntity.getCategoryEntity().getCategoryName(), modelEntity.getName(), modelEntity.getModelStatus());
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
            modelEntity.setName(input);
        }

        // I didn't include update functionality for status cos I think it should be updated in other use cases
        modelEntitySessionBeanRemote.updateModel(modelEntity);
    }

    public void doDeleteModel(ModelEntity modelEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Model Details :: Delete Model ***\n");
        System.out.printf("Confirm Delete Model %s (Model ID: %d) (Enter 'Y' to Delete)> ", modelEntity.getName(), modelEntity.getModelId());
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

        try {
            carEntitySessionBeanRemote.createNewCarEntity(newCarEntity, modelId);
        } catch (ModelNotFoundException ex) {
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
            System.out.printf("%8s%15s%20s%15s%15s\n", carEntity.getCarId().toString(), carEntity.getModelEntity().getName(), carEntity.getLicensePlateNo(), carEntity.getColour(), carEntity.getCarStatus());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    public void doViewCarDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Car Details ***\n");
        System.out.print("Enter Car ID> ");

        Long carId = sc.nextLong();
        try {
            CarEntity carEntity = carEntitySessionBeanRemote.retrieveCarEntityByCarId(carId);
            System.out.printf("%8s%15s%20s%15s%15s\n", "Car ID", "Model", "License Plate No", "Colour", "Status");
            System.out.printf("%8s%15s%20s%15s%15s\n", carEntity.getCarId().toString(), carEntity.getModelEntity().getName(), carEntity.getLicensePlateNo(), carEntity.getColour(), carEntity.getCarStatus());
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
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Car Details :: Update Car ***\n");

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

        // I didn't include update functionality for status cos I think it should be updated in other use cases
        // like when the car is rented it will be changed to USED
        carEntitySessionBeanRemote.updateCar(carEntity);
    }

    public void doDeleteCar(CarEntity carEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Management Client :: Sales Management Module :: View Car Details :: Delete Car ***\n");
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
}
