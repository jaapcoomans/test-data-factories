package nl.jaapcoomans.demo.testdata.gamestore.store;

import java.math.BigDecimal;
import java.util.UUID;

public record Payment(Id id, Type type, BigDecimal amount) {
    public record Id(UUID value) {
    }

    public enum Type {
        CREDIT_CARD, BANK_TRANSFER, POST_PAY, PAYPAL
    }
}
