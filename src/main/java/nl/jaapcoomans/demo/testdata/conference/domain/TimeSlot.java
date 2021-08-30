package nl.jaapcoomans.demo.testdata.conference.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
    public int lengthInMinutes() {
        return (int)Duration.between(startTime, endTime).toMinutes();
    }
}
