package nl.jaapcoomans.demo.testdata.gamestore.store;

import net.datafaker.Faker;

import java.util.stream.IntStream;

import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aGame;
import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aPrice;
import static nl.jaapcoomans.demo.testdata.gamestore.store.CustomerTestDataFactory.aCustomerId;
import static nl.jaapcoomans.demo.testdata.gamestore.store.DeliveryMethodTestDataFactory.aDeliveryMethod;
import static nl.jaapcoomans.demo.testdata.gamestore.store.DemoPrinter.printForDemo;

public class OrderTestDataFactory {
    private static final Faker faker = new Faker();

    public static Order.Status aStatus() {
        return faker.options().option(Order.Status.class);
    }

    public static Order anOrder() {
        var order = anOrder(aStatus());
        return printForDemo(order);
    }

    public static Order anEmptyDraftOrder() {
        var order = Order.create(aCustomerId());
        return printForDemo(order);
    }

    public static Order aDraftOrder() {
        var order = anOrder(Order.Status.DRAFT);
        return printForDemo(order);
    }

    public static Order aConfirmedOrder() {
        var orderBuilder = Order.builder()
                .customerId(aCustomerId())
                .status(Order.Status.CONFIRMED)
                .deliveryMethod(aDeliveryMethod());

        addSomeOrderLines(orderBuilder);

        var order = orderBuilder.build();
        return printForDemo(order);
    }

    public static Order anOrder(Order.Status status) {
        var orderBuilder = Order.builder()
                .customerId(aCustomerId())
                .status(status);

        addSomeOrderLines(orderBuilder);

        var order = orderBuilder.build();
        return printForDemo(order);
    }

    public static int aNumberOfItems() {
        return faker.number().numberBetween(1, 5);
    }

    public static void addSomeOrderLines(Order.OrderBuilder builder) {
        var numberOfOrderLines = faker.number().numberBetween(1, 5);
        IntStream.range(0, numberOfOrderLines)
                .forEach(i -> builder.orderLine(aGame(), aNumberOfItems(), aPrice()));
    }

    public static Order.OrderBuilder anOrderBuilder() {
        return Order.builder()
                .status(Order.Status.DRAFT)
                .orderLine(aGame(), aNumberOfItems(), aPrice())
                .deliveryMethod(aDeliveryMethod())
                .customerId(aCustomerId());
    }
}
