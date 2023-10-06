package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.service.CategoryService;
import vn.techmaster.ecommecerapp.service.DiscountCampaingService;
import vn.techmaster.ecommecerapp.service.ProductService;

@Controller
@RequestMapping("/admin/discount-campaigns")
@RequiredArgsConstructor
public class DiscountCampaingController {
    private final DiscountCampaingService discountCampaingService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String getDiscountPage(Model model) {
        model.addAttribute("discountCampaigns", discountCampaingService.getAllDiscountCampaigns());
        return "admin/discount-campaing/index";
    }

    @GetMapping("/create")
    public String getDiscountCreatePage(Model model) {
        model.addAttribute("statusList", DiscountCampaign.Status.values());
        model.addAttribute("typeList", DiscountCampaign.DiscountType.values());
        return "admin/discount-campaing/create";
    }

    @GetMapping("/{id}/detail")
    public String getDiscountDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("discountCampaign", discountCampaingService.getDiscountCampaignById(id));
        model.addAttribute("statusList", DiscountCampaign.Status.values());
        model.addAttribute("typeList", DiscountCampaign.DiscountType.values());
        model.addAttribute("productList", productService.findAll());
        model.addAttribute("categoryList", categoryService.findAllSubCategory());
        return "admin/discount-campaing/detail";
    }
}
