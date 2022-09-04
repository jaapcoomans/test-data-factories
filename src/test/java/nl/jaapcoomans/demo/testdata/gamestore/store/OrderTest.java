package nl.jaapcoomans.demo.testdata.gamestore.store;

import nl.jaapcoomans.demo.testdata.gamestore.store.discount.CombinationDiscount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aGame;
import static nl.jaapcoomans.demo.testdata.gamestore.store.DeliveryMethodTestDataFactory.aDeliveryMethod;
import static nl.jaapcoomans.demo.testdata.gamestore.store.OrderTestDataBuilder.givenADraftOrder;
import static nl.jaapcoomans.demo.testdata.gamestore.store.OrderTestDataFactory.aDraftOrder;
import static nl.jaapcoomans.demo.testdata.gamestore.store.OrderTestDataFactory.anEmptyDraftOrder;
import static nl.jaapcoomans.demo.testdata.gamestore.store.OrderTestDataFactory.anOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {
    @Test
    public void orderTotalPrice() {
        // Given
        var order = anEmptyDraftOrder();
        var game1 = aGame();
        var game2 = aGame();

        order.addGame(game1, 1);
        order.addGame(game2, 1);

        // When
        var actualTotal = order.calculateTotalAmount();

        // Then
        var expectedTotal = game1.getPrice().add(game2.getPrice());
        assertThat(actualTotal).isEqualByComparingTo(expectedTotal);
    }

    @Test
    public void orderDeliveryPriceInTotal() {
        // Given
        var order = aDraftOrder();
        var totalAmountBefore = order.calculateTotalAmount();
        var deliveryMethod = aDeliveryMethod();

        // When
        order.selectDeliveryMethod(deliveryMethod);
        var actualTotal = order.calculateTotalAmount();

        // Then
        var expectedTotal = totalAmountBefore.add(deliveryMethod.price());
        assertThat(actualTotal).isEqualByComparingTo(expectedTotal);
    }

    @ParameterizedTest
    @EnumSource(value = Order.Status.class, mode = EnumSource.Mode.EXCLUDE, names = "PAID")
    public void onlyPaidOrdersCanBeDelivered(Order.Status status) {
        // Given
        var order = anOrder(status);

        // When + then
        assertThrows(Order.OrderNotPaid.class, order::deliver);
    }

    @Test
    public void complexTest() {
        // Given
        var game1 = aGame();
        var game2 = aGame();

        var order = givenADraftOrder()
                .withOrderLinesFor(game1, game2)
                .build();
        var totalBeforeDiscount = order.calculateTotalAmount();

        var discount = new CombinationDiscount(game1, game2, BigDecimal.TEN);

        // When
        order.applyDiscount(discount);

        // Then
        var totalAfterDiscount = order.calculateTotalAmount();
        assertThat(totalAfterDiscount).isEqualByComparingTo(totalBeforeDiscount.subtract(BigDecimal.TEN));
    }
}