package nl.jaapcoomans.demo.testdata.conference.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Schedule {
    private Conference.Id conferenceId;
    private List<Room> availableRooms;
    private List<TimeSlot> availableTimeSlots;
    private final Set<ScheduledSession> scheduledSessions = new HashSet<>();

    public void scheduleSession(Session session, Room room, TimeSlot timeSlot) {
        validateIfRoomIsAvailable(room, timeSlot);
        unscheduleSession(session);
        scheduledSessions.add(ScheduledSession.create(session, room, timeSlot));
    }

    public void unscheduleSession(Session session) {
        scheduledSessions.removeIf(scheduledSession -> scheduledSession.sessionId.equals(session.getId()));
    }

    private void validateIfRoomIsAvailable(Room room, TimeSlot timeSlot) {
        scheduledSessions.stream()
                .filter(scheduledSession -> scheduledSession.roomName.equals(room.getName()))
                .filter(scheduledSession -> scheduledSession.timeSlot.equals(timeSlot))
                .findAny()
                .ifPresent(scheduledSession -> {
                    throw new RoomAlreadyBooked(room, timeSlot);
                });
    }

    public record ScheduledSession(Session.Id sessionId, String roomName, TimeSlot timeSlot) {
        private static ScheduledSession create(Session session, Room room, TimeSlot timeSlot) {
            return new ScheduledSession(session.getId(), room.getName(), timeSlot);
        }
    }
}
