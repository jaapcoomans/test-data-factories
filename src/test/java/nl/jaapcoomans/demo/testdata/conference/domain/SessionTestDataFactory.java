package nl.jaapcoomans.demo.testdata.conference.domain;

import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aValidPaper;

public class SessionTestDataFactory {
    public static Session aSession(Conference.Id conferenceId) {
        return aValidPaper(Paper.Status.ACCEPTED, SpeakerTestDataFactory.aSpeakerId(), conferenceId).confirm();
    }
}
