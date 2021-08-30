package nl.jaapcoomans.demo.testdata.conference.domain;

public class CantScheduleSession extends RuntimeException {
    public CantScheduleSession(String message) {
        super(message);
    }
}
