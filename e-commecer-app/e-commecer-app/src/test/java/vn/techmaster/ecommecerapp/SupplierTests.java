package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Supplier;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;

import java.util.List;

@SpringBootTest
public class SupplierTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void save_suppliers() {
        Faker faker = new Faker();

        // Tạo 5 nhà cung cấp sử dụng Faker
        for (int i = 0; i < 10; i++) {
            Supplier supplier = new Supplier();
            String name = faker.company().name();
            supplier.setName(name);
            supplier.setAddress(faker.address().fullAddress());
            supplier.setEmail(faker.internet().emailAddress());
            supplier.setPhone(faker.phoneNumber().phoneNumber());
            supplier.setThumbnail(getCharacter(name));
            supplierRepository.save(supplier);
        }
    }

    @Test
    void update_all_thumbnail_for_supplier() {
        List<Supplier> suppliers = supplierRepository.findAll();
        for (Supplier supplier : suppliers) {
            supplier.setThumbnail(generateLinkAuthorAvatar(supplier.getName()));
            supplierRepository.save(supplier);
        }
    }

    // get character first each of word from string, and to uppercase
    private String getCharacter(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(word.charAt(0));
        }
        if(result.length() >= 2) {
            return result.substring(0, 2).toUpperCase();
        } else {
            return result.toString().toUpperCase();
        }
    }

    // generate link author avatar follow struct : https://placehold.co/200x200?text=[...]
    private String generateLinkAuthorAvatar(String authorName) {
        return "https://placehold.co/200x200?text=" + getCharacter(authorName);
    }
}
