package com.example.demo.thymeleaf.crud.controller;

import com.example.demo.thymeleaf.crud.entity.Blog;
import com.example.demo.thymeleaf.crud.entity.Product;
import com.example.demo.thymeleaf.crud.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
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

//    @PostMapping("/saveBlog")
//    public String saveBlog(@Valid @ModelAttribute("blog") Blog blog, BindingResult result) {
//        if (result.hasErrors()) {
//            return "blog/create";
//        }
//        blogService.save(blog);
//        return "redirect:/admin/blogs";
//    }

    @PostMapping("/saveBlog")
    public String saveBlog(@Valid @ModelAttribute("blog") Blog blog, BindingResult result, @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "blog/create";
        }

        try {
            blogService.save(blog, file);
        } catch (IOException e) {
            log.error(e.getMessage());
            redirectAttributes.addFlashAttribute("message", "Upload failed!");
            return "blog/create";
        }

        return "redirect:/admin/blogs";
    }

    @GetMapping("/edit/{id}")
    public String showEditBlogForm(@PathVariable Long id, Model model) {
        Blog blog = blogService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        model.addAttribute("blog", blog);
        return "blog/detail";
    }

//    @PostMapping("/updateBlog")
//    public String updateBlog(@Valid @ModelAttribute("blog") Blog blog, BindingResult result) {
//        if (result.hasErrors()) {
//            return "blog/detail";
//        }
//        blogService.save(blog);
//        return "redirect:/admin/blogs";
//    }

    @PostMapping("/updateBlog")
    public String updateBlog(@Valid @ModelAttribute("blog") Blog blog, BindingResult result, @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "blog/detail";
        }

        try {
            blogService.update(blog, file);
        } catch (IOException e) {
            log.error(e.getMessage());
            redirectAttributes.addFlashAttribute("message", "Upload failed!");
            return "blog/detail";
        }

        return "redirect:/admin/blogs";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/admin/blogs";
    }
}
