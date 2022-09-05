package nl.jaapcoomans.demo.testdata.gamestore.store;

import com.github.javafaker.Faker;

import java.util.stream.IntStream;

import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aGameId;
import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aPrice;
import static nl.jaapcoomans.demo.testdata.gamestore.store.CustomerTestDataFactory.aCustomerId;
import static nl.jaapcoomans.demo.testdata.gamestore.store.DeliveryMethodTestDataFactory.aDeliveryMethod;

public class OrderTestDataFactory {
    private static final Faker faker = Faker.instance();

    public static Order.Status aStatus() {
        return faker.options().option(Order.Status.class);
    }

    public static Order anOrder() {
        return anOrder(aStatus());
    }

    public static Order anEmptyDraftOrder() {
        return Order.create(aCustomerId());
    }

    public static Order aDraftOrder() {
        return anOrder(Order.Status.DRAFT);
    }

    public static Order aConfirmedOrder() {
        var orderBuilder = Order.builder()
                .customerId(aCustomerId())
                .status(Order.Status.CONFIRMED)
                .deliveryMethod(aDeliveryMethod());

        addSomeOrderLines(orderBuilder);

        return orderBuilder.build();
    }

    public static Order anOrder(Order.Status status) {
        var orderBuilder = Order.builder()
                .customerId(aCustomerId())
                .status(status);

        addSomeOrderLines(orderBuilder);

        return orderBuilder.build();
    }

    public static int aNumberOfItems() {
        return faker.number().numberBetween(1, 5);
    }

    public static void addSomeOrderLines(Order.OrderBuilder builder) {
        var numberOfOrderLines = faker.number().numberBetween(1, 10);
        IntStream.range(0, numberOfOrderLines)
                .forEach(i -> builder.orderLine(aGameId(), aNumberOfItems(), aPrice()));

    }
}
