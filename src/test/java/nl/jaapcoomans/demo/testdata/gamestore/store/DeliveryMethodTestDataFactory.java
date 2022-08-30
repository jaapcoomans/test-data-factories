package nl.jaapcoomans.demo.testdata.gamestore.store;

import com.github.javafaker.Faker;

import java.math.BigDecimal;

public class DeliveryMethodTestDataFactory {
    private static final Faker faker = Faker.instance();

    public static DeliveryMethod aDeliveryMethod() {
        return new DeliveryMethod(
                aDeliveryType(),
                aCourierName(),
                aDeliveryPrice()
        );
    }

    public static DeliveryMethod.DeliveryType aDeliveryType() {
        return faker.options().option(DeliveryMethod.DeliveryType.class);
    }

    public static String aCourierName() {
        return faker.company().name();
    }

    public static BigDecimal aDeliveryPrice() {
        long priceInCents = faker.number().numberBetween(5_00L, 20_00L);
        return BigDecimal.valueOf(priceInCents, 2);
    }
}