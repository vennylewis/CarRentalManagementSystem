/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.enumeration.CategoryNameEnum;
import util.enumeration.EmployeeTypeEnum;
import util.exception.CategoryNotFoundException;
import util.exception.EmployeeNotFoundException;
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
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    
    public DataInitSessionBean() {
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        try {
            outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(1l);
            employeeEntitySessionBeanLocal.retrieveEmployeeEntityByEmployeeId(1l);
            categoryEntitySessionBeanLocal.retrieveCategoryEntityByCategoryId(1l);
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
        }
        catch(OutletNotFoundException ex){
            ex.printStackTrace();
        }
    }
}

