package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.BannerService;

@Controller
@RequestMapping("/admin/banners")
public class BannerController {
    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping
    public String getBannerPage(Model model) {
        model.addAttribute("banners", bannerService.getAllBanners());
        return "admin/banner/index";
    }

    @GetMapping("/create")
    public String getBannerCreatePage() {
        return "admin/banner/create";
    }

    @GetMapping("/{id}/detail")
    public String getBannerDetailPage(@PathVariable Integer id, Model model) {
        model.addAttribute("banner", bannerService.getBannerById(id));
        return "admin/banner/detail";
    }

    @GetMapping("/sort")
    public String getBannerSortPage(Model model) {
        model.addAttribute("banners", bannerService.getAllBannersActive());
        return "admin/banner/sort";
    }
}
