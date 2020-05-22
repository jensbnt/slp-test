package be.bewire.slp.service;

import be.bewire.slp.domain.Employee;
import be.bewire.slp.exceptions.MalformedBodyException;
import be.bewire.slp.exceptions.ResourceNotFoundException;
import be.bewire.slp.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit testing for {@link be.bewire.slp.service.EmployeeServiceImpl}
 */
@ExtendWith(SpringExtension.class)
public class EmployeeServiceImplTests {

    /**
     * Test Configuration.
     */
    @TestConfiguration
    static class EmployeeServiceImplTestsContextConfiguration {

        @Autowired
        private EmployeeRepository employeeRepository;

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeServiceImpl(employeeRepository);
        }
    }

    /**
     * Bean of the employee service that will bi tested.
     * This will be supplied by the test configuration.
     */
    @Autowired
    private EmployeeService employeeService;

    /**
     * Mock bean of the employee repository.
     */
    @MockBean
    private EmployeeRepository employeeRepositoryMock;

    /**
     * Test the basic flow of findAll.
     */
    @Test
    public void whenFindAll_thenReturnAllEmployees() {
        // Arrange
        var expected = Arrays.asList(
                new Employee(1, "John", "Doe", "bewire"),
                new Employee(2, "Sarah", "Doe", "c4j"),
                new Employee(3, "Richard", "Doe", "evance")
        );
        when(employeeRepositoryMock.findAll())
                .thenReturn(expected);

        // Act
        var result = employeeService.findAll();

        // Assert
        assertIterableEquals(expected, result);
        verify(employeeRepositoryMock, times(1)).findAll();
    }

    /**
     * Test the basic flow of findById.
     */
    @Test
    public void whenFindByIdWithCorrectId_thenReturnOneEmployee() {
        // Arrange
        var id = 1;
        var expected = new Employee(1, "John", "Doe", "bewire");
        when(employeeRepositoryMock.findById(id))
                .thenReturn(Optional.of(expected));

        // Act
        var result = employeeService.findById(id);

        // Assert
        assertSame(expected, result);
        verify(employeeRepositoryMock, times(1)).findById(anyInt());
    }

    /**
     * Test that findById should throw employee when incorrect id.
     */
    @Test
    public void whenFindByIdWithIncorrectId_thenThrowResourceNotFoundException() {
        // Arrange
        var id = 99;
        when(employeeRepositoryMock.findById(id))
                .thenReturn(Optional.empty());

        // Act
        Executable executable = () -> employeeService.findById(id);

        // Assert
        assertThrows(ResourceNotFoundException.class, executable);
        verify(employeeRepositoryMock, times(1)).findById(anyInt());
    }

    /**
     * Test the basic flow of create.
     */
    @Test
    public void whenCreate_thenCreateNewEmployeeAndReturnOneEmployeeWithId() {
        // Arrange
        var clean = new Employee("John", "Doe", "bewire");
        var expected = new Employee(1, "John", "Doe", "bewire");
        when(employeeRepositoryMock.save(clean))
                .thenReturn(expected);

        // Act
        var result = employeeService.create(clean);

        // Assert
        assertSame(expected, result);
        verify(employeeRepositoryMock, times(1)).save(any(Employee.class));
    }

    /**
     * Test that create should throw employee if employee already has an id.
     */
    @Test
    public void whenCreateWithDirtyBody_thenThrowBadRequestException() {
        // Arrange
        var dirty = new Employee(1, "John", "Doe", "bewire");

        // Act
        Executable executable = () -> employeeService.create(dirty);

        // Assert
        assertThrows(MalformedBodyException.class, executable);
        verify(employeeRepositoryMock, times(0)).save(any(Employee.class));
    }

    /**
     * Test the basic flow of update.
     */
    @Test
    public void whenUpdate_thenUpdateEmployeeAndReturnUpdatedEmployee() {
        // Arrange
        var employee = new Employee(1, "John", "Doe", "bewire");
        var expected = new Employee(1, "John", "Doe", "evance");

        when(employeeRepositoryMock.findById(employee.getId()))
                .thenReturn(Optional.of(employee));

        employee.setCompany("evance");
        when(employeeRepositoryMock.save(employee))
                .thenReturn(expected);

        // Act
        var result = employeeService.update(employee);

        // Assert
        assertSame(expected, result);
        verify(employeeRepositoryMock, times(1)).findById(anyInt());
        verify(employeeRepositoryMock, times(1)).save(any(Employee.class));
    }

    /**
     * Test if update throws exception when the employee does not exist.
     */
    @Test
    public void whenUpdateUnregisteredEmployee_thenThrowResourceNotFoundException() {
        // Arrange
        var employee = new Employee(99, "John", "Doe", "bewire");

        when(employeeRepositoryMock.findById(employee.getId()))
                .thenReturn(Optional.empty());

        // Act
        Executable executable = () -> employeeService.update(employee);

        // Assert
        assertThrows(ResourceNotFoundException.class, executable);
        verify(employeeRepositoryMock, times(1)).findById(anyInt());
        verify(employeeRepositoryMock, times(0)).save(any(Employee.class));
    }

    /**
     * Test the basic flow of deleteById.
     */
    @Test
    public void whenDeleteByIdWithCorrectId_thenDeleteOneEmployee() {
        // Arrange
        var id = 1;
        when(employeeRepositoryMock.existsById(id))
                .thenReturn(true);

        // Act
        Executable executable = () -> employeeService.deleteById(id);

        // Assert
        assertDoesNotThrow(executable);
        verify(employeeRepositoryMock, times(1)).existsById(anyInt());
        verify(employeeRepositoryMock, times(1)).deleteById(anyInt());
    }

    /**
     * Test that deleteById throws exception when the employee does not exist.
     */
    @Test
    public void whenDeleteByIdWithIncorrectId_thenThrowResourceNotFoundException() {
        // Arrange
        var id = 99;
        when(employeeRepositoryMock.existsById(id))
                .thenReturn(false);

        // Act
        Executable executable = () -> employeeService.deleteById(id);

        // Assert
        assertThrows(ResourceNotFoundException.class, executable);
        verify(employeeRepositoryMock, times(1)).existsById(anyInt());
        verify(employeeRepositoryMock, times(0)).deleteById(anyInt());
    }
}
