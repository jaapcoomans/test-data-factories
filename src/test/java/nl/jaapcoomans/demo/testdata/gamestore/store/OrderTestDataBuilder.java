package nl.jaapcoomans.demo.testdata.gamestore.store;

import nl.jaapcoomans.demo.testdata.gamestore.catalog.Game;

import java.math.BigDecimal;
import java.util.Arrays;

import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aGameId;
import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aPrice;
import static nl.jaapcoomans.demo.testdata.gamestore.store.CustomerTestDataFactory.aCustomerId;
import static nl.jaapcoomans.demo.testdata.gamestore.store.OrderTestDataFactory.aNumberOfItems;

class OrderTestDataBuilder {
    private final Order.OrderBuilder orderBuilder;

    private OrderTestDataBuilder(Order.Status status) {
        this.orderBuilder = Order.builder()
                .customerId(aCustomerId())
                .status(status);
    }

    public static OrderTestDataBuilder givenADraftOrder() {
        return new OrderTestDataBuilder(Order.Status.DRAFT);
    }

    public OrderTestDataBuilder withOrderLinesFor(Game.Id... gameIds) {
        Arrays.stream(gameIds)
                .forEach(gameId -> orderBuilder.orderLine(gameId, aNumberOfItems(), aPrice()));
        return this;
    }

    public OrderTestDataBuilder withOrderLinesFor(Game... games) {
        Arrays.stream(games)
                .forEach(game -> orderBuilder.orderLine(game.getId(), aNumberOfItems(), game.getPrice()));
        return this;
    }

    public Order build() {
        return orderBuilder.build();
    }

    public class OrderLineTestDataBuilder {
        private Game.Id gameId = aGameId();
        private int numberOfItems = 1;
        private BigDecimal pricePerPiece = aPrice();

        public OrderLineTestDataBuilder forGame(Game.Id gameId) {
            this.gameId = gameId;
            return this;
        }

        public OrderLineTestDataBuilder withAGame() {
            orderBuilder.orderLine(gameId, numberOfItems, pricePerPiece);
            return new OrderLineTestDataBuilder();
        }

        public Order build() {
            return OrderTestDataBuilder.this.build();
        }
    }
}