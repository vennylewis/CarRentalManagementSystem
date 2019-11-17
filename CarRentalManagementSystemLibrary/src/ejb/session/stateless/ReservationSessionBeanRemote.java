/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import java.util.Date;
import java.util.List;
import util.exception.NoRentalRateApplicableException;


public interface ReservationSessionBeanRemote {
    public double calculateRentalFee(CategoryEntity category, Date rentalStart, Date rentalEnd) throws NoRentalRateApplicableException;

    public List<CategoryEntity> searchCategories(Date rentalStartTime, Date rentalEndTime, OutletEntity pickupOutletEntity, OutletEntity returnOutletEntity);

    public List<ModelEntity> searchModels(Date rentalStartTime, Date rentalEndTime, OutletEntity pickupOutletEntity, OutletEntity returnOutletEntity);
    
}
