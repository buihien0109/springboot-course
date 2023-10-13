package vn.techmaster.ecommecerapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.repository.OrderTableRepository;

@SpringBootTest
public class OrderTests {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void delete_all_order() {
        // Delete all order
        orderTableRepository.deleteAll();
    }
}
