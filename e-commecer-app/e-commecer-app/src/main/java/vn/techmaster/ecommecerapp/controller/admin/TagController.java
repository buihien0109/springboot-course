package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.ecommecerapp.service.TagService;

@Controller
@RequestMapping("/admin/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String getTagPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = "10") Integer limit,
                              Model model) {
        model.addAttribute("pageData", tagService.getAllTags(page, limit));
        model.addAttribute("currentPage", page);
        return "admin/tag/index";
    }
}
