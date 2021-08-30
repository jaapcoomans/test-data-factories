package nl.jaapcoomans.demo.testdata.conference.domain;

import java.text.MessageFormat;
import java.util.UUID;

public class Session {
    private final UUID id;
    private String title;
    private String sessionAbstract;
    private SessionType sessionType;
    private String speakerEmail;
    private Status status;
    private String roomName;
    private TimeSlot timeSlot;

    private Session(UUID id, String title, String sessionAbstract, SessionType sessionType, String speakerEmail, Status status) {
        this.id = id;
        this.title = title;
        this.sessionAbstract = sessionAbstract;
        this.sessionType = sessionType;
        this.speakerEmail = speakerEmail;
        this.status = status;
    }

    public static Session create(String title, String sessionAbstract, SessionType sessionType, String speakerEmail) {
        return new Session(UUID.randomUUID(), title, sessionAbstract, sessionType, speakerEmail, Status.SELECTED);
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

    public UUID getId() {
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

    public String getSpeakerEmail() {
        return speakerEmail;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        SELECTED, SCHEDULED, CANCELLED
    }
}
