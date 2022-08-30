package nl.jaapcoomans.demo.testdata.gamestore.store;

import java.util.UUID;

public class Customer {
    private final Id id;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final Address homeAddress;

    public Customer(Id id, String firstName, String lastName, String emailAddress, Address homeAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.homeAddress = homeAddress;
    }

    public Id getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public record Id(UUID value){}
}
