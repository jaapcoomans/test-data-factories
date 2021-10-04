package nl.jaapcoomans.demo.testdata.conference.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aConferenceId;
import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aPaperId;
import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aSessionType;
import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aValidPaper;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aSpeakerId;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aValidSpeaker;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.anEmptySpeaker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CallForPapersServiceTest {
    private final PaperRepository paperRepository = mock(PaperRepository.class);
    private final SpeakerRepository speakerRepository = mock(SpeakerRepository.class);

    private final CallForPapersService callForPapersService = new CallForPapersService(paperRepository, speakerRepository);

    @BeforeEach
    public void resetMocks() {
        reset(paperRepository);
    }

    @Test
    public void testCreatePaper() {
        // Given
        var conferenceId = aConferenceId();
        var speakerId = aSpeakerId();
        var sessionType = aSessionType();

        // When
        var paper = callForPapersService.createPaper(conferenceId, speakerId, sessionType);

        // Then
        assertThat(paper).isNotNull();
        assertThat(paper.getStatus()).isEqualTo(Paper.Status.DRAFT);

        verify(paperRepository).save(paper);
    }

    @Nested
    public class SubmitPaper {
        private final Speaker speaker = aValidSpeaker();
        private final Paper paper = aValidPaper(Paper.Status.DRAFT, speaker.getSpeakerId());

        @Test
        public void testSubmitPaper() {
            // Given
            when(paperRepository.findById(paper.getId())).thenReturn(Optional.of(paper));
            when(speakerRepository.findById(paper.getSpeakerId())).thenReturn(Optional.of(speaker));

            // When
            callForPapersService.submitPaper(paper.getId());

            // Then
            assertThat(paper.getStatus()).isEqualTo(Paper.Status.SUBMITTED);

            verify(paperRepository).save(paper);
        }

        @Test
        public void testSubmitPaperDoesNotExist() {
            // Given
            var paperId = aPaperId();
            when(paperRepository.findById(paperId)).thenReturn(Optional.empty());

            // When + then
            assertThrows(CallForPapersService.PaperDoesNotExist.class, () ->
                    callForPapersService.submitPaper(paperId)
            );
        }

        @Test
        public void submitPaperSpeakerProfileIncomplete() {
            var incompleteSpeakerProfile = anEmptySpeaker(paper.getSpeakerId());

            when(paperRepository.findById(paper.getId())).thenReturn(Optional.of(paper));
            when(speakerRepository.findById(paper.getSpeakerId())).thenReturn(Optional.of(incompleteSpeakerProfile));

            assertThrows(CallForPapersService.IncompleteSpeakerProfile.class, () ->
                    callForPapersService.submitPaper(paper.getId())
            );
        }
    }
}
