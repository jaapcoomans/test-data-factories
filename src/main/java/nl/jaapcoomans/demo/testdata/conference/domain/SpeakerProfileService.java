package nl.jaapcoomans.demo.testdata.conference.domain;

public class SpeakerProfileService {
    private final SpeakerRepository speakerRepository;

    public SpeakerProfileService(SpeakerRepository speakerRepository) {
        this.speakerRepository = speakerRepository;
    }

    public Speaker createNewSpeaker(String emailAddress) {
        var speaker = Speaker.create(emailAddress, "", "", "");
        speakerRepository.insert(speaker);
        return speaker;
    }

    public Speaker updateSpeakerDetails(Speaker.Id speakerId, String fullName, String twitterHandle) {
        var speaker = speakerRepository.findById(speakerId)
                .orElseThrow(() -> new SpeakerNotFound(speakerId));

        speaker.updateDetails(fullName, twitterHandle);
        speakerRepository.save(speaker);
        return speaker;
    }

    public Speaker updateSpeakerBio(Speaker.Id speakerId, String bio) {
        var speaker = speakerRepository.findById(speakerId)
                .orElseThrow(() -> new SpeakerNotFound(speakerId));

        speaker.updateBio(bio);
        speakerRepository.save(speaker);
        return speaker;
    }

    public static final class SpeakerNotFound extends RuntimeException {
        public SpeakerNotFound(Speaker.Id speakerId) {
            super("No speaker found with ID " + speakerId.emailAddress());
        }
    }
}
