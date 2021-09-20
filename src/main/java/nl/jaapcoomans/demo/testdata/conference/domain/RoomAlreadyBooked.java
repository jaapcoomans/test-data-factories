package nl.jaapcoomans.demo.testdata.conference.domain;

import java.text.MessageFormat;

public class RoomAlreadyBooked extends RuntimeException {
    public RoomAlreadyBooked(Room room, TimeSlot timeSlot) {
        super(createMessage(room, timeSlot));
    }

    private static String createMessage(Room room, TimeSlot timeSlot) {
        return MessageFormat.format("Room {} is already booked for {}.", room.getName(), timeSlot.toString());
    }
}
