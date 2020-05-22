package be.bewire.slp.service;

import be.bewire.slp.domain.Employee;
import be.bewire.slp.exceptions.MalformedBodyException;
import be.bewire.slp.exceptions.ResourceNotFoundException;

/**
 * Interface for user services based around {@link be.bewire.slp.domain.Employee}.
 */
public interface EmployeeService {
    Iterable<Employee> findAll();
    Employee findById(int id) throws ResourceNotFoundException;
    Employee create(Employee employee) throws MalformedBodyException;
    Employee update(Employee employee) throws ResourceNotFoundException;
    void deleteById(int id) throws ResourceNotFoundException;
}
