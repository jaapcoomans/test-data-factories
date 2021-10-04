package nl.jaapcoomans.demo.testdata.conference.domain;

public class CallForPapersService {
    private final PaperRepository paperRepository;

    private final SpeakerRepository speakerRepository;

    public CallForPapersService(PaperRepository paperRepository, SpeakerRepository speakerRepository) {
        this.paperRepository = paperRepository;
        this.speakerRepository = speakerRepository;
    }

    public Paper createPaper(Conference.Id conferenceId, Speaker.Id speakerId, SessionType sessionType) {
        var paper = Paper.create(conferenceId, speakerId, sessionType);
        this.paperRepository.save(paper);
        return paper;
    }

    public void submitPaper(Paper.Id paperId) {
        var paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new PaperDoesNotExist(paperId));

        validateSpeakerProfileComplete(paper.getSpeakerId());

        paper.submit();
        paperRepository.save(paper);
    }

    private void validateSpeakerProfileComplete(Speaker.Id speakerId) {
        speakerRepository.findById(speakerId)
                .filter(Speaker::isProfileComplete)
                .orElseThrow(() -> new IncompleteSpeakerProfile(speakerId));
    }

    public void acceptPaper(Paper.Id paperId) {
        var paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new PaperDoesNotExist(paperId));

        paper.accept();
        paperRepository.save(paper);
    }

    public static class PaperDoesNotExist extends RuntimeException {
        public PaperDoesNotExist(Paper.Id paperId) {
            super("Paper with ID " + paperId + " does not exist.");
        }
    }

    public static class IncompleteSpeakerProfile extends RuntimeException {
        public IncompleteSpeakerProfile(Speaker.Id speakerId) {
            super("Speaker profile for speaker " + speakerId + " is incomplete.");
        }
    }

}
