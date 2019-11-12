/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ModelEntity;
import entity.OutletEntity;
import java.util.Date;
import java.util.List;

public interface ReservationSessionBeanLocal {
    public List<ModelEntity> searchCars(Date rentalStartTime, Date rentalEndTime, OutletEntity pickupOutletEntity, OutletEntity returnOutletEntity);
}
