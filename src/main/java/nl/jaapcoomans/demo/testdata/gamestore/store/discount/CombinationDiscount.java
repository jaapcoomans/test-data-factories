package nl.jaapcoomans.demo.testdata.gamestore.store.discount;

import nl.jaapcoomans.demo.testdata.gamestore.catalog.Game;
import nl.jaapcoomans.demo.testdata.gamestore.store.Discount;
import nl.jaapcoomans.demo.testdata.gamestore.store.Order;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CombinationDiscount implements Discount {
    private final Game.Id gameId1;
    private final Game.Id gameId2;
    private final BigDecimal discount;
    private final String description;

    public CombinationDiscount(Game game1, Game game2, BigDecimal discount) {
        this.gameId1 = game1.getId();
        this.gameId2 = game2.getId();
        this.discount = discount;
        this.description = createDescription(game1, game2, discount);
    }

    private static String createDescription(Game game1, Game game2, BigDecimal discount) {
        return String.format("%s off when combining %s and %s",
                NumberFormat.getCurrencyInstance().format(discount),
                game1.getTitle(),
                game2.getTitle()
        );
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public boolean isApplicableFor(Order order) {
        return order.containsGame(gameId1)
                && order.containsGame(gameId2);
    }

    @Override
    public BigDecimal totalAmount() {
        return discount;
    }
}
