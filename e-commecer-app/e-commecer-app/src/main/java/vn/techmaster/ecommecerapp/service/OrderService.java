package vn.techmaster.ecommecerapp.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.*;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.OrderTablePublic;
import vn.techmaster.ecommecerapp.model.request.AdminCreateOrderRequest;
import vn.techmaster.ecommecerapp.model.request.OrderRequest;
import vn.techmaster.ecommecerapp.repository.OrderTableRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.utils.CartUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final UserRepository userRepository;
    private final OrderTableRepository orderTableRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final HttpServletResponse httpServletResponse;

    public List<OrderTablePublic> getAllOrders() {
        List<OrderTable> orderTables = orderTableRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        return orderTables.stream().map(OrderTablePublic::of).toList();
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
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm"));

            // check product quantity
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new BadRequestException("Sản phẩm " + product.getName() + " không đủ số lượng");
            }

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
        } else {
            CartUtils.setCartToCookie(httpServletResponse, new ArrayList<>());
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

    public List<OrderTablePublic> getOrdersByUserId(Long userId) {
        // check user id is exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResouceNotFoundException("User not found"));

        List<OrderTable> orderTables = orderTableRepository.findByUser_UserIdOrderByOrderDateDesc(userId);

        return orderTables.stream()
                .map(OrderTablePublic::of)
                .toList();
    }

    public String createOrderByAdmin(AdminCreateOrderRequest orderRequest) {
        // check user id is exist
        User user = null;
        if (orderRequest.getUserId() != null) {
            user = userRepository.findById(orderRequest.getUserId())
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy user"));
        }

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
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm"));

            // check product quantity
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new BadRequestException("Sản phẩm " + product.getName() + " không đủ số lượng");
            }

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

            // if product quantity = 0 then set status UNAVAILABLE
            if (product.getStockQuantity() == 0) {
                product.setStatus(Product.Status.UNAVAILABLE);
            }
            productRepository.save(product);
        });

        return orderNumber;
    }

    public void cancelOrderByAdmin(Long id) {
        // find order by id
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy đơn hàng"));

        // check order status
        if (orderTable.getStatus() != OrderTable.Status.WAIT) {
            throw new ResouceNotFoundException("Trạng thái đơn hàng không cho phép hủy");
        }

        // update order status to CANCELED
        orderTable.setStatus(OrderTable.Status.CANCELED);

        // save order to database
        orderTableRepository.save(orderTable);

        // update product quantity
        orderTable.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());

            // if product quantity > 0 then set status AVAILABLE
            if (product.getStockQuantity() > 0) {
                product.setStatus(Product.Status.AVAILABLE);
            }
            productRepository.save(product);
        });
    }
}
