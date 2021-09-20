package nl.jaapcoomans.demo.testdata.conference.domain;

import java.util.Optional;

public interface PaperRepository {
    void save(Paper paper);

    Optional<Paper> findById(Paper.Id paperId);
}
