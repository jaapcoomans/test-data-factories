package nl.jaapcoomans.demo.testdata.gamestore.store;

import java.math.BigDecimal;

public interface Discount {
    String description();

    boolean isApplicableFor(Order order);

    BigDecimal totalAmount();
}
