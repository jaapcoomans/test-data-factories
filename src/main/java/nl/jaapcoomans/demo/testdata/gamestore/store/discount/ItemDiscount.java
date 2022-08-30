package nl.jaapcoomans.demo.testdata.gamestore.store.discount;

import nl.jaapcoomans.demo.testdata.gamestore.catalog.Game;
import nl.jaapcoomans.demo.testdata.gamestore.store.Discount;
import nl.jaapcoomans.demo.testdata.gamestore.store.Order;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class ItemDiscount implements Discount {
    private final Game.Id gameId;
    private final BigDecimal discount;
    private final String description;

    public ItemDiscount(Game game, BigDecimal discount) {
        this.gameId = game.getId();
        this.discount = discount;
        this.description = createDescription(game, discount);
    }

    private static String createDescription(Game game, BigDecimal discount) {
        return String.format("%s off %s",
                NumberFormat.getCurrencyInstance().format(discount),
                game.getTitle()
        );
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public boolean isApplicableFor(Order order) {
        return order.containsGame(gameId);
    }

    @Override
    public BigDecimal totalAmount() {
        return discount;
    }
}
