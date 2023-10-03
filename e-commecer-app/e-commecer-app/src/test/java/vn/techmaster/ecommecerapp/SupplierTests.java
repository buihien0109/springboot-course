package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Supplier;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;

@SpringBootTest
public class SupplierTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void save_suppliers() {
        Faker faker = new Faker();

        // Tạo 5 nhà cung cấp sử dụng Faker
        for (int i = 0; i < 5; i++) {
            Supplier supplier = new Supplier();
            supplier.setName(faker.company().name());
            supplier.setAddress(faker.address().fullAddress());
            supplier.setEmail(faker.internet().emailAddress());
            supplier.setPhone(faker.phoneNumber().phoneNumber());
            supplier.setThumbnail(faker.company().logo());
            supplierRepository.save(supplier);
        }
    }
}
