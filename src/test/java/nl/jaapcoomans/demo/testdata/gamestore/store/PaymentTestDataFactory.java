package nl.jaapcoomans.demo.testdata.gamestore.store;

import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentTestDataFactory {
    private static final Faker faker = new Faker();

    public static Payment.Id aPaymentId() {
        return new Payment.Id(UUID.randomUUID());
    }

    public static Payment.Type aPaymentType() {
        return faker.options().option(Payment.Type.class);
    }

    private static BigDecimal anAmount() {
        long priceInCents = faker.number().numberBetween(1_00L, 250_00L);
        return BigDecimal.valueOf(priceInCents, 2);
    }

    public static Payment aPayment() {
        return aPayment(anAmount());
    }

    public static Payment aPayment(BigDecimal amount) {
        return new Payment(aPaymentId(), aPaymentType(), amount);
    }

    public static Payment aPaymentFor(Order order) {
        return aPayment(order.calculateTotalAmount());
    }

    public static Payment anInsufficientPaymentFor(Order order) {
        var orderTotal = order.calculateTotalAmount();
        var shortage = faker.number().numberBetween(1, orderTotal.unscaledValue().longValue());
        return aPayment(orderTotal.subtract(BigDecimal.valueOf(shortage, 2)));
    }
}
