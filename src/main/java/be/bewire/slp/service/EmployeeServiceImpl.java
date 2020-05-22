package be.bewire.slp.service;

import be.bewire.slp.domain.Employee;
import be.bewire.slp.exceptions.MalformedBodyException;
import be.bewire.slp.exceptions.ResourceNotFoundException;
import be.bewire.slp.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link be.bewire.slp.service.EmployeeService}
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Find all employees
     *
     * @return all employees
     */
    @Override
    public Iterable<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /**
     * Find one employee by its id and check if employee exists
     *
     * @param id employee id
     * @return the requested employee
     */
    @Override
    public Employee findById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if (!employee.isPresent())
            throw new ResourceNotFoundException(String.format("No employee with id %d.", id));

        return employee.get();
    }

    /**
     * Add a new (valid) employee.
     *
     * @param employee valid employee model
     * @return the created employee
     */
    @Override
    public Employee create(Employee employee) {
        if (employee.getId() != 0)
            throw new MalformedBodyException("Employee cannot have an id already.");

        return employeeRepository.save(employee);
    }

    /**
     * Update an existing employee
     *
     * @param employee valid and existing employee model
     * @return the updated employee
     */
    @Override
    public Employee update(Employee employee) {
        // Check if employee exists
        Employee old = findById(employee.getId());

        // Preserve information
        employee.setCreated(old.getCreated());

        return employeeRepository.save(employee);
    }

    /**
     * Delete an existing employee
     *
     * @param id employee id
     */
    @Override
    public void deleteById(int id) {
        // Check if employee exists
        if (!employeeRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("No employee with id %d.", id));

        employeeRepository.deleteById(id);
    }
}
