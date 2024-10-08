import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {
    /////////////////////////////////////// GET OLDEST PER STATUS /////////////////////////////
    @Test
    void getOldestOrderPerStatus_testWithObjectsOfOneStatus(){
        ShopService shopService = new ShopService();
        OrderRepo repo = new OrderMapRepo();
        Product product0 = new Product("1", "Apple");
        Product product1 = new Product("2", "Banana");
        Product product2 = new Product("3", "Orange");
        Product product3 = new Product("4", "Pear");
        Order newerOrderCompleted = new Order("3", List.of(product2),
                OrderStatus.COMPLETED,
                ZonedDateTime.parse("2019-09-05T16:21:48.907793+02:00[Europe/Berlin]"));
        Order olderOrderCompleted = new Order("6875494",
                List.of(product0, product1, product3), OrderStatus.COMPLETED,
                ZonedDateTime.parse("2000-09-05T16:21:48.907793+02:00[Europe/Berlin]"));
        repo.addOrder(newerOrderCompleted);
        repo.addOrder(olderOrderCompleted);
        shopService.setOrderRepo(repo);
        Map<OrderStatus, Order> expected = new HashMap<>(Map.ofEntries(
                Map.entry(OrderStatus.COMPLETED, olderOrderCompleted)
        ));
        Map<OrderStatus, Order> actual = null;
        try {
            actual = shopService.getOldestOrdersPerStatus();
        } catch (OrderDoesntExistException exception) {
        }
        assertEquals(expected, actual);
    }

    @Test
    void getOldestOrderPerStatus() {
        ShopService shopService = new ShopService();
        OrderRepo repo = new OrderMapRepo();
        Product product0 = new Product("1", "Apple");
        Product product1 = new Product("2", "Banana");
        Product product2 = new Product("3", "Orange");
        Product product3 = new Product("4", "Pear");
        Order newerOrderProcessing = new Order("1", List.of(product0),
                OrderStatus.PROCESSING,
                ZonedDateTime.parse("2021-09-05T16:21:48.907793+02:00[Europe/Berlin]"));
        Order olderOrderProcessing = new Order("2", List.of(product1),
                OrderStatus.PROCESSING,
                ZonedDateTime.parse("2010-09-05T16:21:48.907793+02:00[Europe/Berlin]"));
        repo.addOrder(newerOrderProcessing);
        repo.addOrder(olderOrderProcessing);
        Order newerOrderInDelivery = new Order("4", List.of(product3),
                OrderStatus.IN_DELIVERY,
                ZonedDateTime.parse("2024-09-05T16:21:48.907793+02:00[Europe/Berlin]"));
        Order olderOrderInDelivery = new Order("adie78", List.of(product3, product2),
                OrderStatus.IN_DELIVERY,
                ZonedDateTime.parse("2010-09-05T16:21:48.907793+02:00[Europe/Berlin]"));
        repo.addOrder(newerOrderInDelivery);
        repo.addOrder(olderOrderInDelivery);
        Order newerOrderCompleted = new Order("3", List.of(product2),
                OrderStatus.COMPLETED,
                ZonedDateTime.parse("2019-09-05T16:21:48.907793+02:00[Europe/Berlin]"));
        Order olderOrderCompleted = new Order("6875494",
                List.of(product0, product1, product3), OrderStatus.COMPLETED,
                ZonedDateTime.parse("2000-09-05T16:21:48.907793+02:00[Europe/Berlin]"));
        repo.addOrder(newerOrderCompleted);
        repo.addOrder(olderOrderCompleted);
        shopService.setOrderRepo(repo);
        Map<OrderStatus, Order> expected = new HashMap<>(Map.ofEntries(
                Map.entry(OrderStatus.COMPLETED, olderOrderCompleted),
                Map.entry(OrderStatus.IN_DELIVERY, olderOrderInDelivery),
                Map.entry(OrderStatus.PROCESSING, olderOrderProcessing)

        ));
        Map<OrderStatus, Order> actual = null;
        try {
            actual = shopService.getOldestOrdersPerStatus();
        } catch (OrderDoesntExistException exception) {
        }
        assertEquals(expected, actual);
    }

    ///////////////////////////////////// GET OLDEST ORDER OF CERTAIN STATUS ////////////////////////////////////////


    @Test
    void getOldestOrderOfCertainStatus_Test() {
        ShopService shopService = new ShopService();
        OrderRepo repo = new OrderMapRepo();
        Product product0 = new Product("1", "Apple");
        Product product1 = new Product("2", "Banana");
        Product product2 = new Product("3", "Orange");
        Product product3 = new Product("4", "Pear");
        Order newOrder0 = new Order("1", List.of(product0), OrderStatus.PROCESSING,
                ZonedDateTime.now());
        Order newOrder1 = new Order("2", List.of(product1), OrderStatus.PROCESSING,
                ZonedDateTime.now());
        Order newOrder2 = new Order("3", List.of(product2), OrderStatus.PROCESSING,
                ZonedDateTime.now());
        Order newOrder5 = new Order(IdService.generateId(), List.of(product0,
                product1, product3), OrderStatus.PROCESSING, ZonedDateTime.now());
        repo.addOrder(newOrder0);
        repo.addOrder(newOrder1);
        repo.addOrder(newOrder2);
        repo.addOrder(newOrder5);
        shopService.setOrderRepo(repo);
        Order expected = newOrder0;
        Order actual = null;
        try {
            actual = shopService.getOldestOrderOfCertainStatus(OrderStatus.PROCESSING);
        } catch (OrderDoesntExistException exception) {
        }
        ;
        assertEquals(expected, actual);
    }
    //////////////////////////////////////// GET OLDEST ORDER ////////////////////////////////////////

    @Test
    void getOldestOrder_Test() {
        ShopService shopService = new ShopService();
        OrderRepo repo = new OrderMapRepo();
        Product product0 = new Product("1", "Apple");
        Product product1 = new Product("2", "Banana");
        Product product2 = new Product("3", "Orange");
        Product product3 = new Product("4", "Pear");
        Order newOrder0 = new Order("1", List.of(product0), OrderStatus.PROCESSING,
                ZonedDateTime.now());
        Order newOrder1 = new Order("2", List.of(product1), OrderStatus.PROCESSING,
                ZonedDateTime.now());
        Order newOrder2 = new Order("3", List.of(product2), OrderStatus.PROCESSING,
                ZonedDateTime.now());
        Order newOrder5 = new Order(IdService.generateId(), List.of(product0,
                product1, product3), OrderStatus.PROCESSING, ZonedDateTime.now());
        repo.addOrder(newOrder0);
        repo.addOrder(newOrder1);
        repo.addOrder(newOrder2);
        repo.addOrder(newOrder5);
        shopService.setOrderRepo(repo);
        List<Order> orders = repo.getOrders();
        Order expected = newOrder0;
        Order actual = shopService.getOldestOrder(orders).orElse(null);
        assertEquals(expected, actual);
    }


    //////////////////////////////////// GET ALL ORDERS OF CERTAIN STATUS ///////////////////////////////////


    @Test
    void getOrdersByStatus_Test() {
        ShopService shopService = new ShopService();
        OrderRepo repo = new OrderMapRepo();
        Product product0 = new Product("1", "Apple");
        Order newOrder0 = new Order("1", List.of(product0), OrderStatus.PROCESSING,
                ZonedDateTime.now());
        Product product1 = new Product("2", "Banana");
        Order newOrder1 = new Order("2", List.of(product1), OrderStatus.PROCESSING,
                ZonedDateTime.now());
        Product product2 = new Product("3", "Orange");
        Order newOrder2 = new Order("3", List.of(product2), OrderStatus.COMPLETED,
                ZonedDateTime.now());
        Product product3 = new Product("4", "Pear");
        Order newOrder3 = new Order("4", List.of(product3), OrderStatus.IN_DELIVERY,
                ZonedDateTime.now());
        repo.addOrder(newOrder0);
        repo.addOrder(newOrder1);
        repo.addOrder(newOrder2);
        repo.addOrder(newOrder3);
        shopService.setOrderRepo(repo);
        List<Order> expected = new ArrayList<>(Arrays.asList(newOrder0, newOrder1));
        List<Order> actual = null;
        try {
            actual = shopService.getAllOrdersOfCertainStatus(OrderStatus.PROCESSING);
        } catch (Exception exception) {
        }
        assertEquals(expected, actual);
    }

    //////////////////////////////////////// UPDATE ORDER ////////////////////////////////////////
    @Test
    void updateOrder_TestException() {
        ShopService shopService = new ShopService();
        assertThrows(
                Exception.class, () -> shopService.updateOrder("234",
                        OrderStatus.PROCESSING)
        );
    }

    @Test
    void updateOrder_Test() {
        ShopService shopService = new ShopService();
        OrderRepo repo = new OrderMapRepo();
        Product product0 = new Product("1", "Apple");
        Order newOrder0 = new Order("1", List.of(product0), OrderStatus.PROCESSING,
                null);
        repo.addOrder(newOrder0);
        shopService.setOrderRepo(repo);
        Order expected = new Order("1", List.of(product0), OrderStatus.IN_DELIVERY, null);
        Order actual = null;
        try {
            actual = shopService.updateOrder("1", OrderStatus.IN_DELIVERY);
            System.out.println("actual inside try:" + actual);
        } catch (Exception exception) {
        }
        assertEquals(expected, actual);
    }

    //////////////////////////////////////// ADD ORDER ////////////////////////////////////////
    @Test
    void addOrder_TestException() throws Exception {
        ShopService shopService = new ShopService();
        assertThrows(
                Exception.class, () -> shopService.addOrder(List.of("XY"))
        );
    }

    @Test
    void addOrder_Test() throws Exception {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apple")),
                OrderStatus.IN_DELIVERY, null);
        assertEquals(expected.products(), actual.products());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() throws Exception {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN
        Order actual = null;
        try {
            actual = shopService.addOrder(productsIds);
        } catch (Exception exception) {
        }

        //THEN
        assertNull(actual);
    }
}
