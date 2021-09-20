package nl.jaapcoomans.demo.testdata.conference.domain;

import java.text.MessageFormat;
import java.util.UUID;

public class Session {
    private final Session.Id id;
    private final Conference.Id conferenceId;
    private final Speaker.Id speakerId;
    private final String title;
    private final String sessionAbstract;
    private final SessionType sessionType;
    private Status status;

    private Session(Id id, Conference.Id conferenceId, Speaker.Id speakerId, String title, String sessionAbstract, SessionType sessionType, Status status) {
        this.id = id;
        this.conferenceId = conferenceId;
        this.speakerId = speakerId;
        this.title = title;
        this.sessionAbstract = sessionAbstract;
        this.sessionType = sessionType;
        this.status = status;
    }

    public static Session create(Conference.Id conferenceId, Speaker.Id speakerId, String title, String sessionAbstract, SessionType sessionType) {
        return new Session(Id.create(), conferenceId, speakerId, title, sessionAbstract, sessionType, Status.SELECTED);
    }

    public void schedule(String roomName, TimeSlot timeSlot) {
        if (this.status == Status.CANCELLED) {
            throw new CantScheduleSession("Session was already cancelled.");
        }
        if (this.sessionType.getLengthInMinutes() > timeSlot.lengthInMinutes()) {
            throw new CantScheduleSession(MessageFormat.format("SessionType {} is too long for timeslot of {} to {}", sessionType, timeSlot.startTime(), timeSlot.endTime()));
        }

        this.status = Status.SCHEDULED;
    }

    public Id getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSessionAbstract() {
        return sessionAbstract;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public Status getStatus() {
        return status;
    }

    public static final record Id(UUID id) {
        public static Id create() {
            return new Id(UUID.randomUUID());
        }
    }

    public enum Status {
        SELECTED, SCHEDULED, CANCELLED
    }
}
