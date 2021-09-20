package nl.jaapcoomans.demo.testdata.conference.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
    public int lengthInMinutes() {
        return (int) Duration.between(startTime, endTime).toMinutes();
    }

    @Override
    public String toString() {
        return "[" + date.format(DateTimeFormatter.ISO_DATE)
                + " "
                + startTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
                + "-"
                + endTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
                + "]";
    }
}
