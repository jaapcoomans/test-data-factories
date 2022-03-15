package nl.jaapcoomans.demo.testdata.conference.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static nl.jaapcoomans.demo.testdata.conference.domain.PaperTestDataFactory.aConferenceId;
import static nl.jaapcoomans.demo.testdata.conference.domain.ScheduleTestDataFactory.aScheduleFor;
import static nl.jaapcoomans.demo.testdata.conference.domain.SessionTestDataFactory.aSession;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScheduleTest {
    @Test
    @DisplayName("When schedule is full, planning a session for any slot will fail.")
    public void testScheduleFull() {
        // Given
        var conferenceId = aConferenceId();
        var schedule = aScheduleFor(conferenceId)
                .forDates(LocalDate.now().plusMonths(3), 2)
                .withNumberOfRooms(4)
                .andAllSessionsAreScheduled();

        // When
        for (Room room : schedule.getAvailableRooms()) {
            for (TimeSlot timeSlot : schedule.getAvailableTimeSlots()) {
                assertThrows(RoomAlreadyBooked.class, () ->
                        schedule.scheduleSession(aSession(conferenceId), room, timeSlot)
                );
            }
        }
    }

    @Test
    @DisplayName("When a session is rescheduled, the original slot will be available again.")
    public void rescheduleSession() {
        // Given
        var conferenceId = aConferenceId();
        var schedule = aScheduleFor(conferenceId).withoutAnyScheduledSessions();
        var session = aSession(conferenceId);

        var room = schedule.getAvailableRooms().get(0);
        var firstSlot = schedule.getAvailableTimeSlots().get(0);
        var secondSlot = schedule.getAvailableTimeSlots().get(1);

        // When
        schedule.scheduleSession(session, room, firstSlot);
        schedule.scheduleSession(session, room, secondSlot);

        // Then
        assertThat(schedule.isRoomAvailable(room, firstSlot)).isTrue();
        assertThat(schedule.isRoomAvailable(room, secondSlot)).isFalse();
    }
}
