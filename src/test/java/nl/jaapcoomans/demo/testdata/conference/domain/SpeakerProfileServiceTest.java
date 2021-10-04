package nl.jaapcoomans.demo.testdata.conference.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aSpeakerId;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aSpeakerName;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aSpeakerTwitterHandle;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.aValidSpeakerBio;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.anEmptySpeaker;
import static nl.jaapcoomans.demo.testdata.conference.domain.SpeakerTestDataFactory.anInvalidSpeakerBio;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpeakerProfileServiceTest {
    private final SpeakerRepository speakerRepository = mock(SpeakerRepository.class);

    private final SpeakerProfileService speakerProfileService = new SpeakerProfileService(speakerRepository);

    @Test
    public void createSpeaker() {
        // Given
        var emailAddress = SpeakerTestDataFactory.aSpeakerEmail();

        // When
        var speaker = speakerProfileService.createNewSpeaker(emailAddress);

        // Then
        assertThat(speaker.getSpeakerId().emailAddress()).isEqualTo(emailAddress);
        assertThat(speaker.getFullName()).isEmpty();
        assertThat(speaker.getTwitterHandle()).isEmpty();
        assertThat(speaker.getBio()).isEmpty();

        verify(speakerRepository).insert(speaker);
    }

    @Test
    public void createSpeakerInvalidEmail() {
        // Given
        var emailAddress = SpeakerTestDataFactory.anInvalidSpeakerEmail();

        // When + then
        var exception = assertThrows(IllegalArgumentException.class, () ->
                speakerProfileService.createNewSpeaker(emailAddress)
        );

        assertThat(exception.getMessage()).contains(emailAddress);
    }

    @Nested
    public class UpdateDetails {
        private final String fullName = aSpeakerName();
        private final Speaker.Id speakerId = aSpeakerId(fullName);
        private final String twitterHandle = aSpeakerTwitterHandle(fullName);
        private final Speaker emptySpeaker = anEmptySpeaker(speakerId);

        @Test
        public void updateSpeakerDetails() {
            // Given
            when(speakerRepository.findById(speakerId)).thenReturn(Optional.of(emptySpeaker));

            // When
            var speaker = speakerProfileService.updateSpeakerDetails(speakerId, fullName, twitterHandle);

            // Then
            assertThat(speaker.getFullName()).isEqualTo(fullName);
            assertThat(speaker.getTwitterHandle()).isEqualTo(twitterHandle);

            verify(speakerRepository).save(speaker);
        }

        @Test
        public void updateSpeakerDetailsInvalidTwitterHandle() {
            // Given
            var invalidTwitterHandle = "---Invalid---";
            when(speakerRepository.findById(speakerId)).thenReturn(Optional.of(anEmptySpeaker(speakerId)));

            // When
            var exception = assertThrows(IllegalArgumentException.class, () ->
                    speakerProfileService.updateSpeakerDetails(speakerId, fullName, invalidTwitterHandle)
            );

            // Then
            assertThat(exception.getMessage()).contains(invalidTwitterHandle);
        }

        @Test
        public void updateSpeakerDetailsSpeakerNotFound() {
            // Given
            when(speakerRepository.findById(speakerId)).thenReturn(Optional.empty());

            // When + then
            var exception = assertThrows(SpeakerProfileService.SpeakerNotFound.class, () ->
                    speakerProfileService.updateSpeakerDetails(speakerId, fullName, twitterHandle)
            );

            assertThat(exception.getMessage()).contains(speakerId.emailAddress());
        }
    }

    @Nested
    public class UpdateBio {
        private final Speaker.Id speakerId = aSpeakerId();
        private final Speaker emptySpeaker = anEmptySpeaker(speakerId);
        private final String bio = aValidSpeakerBio();

        @Test
        public void updateSpeakerDetails() {
            // Given
            when(speakerRepository.findById(speakerId)).thenReturn(Optional.of(emptySpeaker));

            // When
            var speaker = speakerProfileService.updateSpeakerBio(speakerId, bio);

            // Then
            assertThat(speaker.getBio()).isEqualTo(bio);
            verify(speakerRepository).save(speaker);
        }

        @Test
        public void updateSpeakerDetailsInvalidBio() {
            // Given
            when(speakerRepository.findById(speakerId)).thenReturn(Optional.of(anEmptySpeaker(speakerId)));

            // When
            var exception = assertThrows(IllegalArgumentException.class, () ->
                    speakerProfileService.updateSpeakerBio(speakerId, anInvalidSpeakerBio())
            );

            // Then
            assertThat(exception.getMessage()).contains(String.valueOf(Speaker.MAX_BIO_LENGTH));
        }

        @Test
        public void updateSpeakerDetailsSpeakerNotFound() {
            // Given
            when(speakerRepository.findById(speakerId)).thenReturn(Optional.empty());

            // When + then
            var exception = assertThrows(SpeakerProfileService.SpeakerNotFound.class, () ->
                    speakerProfileService.updateSpeakerBio(speakerId, bio)
            );

            assertThat(exception.getMessage()).contains(speakerId.emailAddress());
        }
    }
}
