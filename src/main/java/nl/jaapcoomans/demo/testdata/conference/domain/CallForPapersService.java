package nl.jaapcoomans.demo.testdata.conference.domain;

public class CallForPapersService {
    private PaperRepository paperRepository;

    public CallForPapersService(PaperRepository paperRepository) {
        this.paperRepository = paperRepository;
    }

    public Paper createPaper(Conference.Id conferenceId, Speaker.Id speakerId, SessionType sessionType) {
        var paper = Paper.create(conferenceId, speakerId, sessionType);
        this.paperRepository.save(paper);
        return paper;
    }

    public void submitPaper(Paper.Id paperId) {
        var paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new PaperDoesNotExist(paperId));

        paper.submit();
        paperRepository.save(paper);
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
}
