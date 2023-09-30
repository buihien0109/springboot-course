package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.BlogService;
import vn.techmaster.ecommecerapp.service.TagService;

@Controller
@RequestMapping("/admin/blogs")
public class BlogController {
    private final BlogService blogService;
    private final TagService tagService;

    public BlogController(BlogService blogService, TagService tagService) {
        this.blogService = blogService;
        this.tagService = tagService;
    }

    // Danh sách tất cả bài viết
    @GetMapping
    public String getBlogPage(Model model) {
        model.addAttribute("blogs", blogService.getAllBlogsAdmin());
        return "admin/blog/index";
    }

    // Danh sách bài viết của tôi
    @GetMapping("/own-blogs")
    public String getOwnBlogPage(Model model) {
        model.addAttribute("blogs", blogService.getAllOwnBlogAdmin());
        return "admin/blog/own-blog";
    }

    // Tạo bài viết
    @GetMapping("/create")
    public String getCreateBlogPage(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        return "admin/blog/create";
    }

    // Chi tiết bài viết
    @GetMapping("/{id}/detail")
    public String getBlogDetailPage(@PathVariable Integer id, Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("blog", blogService.getBlogById(id));
        return "admin/blog/detail";
    }
}
