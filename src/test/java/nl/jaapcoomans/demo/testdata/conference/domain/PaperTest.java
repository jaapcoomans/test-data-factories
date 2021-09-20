package nl.jaapcoomans.demo.testdata.conference.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aConferenceId;
import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aSessionType;
import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aValidPaper;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aSpeakerId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaperTest {
    @Test
    public void testCreatePaper() {
        // Given
        var conferenceId = aConferenceId();
        var speakerId = aSpeakerId();
        var sessionType = aSessionType();

        // When
        var paper = Paper.create(conferenceId, speakerId, sessionType);

        // Then
        assertThat(paper.getId()).isNotNull();
        assertThat(paper.getId().conferenceId()).isEqualTo(conferenceId);
        assertThat(paper.getSpeakerId()).isEqualTo(speakerId);
        assertThat(paper.getStatus()).isEqualTo(Paper.Status.DRAFT);
    }

    @Test
    public void testSubmit() {
        // Given
        var paper = aValidPaper(Paper.Status.DRAFT);

        // When
        paper.submit();

        // Then
        assertThat(paper.getStatus()).isEqualTo(Paper.Status.SUBMITTED);
    }

    @Test
    public void testSubmitNoTitle() {
        // Given
        var paper = aValidPaper(Paper.Status.DRAFT);
        paper.setTitle("");

        // When + then
        assertThrows(IllegalStateException.class, paper::submit);
    }

    @ParameterizedTest
    @EnumSource(value = Paper.Status.class, mode = EnumSource.Mode.EXCLUDE, names = "DRAFT")
    public void testSubmitNotDraft(Paper.Status status) {
        // Given
        var paper = aValidPaper(status);

        // When
        assertThrows(IllegalStateException.class, paper::submit);
    }

    @Test
    public void testAccept() {
        // Given
        var paper = aValidPaper(Paper.Status.SUBMITTED);

        // When
        paper.accept();

        // Then
        assertThat(paper.getStatus()).isEqualTo(Paper.Status.ACCEPTED);
    }

    @ParameterizedTest
    @EnumSource(value = Paper.Status.class, mode = EnumSource.Mode.EXCLUDE, names = "SUBMITTED")
    public void testAcceptNotSubmitted(Paper.Status status) {
        // Given
        var paper = aValidPaper(status);

        // When
        assertThrows(IllegalStateException.class, paper::accept);
    }
}
