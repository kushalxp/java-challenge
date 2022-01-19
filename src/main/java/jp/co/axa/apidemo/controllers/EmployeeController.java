package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

/*
    This is not required due to auto wiring
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
 */

    @GetMapping("/employees")
    @ApiOperation(value = "API will return list of all the employees exist in the database."
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "Return list of all existing Employees from database."
            , httpMethod = "GET"
            , response = Employee.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Employee.class, responseContainer = "List")})
    public ResponseEntity<List<Employee>> getEmployees() {
        if (logger.isDebugEnabled()) {
            logger.debug(" Get all employee API called.");
        }
        List<Employee> employees = employeeService.retrieveEmployees();
        return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    @ApiOperation(value = "API will return the data of employee based on input Id"
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "Return the all the data of employee based on the passed employee id"
            , httpMethod = "GET"
            , response = Employee.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Employee.class)})
    public ResponseEntity<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting Employee details for [{}]", employeeId);
        }
        Employee employee = employeeService.getEmployee(employeeId);
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }

    @PostMapping("/employees")
    @ApiOperation(value = "API will create an employee record when a valid data is provided."
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "Register the given valid data in database"
            , httpMethod = "POST"
            , response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public ResponseEntity<Void> saveEmployee(@Valid @RequestBody Employee employee){
        if (logger.isDebugEnabled()) {
            logger.debug("Saving Employee details with ID [{}]", employee.getId());
        }
        Employee savedEmployee = employeeService.saveEmployee(employee);
        logger.info("Employee Saved Successfully");
        if (logger.isDebugEnabled()) {
            logger.debug("Employee record added successfully for Employee [{}].", savedEmployee.getId());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/employees/{employeeId}")
    @ApiOperation(value = "API will delete the data of given employee in case the employee id is valid"
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "Delete the employee record when a valid employeeId is provided."
            , httpMethod = "DELETE"
            , response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Deleted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        if (logger.isDebugEnabled()) {
            logger.debug("Deleting employee details with ID [{}]", employeeId);
        }
        Boolean result = employeeService.deleteEmployee(employeeId);
        if (logger.isDebugEnabled()) {
            logger.debug("Deleted successfully with Result: " + result);
        }
        logger.info("Employee Deleted Successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/employees/{employeeId}")
    @ApiOperation(value = "API will update the data of employee record in case the employee id is valid."
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "Update the employee record when a valid employee id is provided."
            , httpMethod = "PUT"
            , response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully Updated"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public ResponseEntity<Void>  updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        if (logger.isDebugEnabled()) {
            logger.debug("Updating employee details for ID [{}]", employeeId);
        }
        Employee emp = employeeService.getEmployee(employeeId);

        if (logger.isDebugEnabled()) {
            logger.debug("Updated employee details successfully.");
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

}
