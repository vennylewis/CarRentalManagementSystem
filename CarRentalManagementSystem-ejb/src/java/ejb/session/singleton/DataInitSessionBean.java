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
          outletEntitySessionBeanLocal.createNewOutlet(new OutletEntity("Clementi Avenue 1", "0900 - 2100"));
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Sales Manager", EmployeeTypeEnum.SALESMANAGER, "salesmanager", "password"), 1l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Ops Manager", EmployeeTypeEnum.OPSMANAGER, "opsmanager", "password"), 1l);
          employeeEntitySessionBeanLocal.createEmployeeEntity(new EmployeeEntity("Customer Service Exec", EmployeeTypeEnum.CUSTOMERSERVICEEXEC, "customerserviceexec", "password"), 1l);
          categoryEntitySessionBeanLocal.createCategoryEntity(new CategoryEntity(CategoryNameEnum.LUXURY));
          categoryEntitySessionBeanLocal.createCategoryEntity(new CategoryEntity(CategoryNameEnum.FAMILY_SEDAN));
          categoryEntitySessionBeanLocal.createCategoryEntity(new CategoryEntity(CategoryNameEnum.STANDARD_SEDAN));
          categoryEntitySessionBeanLocal.createCategoryEntity(new CategoryEntity(CategoryNameEnum.SUV_MINIVAN));
        }
        catch(OutletNotFoundException ex){
            ex.printStackTrace();
            //what does this mean
        }
    }
}

