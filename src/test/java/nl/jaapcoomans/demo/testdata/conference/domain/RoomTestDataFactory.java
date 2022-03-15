package nl.jaapcoomans.demo.testdata.conference.domain;

import com.github.javafaker.Faker;

public class RoomTestDataFactory {
    private static final Faker faker = new Faker();

    public static Room aRoom(int number) {
        return new Room(aRoomName(number), aCapacity());
    }

    private static String aRoomName(int number) {
        return "Room " + number;
    }

    private static int aCapacity() {
        return faker.number().numberBetween(50, 500);
    }
}
