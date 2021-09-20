package nl.jaapcoomans.demo.testdata.conference.domain;

import com.github.javafaker.Faker;

import java.text.MessageFormat;
import java.time.Year;
import java.time.format.DateTimeFormatter;

import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aSpeakerId;

public class PaperTestDataFactory {
    private static final Faker faker = new Faker();

    public static Paper.Id aPaperId() {
        return aPaperId(aConferenceId());
    }

    public static Paper.Id aPaperId(Conference.Id conferenceId) {
        return Paper.Id.create(conferenceId);
    }

    public static Conference.Id aConferenceId() {
        var conferenceName = faker.programmingLanguage().name().replaceAll(" ", "") + "Conf" + Year.now().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy"));
        return new Conference.Id(conferenceName);
    }

    public static String aTitle() {
        var tech = faker.options().option("Java 42", "AWS InfiniDash", "Random number generators");
        var titleTemplate = faker.options().option(
                "{0}, the good the bad and the ugly",
                "7 reasons why {0} is awesome",
                "{0} from the trenches",
                "11 ways to improve your tests with {0}"
        );
        return MessageFormat.format(titleTemplate, tech);
    }

    private static String anOutline() {
        return """
                Part 1: Introduction
                Part 2: %s
                Part 3: %s
                Part 4: %s
                Part 5: Q&A
                """
                .formatted(
                        faker.lorem().sentence(),
                        faker.lorem().sentence(),
                        faker.lorem().sentence()
                );
    }

    private static String anAbstract() {
        return faker.lorem().paragraph(15);
    }

    private static String aMessageToProgramCommittee() {
        return String.join("\n", faker.lorem().paragraphs(3));
    }

    private static Paper.Status aStatus() {
        return faker.options().option(Paper.Status.class);
    }

    public static SessionType aSessionType() {
        return faker.options().option(SessionType.class);
    }

    public static Paper aValidPaper(Paper.Status status) {
        var conferenceId = aConferenceId();

        return new Paper(
                aPaperId(conferenceId),
                aSpeakerId(),
                aTitle(),
                anAbstract(),
                anOutline(),
                aMessageToProgramCommittee(),
                aSessionType(),
                status
        );
    }
}
