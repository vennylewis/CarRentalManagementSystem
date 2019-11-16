/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.CategoryEntity;
import entity.ModelEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.exception.CreateNewRentalReservationException;
import util.exception.CustomerNotFoundException;

public interface ReservationSessionBeanRemote {
    public void triggerSearch(Date rentalStartTime, Date rentalEndTime, Long pickupOutletEntityId, Long returnOutletEntityId);
    public void endSearch();
    public List<CategoryEntity> getCategories();
    public List<ModelEntity> getModels();
    public ArrayList<Double> getRentalFeePerCategory();
    public void createRentalReservation(Long categoryId, Long modelId, Long customerId, Long ccNum, boolean paymentStatus) throws CustomerNotFoundException, CreateNewRentalReservationException;
    
}
