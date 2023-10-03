package vn.techmaster.ecommecerapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.Discount;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.model.projection.ProductPublic;
import vn.techmaster.ecommecerapp.repository.DiscountCampaignRepository;
import vn.techmaster.ecommecerapp.repository.DiscountRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
public class DiscountTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private DiscountCampaignRepository discountCampaignRepository;

    @Test
    void save_discount_campaing() {
        DiscountCampaign discountCampaign = new DiscountCampaign();
        discountCampaign.setName("Giảm giá tết 2024");
        discountCampaign.setSlug("giam-gia-30");
        discountCampaign.setDescription("Giảm giá 30% cho tất cả sản phẩm");
        discountCampaign.setStatus(DiscountCampaign.Status.PENDING);
        discountCampaign.setStartDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        discountCampaign.setEndDate(calendar.getTime());
        discountCampaignRepository.save(discountCampaign);
    }

    @Test
    void save_discount_and_discount_campaing() {
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(4L).get();

        Discount discount = new Discount();
        discount.setDiscountType(Discount.DiscountType.PERCENT);
        discount.setDiscountValue(30);
        discount.setDiscountCampaign(discountCampaign);
        discountRepository.save(discount);

        Discount discount2 = new Discount();
        discount2.setDiscountType(Discount.DiscountType.AMOUNT);
        discount2.setDiscountValue(1000);
        discount2.setDiscountCampaign(discountCampaign);
        discountRepository.save(discount2);
    }

    @Transactional
    @Rollback(false)
    @Test
    public void save_product_to_discount_campaing() {
        // product id 1 -> 10 add to discount id = 1
        Discount discount = discountRepository.findById(3L).get();
        for (long i = 21; i <= 30; i++) {
            Product product = productRepository.findById(i).get();
            product.getDiscounts().add(discount);
            discount.getProducts().add(product);
            productRepository.save(product);
        }

        // product id 11 -> 20 add to discount id = 2
        Discount discount2 = discountRepository.findById(4L).get();
        for (long i = 31; i <= 40; i++) {
            Product product = productRepository.findById(i).get();
            product.getDiscounts().add(discount2);
            discount2.getProducts().add(product);
            productRepository.save(product);
        }
    }

    @Transactional
    @Test
    void find_discount_by_product_id() {
        Product product = productRepository.findById(1L).get();
        ProductPublic productPublic = ProductPublic.of(product);

        System.out.println(productPublic.getPrice());
        System.out.println(productPublic.getDiscountPrice());

        Product product1 = productRepository.findById(11L).get();
        ProductPublic productPublic1 = ProductPublic.of(product1);

        System.out.println(productPublic1.getPrice());
        System.out.println(productPublic1.getDiscountPrice());

        Product product2 = productRepository.findById(21L).get();
        ProductPublic productPublic2 = ProductPublic.of(product2);

        System.out.println(productPublic2.getPrice());
        System.out.println(productPublic2.getDiscountPrice());
    }

    @Test
    void update_status_campaing() {
        // find campaing by id = 4
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(4L).get();
        discountCampaign.setStatus(DiscountCampaign.Status.PENDING);
        discountCampaignRepository.save(discountCampaign);
    }
}
