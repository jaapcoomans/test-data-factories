package nl.jaapcoomans.demo.testdata.gamestore.store;

import nl.jaapcoomans.demo.testdata.gamestore.catalog.Game;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Order {
    private final Customer.Id customerId;
    private final List<OrderLine> orderLines;
    private Status status;
    private final Set<Discount> discounts;
    private DeliveryMethod deliveryMethod;

    private Order(Customer.Id customerId, Status status) {
        this(customerId, new ArrayList<>(), status, new HashSet<>(), null);
    }

    private Order(Customer.Id customerId, List<OrderLine> orderLines, Status status, Set<Discount> discounts, DeliveryMethod deliveryMethod) {
        this.customerId = customerId;
        this.orderLines = orderLines;
        this.status = status;
        this.discounts = discounts;
        this.deliveryMethod = deliveryMethod;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static Order create(Customer.Id customerId) {
        return new Order(customerId, Status.DRAFT);
    }

    public static Order create(Customer.Id customerId, Game game) {
        var order = create(customerId);
        order.addGame(game, 1);
        return order;
    }

    public void addGame(Game game, int numberOfItems) {
        if (status != Status.DRAFT) {
            throw new OrderCannotBeModified();
        }
        orderLines.add(new OrderLine(game.getId(), numberOfItems, game.getPrice()));
    }

    public void selectDeliveryMethod(DeliveryMethod deliveryMethod) {
        if (status != Status.DRAFT) {
            throw new OrderCannotBeModified();
        }
        this.deliveryMethod = deliveryMethod;
    }

    public void confirm() {
        if (status != Status.DRAFT) {
            throw new OrderAlreadyConfirmed();
        }
        if (orderLines.isEmpty()) {
            throw new OrderIsEmpty();
        }
        if (deliveryMethod == null) {
            throw new NoDeliveryMethodSelected();
        }
        status = Status.CONFIRMED;
    }

    public void deliver() {
        if (status != Status.PAID) {
            throw new OrderNotPaid();
        }
        status = Status.SHIPPED;
    }

    public void applyDiscount(Discount discount) {
        if (discount.isApplicableFor(this)) {
            this.discounts.add(discount);
        }
    }

    public boolean containsGame(Game.Id gameId) {
        return orderLines.stream()
                .anyMatch(line -> line.gameId.equals(gameId));
    }

    public Customer.Id getCustomerId() {
        return customerId;
    }

    public List<OrderLine> getOrderLines() {
        return List.copyOf(orderLines);
    }

    public Status getStatus() {
        return status;
    }

    public Set<Discount> getDiscounts() {
        return Set.copyOf(discounts);
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public BigDecimal calculateTotalAmount() {
        var totalOrderLines = this.orderLines.stream()
                .map(OrderLine::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var totalDiscounts = this.discounts.stream()
                .filter(discount -> discount.isApplicableFor(this))
                .map(Discount::totalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var deliveryCost = deliveryMethod != null ? deliveryMethod.price() : BigDecimal.ZERO;

        return totalOrderLines
                .subtract(totalDiscounts)
                .add(deliveryCost);
    }

    public static class OrderLine {
        private final Game.Id gameId;
        private final int numberOfItems;
        private final BigDecimal pricePerPiece;

        private OrderLine(Game.Id gameId, int numberOfItems, BigDecimal pricePerPiece) {
            this.gameId = gameId;
            this.numberOfItems = numberOfItems;
            this.pricePerPiece = pricePerPiece;
        }

        public Game.Id getGameId() {
            return gameId;
        }

        public int getNumberOfItems() {
            return numberOfItems;
        }

        public BigDecimal getPricePerPiece() {
            return pricePerPiece;
        }

        public BigDecimal totalPrice() {
            return pricePerPiece.multiply(BigDecimal.valueOf(numberOfItems));
        }
    }

    public enum Status {
        DRAFT, CONFIRMED, PAID, SHIPPED
    }

    public static class OrderBuilder {
        private Customer.Id customerId;
        private final List<OrderLine> orderLines = new ArrayList<>();
        private Status status;
        private final Set<Discount> discounts = new HashSet<>();
        private DeliveryMethod deliveryMethod;

        private OrderBuilder() {
        }

        public OrderBuilder customerId(Customer.Id customerId) {
            this.customerId = customerId;
            return this;
        }

        public OrderBuilder orderLine(Game.Id gameId, int numberOfItems, BigDecimal pricePerPiece) {
            this.orderLines.add(new OrderLine(gameId, numberOfItems, pricePerPiece));
            return this;
        }

        public OrderBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public OrderBuilder discount(Discount discount) {
            this.discounts.add(discount);
            return this;
        }

        public OrderBuilder deliveryMethod(DeliveryMethod deliveryMethod) {
            this.deliveryMethod = deliveryMethod;
            return this;
        }

        public Order build() {
            Objects.requireNonNull(customerId, "customerId must not be null");
            Objects.requireNonNull(status, "status must not be null");

            return new Order(
                    customerId,
                    new ArrayList<>(orderLines),
                    status,
                    new HashSet<>(discounts),
                    deliveryMethod
            );
        }
    }

    public static class OrderCannotBeModified extends RuntimeException {
    }

    private static class OrderAlreadyConfirmed extends RuntimeException {
    }

    public static class OrderNotPaid extends RuntimeException {
    }

    private static class OrderIsEmpty extends RuntimeException {
    }

    private static class NoDeliveryMethodSelected extends RuntimeException {
    }
}
