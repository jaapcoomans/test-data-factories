package nl.jaapcoomans.demo.testdata.conference.domain;

public enum SessionType {
    DEEP_DIVE(180),
    CONFERENCE_TALK(50),
    QUICKIE(15),
    IGNITE(5);

    private int lengthInMinutes;

    SessionType(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }
}
