package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.*;
import vn.techmaster.ecommecerapp.repository.CategoryRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class ProductTests {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Slugify slugify;
    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void save_products() {
        // Create 10 products, each product has 4 attributes, 4 images, 1 category using Faker
        Faker faker = new Faker();
        Random rd = new Random();
        List<Category> categories = categoryRepository.findByParentCategoryIsNotNull();
        List<Supplier> suppliers = supplierRepository.findAll();
        for (int i = 0; i < 100; i++) {
            Product product = new Product();
            String name = faker.commerce().productName();
            product.setName(name);
            product.setSlug(slugify.slugify(name));
            product.setDescription(faker.lorem().sentence());
            product.setPrice(randomPrice());
            product.setStockQuantity(faker.number().numberBetween(10, 20));
            product.setStatus(Product.Status.AVAILABLE);

            // Create 4 attributes for each product
            ProductAttribute attribute1 = new ProductAttribute();
            attribute1.setAttributeName("Xuất Xứ");
            attribute1.setAttributeValue(faker.country().name());

            ProductAttribute attribute2 = new ProductAttribute();
            attribute2.setAttributeName("Thành Phần");
            attribute2.setAttributeValue(faker.lorem().sentence());

            ProductAttribute attribute3 = new ProductAttribute();
            attribute3.setAttributeName("Hướng Dẫn Sử Dụng");
            attribute3.setAttributeValue(faker.lorem().sentence());

            ProductAttribute attribute4 = new ProductAttribute();
            attribute4.setAttributeName("Bảo Quản");
            attribute4.setAttributeValue(faker.lorem().sentence());

            // Set relationship
            attribute1.setProduct(product);
            attribute2.setProduct(product);
            attribute3.setProduct(product);
            attribute4.setProduct(product);

            // Save to database
            product.getAttributes().add(attribute1);
            product.getAttributes().add(attribute2);
            product.getAttributes().add(attribute3);
            product.getAttributes().add(attribute4);

            Category rdCate = categories.get(rd.nextInt(categories.size()));
            product.setCategory(rdCate);

            Supplier rdSupplier = suppliers.get(rd.nextInt(suppliers.size()));
            product.setSupplier(rdSupplier);

            productRepository.save(product);
        }
    }

    @Transactional
    @Test
    void insert_an_product_attribute_to_product() {
        // Insert an attribute to an existing product
        Product product = productRepository.findById(1L).get();
        ProductAttribute attribute = new ProductAttribute();
        attribute.setAttributeName("Xuất Xứ");
        attribute.setAttributeValue("Việt Nam");
        attribute.setProduct(product);
        product.getAttributes().add(attribute);

        System.out.println(attribute);
        productRepository.save(product);
    }

    @Transactional
    @Test
    void insert_an_product_image_to_product() {
        // Insert an image to an existing product
        Product product = productRepository.findById(1L).get();
        ProductImage image = new ProductImage();
        image.setImageUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.istockphoto.com%2Fvn%2Fphotos%2F%25C4%2591%25E1%25BB%2591i-h%25E1%25BB%258Dc&psig=AOvVaw0QZ3Z3Z2Z2Z2Z2Z2Z2Z2Z2&ust=1629788450008000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCJjQ1ZqBzvICFQAAAAAdAAAAABAD");
        image.setImageType(ProductImage.ImageType.MAIN);
        image.setProduct(product);
        product.getImages().add(image);

        System.out.println(image);
        productRepository.save(product);
    }

    @Test
    void update_slug() {
        // update all slug of products
        for (Product product : productRepository.findAll()) {
            product.setSlug(slugify.slugify(product.getName()));
            productRepository.save(product);
        }
    }

    @Test
    void update_supplier() {
        Random random = new Random();
        List<Supplier> suppliers = supplierRepository.findAll();

        // for each product, random a supplier
        for (Product product : productRepository.findAll()) {
            product.setSupplier(suppliers.get(random.nextInt(suppliers.size())));
            productRepository.save(product);
        }
    }

    @Test
    void update_stock_quantity() {
        Faker faker = new Faker();
        // update stock quantity of all products
        for (Product product : productRepository.findAll()) {
            if (product.getStatus() == Product.Status.AVAILABLE) {
                product.setStockQuantity(faker.number().numberBetween(10, 30));
            } else {
                product.setStockQuantity(0);
            }
            productRepository.save(product);
        }
    }

    @Test
    void update_price_product() {
        // update price of all products
        for (Product product : productRepository.findAll()) {
            product.setPrice(randomPrice());
            productRepository.save(product);
        }
    }

    @Test
    void update_category_product() {
        Random rd = new Random();
        List<Product> products = productRepository.findAll();
        List<Category> categories = categoryRepository.findByParentCategoryIsNotNull();

        for (Product product: products) {
            Category rdCate = categories.get(rd.nextInt(categories.size()));
            product.setCategory(rdCate);
            productRepository.save(product);
        }
    }

    // viết method để random số từ 10000 đến 1000000 và làm tròn đến hàng nghìn
    public int randomPrice() {
        Random random = new Random();
        int price = random.nextInt(100000 - 10000 + 1) + 10000;
        price = price / 1000;
        price = price * 1000;
        return price;
    }

    // crawl image from unsplash.com with keyword = food
    // https://unsplash.com/s/photos/food

}
