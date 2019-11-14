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
import util.exception.OutletNotFoundException;


@Stateless
@Remote(ReservationSessionBeanRemote.class)
@Local(ReservationSessionBeanLocal.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

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
               
        List<ModelEntity> allModels = modelEntitySessionBeanLocal.retrieveAllModels();
        availableModels = new ArrayList<>();
        availableCategories = new ArrayList<>();

        for(ModelEntity model: allModels) {
            Integer availCarsNum = model.getCarEntities().size();
            System.out.println(model.getMake() + " at the start " + availCarsNum);
            if (!model.getRentalReservationEntities().isEmpty()) {
                List<RentalReservationEntity> modelReservations = model.getRentalReservationEntities();
                for(RentalReservationEntity modelReservation: modelReservations) {
                    if(rentalStartTime.before(modelReservation.getRentalEndTime()) && rentalStartTime.after(modelReservation.getRentalStartTime())) {
                        availCarsNum--;
                        break;
                    } else if(rentalEndTime.before(modelReservation.getRentalEndTime()) && rentalEndTime.after(modelReservation.getRentalStartTime())) {
                        availCarsNum--;
                        break;
                    } else if(rentalStartTime.after(modelReservation.getRentalStartTime()) && rentalEndTime.before(modelReservation.getRentalEndTime())) {
                        availCarsNum--;
                        break;
                    } else if(rentalStartTime.before(modelReservation.getRentalStartTime()) && rentalEndTime.after(modelReservation.getRentalEndTime())) {
                        availCarsNum--;
                        break;
                    } else if(rentalStartTime.after(modelReservation.getRentalEndTime())) {
                        Long difference = rentalStartTime.getTime() - modelReservation.getRentalEndTime().getTime();
                        Long minDiff = TimeUnit.MILLISECONDS.toMinutes(difference);
                        if (minDiff < 120 && modelReservation.getReturnOutletEntity().equals(pickupOutletEntity) == false) {
                            availCarsNum--;
                            break;
                        }
                    }
                    
                    System.out.println(model.getMake() + " aft each deducting for 1 reservation " + availCarsNum);  
                }
            }
            
            System.out.println(model.getMake() + " aft deducting existing reservations " + availCarsNum);
            if(availCarsNum != 0) {
                availableModels.add(model);
                if (!availableCategories.contains(model.getCategoryEntity())) {
                    availableCategories.add(model.getCategoryEntity());
                } 
                System.out.println("added the model where there are available cars" + model.getMake());
            }
        }

        for (ModelEntity modell: availableModels) {
            System.out.println(modell);
        }
        
        for (CategoryEntity categoryy: availableCategories) {
            System.out.println(categoryy);
        }
        return availableModels;
    } 
    
    @Override
    public List<CategoryEntity> searchCategories(Date rentalStartTime, Date rentalEndTime, OutletEntity pickupOutletEntity, OutletEntity returnOutletEntity){
        searchModels(rentalStartTime, rentalEndTime, pickupOutletEntity, returnOutletEntity);
        
        return availableCategories;
    }
}
