package nl.jaapcoomans.demo.testdata.gamestore.store;

import java.util.UUID;

public class CustomerTestDataFactory {
    public static Customer.Id aCustomerId() {
        return new Customer.Id(UUID.randomUUID());
    }
}