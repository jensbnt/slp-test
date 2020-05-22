package be.bewire.slp.repository;

import be.bewire.slp.domain.Employee;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface for generic CRUD operations on a repository for a {@link be.bewire.slp.domain.Employee}.
 *
 * @author Jens Beernaert
 */
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}
