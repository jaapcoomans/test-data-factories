package nl.jaapcoomans.demo.testdata.conference.domain;

import com.github.javafaker.Faker;

import java.util.Locale;

public class SpeakerTestDataFactory {
    private static final Faker faker = new Faker();

    public static String aSpeakerEmail() {
        return aSpeakerEmail(aSpeakerName());
    }

    public static String aSpeakerName() {
        return faker.name().fullName();
    }

    public static String aSpeakerEmail(String fullName) {
        var localPart = fullName.replaceAll(" ", ".");
        return faker.internet().emailAddress(localPart);
    }

    private static String aSpeakerBio() {
        return faker.lorem().paragraph(10);
    }

    private static String aSpeakerTwitterHandle(String fullName) {
        return "@" + fullName.toLowerCase(Locale.ROOT).replaceAll(" ", "") + "tweets";
    }

    public static Speaker aSpeaker() {
        var fullName = aSpeakerName();

        return Speaker.create(
                aSpeakerEmail(fullName),
                fullName,
                aSpeakerTwitterHandle(fullName),
                aSpeakerBio()
        );
    }
}
