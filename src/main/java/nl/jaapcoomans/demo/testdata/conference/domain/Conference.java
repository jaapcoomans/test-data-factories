package nl.jaapcoomans.demo.testdata.conference.domain;

import java.util.List;

public class Conference {
    private final Id id;
    private final String name;
    private final List<Session.Id> sessions;

    public Conference(Id id, String name, List<Session.Id> sessions) {
        this.id = id;
        this.name = name;
        this.sessions = List.copyOf(sessions);
    }

    public Id getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Session.Id> getSessions() {
        return sessions;
    }

    public static record Id(String code) {
    }
}
