package com.example.demo.thymeleaf.crud.controller;

import com.example.demo.thymeleaf.crud.entity.Blog;
import com.example.demo.thymeleaf.crud.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/blogs")
public class BlogController {
    private final BlogService blogService;

    @GetMapping
    public String viewHomePage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(required = false) String keyword) {
        Page<Blog> blogPage = blogService.findPaginatedWithSearch(keyword, page, size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("totalItems", blogPage.getTotalElements());
        model.addAttribute("listBlogs", blogPage.getContent());
        model.addAttribute("keyword", keyword);
        return "blog/index";
    }

    @GetMapping("/showNewBlogForm")
    public String showNewBlogForm(Model model) {
        Blog blog = new Blog();
        model.addAttribute("blog", blog);
        return "blog/create";
    }

    @PostMapping("/saveBlog")
    public String saveBlog(@Valid @ModelAttribute("blog") Blog blog, BindingResult result) {
        if (result.hasErrors()) {
            return "blog/create";
        }
        blogService.save(blog);
        return "redirect:/admin/blogs";
    }

    @GetMapping("/edit/{id}")
    public String showEditBlogForm(@PathVariable Long id, Model model) {
        Blog blog = blogService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        model.addAttribute("blog", blog);
        return "blog/detail";
    }

    @PostMapping("/updateBlog")
    public String updateBlog(@Valid @ModelAttribute("blog") Blog blog, BindingResult result) {
        if (result.hasErrors()) {
            return "blog/detail";
        }
        blogService.save(blog);
        return "redirect:/admin/blogs";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/admin/blogs";
    }
}
