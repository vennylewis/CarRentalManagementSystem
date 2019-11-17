package ejb.session.ws;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RentalReservationEntitySessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalReservationEntity;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.enumeration.CategoryNameEnum;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotFoundException;
import util.exception.NoRentalRateApplicableException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

@WebService(serviceName = "CaRMSWebService")
@Stateless()
public class CaRMSWebService {

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBean;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBean;

    @EJB
    private ModelEntitySessionBeanLocal modelEntitySessionBean;
    
    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBean;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBean;
    
    // web methods organised in order of usage in MainApp (Holiday Reservation System Client)
    
    @WebMethod
    public PartnerEntity login(@WebParam String email, String password) throws InvalidLoginCredentialException {
        return partnerEntitySessionBean.login(email, password);
    }
    
    @WebMethod
    public List<OutletEntity> retrieveAllOutlets() {
        return outletEntitySessionBean.retrieveAllOutlets();
    }
    
    @WebMethod
    public OutletEntity retrieveOutletEntityByOutletId(@WebParam Long outletId) throws OutletNotFoundException {
        return outletEntitySessionBean.retrieveOutletEntityByOutletId(outletId);
    }
    
    @WebMethod
    public List<ModelEntity> searchModels(@WebParam Date rentalStartTime, Date rentalEndTime, OutletEntity pickupOutletEntity, OutletEntity returnOutletEntity) {
        return reservationSessionBean.searchModels(rentalStartTime, rentalEndTime, pickupOutletEntity, returnOutletEntity);
    }
    
    @WebMethod
    public List<CategoryEntity> searchCategories(@WebParam Date rentalStartTime, Date rentalEndTime, OutletEntity pickupOutletEntity, OutletEntity returnOutletEntity){
        return reservationSessionBean.searchCategories(rentalStartTime, rentalEndTime, pickupOutletEntity, returnOutletEntity);
    }
    
    @WebMethod
    public double calculateRentalFee(@WebParam CategoryEntity category, Date rentalStart, Date rentalEnd) throws NoRentalRateApplicableException {
        return reservationSessionBean.calculateRentalFee(category, rentalStart, rentalEnd);
    }
    
    @WebMethod
    public CategoryNameEnum getCategoryNamebyCategoryId(Long categoryId) throws CategoryNotFoundException {
        return categoryEntitySessionBean.getCategoryNamebyCategoryId(categoryId);
    }
    
    @WebMethod
    public ModelEntity retrieveModelEntityByModelId(@WebParam Long modelId) throws ModelNotFoundException {
        return modelEntitySessionBean.retrieveModelEntityByModelId(modelId);
    }
    
    @WebMethod
    public CategoryEntity retrieveCategoryEntityByCategoryId(@WebParam Long categoryId) throws CategoryNotFoundException {
        return categoryEntitySessionBean.retrieveCategoryEntityByCategoryId(categoryId);
    }
    
    @WebMethod
    public CustomerEntity retrieveCustomerEntitybyPassportNumber(@WebParam String passportNumber) throws CustomerNotFoundException {
        return customerEntitySessionBean.retrieveCustomerEntitybyPassportNumber(passportNumber);
    }
    
    @WebMethod
    public CustomerEntity createCustomerEntity(@WebParam CustomerEntity newCustomerEntity) {
        return customerEntitySessionBean.createCustomerEntity(newCustomerEntity);
    }
           
    @WebMethod
    public RentalReservationEntity createRentalReservationEntity(@WebParam RentalReservationEntity newRentalReservationEntity, Long customerId, Long returnOutletId, Long pickupOutletId) throws OutletNotFoundException {
        return rentalReservationEntitySessionBean.createRentalReservationEntity(newRentalReservationEntity, customerId, returnOutletId, pickupOutletId);
    }
    
    @WebMethod
    public void updateRentalReservation(@WebParam RentalReservationEntity rentalReservationEntity) {
        rentalReservationEntitySessionBean.updateRentalReservation(rentalReservationEntity);
    }
    
    @WebMethod
    public void setCategory(@WebParam Long rentalReservationEntityId, Long categoryEntityId) {
        rentalReservationEntitySessionBean.setCategory(rentalReservationEntityId, categoryEntityId);
    }
    
    @WebMethod
    public void setModel(@WebParam Long rentalReservationEntityId, Long modelEntityId) {
        rentalReservationEntitySessionBean.setModel(rentalReservationEntityId, modelEntityId);
    }
    
    @WebMethod
    public CustomerEntity retrieveCustomerEntityByCustomerId(@WebParam Long customerId) {
        return customerEntitySessionBean.retrieveCustomerEntityByCustomerId(customerId);
    }
    
    @WebMethod
    public RentalReservationEntity retrieveRentalReservationEntityByRentalReservationId(@WebParam Long rentalReservationId) throws RentalReservationNotFoundException {
        return rentalReservationEntitySessionBean.retrieveRentalReservationEntityByRentalReservationId(rentalReservationId);
    }
    
    @WebMethod
    public void deleteRentalReservation(@WebParam Long rentalReservationId) throws RentalReservationNotFoundException {
        rentalReservationEntitySessionBean.deleteRentalReservation(rentalReservationId);
    }
}
