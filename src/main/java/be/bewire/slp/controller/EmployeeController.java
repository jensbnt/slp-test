package be.bewire.slp.controller;

import be.bewire.slp.domain.Employee;
import be.bewire.slp.exceptions.MalformedBodyException;
import be.bewire.slp.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Routes for managing {@link be.bewire.slp.domain.Employee}
 *
 * @author Jens Beernaert
 */
@RestController
@RequestMapping(value = "/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET route for retrieving all employees
     *
     * @return http response with all users
     */
    @GetMapping
    public Iterable<Employee> findAll() {
        return employeeService.findAll();
    }

    /**
     * GET route for retrieving one employee
     *
     * @param id id of the desired employee
     * @return http response with one employee
     */
    @GetMapping("{id}")
    public Employee findById(@PathVariable int id) {
        return employeeService.findById(id);
    }

    /**
     * POST route to add employee
     *
     * @param employee request body of one employee
     * @return http response with the created employee
     */
    @PostMapping
    public Employee create(@Valid @RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    /**
     * PUT route to update one employee
     *
     * @param id       id of the to-be-updated employee
     * @param employee request body of one employee
     * @return http response with the updated employee
     */
    @PutMapping("{id}")
    public Employee update(@PathVariable String id,
                           @Valid @RequestBody Employee employee) {
        if (Integer.parseInt(id) != employee.getId())
            throw new MalformedBodyException("Url id does not match request body id.");

        return employeeService.update(employee);
    }

    /**
     * DELETE route to delete one employee
     *
     * @param id id of the to-be-deleted employee
     */
    @DeleteMapping("{id}")
    public void delete(@PathVariable int id) {
        employeeService.deleteById(id);
    }
}
