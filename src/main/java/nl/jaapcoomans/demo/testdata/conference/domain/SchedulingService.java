package nl.jaapcoomans.demo.testdata.conference.domain;

public class SchedulingService {
    public void scheduleSession(Session session, Room room, TimeSlot timeSlot) {
        session.schedule(room.getName(), timeSlot);
    }
}
