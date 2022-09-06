package nl.jaapcoomans.demo.testdata.gamestore.store;

import nl.jaapcoomans.demo.testdata.gamestore.catalog.Game;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Order {
    private final Id id;
    private final Customer.Id customerId;
    private final List<OrderLine> orderLines;
    private Status status;
    private final Set<Discount> discounts;
    private DeliveryMethod deliveryMethod;
    private Payment.Id paymentId;
    private Payment.Type paymentType;
    private LocalDate paymentDate;

    private Order(Customer.Id customerId, Status status) {
        this(customerId, new ArrayList<>(), status, new HashSet<>(), null, null, null, null);
    }

    private Order(Customer.Id customerId, List<OrderLine> orderLines, Status status, Set<Discount> discounts, DeliveryMethod deliveryMethod, Payment.Id paymentId, Payment.Type paymentType,
                  LocalDate paymentDate) {
        this.id = new Id(UUID.randomUUID());
        this.customerId = customerId;
        this.orderLines = orderLines;
        this.status = status;
        this.discounts = discounts;
        this.deliveryMethod = deliveryMethod;
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
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
        orderLines.add(new OrderLine(game, numberOfItems, game.getPrice()));
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

    public void processPayment(Payment payment) {
        if (status != Status.CONFIRMED) {
            throw new OrderNotConfirmed();
        }
        if (payment.amount().compareTo(this.calculateTotalAmount()) < 0) {
            throw new PaymentInsufficient(payment.amount(), this.calculateTotalAmount());
        }
        this.paymentId = payment.id();
        this.paymentType = payment.type();
        this.paymentDate = LocalDate.now();
        status = Status.PAID;
    }

    public Order cancelItem(Game.Id gameId) {
        if (status == Status.SHIPPED) {
            throw new OrderAlreadyShipped();
        } else if (status == Status.PAID && orderLines.size() <= 1) {
            throw new CannotCancelLastItem();
        }
        this.status = Status.CANCELLED;

        return new Order(
                customerId,
                orderLines.stream()
                        .filter(orderLine -> orderLine.gameId != gameId)
                        .toList(),
                status,
                discounts,
                deliveryMethod,
                paymentId,
                paymentType,
                paymentDate
        );
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

    public Id getId() {
        return id;
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

    public Payment.Id getPaymentId() {
        return paymentId;
    }

    public Payment.Type getPaymentType() {
        return paymentType;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
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

    public record Id(UUID value) {
    }

    public static class OrderLine {
        private final Game.Id gameId;
        private final String description;
        private final int numberOfItems;
        private final BigDecimal pricePerPiece;

        private OrderLine(Game game, int numberOfItems, BigDecimal pricePerPiece) {
            this.gameId = game.getId();
            this.description = game.getTitle();
            this.numberOfItems = numberOfItems;
            this.pricePerPiece = pricePerPiece;
        }

        public Game.Id getGameId() {
            return gameId;
        }

        public String getDescription() {
            return description;
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
        DRAFT, CONFIRMED, PAID, SHIPPED, CANCELLED
    }

    public static class OrderBuilder {
        private Customer.Id customerId;
        private final List<OrderLine> orderLines = new ArrayList<>();
        private Status status;
        private final Set<Discount> discounts = new HashSet<>();
        private DeliveryMethod deliveryMethod;
        private Payment.Id paymentId;
        private Payment.Type paymentType;
        private LocalDate paymentDate;

        private OrderBuilder() {
        }

        public OrderBuilder customerId(Customer.Id customerId) {
            this.customerId = customerId;
            return this;
        }

        public OrderBuilder orderLine(Game game, int numberOfItems, BigDecimal pricePerPiece) {
            this.orderLines.add(new OrderLine(game, numberOfItems, pricePerPiece));
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

        public OrderBuilder paymentId(Payment.Id paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public OrderBuilder paymentType(Payment.Type paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public OrderBuilder paymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
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
                    deliveryMethod,
                    paymentId,
                    paymentType,
                    paymentDate
            );
        }
    }

    public static class OrderCannotBeModified extends RuntimeException {
    }

    public static class OrderAlreadyConfirmed extends RuntimeException {
    }

    public static class OrderNotPaid extends RuntimeException {
    }

    public static class OrderIsEmpty extends RuntimeException {
    }

    public static class NoDeliveryMethodSelected extends RuntimeException {
    }

    public static class OrderNotConfirmed extends RuntimeException {
    }

    public static class OrderAlreadyShipped extends RuntimeException {
    }

    public static class CannotCancelLastItem extends RuntimeException {
    }

    public static class PaymentInsufficient extends RuntimeException {
        private PaymentInsufficient(BigDecimal paymentAmount, BigDecimal orderTotal) {
            super(toMessage(paymentAmount, orderTotal));
        }

        private static String toMessage(BigDecimal paymentAmount, BigDecimal orderTotal) {
            return String.format("Payment amount (%s) insufficient for order total of %s", paymentAmount, orderTotal);
        }
    }
}
