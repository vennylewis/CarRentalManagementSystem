/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;

public interface EmployeeEntitySessionBeanLocal {
    public EmployeeEntity createEmployeeEntity(EmployeeEntity newEmployeeEntity, Long outletId) throws OutletNotFoundException;
    
    public List<EmployeeEntity> retrieveAllEmployees();
    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(Long employeeId) throws EmployeeNotFoundException;
}
