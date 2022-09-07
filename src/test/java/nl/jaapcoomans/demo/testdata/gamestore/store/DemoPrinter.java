package nl.jaapcoomans.demo.testdata.gamestore.store;

import nl.jaapcoomans.demo.testdata.gamestore.catalog.Game;

public class DemoPrinter {

    public static Game printForDemo(Game game) {
        System.out.println("=================== CREATED GAME ====================");
        System.out.printf("ID              : %s%n", game.getId().value());
        System.out.printf("Title           : %s%n", game.getTitle());
        System.out.printf("Category        : %s%n", game.getCategory());
        System.out.printf("Designer        : %s%n", game.getDesigner());
        System.out.printf("Nr of players   : %s%n", format(game.getNumberOfPlayers()));
        System.out.printf("Playtime in min : %s%n", format(game.getPlayingTimeInMinutes()));
        System.out.printf("Publisher ID    : %s%n", game.getPublisherId().value());
        System.out.printf("EAN code        : %s%n", game.getEanCode());

        return game;
    }

    private static String format(Game.NumberRange numberRange) {
        return String.format("(%d - %d)", numberRange.minInclusive(), numberRange.maxInclusive());
    }

    public static Order printForDemo(Order order) {
        System.out.println("=================== CREATED ORDER ===================");
        System.out.printf("ID              : %s%n", order.getId().value());
        System.out.printf("Customer ID     : %s%n", order.getCustomerId().value());
        System.out.printf("Status          : %s%n", order.getStatus());
        System.out.printf("Delivery method : %s%n", order.getDeliveryMethod());
        System.out.printf("Payment ID      : %s%n", order.getPaymentId());
        System.out.printf("Payment Date    : %s%n", order.getPaymentDate());
        System.out.printf("Payment Type    : %s%n", order.getPaymentType());
        System.out.println("Order Lines    :");
        order.getOrderLines().forEach(DemoPrinter::printForDemo);
        System.out.printf("Total amount: %s%n", order.calculateTotalAmount());

        return order;
    }

    private static void printForDemo(Order.OrderLine orderLine) {
        System.out.printf(" * %s: %d x %s = %s%n", orderLine.getDescription(), orderLine.getNumberOfItems(), orderLine.getPricePerPiece(), orderLine.totalPrice());
    }
}
