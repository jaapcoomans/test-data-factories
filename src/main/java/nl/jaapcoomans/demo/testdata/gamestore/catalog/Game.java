package nl.jaapcoomans.demo.testdata.gamestore.catalog;

import java.math.BigDecimal;
import java.util.UUID;

public class Game {
    private final Id id;
    private final String title;
    private final Category category;
    private final String designer;
    private final Publisher.Id publisherId;
    private final NumberRange numberOfPlayers;
    private final NumberRange playingTimeInMinutes;
    private final int minimumAge;

    private final BigDecimal price;
    private final String eanCode;

    public Game(Id id, String title, Category category, String designer, Publisher.Id publisherId, NumberRange numberOfPlayers, NumberRange playingTimeInMinutes, int minimumAge, BigDecimal price, String eanCode) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.designer = designer;
        this.publisherId = publisherId;
        this.numberOfPlayers = numberOfPlayers;
        this.playingTimeInMinutes = playingTimeInMinutes;
        this.minimumAge = minimumAge;
        this.price = price;
        this.eanCode = eanCode;
    }

    public Id getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public String getDesigner() {
        return designer;
    }

    public Publisher.Id getPublisherId() {
        return publisherId;
    }

    public NumberRange getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public NumberRange getPlayingTimeInMinutes() {
        return playingTimeInMinutes;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getEanCode() {
        return eanCode;
    }

    public record Id(UUID value){}

    public enum Category {
        BOARD_GAME, CARD_GAME, DICE_GAME, PAPER_AND_PENCIL, ROLE_PLAYING, TILE_BASED
    }

    public record NumberRange(int minInclusive, int maxInclusive){}
}
