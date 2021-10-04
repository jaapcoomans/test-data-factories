package nl.jaapcoomans.demo.testdata.conference.domain;

import java.util.Optional;

public interface SpeakerRepository {
    void insert(Speaker speaker);

    void save(Speaker speaker);

    Optional<Speaker> findById(Speaker.Id speakerId);
}
