package be.bewire.slp.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Bewire employee domain model
 */
@Entity
public class Employee {

    /**
     * Entity PKI
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(updatable = false, nullable = false)
    private int id;

    /**
     * First name of the employee.
     */
    @Column
    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    /**
     * Last name of the employee.
     */
    @Column
    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    /**
     * Company of the employee.
     * eg. Evance, C4J, ...
     */
    @Column
    @NotBlank(message = "Company is mandatory")
    private String company;

    /**
     * Timestamp of the time of creation.
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date created;

    /**
     * Timestamp of the last update.
     */
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date updated;

    /**
     * Empty constructor (JPA specification).
     */
    public Employee() {
    }



    /**
     * Constructor for values
     *
     * @param firstName Initialize class variable.
     * @param lastName  Initialize class variable.
     * @param company   Initialize class variable.
     */
    public Employee(String firstName, String lastName, String company) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
    }

    /**
     * Constructor for values and PKI
     *
     * @param id        Initialize class variable.
     * @param firstName Initialize class variable.
     * @param lastName  Initialize class variable.
     * @param company   Initialize class variable.
     */
    public Employee(int id, String firstName, String lastName, String company) {
        this(firstName, lastName, company);

        this.id = id;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
