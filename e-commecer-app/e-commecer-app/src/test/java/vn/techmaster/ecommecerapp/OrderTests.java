package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.*;
import vn.techmaster.ecommecerapp.model.projection.product.ProductPublic;
import vn.techmaster.ecommecerapp.repository.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@SpringBootTest
public class OrderTests {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Slugify slugify;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;


    @Test
    void delete_all_order() {
        orderTableRepository.deleteAll();
    }

    @Transactional
    @Rollback(false)
    @Test
    void save_orders() {
        Faker faker = new Faker();
        Random rd = new Random();
        List<User> userList = userRepository.findByRoles_NameIn(List.of("USER"));
        List<Province> provinceList = provinceRepository.findAll();

        Date start = new Calendar.Builder().setDate(2023, 11, 1).build().getTime();
        Date end = new Date();

        for (int i = 0; i < 20; i++) {
            // Random user
            User rdUser = null;
            String name = generateName();
            String phone = generatePhone();
            String email = generateEmail(name);
            boolean isUser = rd.nextInt(2) == 1;
            if (isUser) {
                rdUser = userList.get(rd.nextInt(userList.size()));
                name = rdUser.getUsername();
                phone = rdUser.getPhone();
                email = rdUser.getEmail();
            }
            OrderTable.UseType useType = isUser ? OrderTable.UseType.USER : OrderTable.UseType.ANONYMOUS;

            // Random address
            Province rdProvince = provinceList.get(rd.nextInt(provinceList.size()));

            List<District> districtList = districtRepository.findByProvince_Code(rdProvince.getCode());
            District rdDistrict = districtList.get(rd.nextInt(districtList.size()));

            List<Ward> wardList = wardRepository.findByDistrict_Code(rdDistrict.getCode());
            Ward rdWard = wardList.get(rd.nextInt(wardList.size()));

            // Create Order
            OrderTable orderTable = new OrderTable();
            String orderNumber = RandomStringUtils.randomAlphanumeric(8);
            orderTable.setOrderNumber(orderNumber);
            orderTable.setOrderDate(randomDateBetweenTwoDates(start, end));
            orderTable.setUsername(name);
            orderTable.setPhone(phone);
            orderTable.setEmail(email);
            orderTable.setProvince(rdProvince.getName());
            orderTable.setDistrict(rdDistrict.getName());
            orderTable.setWard(rdWard.getName());
            orderTable.setAddress("Số " + (rd.nextInt(100) + 1) + " " + faker.address().streetName());
            orderTable.setShippingMethod(OrderTable.ShippingMethod.values()[rd.nextInt(OrderTable.ShippingMethod.values().length)]);
            orderTable.setPaymentMethod(OrderTable.PaymentMethod.values()[rd.nextInt(OrderTable.PaymentMethod.values().length)]);
            orderTable.setStatus(OrderTable.Status.COMPLETE);
            orderTable.setUseType(useType);
            orderTable.setUser(rdUser);
            orderTableRepository.save(orderTable);
        }
    }

    @Transactional
    @Rollback(false)
    @Test
    void save_order_item_to_order() {
        Random rd = new Random();
        List<OrderTable> orderList = orderTableRepository.findAll();
        List<Product> productList = productRepository.findByStatusIn(List.of(Product.Status.AVAILABLE));

        // Random 2-3 OrderItem
        for (OrderTable order : orderList) {
            for (int j = 0; j < rd.nextInt(2) + 2; j++) {
                Product rdProduct = productList.get(rd.nextInt(productList.size()));
                // Create OrderItem
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(rdProduct);
                orderItem.setQuantity(rd.nextInt(2) + 1);

                ProductPublic productPublic = ProductPublic.of(rdProduct);
                orderItem.setPrice(productPublic.getDiscountPrice() == null ? productPublic.getPrice() : productPublic.getDiscountPrice());
                orderItem.setOrder(order);
                orderItemRepository.save(orderItem);
            }
        }

    }

    public String generateName() {
        List<String> listHo = List.of(
                "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng",
                "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý", "Đào", "Đinh", "Lâm", "Phùng", "Mai",
                "Tô", "Trịnh", "Đoàn", "Tăng", "Bành", "Hà", "Thái", "Tạ", "Tăng", "Thi"
        );

        // Random ds tên đệm người dùng theo tên gọi Việt Nam
        List<String> listTenDem = List.of(
                "Văn", "Thị", "Hồng", "Hải", "Hà", "Hưng", "Hùng", "Hạnh", "Hạ", "Thanh");

        // Random ds tên người dùng theo tên gọi Việt Nam (30 tên phổ biến từ A -> Z) (ít vần H)
        List<String> listTen = List.of(
                "An", "Bình", "Cường", "Dũng", "Đức", "Giang", "Hải", "Hào", "Hùng", "Hưng", "Minh", "Nam", "Nghĩa", "Phong", "Phúc", "Quân", "Quang", "Quốc", "Sơn", "Thắng", "Thành", "Thiên", "Thịnh", "Thuận", "Tiến", "Trung", "Tuấn", "Vinh", "Vũ", "Xuân"
        );

        Random random = new Random();
        String ho = listHo.get(random.nextInt(listHo.size()));
        String tenDem = listTenDem.get(random.nextInt(listTenDem.size()));
        String ten = listTen.get(random.nextInt(listTen.size()));

        return ho + " " + tenDem + " " + ten;
    }

    public String generatePhone() {
        // Random ds số điện thoại Việt Nam bao gồm 10 số
        List<String> phones = List.of(
                "086", "096", "097", "098", "032", "033", "034", "035", "036", "037", "038", "039",
                "090", "093", "070", "079", "077", "076", "078", "089", "088", "091", "094", "083",
                "085", "081", "082", "092", "056", "058", "099"
        );

        Random random = new Random();
        StringBuilder phone = new StringBuilder(phones.get(random.nextInt(phones.size())));
        for (int i = 0; i < 7; i++) {
            phone.append(random.nextInt(10));
        }
        return phone.toString();
    }

    public String generateEmail(String name) {
        return slugify.slugify(name.toLowerCase()).replaceAll("-", "") + "@gmail.com";
    }

    @Test
    void update_order_date() {
        List<OrderTable> orderList = orderTableRepository.findAll();
        Date start = new Calendar.Builder().setDate(2023, 11, 1).build().getTime();
        Date end = new Date();
        for (OrderTable order : orderList) {
            order.setOrderDate(randomDateBetweenTwoDates(start, end));
            orderTableRepository.save(order);
        }
    }

    // write method to random date between 2 date
    private Date randomDateBetweenTwoDates(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }
}
