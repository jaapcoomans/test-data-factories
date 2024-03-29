package nl.jaapcoomans.demo.testdata.gamestore.store;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aGame;
import static nl.jaapcoomans.demo.testdata.gamestore.store.CustomerTestDataFactory.aCustomerId;
import static nl.jaapcoomans.demo.testdata.gamestore.store.DemoPrinter.printForDemo;
import static nl.jaapcoomans.demo.testdata.gamestore.store.OrderTestDataFactory.aNumberOfItems;
import static nl.jaapcoomans.demo.testdata.gamestore.store.PaymentTestDataFactory.aPaymentId;
import static nl.jaapcoomans.demo.testdata.gamestore.store.PaymentTestDataFactory.aPaymentType;

class OrderTestDataBuilder {
    private static final Faker faker = new Faker();

    private final Order.OrderBuilder orderBuilder;

    private OrderTestDataBuilder(Order.OrderBuilder orderBuilder) {
        this.orderBuilder = orderBuilder;
    }

    public static OrderTestDataBuilder givenAnOrder() {
        return new OrderTestDataBuilder(
                Order.builder()
                        .customerId(aCustomerId())
        );
    }

    public OrderTestDataBuilder withMultipleOrderLines() {
        IntStream.range(0, faker.number().numberBetween(2, 6))
                .mapToObj(i -> aGame())
                .forEach(game -> orderBuilder.orderLine(game, aNumberOfItems(), game.getPrice()));
        return this;
    }

    public OrderTestDataBuilder thatHasBeenPaid() {
        orderBuilder.status(Order.Status.PAID)
                .paymentDate(LocalDate.now())
                .paymentId(aPaymentId())
                .paymentType(aPaymentType());
        return this;
    }

    public Order build() {
        var order = orderBuilder.build();
        return printForDemo(order);
    }
}
