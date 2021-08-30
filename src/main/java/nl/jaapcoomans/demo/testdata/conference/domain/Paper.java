package nl.jaapcoomans.demo.testdata.conference.domain;

public class Paper {
    private String speakerEmail;
    private String title;
    private String sessionAbstract;
    private String outline;
    private String messageToProgramCommittee;
    private SessionType sessionType;
    private Status status;

    public Paper(String speakerEmail, String title, String sessionAbstract, String outline, String messageToProgramCommittee, SessionType sessionType, Status status) {
        this.speakerEmail = speakerEmail;
        this.title = title;
        this.sessionAbstract = sessionAbstract;
        this.outline = outline;
        this.messageToProgramCommittee = messageToProgramCommittee;
        this.sessionType = sessionType;
        this.status = status;
    }

    public void submit() {
        //TODO: Do validations
        this.status = Status.SUBMITTED;
    }

    public Session confirm() {
        this.status = Status.CONFIRMED;
        return Session.create(
                this.title,
                this.sessionAbstract,
                this.sessionType,
                this.speakerEmail
        );
    }

    public String getSpeakerEmail() {
        return speakerEmail;
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

    public enum Status {
        DRAFT, SUBMITTED, ACCEPTED, REJECTED, CONFIRMED, WITHDRAWN
    }
}
