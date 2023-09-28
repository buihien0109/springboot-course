package vn.techmaster.ecommecerapp.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.*;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.OrderTablePublic;
import vn.techmaster.ecommecerapp.model.request.OrderRequest;
import vn.techmaster.ecommecerapp.repository.OrderTableRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.List;

@Service
public class OrderService {

    private final OrderTableRepository orderTableRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public OrderService(OrderTableRepository orderTableRepository, CartService cartService, ProductRepository productRepository) {
        this.orderTableRepository = orderTableRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    public String createOrder(OrderRequest orderRequest) {
        // Lấy thông tin user từ SecurityContextHolder
        User user = SecurityUtils.getCurrentUserLogin();

        // Create order from request info
        OrderTable orderTable = new OrderTable();

        // generate order number with 8 characters
        String orderNumber = RandomStringUtils.randomAlphanumeric(8);
        orderTable.setOrderNumber(orderNumber);
        orderTable.setUsername(orderRequest.getUsername());
        orderTable.setPhone(orderRequest.getPhone());
        orderTable.setEmail(orderRequest.getEmail());
        orderTable.setProvince(orderRequest.getProvince());
        orderTable.setDistrict(orderRequest.getDistrict());
        orderTable.setWard(orderRequest.getWard());
        orderTable.setAddress(orderRequest.getAddress());
        orderTable.setNote(orderRequest.getNote());
        orderTable.setShippingMethod(orderRequest.getShippingMethod());
        orderTable.setPaymentMethod(orderRequest.getPaymentMethod());
        orderTable.setCouponCode(orderRequest.getCouponCode());
        orderTable.setCouponDiscount(orderRequest.getCouponDiscount());
        orderTable.setUser(user);
        orderTable.setStatus(OrderTable.Status.WAIT);
        orderTable.setUseType(user == null ? OrderTable.UseType.ANONYMOUS : OrderTable.UseType.USER);

        // save list order item from request to order item
        orderRequest.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new ResouceNotFoundException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderTable.addOrderItem(orderItem);
        });

        // save order to database
        orderTableRepository.save(orderTable);

        // update product quantity
        orderTable.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        });

        // delete all cart item in cart of user
        if (user != null) {
            Cart cart = cartService.getCartForLoggedInUser(user.getUserId());
            cart.getCartItems().clear();
            cartService.saveCart(cart);
        }

        return orderNumber;
    }

    public OrderTablePublic getOrderById(Long id) {
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Order not found"));

        return OrderTablePublic.of(orderTable);
    }

    public OrderTablePublic getOrderByOrderNumber(String orderNumber) {
        OrderTable orderTable = orderTableRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResouceNotFoundException("Order not found"));

        return OrderTablePublic.of(orderTable);
    }

    public List<OrderTablePublic> getOrderHistoryByUserLogin() {
        User user = SecurityUtils.getCurrentUserLogin();
        List<OrderTable> orderTables = orderTableRepository.findByUser_UserIdOrderByOrderDateDesc(user.getUserId());

        // filter order by status. get order with status is COMPLETED, RETURNED, CANCELLED
        List<OrderTable> orderTablesReturn = orderTables.stream()
                .filter(order -> order.getStatus() == OrderTable.Status.COMPLETE
                        || order.getStatus() == OrderTable.Status.RETURNED
                        || order.getStatus() == OrderTable.Status.CANCELED)
                .toList();

        return orderTablesReturn.stream()
                .map(OrderTablePublic::of)
                .toList();
    }

    public List<OrderTablePublic> getAllOrderWaitAndDeliveryByUserLogin() {
        User user = SecurityUtils.getCurrentUserLogin();
        List<OrderTable> orderTables = orderTableRepository.findByUser_UserIdOrderByOrderDateDesc(user.getUserId());

        // filter order by status. get order with status is WAIT, DELIVERY
        List<OrderTable> orderTablesReturn = orderTables.stream()
                .filter(order -> order.getStatus() == OrderTable.Status.WAIT
                        || order.getStatus() == OrderTable.Status.DELIVERY)
                .toList();

        return orderTablesReturn.stream()
                .map(OrderTablePublic::of)
                .toList();
    }

    public void cancelOrder(Long id) {
        // find order by id
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Order not found"));

        // check order status
        if (orderTable.getStatus() != OrderTable.Status.WAIT) {
            throw new ResouceNotFoundException("Order status is not WAIT");
        }

        // update order status to CANCELED
        orderTable.setStatus(OrderTable.Status.CANCELED);

        // save order to database
        orderTableRepository.save(orderTable);

        // update product quantity
        orderTable.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        });
    }
}
