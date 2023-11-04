package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.model.request.OrderItemRequest;
import vn.techmaster.ecommecerapp.model.request.OrderRequest;
import vn.techmaster.ecommecerapp.repository.OrderTableRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;
import vn.techmaster.ecommecerapp.service.AddressService;
import vn.techmaster.ecommecerapp.service.OrderService;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@SpringBootTest
public class OrderTests {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;

    @Test
    void delete_all_order() {
        // Delete all order
        orderTableRepository.deleteAll();
    }

    @Test
    void save_orders() {
        Faker faker = new Faker();
        Random rd = new Random();
        List<User> userList = userRepository.findAll();
        List<Product> productList = productRepository.findByStatusIn(List.of(Product.Status.AVAILABLE));

        for (int i = 0; i < 100; i++) {
            // Random user
            User rdUser = userList.get(rd.nextInt(userList.size()));

            // Random 2-3 product
            List<Product> rdProductList = new ArrayList<>();
            List<OrderItemRequest> orderItemRequests = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Product rdProduct = productList.get(rd.nextInt(productList.size()));
                if (!rdProductList.contains(rdProduct)) {
                    rdProductList.add(rdProduct);

                    // Create OrderItemRequest
                    OrderItemRequest orderItemRequest = new OrderItemRequest();
                    orderItemRequest.setProductId(rdProduct.getProductId());
                    orderItemRequest.setQuantity(1);
                    orderItemRequest.setPrice(rdProduct.getPrice());
                    orderItemRequests.add(orderItemRequest);
                }
            }

            // Create OrderRequest
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setUsername(rdUser.getUsername());
            orderRequest.setPhone(rdUser.getPhone());
            orderRequest.setEmail(rdUser.getEmail());
            orderRequest.setProvince("Hà Nội");
            orderRequest.setDistrict("Quận Nam Từ Liêm");
            orderRequest.setWard("Phường Trung Văn");
            orderRequest.setAddress(faker.address().streetAddress());
            orderRequest.setNote(faker.lorem().sentence());
            orderRequest.setShippingMethod(OrderTable.ShippingMethod.STANDARD);
            orderRequest.setPaymentMethod(OrderTable.PaymentMethod.COD);
            orderRequest.setItems(orderItemRequests);


            String orderNumber = orderService.createOrder(orderRequest);
            log.info("orderNumber : {}", orderNumber);
        }
    }

    @Test
    void update_order() {
        List<OrderTable> orderList = orderTableRepository.findAll();
        for (OrderTable order : orderList) {
            String email = order.getEmail();
            User user = userRepository.findByEmail(email).get();
            order.setUser(user);
            order.setUseType(OrderTable.UseType.USER);
            orderTableRepository.save(order);
        }
    }

    @Test
    void update_order_date() {
        List<OrderTable> orderList = orderTableRepository.findAll();
        Date start = new Calendar.Builder().setDate(2023, 8, 1).build().getTime();
        Date end = new Date();
        for (OrderTable order : orderList) {
            order.setOrderDate(randomDateBetweenTwoDates(start, end));
            orderTableRepository.save(order);
        }
    }

    @Test
    void update_order_status() {
        for (int i = 102; i < 152; i++) {
            OrderTable orderTable = orderTableRepository.findById(Long.valueOf(i)).get();
            orderTable.setStatus(OrderTable.Status.COMPLETE);
            orderTableRepository.save(orderTable);
        }
        for (int i = 152; i < 172; i++) {
            OrderTable orderTable = orderTableRepository.findById(Long.valueOf(i)).get();
            orderTable.setStatus(OrderTable.Status.WAIT);
            orderTableRepository.save(orderTable);
        }
        for (int i = 172; i < 182; i++) {
            OrderTable orderTable = orderTableRepository.findById(Long.valueOf(i)).get();
            orderTable.setStatus(OrderTable.Status.WAIT_DELIVERY);
            orderTableRepository.save(orderTable);
        }
        for (int i = 182; i < 192; i++) {
            OrderTable orderTable = orderTableRepository.findById(Long.valueOf(i)).get();
            orderTable.setStatus(OrderTable.Status.DELIVERY);
            orderTableRepository.save(orderTable);
        }
        for (int i = 192; i < 197; i++) {
            OrderTable orderTable = orderTableRepository.findById(Long.valueOf(i)).get();
            orderTable.setStatus(OrderTable.Status.CANCELED);
            orderTableRepository.save(orderTable);
        }
        for (int i = 197; i < 202; i++) {
            OrderTable orderTable = orderTableRepository.findById(Long.valueOf(i)).get();
            orderTable.setStatus(OrderTable.Status.RETURNED);
            orderTableRepository.save(orderTable);
        }

    }

    // write method to random date between 2 date
    private Date randomDateBetweenTwoDates(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }
}
