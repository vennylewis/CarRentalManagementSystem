/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.ModelEntitySessionBeanLocal;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RentalRateEntitySessionBeanLocal;
import entity.CarEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalRateEntity;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.enterprise.inject.Model;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.enumeration.CategoryNameEnum;
import util.enumeration.EmployeeTypeEnum;
import util.enumeration.RentalStatusEnum;
import util.exception.CategoryNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;

/**
 *
 * @author Venny
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    
//    consider loading test data here for quick loading;
    @EJB
    private RentalRateEntitySessionBeanLocal rentalRateEntitySessionBeanLocal;

    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;

    @EJB
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;
    
    
    
    public DataInitSessionBean() {
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        try {
            outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(1l);
            employeeEntitySessionBeanLocal.retrieveEmployeeEntityByEmployeeId(1l);
            categoryEntitySessionBeanLocal.retrieveCategoryEntityByCategoryId(1l);
            partnerEntitySessionBeanLocal.retrievePartnerEntityByPartnerId(1l);
        }
        catch (OutletNotFoundException | EmployeeNotFoundException | CategoryNotFoundException ex) {       
            initializeData();
        }

    }
    
    private void initializeData() { 
        try {
          outletEntitySessionBeanLocal.createNewOutlet(new OutletEntity("Outlet A", "Clementi Avenue 1", null, null));
          outletEntitySessionBeanLocal.createNewOutlet(new OutletEntity("Outlet B", "Siglap Road Block 52", null, null));
          outletEntitySessionBeanLocal.createNewOutlet(new OutletEntity("Outlet C", "Sentosa Spring Drive", "10:00", "22:00"));
          
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee A1", EmployeeTypeEnum.SALESMANAGER, "salesmanagerA1", "password"), 1l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee A2", EmployeeTypeEnum.OPSMANAGER, "opsmanagerA2", "password"), 1l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee A3", EmployeeTypeEnum.CUSTOMERSERVICEEXEC, "customerserviceexecA3", "password"), 1l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee A4", EmployeeTypeEnum.EMPLOYEE, "employeeA4", "password"), 1l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee A5", EmployeeTypeEnum.EMPLOYEE, "employeeA5", "password"), 1l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee B1", EmployeeTypeEnum.SALESMANAGER, "salesmanagerB1", "password"), 2l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee B2", EmployeeTypeEnum.OPSMANAGER, "opsmanagerB2", "password"), 2l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee B3", EmployeeTypeEnum.CUSTOMERSERVICEEXEC, "customerserviceexecB3", "password"), 2l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee C1", EmployeeTypeEnum.SALESMANAGER, "salesmanagerC1", "password"), 3l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee C2", EmployeeTypeEnum.OPSMANAGER, "opsmanagerC2", "password"), 3l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Employee C3", EmployeeTypeEnum.CUSTOMERSERVICEEXEC, "customerserviceexecC3", "password"), 3l);
          categoryEntitySessionBeanLocal.createCategoryEntity(new CategoryEntity(CategoryNameEnum.FAMILY_SEDAN));
          categoryEntitySessionBeanLocal.createCategoryEntity(new CategoryEntity(CategoryNameEnum.LUXURY));     
          categoryEntitySessionBeanLocal.createCategoryEntity(new CategoryEntity(CategoryNameEnum.STANDARD_SEDAN));
          categoryEntitySessionBeanLocal.createCategoryEntity(new CategoryEntity(CategoryNameEnum.SUV_MINIVAN));
          
          partnerEntitySessionBeanLocal.createPartnerEntity(new PartnerEntity("Holiday.com", "holiday@gmail.com", "password"));
          
          //Data Init for testing
          modelEntitySessionBeanLocal.createNewModelEntity(new ModelEntity("Toyota", "Corolla"), 3l);
          modelEntitySessionBeanLocal.createNewModelEntity(new ModelEntity("Honda", "Civic"), 3l);
          modelEntitySessionBeanLocal.createNewModelEntity(new ModelEntity("Nissan", "Sunny"), 3l);
          modelEntitySessionBeanLocal.createNewModelEntity(new ModelEntity("Mercedes", "E Class"), 2l);
          modelEntitySessionBeanLocal.createNewModelEntity(new ModelEntity("BMW", "5 Series"), 2l);
          modelEntitySessionBeanLocal.createNewModelEntity(new ModelEntity("Audi", "A6"), 2l);
          
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00A1TC", "Black"), 1l, 1l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00A2TC", "Black"), 1l, 1l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00A3TC", "Black"), 1l, 1l);   
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00B1HC", "Black"), 2l, 2l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00B2HC", "Black"), 2l, 2l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00B3HC", "Black"), 2l, 2l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00C1NS", "Black"), 3l, 3l);
          CarEntity repairCar = carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00C2NS", "Black"), 3l, 3l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00C3NS", "Black"), 3l, 3l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00A4ME", "Black"), 4l, 1l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00B4B5", "Black"), 5l, 2l);
          carEntitySessionBeanLocal.createNewCarEntity(new CarEntity("SS00C4A6", "Black"), 6l, 3l);
          
          repairCar.setRentalStatus(RentalStatusEnum.REPAIR);
          carEntitySessionBeanLocal.updateCar(repairCar);

          rentalRateEntitySessionBeanLocal.createRentalRateEntity(new RentalRateEntity("Default", 100, null, null), 3l);
          rentalRateEntitySessionBeanLocal.createRentalRateEntity(new RentalRateEntity("Weekend Promo", 80, new Date(119, 11, 6, 12, 0), new Date(119, 11, 8, 0, 0)), 3l);
          rentalRateEntitySessionBeanLocal.createRentalRateEntity(new RentalRateEntity("Default", 200, null, null), 1l);
          rentalRateEntitySessionBeanLocal.createRentalRateEntity(new RentalRateEntity("Monday", 310, new Date(119, 11, 2, 0, 0), new Date(119, 11, 2, 23, 59)), 2l);
          rentalRateEntitySessionBeanLocal.createRentalRateEntity(new RentalRateEntity("Tuesday", 320, new Date(119, 11, 3, 0, 0), new Date(119, 11, 3, 23, 59)), 2l);
          rentalRateEntitySessionBeanLocal.createRentalRateEntity(new RentalRateEntity("Wednesday", 330, new Date(119, 11, 4, 0, 0), new Date(119, 11, 4, 23, 59)), 2l);
          rentalRateEntitySessionBeanLocal.createRentalRateEntity(new RentalRateEntity("Weekday Promo", 250, new Date(119, 11, 4, 12, 0), new Date(119, 11, 5, 12, 0)), 2l);
                     
        }
        catch(OutletNotFoundException | CategoryNotFoundException | ModelNotFoundException ex){
            //Remove irrelevent exception when removing the testing data
            ex.printStackTrace();
        }
    }
}

