package nl.jaapcoomans.demo.testdata.gamestore.store;

public class OrderDemoPrinter {

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
        order.getOrderLines().forEach(OrderDemoPrinter::printForDemo);
        System.out.printf("Total amount: %s%n", order.calculateTotalAmount());

        return order;
    }

    private static void printForDemo(Order.OrderLine orderLine) {
        System.out.printf(" * %s: %d x %s = %s%n", orderLine.getDescription(), orderLine.getNumberOfItems(), orderLine.getPricePerPiece(), orderLine.totalPrice());
    }
}
