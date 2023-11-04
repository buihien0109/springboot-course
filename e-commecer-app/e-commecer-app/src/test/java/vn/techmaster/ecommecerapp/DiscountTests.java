package vn.techmaster.ecommecerapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.model.projection.product.ProductPublic;
import vn.techmaster.ecommecerapp.repository.DiscountCampaignRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
public class DiscountTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountCampaignRepository discountCampaignRepository;

    @Test
    void save_discount_campaing() {
        DiscountCampaign discountCampaign = new DiscountCampaign();
        discountCampaign.setName("Giảm giá tết 2024");
        discountCampaign.setSlug("gia-gia-tet-2024");
        discountCampaign.setDescription("Giảm giá 30% cho tất cả sản phẩm");
        discountCampaign.setDiscountType(DiscountCampaign.DiscountType.PERCENT);
        discountCampaign.setDiscountValue(30);
        discountCampaign.setStatus(DiscountCampaign.Status.ACTIVE);
        discountCampaign.setStartDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        discountCampaign.setEndDate(calendar.getTime());
        discountCampaignRepository.save(discountCampaign);

        DiscountCampaign discountCampaign2 = new DiscountCampaign();
        discountCampaign2.setName("Khuyến mại ngày phụ nữ 20/10");
        discountCampaign2.setSlug("khuyen-mai-20-10");
        discountCampaign2.setDescription("Khuyến mại ngày phụ nữ 20/10");
        discountCampaign2.setDiscountType(DiscountCampaign.DiscountType.AMOUNT);
        discountCampaign2.setDiscountValue(10000);
        discountCampaign2.setStatus(DiscountCampaign.Status.PENDING);
        discountCampaign2.setStartDate(new Date());
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, 7);
        discountCampaign2.setEndDate(calendar2.getTime());
        discountCampaignRepository.save(discountCampaign2);

        DiscountCampaign discountCampaign3 = new DiscountCampaign();
        discountCampaign3.setName("Giảm giá nhân dịp sinh nhật 1 năm");
        discountCampaign3.setSlug("giam-gia-sinh-nhat-1-nam");
        discountCampaign3.setDescription("Đây là chương trình giảm giá nhân dịp sinh nhật 1 năm");
        discountCampaign3.setDiscountType(DiscountCampaign.DiscountType.SAME_PRICE);
        discountCampaign3.setDiscountValue(30000);
        discountCampaign3.setStatus(DiscountCampaign.Status.ACTIVE);
        discountCampaign3.setStartDate(new Date());
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DATE, 7);
        discountCampaign3.setEndDate(calendar3.getTime());
        discountCampaignRepository.save(discountCampaign3);
    }

    @Transactional
    @Rollback(false)
    @Test
    public void save_product_to_discount_campaing() {
        // product id 1 -> 10 add to discount id = 1
        DiscountCampaign discount = discountCampaignRepository.findById(1L).get();
        for (long i = 1; i <= 10; i++) {
            Product product = productRepository.findById(i).get();
            product.getDiscounts().add(discount);
            discount.getProducts().add(product);
            productRepository.save(product);
        }

        // product id 11 -> 20 add to discount id = 2
        DiscountCampaign discount2 = discountCampaignRepository.findById(2L).get();
        for (long i = 11; i <= 20; i++) {
            Product product = productRepository.findById(i).get();
            product.getDiscounts().add(discount2);
            discount2.getProducts().add(product);
            productRepository.save(product);
        }

        // product id 11 -> 20 add to discount id = 2
        DiscountCampaign discount3 = discountCampaignRepository.findById(3L).get();
        for (long i = 21; i <= 30; i++) {
            Product product = productRepository.findById(i).get();
            product.getDiscounts().add(discount3);
            discount3.getProducts().add(product);
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
