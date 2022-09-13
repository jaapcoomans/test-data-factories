package nl.jaapcoomans.demo.testdata.conference.domain;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nl.jaapcoomans.demo.testdata.conference.domain.RoomTestDataFactory.aRoom;
import static nl.jaapcoomans.demo.testdata.conference.domain.SessionTestDataFactory.aSession;

public class ScheduleTestDataFactory {
    private static final Faker faker = new Faker();

    public static ScheduleTestDataBuilder aScheduleFor(Conference.Id conferenceId) {
        return new ScheduleTestDataBuilder(conferenceId);
    }

    public static class ScheduleTestDataBuilder {
        private final Schedule.Builder scheduleBuilder;
        private List<LocalDate> dates = List.of(LocalDate.now().plusMonths(2));
        private int numberOfRooms = faker.number().numberBetween(1, 10);
        private int slotsPerDay = faker.number().numberBetween(4, 8);
        private int sessionLengthMinutes = faker.options().option(40, 45, 50, 60);
        private int minutesBetweenSessions = faker.options().option(5, 10, 15);
        private LocalTime startTime = faker.options().option(LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0));

        private ScheduleTestDataBuilder(Conference.Id conferenceId) {
            this.scheduleBuilder = Schedule.builder().conferenceId(conferenceId);
        }

        public ScheduleTestDataBuilder forDates(LocalDate startDate, int numberOfDays) {
            dates = IntStream.range(0, numberOfDays)
                    .mapToObj(startDate::plusDays)
                    .collect(Collectors.toList());
            return this;
        }

        public ScheduleTestDataBuilder withNumberOfRooms(int numberOfRooms) {
            this.numberOfRooms = numberOfRooms;

            return this;
        }

        public Schedule andAllSessionsAreScheduled() {
            var schedule = withoutAnyScheduledSessions();
            for (Room room : schedule.getAvailableRooms()) {
                for (TimeSlot timeSlot : schedule.getAvailableTimeSlots()) {
                    var session = aSession(scheduleBuilder.getConferenceId());
                    schedule.scheduleSession(session, room, timeSlot);
                }
            }

            return schedule;
        }

        public Schedule withoutAnyScheduledSessions() {
            IntStream.range(0, numberOfRooms)
                    .mapToObj(i -> aRoom(i + 1))
                    .forEach(scheduleBuilder::room);

            for (LocalDate date : dates) {
                for (int slot = 0; slot < slotsPerDay; slot++) {
                    var sessionStart = this.startTime.plusMinutes((long) slot * (minutesBetweenSessions + sessionLengthMinutes));
                    var sessionEnd = sessionStart.plusMinutes(sessionLengthMinutes);
                    scheduleBuilder.timeSlot(new TimeSlot(date, sessionStart, sessionEnd));
                }
            }

            return scheduleBuilder.build();
        }
    }
}
