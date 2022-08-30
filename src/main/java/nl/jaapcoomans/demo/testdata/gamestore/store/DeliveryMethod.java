package nl.jaapcoomans.demo.testdata.gamestore.store;

import java.math.BigDecimal;

public record DeliveryMethod(DeliveryType deliveryType, String courierName, BigDecimal price) {
    public enum DeliveryType {
        REGULAR, NEXT_DAY, SAME_DAY
    }
}
