package nl.jaapcoomans.demo.testdata.conference.domain;

public class Room {
    private final String name;
    private final int capacity;

    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }
}
