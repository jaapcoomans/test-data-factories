package nl.jaapcoomans.demo.testdata.gamestore.catalog;

import java.util.UUID;

public class PublisherTestDataFactory {
    public static Publisher.Id aPublisherId() {
        return new Publisher.Id(UUID.randomUUID());
    }
}