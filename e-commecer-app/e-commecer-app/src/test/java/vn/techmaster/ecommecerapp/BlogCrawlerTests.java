package vn.techmaster.ecommecerapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.utils.crawl.BlogCrawlerService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BlogCrawlerTests {
    @Autowired
    private BlogCrawlerService blogCrawlerService;

    @Test
    void crawl_mutilple_blog() {
        List<String> urls = new ArrayList<>(List.of(
                "https://pasgo.vn/blog/cach-lam-suon-sot-chua-ngot-kieu-mien-bac-cuc-ngon-com-3019",
                "https://pasgo.vn/blog/cach-lam-banh-mi-cha-bong-thit-nuong-ngon-nhu-hang-com-3018",
                "https://pasgo.vn/blog/cach-ngam-chanh-dao-mat-ong-ngon-khong-dang-khong-noi-vang-don-gian-ngay-tai-nha-hoamkt-823-5415",
                "https://pasgo.vn/blog/vitanin-nao-tot-cho-da-trangmkt-4871",
                "https://pasgo.vn/blog/bau-an-kho-qua-duoc-khong-lo-an-co-say-thai-khong-thumkt-823-5395",
                "https://pasgo.vn/blog/me-bau-an-mang-duoc-khong-thumkt-4912",
                "https://pasgo.vn/blog/an-buoi-co-giam-can-khong-cach-an-buoi-giam-can-dung-hieu-qua-hoamkt-823-5388",
                "https://pasgo.vn/blog/tac-dung-tuyet-voi-cua-chanh-dao-ngam-mat-ong-than-duoc-cho-suc-khoe-hoamkt-823-5412",
                "https://pasgo.vn/blog/trung-ga-ky-gi-ky-voi-rau-nao-thumkt-5202",
                "https://pasgo.vn/blog/kho-qua-ky-voi-gi-muop-dang-ky-voi-gi-thumkt-5206",
                "https://pasgo.vn/blog/nguoi-lon-uong-canxi-bao-lau-thi-ngung-thumkt-4970",
                "https://pasgo.vn/blog/mong-tay-bi-san-gon-song-trangmkt-5035",
                "https://pasgo.vn/blog/nen-uong-vitamin-e-luc-nao-vitamin-e-ngay-uong-may-vien-trangmkt-5040",
                "https://pasgo.vn/blog/nen-bo-sung-kem-trong-bao-lau-1-nam-bo-sung-kem-may-lan-trangmkt-5029",
                "https://pasgo.vn/blog/uong-collagen-luc-nao-tot-nhat-trangmkt-5001",
                "https://pasgo.vn/blog/cua-dong-ky-gi-ky-voi-rau-gi-thumkt-5205",
                "https://pasgo.vn/blog/tom-ki-gi-10-thuc-pham-an-cung-tom-gay-tieu-chay-5216",
                "https://pasgo.vn/blog/sot-xuat-huyet-kieng-gi-khong-nen-an-gi-kieng-gio-khong-thumkt-923-5503",
                "https://pasgo.vn/blog/bi-sot-xuat-huyet-nen-an-gi-de-tang-tieu-cau-nhanh-khoi-thumkt-923-5502",
                "https://pasgo.vn/blog/nam-ngu-quay-dau-huong-nao-tot-trangmkt-4978",
                "https://pasgo.vn/blog/trieu-chung-sot-xuat-huyet-o-nguoi-lon-thumkt-923-4954",
                "https://pasgo.vn/blog/sot-xuat-huyet-co-lay-khong-lay-qua-duong-nao-nhanh-nhat-thumkt-923-5499",
                "https://pasgo.vn/blog/sot-xuat-huyet-co-duoc-tam-khong-co-gay-so-co-giat-thumkt-923-5500",
                "https://pasgo.vn/blog/trieu-chung-sot-xuat-huyet-va-cach-dieu-tri-thumkt-723-5496",
                "https://pasgo.vn/blog/uong-nuoc-dua-nhieu-co-tot-khong-nuoc-dua-co-thay-the-nuoc-loc-khong-hoamkt-823-5454",
                "https://pasgo.vn/blog/nuoc-dua-de-duoc-bao-lau-cach-bao-quan-nuoc-dua-ngon-lau-khong-bi-thiu-hong-hoamkt-823-5455",
                "https://pasgo.vn/blog/tac-dung-cua-nuoc-dua-la-gi-cong-dung-tuyet-voi-ban-chac-chan-chua-biet-hoamkt-823-5452",
                "https://pasgo.vn/blog/an-tao-co-beo-khong-cach-giam-can-bang-tao-trong-3-ngay-hieu-qua-hoamkt-823-5446",
                "https://pasgo.vn/blog/an-na-co-nong-khong-canh-bao-nhung-nguoi-nay-tuyet-doi-dung-an-na-hoamkt-823-5434",
                "https://pasgo.vn/blog/cach-tri-ho-ngua-co-cho-ba-bau-nhanh-an-toan-thumkt-823-5429",
                "https://pasgo.vn/blog/an-nho-co-beo-khong-giai-dap-giam-can-nen-hay-khong-nen-an-nho-hoamkt-823-5426",
                "https://pasgo.vn/blog/vitanin-nao-tot-cho-da-trangmkt-4871",
                "https://pasgo.vn/blog/3-cach-lam-banh-trung-thu-thap-cam-sangmkt-723-5313",
                "https://pasgo.vn/blog/cach-hap-tom-hum-ngon-nhat-don-gian-nhat-sangmkt-823-5398",
                "https://pasgo.vn/blog/2-cach-lam-banh-thu-nhan-dau-xanh-de-nhat-sangmkt-723-5312",
                "https://pasgo.vn/blog/cach-nau-xoi-dau-den-tai-nha-phong-chong-tim-mach-99-khong-biet-4208",
                "https://pasgo.vn/blog/nhung-mon-an-han-quoc-noi-tieng-lam-tu-thit-ga-3857",
                "https://pasgo.vn/blog/cach-nau-bun-mang-vit-ngon-dam-da-ca-nha-me-tit-3848",
                "https://pasgo.vn/blog/cach-lam-com-tam-sai-gon-ngon-dung-dieu-don-gian-3841",
                "https://pasgo.vn/blog/bi-quyet-lam-mon-nem-nuong-cuc-ngon-va-hap-dan-3844",
                "https://pasgo.vn/blog/cach-lam-ca-keo-kho-ngon-don-gian-tot-cho-suc-khoe-3847",
                "https://pasgo.vn/blog/tiet-lo-cach-kho-thit-ngon-chuan-com-me-nau-3843",
                "https://pasgo.vn/blog/3-cach-lam-banh-yen-mach-giup-an-kieng-giam-can-hieu-qua-ngay-tai-nha-4485",
                "https://pasgo.vn/blog/an-yen-mach-giam-can-dung-hay-khong-va-cac-cach-an-yen-mach-giam-can-4524",
                "https://pasgo.vn/blog/cach-nau-chao-yen-mach-cho-be-chi-voi-4-buoc-don-gian-va-nhanh-chong-4514",
                "https://pasgo.vn/blog/mach-ban-cach-giam-can-hieu-qua-bang-yen-mach-4398",
                "https://pasgo.vn/blog/yen-mach-la-gi-phan-loai-gia-tri-dinh-duong-va-tac-dung-cua-yen-mach-4475",
                "https://pasgo.vn/blog/thien-duong-moi-la--noi-ban-co-the-buoc-di-tren-song-1336",
                "https://pasgo.vn/blog/choang-ngop-voi-ve-dep-ngo-ngang-cua-bien-thien-cam-1317",
                "https://pasgo.vn/blog/lieu-ban-da-biet-den-thien-duong-co-that-nay-chua-1309",
                "https://pasgo.vn/blog/6-dia-diem-check-in-chua-tung-ha-nhiet-trong-mua-he-nay-1302",
                "https://pasgo.vn/blog/cam-nang-hanh-trinh-du-lich-kham-pha-da-nang-1290"
        ));

        for (String url : urls) {
            blogCrawlerService.crawlAndSaveBlogPost(url);
        }
    }
}
