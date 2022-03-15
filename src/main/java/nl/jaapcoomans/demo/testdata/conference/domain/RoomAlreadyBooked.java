package nl.jaapcoomans.demo.testdata.conference.domain;

public class RoomAlreadyBooked extends RuntimeException {
    public RoomAlreadyBooked(Room room, TimeSlot timeSlot) {
        super(createMessage(room, timeSlot));
    }

    private static String createMessage(Room room, TimeSlot timeSlot) {
        return String.format("Room %s is already booked for %s.", room.getName(), timeSlot.toString());
    }
}
