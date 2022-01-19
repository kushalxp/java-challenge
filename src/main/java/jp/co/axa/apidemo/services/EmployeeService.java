package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

     List<Employee> retrieveEmployees();

     Employee getEmployee(Long employeeId);

     Employee saveEmployee(Employee employee);

     Boolean deleteEmployee(Long employeeId);

     Employee updateEmployee(Long employeeId, Employee employee);
}