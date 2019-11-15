/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.CategoryEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.RentalReservationEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import util.enumeration.StatusEnum;
import util.exception.OutletNotFoundException;


@Stateless
@Remote(ReservationSessionBeanRemote.class)
@Local(ReservationSessionBeanLocal.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB(name = "categoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB(name = "outletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    @EJB(name = "modelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;
    
    @EJB(name = "rentalReservationEntitySessionBeanLocal")
    private RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBeanLocal;
    
    private Date rentalStartTime;
    private Date rentalEndTime;
    private OutletEntity pickupOutletEntity;
    private OutletEntity returnOutletEntity;
    List<ModelEntity> availableModels;
    List<CategoryEntity> availableCategories;
    
    @Override
    public List<ModelEntity> searchModels(Date rentalStartTime, Date rentalEndTime, OutletEntity pickupOutletEntity, OutletEntity returnOutletEntity) {
               
        List<CategoryEntity> allCategories = categoryEntitySessionBeanLocal.retrieveAllCategories();
        List<ModelEntity> allModels = modelEntitySessionBeanLocal.retrieveAllModels();
        availableModels = new ArrayList<>();
        availableCategories = new ArrayList<>();

        for(CategoryEntity cat: allCategories){
            Integer catNumReserved = 0;
            List<ModelEntity> availModelsinCat = new ArrayList<>();
            if(!cat.getRentalReservationEntities().isEmpty()) {
                List<RentalReservationEntity> catReservations = cat.getRentalReservationEntities();
                for(RentalReservationEntity catReservation: catReservations) {
                    boolean carCanBeReserved = checkCarIsAvailable(rentalStartTime, rentalEndTime, catReservation.getRentalStartTime(), catReservation.getRentalEndTime(), catReservation.getReturnOutletEntity(), pickupOutletEntity);
                    if(carCanBeReserved == false) {
                        catNumReserved++;
                    }
                }      
            }
            
            System.out.println(cat.getCategoryName().toString() + " has this number of cars reserved based on category " + catNumReserved);
            
            Integer totalAvailCarsInModelsNum = 0;
            List<ModelEntity> modelsinCat = cat.getModelEntities();
            for(ModelEntity model: modelsinCat) {
                Integer availCarsNum = model.getCarEntities().size();
                System.out.println(model.getMake() + " total available initial cars " + availCarsNum);
                if (!model.getRentalReservationEntities().isEmpty()) {
                    List<RentalReservationEntity> modelReservations = model.getRentalReservationEntities();
                    for(RentalReservationEntity modelReservation: modelReservations) {
                        boolean carCanBeReserved = checkCarIsAvailable(rentalStartTime, rentalEndTime, modelReservation.getRentalStartTime(), modelReservation.getRentalEndTime(), modelReservation.getReturnOutletEntity(), pickupOutletEntity);
                        if(carCanBeReserved == false) {
                            availCarsNum--;
                        }
                        System.out.println(model.getMake() + " aft each deducting for 1 reservation " + availCarsNum);
                    }
                }
                 
                if(availCarsNum > 0) {
                    List<CarEntity> carsInModel = model.getCarEntities();
                    for(CarEntity car: carsInModel) {
                        if(car.getCarStatus().equals(StatusEnum.DISABLED)){
                            availCarsNum--;
                        }
                    }
                    totalAvailCarsInModelsNum = totalAvailCarsInModelsNum + availCarsNum;
                    availModelsinCat.add(model);
                    System.out.println("Model is available for renting, and added to availModelsinCat list");
                }
                
                System.out.println("Total num of cars available in the model aft adding (or not) 1 model " + totalAvailCarsInModelsNum);
                
            }
            
            Integer availCarsInCatNum = totalAvailCarsInModelsNum - catNumReserved;
            System.out.println("availCarsInCatNum (the number of cars after removing cars reserved based on models and categories) " + availCarsInCatNum);
            if(availCarsInCatNum > 0) {
                for(ModelEntity availModel : availModelsinCat) {
                    System.out.println("Add model to the list of models that can be booked by customer " + availModel.getMake());
                    availableModels.add(availModel);
                }
                availableCategories.add(cat);
            }  
        }
        
        return availableModels;
        
    }
//        for(ModelEntity model: allModels) {
//            Integer availCarsNum = model.getCarEntities().size();
//            System.out.println(model.getMake() + " at the start " + availCarsNum);
//            if (!model.getRentalReservationEntities().isEmpty()) {
//                List<RentalReservationEntity> modelReservations = model.getRentalReservationEntities();
//                for(RentalReservationEntity modelReservation: modelReservations) {
//                    if(rentalStartTime.before(modelReservation.getRentalEndTime()) && rentalStartTime.after(modelReservation.getRentalStartTime())) {
//                        availCarsNum--;
//                        break;
//                    } else if(rentalEndTime.before(modelReservation.getRentalEndTime()) && rentalEndTime.after(modelReservation.getRentalStartTime())) {
//                        availCarsNum--;
//                        break;
//                    } else if(rentalStartTime.after(modelReservation.getRentalStartTime()) && rentalEndTime.before(modelReservation.getRentalEndTime())) {
//                        availCarsNum--;
//                        break;
//                    } else if(rentalStartTime.before(modelReservation.getRentalStartTime()) && rentalEndTime.after(modelReservation.getRentalEndTime())) {
//                        availCarsNum--;
//                        break;
//                    } else if(rentalStartTime.after(modelReservation.getRentalEndTime())) {
//                        Long difference = rentalStartTime.getTime() - modelReservation.getRentalEndTime().getTime();
//                        Long minDiff = TimeUnit.MILLISECONDS.toMinutes(difference);
//                        if (minDiff < 120 && modelReservation.getReturnOutletEntity().equals(pickupOutletEntity) == false) {
//                            availCarsNum--;
//                            break;
//                        }
//                    }
//                    boolean carCanbeReserved = checkCarIsAvailable(rentalStartTime, rentalEndTime, modelReservation.getRentalStartTime(), modelReservation.getRentalEndTime(), modelReservation.getReturnOutletEntity(), pickupOutletEntity);
//                    
//                    System.out.println(model.getMake() + " aft each deducting for 1 reservation " + availCarsNum);  
//                }
//            }
//            
//            System.out.println(model.getMake() + " aft deducting existing reservations " + availCarsNum);
//            if(availCarsNum != 0) {
//                availableModels.add(model);
//                if (!availableCategories.contains(model.getCategoryEntity())) {
//                    availableCategories.add(model.getCategoryEntity());
//                } 
//                System.out.println("added the model where there are available cars" + model.getMake());
//            }
//        }
//
//        for (ModelEntity modell: availableModels) {
//            System.out.println(modell);
//        }
//        
//        for (CategoryEntity categoryy: availableCategories) {
//            System.out.println(categoryy);
//        }
//        return availableModels;
    
    @Override
    public List<CategoryEntity> searchCategories(Date rentalStartTime, Date rentalEndTime, OutletEntity pickupOutletEntity, OutletEntity returnOutletEntity){
//        searchModels(rentalStartTime, rentalEndTime, pickupOutletEntity, returnOutletEntity);
        
        return availableCategories;
    }
    
    private boolean checkCarIsAvailable(Date rentalStartTime, Date rentalEndTime, Date existingStartTime, Date existingEndTime, OutletEntity existingReturnOutlet, OutletEntity rentalPickupOutlet) {
        boolean check = true;
        System.out.println("Existing start time " + existingStartTime + " and end time " + existingEndTime);
        System.out.println("Existing return outlet " + existingReturnOutlet.getName() + " and Rental pickup outlet " + rentalPickupOutlet.getName());
        System.out.println("Rental start time " + rentalStartTime + " and end time " + rentalEndTime);
        if(existingStartTime.before(rentalEndTime) && existingStartTime.after(rentalStartTime)) {
            check = false;
        } else if(existingEndTime.before(rentalEndTime) && existingEndTime.after(rentalStartTime)) {
            check = false;
        } else if(existingStartTime.before(rentalStartTime) && existingEndTime.after(rentalEndTime)) {
            check = false;
        }  else if(existingEndTime.before(rentalStartTime)) {
            Long difference = rentalStartTime.getTime() - existingEndTime.getTime();
            Long minDiff = TimeUnit.MILLISECONDS.toMinutes(difference);
            if (minDiff < 120 && existingReturnOutlet.equals(rentalPickupOutlet) == false) {
                check = false;
            }
        }
        System.out.println("Car is available for booking" + check);
        return check;
    }
}
