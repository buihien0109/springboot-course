package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Category;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.ProductAttribute;
import vn.techmaster.ecommecerapp.entity.ProductImage;
import vn.techmaster.ecommecerapp.repository.CategoryRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.math.BigDecimal;

@SpringBootTest
public class ProductTests {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void save_products() {
        // Create 10 products, each product has 4 attributes, 4 images, 1 category using Faker
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            Product product = new Product();
            product.setName(faker.commerce().productName());
            product.setDescription(faker.commerce().material());
            product.setPrice(faker.number().numberBetween(1000, 100000));
            product.setStockQuantity(faker.number().numberBetween(10, 20));

            // Create 4 attributes for each product
            ProductAttribute attribute1 = new ProductAttribute();
            attribute1.setAttributeName("Xuất Xứ");
            attribute1.setAttributeValue(faker.country().name());

            ProductAttribute attribute2 = new ProductAttribute();
            attribute2.setAttributeName("Thành Phần");
            attribute2.setAttributeValue(faker.commerce().material());

            ProductAttribute attribute3 = new ProductAttribute();
            attribute3.setAttributeName("Hướng Dẫn Sử Dụng");
            attribute3.setAttributeValue(faker.commerce().material());

            ProductAttribute attribute4 = new ProductAttribute();
            attribute4.setAttributeName("Bảo Quản");
            attribute4.setAttributeValue(faker.commerce().material());

            // Create 4 images for each product, has 1 main image and 3 sub images
             ProductImage image1 = new ProductImage();
             image1.setImageUrl(faker.company().logo());
             image1.setImageType(ProductImage.ImageType.MAIN);

             ProductImage image2 = new ProductImage();
             image2.setImageUrl(faker.company().logo());
             image2.setImageType(ProductImage.ImageType.SUB);

             ProductImage image3 = new ProductImage();
             image3.setImageUrl(faker.company().logo());
                image3.setImageType(ProductImage.ImageType.SUB);

             ProductImage image4 = new ProductImage();
             image4.setImageUrl(faker.company().logo());
             image4.setImageType(ProductImage.ImageType.SUB);

            // random 1 sub category for each product
            Category category = categoryRepository.findById((long) faker.number().numberBetween(10, 12)).get();

            // Set relationship
            attribute1.setProduct(product);
            attribute2.setProduct(product);
            attribute3.setProduct(product);
            attribute4.setProduct(product);

            image1.setProduct(product);
            image2.setProduct(product);
            image3.setProduct(product);
            image4.setProduct(product);

            product.setCategory(category);

            // Save to database
            product.getAttributes().add(attribute1);
            product.getAttributes().add(attribute2);
            product.getAttributes().add(attribute3);
            product.getAttributes().add(attribute4);

            product.getImages().add(image1);
            product.getImages().add(image2);
            product.getImages().add(image3);
            product.getImages().add(image4);

            product.setCategory(category);
            productRepository.save(product);
        }
    }
}
