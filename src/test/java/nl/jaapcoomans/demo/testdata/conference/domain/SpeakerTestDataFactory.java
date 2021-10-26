package nl.jaapcoomans.demo.testdata.conference.domain;

import com.github.javafaker.Faker;

import java.util.Locale;

public class SpeakerTestDataFactory {
    private static final Faker faker = new Faker();

    public static Speaker.Id aSpeakerId() {
        return new Speaker.Id(aSpeakerEmail());
    }

    public static Speaker.Id aSpeakerId(String fullName) {
        return new Speaker.Id(aSpeakerEmail(fullName));
    }

    public static String aSpeakerEmail() {
        return aSpeakerEmail(aSpeakerName());
    }

    public static String anInvalidSpeakerEmail() {
        return "invalid";
    }

    public static String aSpeakerName() {
        return faker.name().fullName();
    }

    public static String aSpeakerEmail(String fullName) {
        var localPart = fullName.replaceAll(" ", ".");
        return faker.internet().emailAddress(localPart);
    }

    public static String aValidSpeakerBio() {
        var speakerBio = faker.lorem().paragraph(10);
        if (speakerBio.length() > Speaker.MAX_BIO_LENGTH) {
            return speakerBio.substring(0, Speaker.MAX_BIO_LENGTH);
        } else {
            return speakerBio;
        }
    }

    public static String anInvalidSpeakerBio() {
        var speakerBio = new StringBuilder(faker.lorem().paragraph(10));
        while (speakerBio.length() <= Speaker.MAX_BIO_LENGTH) {
            speakerBio.append(faker.lorem().paragraph(10));
        }
        return speakerBio.toString();
    }

    public static String aSpeakerTwitterHandle(String fullName) {
        var twitterHandle = "@" + fullName.toLowerCase(Locale.ROOT).replaceAll(" ", "")
                .replaceAll("\\.", "_") + "tweets";
        if (twitterHandle.length() > 16) {
            return twitterHandle.substring(0, 16);
        } else {
            return twitterHandle;
        }
    }

    public static Speaker anEmptySpeaker(Speaker.Id speakerId) {
        return Speaker.create(speakerId.emailAddress(), "", "", "");
    }

    public static Speaker aValidSpeaker() {
        var fullName = aSpeakerName();

        return Speaker.create(
                aSpeakerEmail(fullName),
                fullName,
                aSpeakerTwitterHandle(fullName),
                aValidSpeakerBio()
        );
    }
}
