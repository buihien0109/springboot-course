package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.OrderItem;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.OrderItemPublic;
import vn.techmaster.ecommecerapp.model.request.AdminUpsertOrderItemRequest;
import vn.techmaster.ecommecerapp.repository.OrderItemRepository;
import vn.techmaster.ecommecerapp.repository.OrderTableRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final ProductRepository productRepository;

    // get all order complete item by user login
    public List<OrderItemPublic> findAllOrderItemByUserLogin() {
        User user = SecurityUtils.getCurrentUserLogin();
        List<OrderItem> orderItems = orderItemRepository
                .findByOrder_User_UserIdAndOrder_Status(user.getUserId(), OrderTable.Status.COMPLETE);
        return orderItems.stream().map(OrderItemPublic::of).toList();
    }

    public OrderItemPublic adminCreateOrderItem(AdminUpsertOrderItemRequest request) {
        // check order exist
        OrderTable orderTable = orderTableRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy đơn hàng"));

        // check order status
        if (orderTable.getStatus() != OrderTable.Status.WAIT) {
            throw new BadRequestException("Trạng thái đơn hàng không cho phép thêm sản phẩm");
        }

        // check product exist
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm"));

        // check product status
        if (product.getStatus() != Product.Status.AVAILABLE) {
            throw new BadRequestException("Sản phẩm không còn trạng thái bán");
        }

        // check product quantity
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new BadRequestException("Số lượng sản phẩm không đủ");
        }

        // if product exist in order item then throw exception
        OrderItem orderItemExisted = orderItemRepository
                .findByOrder_OrderIdAndProduct_ProductId(request.getOrderId(), request.getProductId())
                .orElse(null);
        if (orderItemExisted != null) {
            throw new BadRequestException("Sản phẩm đã tồn tại trong đơn hàng");
        }

        // create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(orderTable);
        orderItem.setProduct(product);
        orderItem.setQuantity(request.getQuantity());
        orderItem.setPrice(product.getPrice());

        // update product quantity
        product.setStockQuantity(product.getStockQuantity() - request.getQuantity());

        // if product quantity = 0 then set status UNAVAILABLE
        if (product.getStockQuantity() == 0) {
            product.setStatus(Product.Status.UNAVAILABLE);
        }
        productRepository.save(product);

        // save order item
        orderItemRepository.save(orderItem);
        return OrderItemPublic.of(orderItem);
    }

    public OrderItemPublic adminUpdateOrderItem(Long id, AdminUpsertOrderItemRequest request) {
        // check order exist
        OrderTable orderTable = orderTableRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy đơn hàng"));

        // check order status
        if (orderTable.getStatus() != OrderTable.Status.WAIT) {
            throw new BadRequestException("Trạng thái đơn hàng không cho phép cập nhật sản phẩm");
        }

        // check order item id exist
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm trong đơn hàng"));

        // check product exist
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm"));

        // check product in order item
        if (!Objects.equals(orderItem.getProduct().getProductId(), request.getProductId())) {
            throw new BadRequestException("Sản phẩm không tồn tại trong đơn hàng");
        }

        // calculate difference between old quantity and new quantity
        int quantityDiff = request.getQuantity() - orderItem.getQuantity();

        // check product quantity
        if (product.getStockQuantity() < quantityDiff) {
            throw new BadRequestException("Số lượng sản phẩm không đủ");
        }

        // update product quantity
        product.setStockQuantity(product.getStockQuantity() - quantityDiff);

        // if product quantity > 0 then set status AVAILABLE
        if (product.getStockQuantity() > 0) {
            product.setStatus(Product.Status.AVAILABLE);
        }

        productRepository.save(product);

        // update order item
        orderItem.setQuantity(request.getQuantity());
        orderItem.setPrice(product.getPrice());
        orderItemRepository.save(orderItem);

        return OrderItemPublic.of(orderItem);
    }

    public void adminDeleteOrderItem(Long id) {
        // check order item exist
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm trong đơn hàng"));

        // check order status
        if (orderItem.getOrder().getStatus() != OrderTable.Status.WAIT) {
            throw new BadRequestException("Trạng thái đơn hàng không cho phép xóa sản phẩm");
        }

        // update product quantity
        Product product = orderItem.getProduct();
        product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
        // if product quantity > 0 then set status AVAILABLE
        if (product.getStockQuantity() > 0) {
            product.setStatus(Product.Status.AVAILABLE);
        }
        productRepository.save(product);

        // delete order item
        orderItemRepository.delete(orderItem);
    }
}
