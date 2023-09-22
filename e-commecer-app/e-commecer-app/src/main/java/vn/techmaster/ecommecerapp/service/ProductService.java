package vn.techmaster.ecommecerapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.CategorySeparatePublic;
import vn.techmaster.ecommecerapp.model.projection.ProductPublic;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public List<ProductPublic> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductPublic::of).toList();
    }

    public ProductPublic findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find product by id " + id));
        return ProductPublic.of(product);
    }

    public ProductPublic save(Product product) {
        Product productSaved = productRepository.save(product);
        return ProductPublic.of(productSaved);
    }

    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find product by id " + id));
        productRepository.deleteById(id);
    }

    // get all product by category id
    public List<ProductPublic> findAllProductByCategoryId(Long id) {
        List<Product> products = productRepository.findByCategory_CategoryId(id);
        return products.stream().map(ProductPublic::of).toList();
    }

    // get products list same category with product id and get by limit
    public List<ProductPublic> findAllProductByCategoryIdAndProductIdNot(Long categoryId, Long productId, int limit) {
        List<Product> products = productRepository.findByCategory_CategoryIdAndProductIdNot(categoryId, productId);
        return products.stream().map(ProductPublic::of).limit(limit).toList();
    }

    // get all product by category name
    public List<ProductPublic> findAllProductByCategoryName(String name) {
        List<Product> products = productRepository.findByCategory_NameIgnoreCase(name);
        return products.stream().map(ProductPublic::of).toList();
    }

    // get all product by parent category name
    public List<ProductPublic> findAllProductByParentCategoryName(String name) {
        List<Product> products = productRepository.findByCategory_ParentCategory_NameIgnoreCase(name);
        return products.stream().map(ProductPublic::of).toList();
    }

    // get all product by parent category slug
    public List<ProductPublic> findAllProductByParentCategorySlug(String slug) {
        List<Product> products = productRepository.findByCategory_ParentCategory_SlugIgnoreCase(slug);
        return products.stream().map(ProductPublic::of).toList();
    }

    // get all product by parent category slug, page, size, sub category
    // return Page<ProductPublic>
    // page, size, sub category can be null
    public Page<ProductPublic> findAllProductByParentCategorySlug(String slug, Integer page, Integer size, String sub) {
        Page<Product> pageData;

        if (sub != null && !sub.isEmpty()) {
            pageData = productRepository.findByCategory_ParentCategory_SlugIgnoreCaseAndCategory_SlugIgnoreCase(slug, sub, PageRequest.of(page - 1, size));
        } else {
            pageData = productRepository.findByCategory_ParentCategory_SlugIgnoreCase(slug, PageRequest.of(page - 1, size));
        }

        return pageData.map(ProductPublic::of);
    }

    // get all product has discount valid date (not expies) and pagination with param page and size
    public Map<String, Object> findAllProductHasDiscountValidDate(Integer page, Integer size) {
        Page<Product> pageData = productRepository.findByDiscounts_EndDateAfter(
                new Date(),
                PageRequest.of(page - 1, size)
        );
        return Map.of(
                "name", "Sản phẩm giảm giá",
                "products", pageData.getContent().stream().map(ProductPublic::of).toList(),
                "totalElements", pageData.getTotalElements()
        );
    }

    // get all product by parent category and pagination with param page and size with product
    public List<Map<String, Object>> findAllProductCombineCategory(Integer page, Integer size) {
        List<CategorySeparatePublic> categories = categoryService.findAllByParentCategoryIsNull();
        List<Map<String, Object>> data = new ArrayList<>();
        categories.forEach(category -> {
            Page<Product> pageData = productRepository.findByCategory_ParentCategory_SlugIgnoreCase(
                    category.getMainCategory().getSlug(),
                    PageRequest.of(page - 1, size)
            );
            Map<String, Object> map = Map.of(
                    "id", category.getMainCategory().getCategoryId(),
                    "name", category.getMainCategory().getName(),
                    "slug", category.getMainCategory().getSlug(),
                    "products", pageData.getContent().stream().map(ProductPublic::of).toList(),
                    "totalElements", pageData.getTotalElements()
            );
            data.add(map);
        });
        return data;
    }

    public List<Map<String, Object>> findAllProductCombineCategoryAndDiscount(Integer page, Integer size) {
        List<Map<String, Object>> data = new ArrayList<>();
        List<Map<String, Object>> dataProductCombineCategory = findAllProductCombineCategory(page, size);
        Map<String, Object> dataDiscount = findAllProductHasDiscountValidDate(page, size);
        data.add(dataDiscount);
        data.addAll(dataProductCombineCategory);
        return data;
    }
}
