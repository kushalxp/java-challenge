package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ApiRuntimeException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    private Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    // This is not required due to auto wiring
//    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }

    /**
     * @param employeeId      - for checking if an employee record exists in the system for given employeeId.
     * @param isSaveOperation - flag to decide if to throw an EntityNotFoundException in case of save/update operation.
     * @return
     */
    private Employee getEmployeeById(Long employeeId, boolean isSaveOperation) {
        Optional<Employee> optionalOfEmployee = employeeRepository.findById(employeeId);
        if (!isSaveOperation) {
            return optionalOfEmployee.orElseThrow(() ->
                    new EntityNotFoundException("Given employee Id [" + employeeId + "] is invalid or record does not exist."));
        } else {
            return optionalOfEmployee.orElse(null);
        }
    }

    /**
     *
     * @return -: List of all the existing employees from database
     */
    @Cacheable(value = "employees")
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = null;
        try {
            employees = employeeRepository.findAll();
            if (Objects.isNull(employees) || CollectionUtils.isEmpty(employees)) {
                logger.warn("No records found for given Employees.");
            }
        } catch (Throwable t) {
            logger.error("Error occurred while fetching Employee records.");
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Get all API call Failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employees;
    }

    /**
     *
     * @param employeeId -: Fetch the details of all the employees for given id
     * @return -: employee record
     */
    @Cacheable(cacheNames = "employees", key = "#employeeId")
    public Employee getEmployee(Long employeeId) {
        Employee employee;
        try {
            employee = getEmployeeById(employeeId, false);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw entityNotFoundException;
        } catch (Throwable t) {
            logger.error("Unable to fetch record for Employee [{}]", employeeId);
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Get operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employee;
    }

    public Employee saveEmployee(Employee employee){
        try {
            if (employee != null && getEmployeeById(employee.getId(), true) != null) {
                logger.error("Employee record already exists for id [{}]", employee.getId());
                throw ExceptionUtil.prepareExceptionDetails("Record already exists in the System for given Id [" + employee.getId() + "]", HttpStatus.BAD_REQUEST);
            }
            employee = employeeRepository.save(employee);
        } catch (ApiRuntimeException apiRuntimeException) {
            throw apiRuntimeException;
        } catch (Throwable t) {
            logger.error("Unable to add a new record for Employee [{}]", employee);
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Save operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employee;
    }
    /**
     * @param employeeId - for updating a particular employee record in the database.
     * @param employee   - for updating the record of an Employee in the database for a given EmployeeId.
     * @return - Only the HttpStatus as NO_CONTENT using ResponseEntity object.
     */
    @CachePut(cacheNames = "employees", key = "#employeeId")
    public Employee updateEmployee(Long employeeId, Employee employee) {
        try {
            if (!Objects.equals(employeeId, employee.getId())) {
                throw ExceptionUtil.prepareExceptionDetails("Invalid Payload provided, please check if EmployeeId is correct or not.", HttpStatus.BAD_REQUEST);
            }
            if (getEmployeeById(employeeId, false) != null)
                employeeRepository.save(employee);
        } catch (EntityNotFoundException | ApiRuntimeException apiException) {
            throw apiException;
        } catch (Throwable t) {
            logger.error("Unable to update the record for Employee [{}] with employee details [{}]", employee.getId(), employee);
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Update operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employee;
    }
    /**
     * @param employeeId - Delete the record of an employee from the database
     * @return only the HttpStatus as OK using the ResponseEntity object.
     */
    @CacheEvict(cacheNames = "employees", key = "#employeeId", allEntries = false)
    public Boolean deleteEmployee(Long employeeId){
        try {
            Employee employeeOptional = getEmployeeById(employeeId, false);
            if (employeeOptional != null)
                employeeRepository.delete(employeeOptional);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw entityNotFoundException;
        } catch (Throwable t) {
            logger.error("Unable to delete the record for Employee [{}]", employeeId);
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Delete operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return Boolean.TRUE;
    }


}