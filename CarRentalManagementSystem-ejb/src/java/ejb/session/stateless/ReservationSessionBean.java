/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
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
    
    private List<ModelEntity> searchCars(Date rentalStartTime, Date rentalEndTime, Long pickupOutletEntityId, Long returnOutletEntityId) throws OutletNotFoundException {
        try{
            OutletEntity pickupOutletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(pickupOutletEntityId);
            OutletEntity returnOutletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(returnOutletEntityId);
    //        List<RentalReservationEntity> rentalReservations = rentalReservationEntitySessionBeanLocal.retrieveAllReservations();
            List<ModelEntity> allModels = modelEntitySessionBeanLocal.retrieveAllModels();
    //        availableModels = new ArrayList<>();

            for(ModelEntity model: allModels) {
                Integer availCarsNum = model.getCarEntities().size();
                System.out.println(model.getName() + " " + availCarsNum);
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
                    }
                    System.out.println(model.getName() + " " + availCarsNum);
                    if(availCarsNum != 0) {
                        availableModels.add(model);
                    }
                }
            }

        } catch(OutletNotFoundException ex) {
            ex.getMessage();
        }
        
            return availableModels;
    }   
}
