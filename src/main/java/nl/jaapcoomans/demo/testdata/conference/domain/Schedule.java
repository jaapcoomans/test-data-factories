package nl.jaapcoomans.demo.testdata.conference.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Schedule {
    private final Conference.Id conferenceId;
    private final List<Room> availableRooms;
    private final List<TimeSlot> availableTimeSlots;
    private final Set<ScheduledSession> scheduledSessions = new HashSet<>();

    private Schedule(Conference.Id conferenceId, List<Room> availableRooms, List<TimeSlot> availableTimeSlots) {
        this.conferenceId = conferenceId;
        this.availableRooms = List.copyOf(availableRooms);
        this.availableTimeSlots = List.copyOf(availableTimeSlots);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void scheduleSession(Session session, Room room, TimeSlot timeSlot) {
        validateIfRoomIsAvailable(room, timeSlot);
        unscheduleSession(session);
        scheduledSessions.add(ScheduledSession.create(session, room, timeSlot));
    }

    public void unscheduleSession(Session session) {
        scheduledSessions.removeIf(scheduledSession -> scheduledSession.sessionId.equals(session.getId()));
    }

    private void validateIfRoomIsAvailable(Room room, TimeSlot timeSlot) {
        if (!isRoomAvailable(room, timeSlot)) {
            throw new RoomAlreadyBooked(room, timeSlot);
        }
    }

    public boolean isRoomAvailable(Room room, TimeSlot timeSlot) {
        return scheduledSessions.stream()
                .filter(scheduledSession -> scheduledSession.roomName.equals(room.getName()))
                .noneMatch(scheduledSession -> scheduledSession.timeSlot.equals(timeSlot));
    }

    public List<Room> getAvailableRooms() {
        return availableRooms;
    }

    public List<TimeSlot> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public Set<ScheduledSession> getScheduledSessions() {
        return Set.copyOf(scheduledSessions);
    }

    public record ScheduledSession(Session.Id sessionId, String roomName, TimeSlot timeSlot) {
        private static ScheduledSession create(Session session, Room room, TimeSlot timeSlot) {
            return new ScheduledSession(session.getId(), room.getName(), timeSlot);
        }
    }

    public static class Builder {
        private Conference.Id conferenceId;
        private final List<Room> rooms = new ArrayList<>();
        private final List<TimeSlot> timeslots = new ArrayList<>();

        public Builder conferenceId(Conference.Id conferenceId) {
            this.conferenceId = conferenceId;
            return this;
        }

        public Builder room(Room room) {
            this.rooms.add(room);
            return this;
        }

        public Builder timeSlot(TimeSlot timeSlot) {
            this.timeslots.add(timeSlot);
            return this;
        }

        public Schedule build() {
            return new Schedule(conferenceId, rooms, timeslots);
        }

        public Conference.Id getConferenceId() {
            return this.conferenceId;
        }
    }
}
