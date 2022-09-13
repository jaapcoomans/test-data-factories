package nl.jaapcoomans.demo.testdata.gamestore.store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDate;

import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aGame;
import static nl.jaapcoomans.demo.testdata.gamestore.catalog.GameTestDataFactory.aPrice;
import static nl.jaapcoomans.demo.testdata.gamestore.store.DeliveryMethodTestDataFactory.aDeliveryMethod;
import static nl.jaapcoomans.demo.testdata.gamestore.store.OrderTestDataBuilder.givenAnOrder;
import static nl.jaapcoomans.demo.testdata.gamestore.store.OrderTestDataFactory.*;
import static nl.jaapcoomans.demo.testdata.gamestore.store.PaymentTestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {
    @Test
    @DisplayName("The total price of an order without delivery method should be equal to the sum of the prices of all games.")
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
    @DisplayName("The total price of an order should be equal to the sum of the prices of all games and the delivery cost.")
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

    @Test
    @DisplayName("When an order is paid, the status becomes PAID, the payment and the payment date are set.")
    public void payOrder() {
        // Given
        var order = aConfirmedOrder();
        var payment = aPaymentFor(order);

        // When
        order.processPayment(payment);

        // Then
        assertThat(order.getStatus()).isEqualTo(Order.Status.PAID);
        assertThat(order.getPaymentDate()).isNotNull();
        assertThat(order.getPaymentId()).isEqualTo(payment.id());
        assertThat(order.getPaymentType()).isEqualTo(payment.type());
    }

    @Test
    @DisplayName("When the payment for an order is insufficient, an exception is thrown.")
    public void payOrderInsufficient() {
        // Given
        var order = aConfirmedOrder();
        var payment = anInsufficientPaymentFor(order);

        // When
        var exception = assertThrows(Order.PaymentInsufficient.class, () ->
                order.processPayment(payment)
        );

        assertThat(exception.getMessage()).isEqualTo(String.format("Payment amount (%s) insufficient for order total of %s", payment.amount(), order.calculateTotalAmount()));
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
    public void aComplexTestWithBuilder() {
        // Given
        var order = anOrderBuilder()
                .orderLine(aGame(), aNumberOfItems(), aPrice())
                .orderLine(aGame(), aNumberOfItems(), aPrice())
                .status(Order.Status.PAID)
                .paymentId(aPaymentId())
                .paymentType(aPaymentType())
                .paymentDate(LocalDate.now())
                .build();
        var itemToCancel = order.getOrderLines().get(0);

        // When
        var newOrder = order.cancelItem(itemToCancel.getGameId());

        // Then
        assertThat(order.getId()).isNotEqualTo(newOrder.getId());
        assertThat(order.getStatus()).isEqualTo(Order.Status.CANCELLED);
        assertThat(newOrder.getOrderLines().size()).isEqualTo(order.getOrderLines().size() - 1);
        assertThat(newOrder.calculateTotalAmount()).isEqualTo(order.calculateTotalAmount().subtract(itemToCancel.totalPrice()));
    }

    @Test
    public void aComplexTestWithTestDataBuilder() {
        // Given
        var order = givenAnOrder()
                .withMultipleOrderLines()
                .thatHasBeenPaid()
                .build();
        var itemToCancel = order.getOrderLines().get(0);

        // When
        var newOrder = order.cancelItem(itemToCancel.getGameId());

        // Then
        assertThat(order.getId()).isNotEqualTo(newOrder.getId());
        assertThat(order.getStatus()).isEqualTo(Order.Status.CANCELLED);
        assertThat(newOrder.getOrderLines().size()).isEqualTo(order.getOrderLines().size() - 1);
        assertThat(newOrder.calculateTotalAmount()).isEqualTo(order.calculateTotalAmount().subtract(itemToCancel.totalPrice()));
    }
}
