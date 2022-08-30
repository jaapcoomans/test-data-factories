package nl.jaapcoomans.demo.testdata.gamestore.catalog;

import java.util.UUID;

public class Publisher {
    private final Id id;
    private final String name;
    private final String country;

    public Publisher(Id id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Id getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public record Id(UUID value){}
}
