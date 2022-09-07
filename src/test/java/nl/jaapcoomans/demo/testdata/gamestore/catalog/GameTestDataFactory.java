package nl.jaapcoomans.demo.testdata.gamestore.catalog;

import com.github.javafaker.Faker;
import nl.jaapcoomans.demo.testdata.gamestore.store.DemoPrinter;

import java.math.BigDecimal;
import java.util.UUID;

import static nl.jaapcoomans.demo.testdata.gamestore.catalog.PublisherTestDataFactory.aPublisherId;
import static nl.jaapcoomans.demo.testdata.gamestore.store.DemoPrinter.printForDemo;

public class GameTestDataFactory {
    private static final Faker faker = Faker.instance();

    public static Game.Id aGameId() {
        return new Game.Id(UUID.randomUUID());
    }

    private static String aGameTitle() {
        return faker.options().option(
                "The boomers of " + faker.country().capital(),
                "Spaceships to " + faker.space().galaxy(),
                faker.book().title() + " the game",
                faker.address().cityName(),
                faker.ancient().god() + " & " + faker.ancient().hero()
        );
    }

    private static Game.Category aCategory() {
        return faker.options().option(Game.Category.class);
    }

    private static String aGameDesigner() {
        return faker.name().fullName();
    }

    private static Game.NumberRange aNumberOfPlayers() {
        var minInclusive = faker.number().numberBetween(1, 5);
        var maxInclusive = faker.number().numberBetween(minInclusive, 11);
        return new Game.NumberRange(minInclusive, maxInclusive);
    }

    private static Game.NumberRange aPlayingTimeInMinutes() {
        var minPlayingTime = faker.options().option(10, 15, 20, 30, 45, 60, 90, 120);
        var maxPlayingTime = minPlayingTime * faker.number().numberBetween(2, 5);
        return new Game.NumberRange(minPlayingTime, maxPlayingTime);
    }

    private static int aMinimumAge() {
        return faker.number().numberBetween(2, 11);
    }

    public static BigDecimal aPrice() {
        long priceInCents = faker.number().numberBetween(1_00L, 100_00L);
        return BigDecimal.valueOf(priceInCents, 2);
    }

    public static String aValidEan13Code() {
        return faker.code().ean13();
    }

    public static Game aGame() {
        var game = new Game(
                aGameId(),
                aGameTitle(),
                aCategory(),
                aGameDesigner(),
                aPublisherId(),
                aNumberOfPlayers(),
                aPlayingTimeInMinutes(),
                aMinimumAge(),
                aPrice(),
                aValidEan13Code());

        return printForDemo(game);
    }
}