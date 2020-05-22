package be.bewire.slp.controller;

import be.bewire.slp.domain.Employee;
import be.bewire.slp.exceptions.MalformedBodyException;
import be.bewire.slp.exceptions.ResourceNotFoundException;
import be.bewire.slp.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit testing for {@link be.bewire.slp.controller.EmployeeController}
 *
 * @author Jens Beernaert
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    /**
     * Mock object to simulate http requests
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Mock of the employee services
     */
    @MockBean
    private EmployeeService service;

    /**
     * Base url of the api
     */
    private static final String API_URL = "/api/employee/";

    /**
     * Test the basic flow of findAll.
     *
     * @throws Exception /
     */
    @Test
    public void whenGetUsers_thenResponseWithUsers() throws Exception {
        // Arrange
        var expected = Arrays.asList(
                new Employee(1, "John", "Doe", "bewire"),
                new Employee(2, "Sarah", "Doe", "c4j"),
                new Employee(3, "Richard", "Doe", "evance")
        );

        given(service.findAll())
                .willReturn(expected);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(expected.get(0).getId())));

        verify(service, times(1)).findAll();
    }

    /**
     * Test the basic flow of findById.
     *
     * @throws Exception /
     */
    @Test
    public void whenGetUserById_thenResponseWithOneUser() throws Exception {
        // Arrange
        int id = 1;
        var expected = new Employee(1, "John", "Doe", "bewire");

        given(service.findById(id))
                .willReturn(expected);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(API_URL + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)));

        verify(service, times(1)).findById(id);
    }

    /**
     * Test that findById gives 404 status if incorrect id.
     *
     * @throws Exception /
     */
    @Test
    public void whenGetUserByIncorrectId_thenReturn404() throws Exception {
        // Arrange
        int id = 99;

        given(service.findById(id))
                .willThrow(ResourceNotFoundException.class);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(API_URL + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).findById(id);
    }

    /**
     * Test the basic flow of create.
     *
     * @throws Exception /
     */
    @Test
    public void whenCreate_thenReturnCreatedEmployee() throws Exception {
        // Arrange
        var body = new Employee("John", "Doe", "bewire");
        var expected = new Employee(1, "John", "Doe", "bewire");

        given(service.create(Mockito.any(Employee.class)))
                .willReturn(expected);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post(API_URL)
                .content(asJsonString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

        verify(service, times(1)).create(Mockito.any(Employee.class));
    }

    /**
     * Create should give 500 status if there is an id in the request body.
     *
     * @throws Exception /
     */
    @Test
    public void whenCreateWithInvalidBody_thenReturn500() throws Exception {
        // Arrange
        var body = new Employee(1, "John", "Doe", "bewire");

        given(service.create(Mockito.any(Employee.class)))
                .willThrow(MalformedBodyException.class);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post(API_URL)
                .content(asJsonString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).create(Mockito.any(Employee.class));
    }

    /**
     * Test the basic flow of update.
     *
     * @throws Exception /
     */
    @Test
    public void whenUpdate_thenReturnUpdatedEmployee() throws Exception {
        // Arrange
        var body = new Employee("John", "Doe", "evance");
        var expected = new Employee(1, "John", "Doe", "evance");

        given(service.update(Mockito.any(Employee.class)))
                .willReturn(expected);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(API_URL + body.getId())
                .content(asJsonString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

        verify(service, times(1)).update(Mockito.any(Employee.class));
    }

    /**
     * Update should throw 404 when the employee does not exist.
     *
     * @throws Exception /
     */
    @Test
    public void whenUpdateNonExistingEmployee_thenReturn404() throws Exception {
        // Arrange
        var body = new Employee(99, "John", "Doe", "bewire");

        given(service.update(Mockito.any(Employee.class)))
                .willThrow(ResourceNotFoundException.class);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(API_URL + 99)
                .content(asJsonString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).update(Mockito.any(Employee.class));
    }

    /**
     * Update should throw 400 when url id does not match request body id.
     *
     * @throws Exception /
     */
    @Test
    public void whenUpdateWithDifferentIds_thenReturn500() throws Exception {
        // Arrange
        var body = new Employee(1, "John", "Doe", "bewire");

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(API_URL + 99)
                .content(asJsonString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).update(Mockito.any(Employee.class));
    }

    /**
     * Test the basic flow of delete.
     *
     * @throws Exception /
     */
    @Test
    public void whenDeleteUserById_thenResponseOk() throws Exception {
        // Arrange
        int id = 1;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete(API_URL + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(id);
    }

    /**
     * Delete should give 404 status if the employee does not exist.
     *
     * @throws Exception /
     */
    @Test
    public void whenDeleteUserByIncorrectId_thenReturn404() throws Exception {
        // Arrange
        int id = 99;

        doThrow(ResourceNotFoundException.class)
                .when(service)
                .deleteById(any(Integer.class));

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete(API_URL + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).deleteById(id);
    }

    /**
     * Helper function
     * Maps an object to a json string
     *
     * @param obj Object you want to map.
     * @return Json string
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
