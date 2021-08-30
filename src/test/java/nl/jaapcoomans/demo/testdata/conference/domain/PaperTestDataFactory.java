package nl.jaapcoomans.demo.testdata.conference.domain;

import com.github.javafaker.Faker;

import java.text.MessageFormat;

import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aSpeakerEmail;

public class PaperTestDataFactory {
    private static final Faker faker = new Faker();

    public static String aTitle() {
        var tech  = faker.options().option("Java 42", "AWS InfiniDash", "Random number generators", "", "");
        var titleTemplate = faker.options().option(
                "{}, the good the bad and the ugly",
                "7 reasons why {} is awesome",
                "{} from the trenches",
                "11 ways to improve your tests with {}"
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

    private static SessionType aSessionType() {
        return faker.options().option(SessionType.class);
    }

    public static Paper aPaper() {
        return new Paper(
                aSpeakerEmail(),
                aTitle(),
                anAbstract(),
                anOutline(),
                aMessageToProgramCommittee(),
                aSessionType(),
                aStatus()
        );
    }
}
