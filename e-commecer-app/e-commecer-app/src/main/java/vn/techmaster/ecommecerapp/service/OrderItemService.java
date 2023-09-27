package vn.techmaster.ecommecerapp.service;

import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.OrderItem;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.repository.OrderItemRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // get all order complete item by user login
    public List<OrderItem> findAllOrderItemByUserLogin() {
        User user = SecurityUtils.getCurrentUserLogin();
        return orderItemRepository.findByOrder_User_UserIdAndOrder_Status(user.getUserId(), OrderTable.Status.COMPLETE);
    }
}
