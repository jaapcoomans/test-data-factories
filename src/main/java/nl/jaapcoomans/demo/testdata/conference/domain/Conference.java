package nl.jaapcoomans.demo.testdata.conference.domain;

import java.util.List;

public class Conference {
    private Id id;
    private String name;
    private List<Session.Id> sessions;

    public static record Id(String code) {
    }
}
