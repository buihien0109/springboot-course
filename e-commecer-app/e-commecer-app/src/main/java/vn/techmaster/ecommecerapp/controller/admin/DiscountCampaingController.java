package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.service.DiscountCampaingService;

@Controller
@RequestMapping("/admin/discount-campaigns")
public class DiscountCampaingController {

    private final DiscountCampaingService discountCampaingService;

    public DiscountCampaingController(DiscountCampaingService discountCampaingService) {
        this.discountCampaingService = discountCampaingService;
    }

    @GetMapping
    public String getDiscountPage(Model model) {
        model.addAttribute("discountCampaigns", discountCampaingService.getAllDiscountCampaigns());
        return "admin/discount-campaing/index";
    }

    @GetMapping("/create")
    public String getDiscountCreatePage(Model model) {
        model.addAttribute("statusList", DiscountCampaign.Status.values());
        return "admin/discount-campaing/create";
    }

    @GetMapping("/{id}/detail")
    public String getDiscountDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("discountCampaign", discountCampaingService.getDiscountCampaignById(id));
        model.addAttribute("statusList", DiscountCampaign.Status.values());
        return "admin/discount-campaing/detail";
    }
}
