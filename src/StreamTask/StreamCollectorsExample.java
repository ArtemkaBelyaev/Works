package StreamTask;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Order {
    private String product;
    private double cost;

    public Order(String product, double cost) {
        this.product = product;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public double getCost() {
        return cost;
    }
    @Override
    public String toString() {
        return product;
    }
}

public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        Map<String, Double> totalCostAndGroupByProduct = orders.stream()
                .collect(Collectors.groupingBy
                        (Order::getProduct,
                                Collectors.summingDouble(Order::getCost)));

        List<Order> sortedOrders = orders.stream()
                .sorted(Comparator.comparing(Order::getCost).reversed())
                .collect(Collectors.toList());

        List<Order> topProductCost = orders.stream()
                .sorted(Comparator.comparing(Order::getCost).reversed())
                .limit(3)
                .collect(Collectors.toList());

        double totalCost = topProductCost.stream()
                .mapToDouble(Order::getCost).sum();

        topProductCost.forEach(System.out::println);
        System.out.println("Total cost: " + totalCost);
    }
}