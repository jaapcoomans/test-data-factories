package nl.jaapcoomans.demo.testdata.conference.domain;

import java.util.Objects;
import java.util.UUID;

public class Paper {
    private final Id id;
    private final Speaker.Id speakerId;
    private String title;
    private String sessionAbstract;
    private String outline;
    private String messageToProgramCommittee;
    private final SessionType sessionType;
    private Status status;

    Paper(Id id, Speaker.Id speakerId, String title, String sessionAbstract, String outline, String messageToProgramCommittee, SessionType sessionType, Status status) {
        this.id = id;
        this.speakerId = speakerId;
        this.title = title;
        this.sessionAbstract = sessionAbstract;
        this.outline = outline;
        this.messageToProgramCommittee = messageToProgramCommittee;
        this.sessionType = sessionType;
        this.status = status;
    }

    public static Paper create(Conference.Id conferenceId, Speaker.Id speakerId, SessionType sessionType) {
        Objects.requireNonNull(conferenceId, "conferenceId cannot be null");
        Objects.requireNonNull(speakerId, "speakerId cannot be null");
        Objects.requireNonNull(sessionType, "sessionType cannot be null");

        return new Paper(
                Id.create(conferenceId),
                speakerId,
                null,
                null,
                null,
                null,
                sessionType,
                Status.DRAFT
        );
    }

    public void submit() {
        if (status != Status.DRAFT) {
            throw new IllegalStateException("Paper can only be submitted if it has status DRAFT");
        }
        validateTitle();
        validateAbstract();
        validateOutline();
        validateMessageToProgramCommittee();
        this.status = Status.SUBMITTED;
    }

    private void validateTitle() {
        if (title == null || title.isBlank()) {
            throw new IllegalStateException("Title must be filled.");
        }
    }

    private void validateAbstract() {
        if (sessionAbstract == null || sessionAbstract.isBlank()) {
            throw new IllegalStateException("Abstract must be filled.");
        }
    }

    private void validateOutline() {
        if (outline == null || outline.isBlank()) {
            throw new IllegalStateException("Outline must be filled.");
        }
    }

    private void validateMessageToProgramCommittee() {
        if (messageToProgramCommittee == null || messageToProgramCommittee.isBlank()) {
            throw new IllegalStateException("Message to program committee must be filled.");
        }
    }

    public void accept() {
        if (status != Status.SUBMITTED) {
            throw new IllegalStateException("Paper can only be accepted if it has status SUBMITTED");
        }
        this.status = Status.ACCEPTED;
    }

    public void reject() {
        if (status != Status.SUBMITTED) {
            throw new IllegalStateException("Paper can only be rejected if it has status SUBMITTED");
        }
        this.status = Status.REJECTED;
    }

    public Session confirm() {
        if (status != Status.ACCEPTED) {
            throw new IllegalStateException("Paper can only be confirmed if it has status ACCEPTED");
        }
        this.status = Status.CONFIRMED;
        return Session.create(
                this.id.conferenceId,
                this.speakerId,
                this.title,
                this.sessionAbstract,
                this.sessionType
        );
    }

    public Paper.Id getId() {
        return id;
    }

    public Speaker.Id getSpeakerId() {
        return speakerId;
    }

    public String getTitle() {
        return title;
    }

    public String getSessionAbstract() {
        return sessionAbstract;
    }

    public String getOutline() {
        return outline;
    }

    public String getMessageToProgramCommittee() {
        return messageToProgramCommittee;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public Status getStatus() {
        return status;
    }

    public static final record Id(Conference.Id conferenceId, UUID uuid) {
        public static Id create(Conference.Id conferenceId) {
            return new Id(conferenceId, UUID.randomUUID());
        }
    }

    public enum Status {
        DRAFT, SUBMITTED, ACCEPTED, REJECTED, CONFIRMED, WITHDRAWN
    }
}
