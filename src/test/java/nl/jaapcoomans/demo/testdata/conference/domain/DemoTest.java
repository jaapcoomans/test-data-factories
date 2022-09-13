package nl.jaapcoomans.demo.testdata.conference.domain;

import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Test;

import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aValidPaper;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aValidSpeaker;

/**
 * This test is just here for demo purposes. It uses 2 Test Data Factories to create test data and then prints it out
 * formatted.
 */
public class DemoTest {
    @Test
    public void demo() {
        var speaker = aValidSpeaker();
        var paper = aValidPaper(Paper.Status.DRAFT, speaker.getSpeakerId());

        printSpeaker(speaker);
        printPaper(paper);
    }

    private static void printSpeaker(Speaker speaker) {
        printDivider();

        System.out.printf("Email      : %s%n", speaker.getSpeakerId().emailAddress());
        System.out.printf("Name       : %s%n", speaker.getFullName());
        System.out.printf("Bio        : %s%n", speaker.getBio());
        System.out.printf("Twitter    : %s%n", speaker.getTwitterHandle());
    }

    private static void printPaper(Paper paper) {
        printDivider();

        System.out.printf("Conference : %s%n", paper.getId().conferenceId().code());
        System.out.printf("Title      : %s%n", paper.getTitle());
        System.out.printf("Type       : %s%n", paper.getSessionType());
        System.out.printf("Status     : %s%n", paper.getStatus());
        System.out.println("== Abstract ==");
        System.out.println(WordUtils.wrap(paper.getSessionAbstract(), 80).indent(2));
        System.out.println("== Message to Program Committee ==");
        System.out.println(WordUtils.wrap(paper.getMessageToProgramCommittee(), 80).indent(2));
        System.out.println("== Outline ==");
        System.out.println(WordUtils.wrap(paper.getOutline(), 80).indent(2));
    }

    private static void printDivider() {
        System.out.println("===============================================================");
    }
}
