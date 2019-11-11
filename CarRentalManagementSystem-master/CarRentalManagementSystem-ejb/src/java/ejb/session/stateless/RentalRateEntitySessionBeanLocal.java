package ejb.session.stateless;

import entity.RentalRateEntity;
import java.util.List;
import util.exception.CategoryNotFoundException;
import util.exception.RentalRateNotFoundException;

public interface RentalRateEntitySessionBeanLocal {

    public RentalRateEntity createRentalRateEntity(RentalRateEntity newRentalRateEntity, Long categoryId) throws CategoryNotFoundException;
    public List<RentalRateEntity> retrieveAllRentalRates();
    public RentalRateEntity retrieveRentalRateEntityByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;

}
