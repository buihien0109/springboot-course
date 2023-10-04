package vn.techmaster.ecommecerapp.service;

import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.*;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.CategorySeparatePublic;
import vn.techmaster.ecommecerapp.model.projection.ProductAttributePublic;
import vn.techmaster.ecommecerapp.model.projection.ProductImagePublic;
import vn.techmaster.ecommecerapp.model.projection.ProductPublic;
import vn.techmaster.ecommecerapp.model.request.CreateProductRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateProductRequest;
import vn.techmaster.ecommecerapp.model.request.UpsertProductAttributeRequest;
import vn.techmaster.ecommecerapp.repository.CategoryRepository;
import vn.techmaster.ecommecerapp.repository.ProductAttributeRepository;
import vn.techmaster.ecommecerapp.repository.ProductImageRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final Slugify slugify;
    private final ProductAttributeRepository productAttributeRepository;
    private final FileServerService fileServerService;
    private final ProductImageRepository productImageRepository;

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
        Page<Product> pageData = productRepository.findByDiscounts_DiscountCampaign_Status(
                DiscountCampaign.Status.ACTIVE,
                PageRequest.of(page - 1, size)
        );
        return Map.of(
                "name", "Sản phẩm giảm giá",
                "slug", "",
                "products", pageData.getContent().stream().map(ProductPublic::of).toList(),
                "totalElements", pageData.getTotalElements(),
                "remain", pageData.getTotalElements() - (long) page * size < 0 ? 0 : pageData.getTotalElements() - (long) page * size,
                "currentPage", pageData.getNumber() + 1
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
                    "totalElements", pageData.getTotalElements(),
                    "remain", pageData.getTotalElements() - (long) page * size < 0 ? 0 : pageData.getTotalElements() - (long) page * size,
                    "currentPage", pageData.getNumber() + 1
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

    public Map<String, Object> loadMoreProduct(String categorySlug, Integer page, Integer size) {
        // if category name is null or empty, get all product discount valid date has pagination. Owtherwise, get all product by category name has pagination
        if (categorySlug == null || categorySlug.isEmpty()) {
            return findAllProductHasDiscountValidDate(page, size);
        }
        Page<Product> pageData = productRepository.findByCategory_ParentCategory_SlugIgnoreCase(
                categorySlug,
                PageRequest.of(page - 1, size)
        );
        return Map.of(
                "slug", categorySlug,
                "products", pageData.getContent().stream().map(ProductPublic::of).toList(),
                "totalElements", pageData.getTotalElements(),
                "remain", pageData.getTotalElements() - (long) page * size < 0 ? 0 : pageData.getTotalElements() - (long) page * size,
                "currentPage", pageData.getNumber() + 1
        );
    }

    public ProductPublic createProduct(CreateProductRequest request) {
        // check category id is exist
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy danh mục với id " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setSlug(slugify.slugify(request.getName()));
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(0);
        product.setStatus(request.getStatus());
        product.setCategory(category);

        productRepository.save(product);
        return ProductPublic.of(product);
    }

    public ProductPublic updateProductById(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + id));

        // check category id is exist
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy danh mục với id " + request.getCategoryId()));

        product.setName(request.getName());
        product.setSlug(slugify.slugify(request.getName()));
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());
        product.setCategory(category);

        // update attribute for request
        request.getAttributes().forEach(attributeRequest -> {
            // check attribute id is exist
            ProductAttribute productAttribute = product.getAttributes().stream()
                    .filter(attribute -> attribute.getAttributeId().equals(attributeRequest.getAttributeId()))
                    .findFirst()
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy thuộc tính với id " + attributeRequest.getAttributeId()));

            productAttribute.setAttributeName(attributeRequest.getAttributeName());
            productAttribute.setAttributeValue(attributeRequest.getAttributeValue());
        });

        productRepository.save(product);
        return ProductPublic.of(product);
    }

    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + id));
        productRepository.deleteById(id);
    }

    // ------------------- Logic for attribute -------------------
    @Transactional
    public ProductAttributePublic createAttribute(Long productId, UpsertProductAttributeRequest request) {
        log.info("Attribute request: {}", request);
        log.info("productId: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        // check attribute name is exist
        product.getAttributes().forEach(attribute -> {
            if (attribute.getAttributeName().equals(request.getAttributeName())) {
                throw new ResouceNotFoundException("Thuộc tính " + request.getAttributeName() + " đã tồn tại");
            }
        });

        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setAttributeName(request.getAttributeName());
        productAttribute.setAttributeValue("");
        productAttribute.setProduct(product);
        productAttributeRepository.save(productAttribute);

        log.info("Attribute created: {}", productAttribute);
        return ProductAttributePublic.of(productAttribute);
    }

    public ProductAttributePublic updateAttribute(Long productId, Long attributeId, UpsertProductAttributeRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        ProductAttribute productAttribute = product.getAttributes().stream()
                .filter(attribute -> attribute.getAttributeId().equals(attributeId))
                .findFirst()
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy thuộc tính với id " + attributeId));

        // check attribute name is exist and not same attribute name
        product.getAttributes().forEach(attribute -> {
            if (attribute.getAttributeName().equals(request.getAttributeName()) && !attribute.getAttributeId().equals(attributeId)) {
                throw new ResouceNotFoundException("Thuộc tính " + request.getAttributeName() + " đã tồn tại");
            }
        });

        productAttribute.setAttributeName(request.getAttributeName());
        productRepository.save(product);
        return ProductAttributePublic.of(productAttribute);
    }

    public void deleteAttribute(Long productId, Long attributeId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        ProductAttribute productAttribute = product.getAttributes().stream()
                .filter(attribute -> attribute.getAttributeId().equals(attributeId))
                .findFirst()
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy thuộc tính với id " + attributeId));

        product.getAttributes().remove(productAttribute);
        productRepository.save(product);
    }

    public ProductImagePublic uploadMainImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        FileServer fileServer = fileServerService.uploadFile(file);
        String url = "/api/v1/files/" + fileServer.getId();

        // delete old main image
        product.getImages().stream()
                .filter(image -> image.getImageType().equals(ProductImage.ImageType.MAIN))
                .findFirst()
                .ifPresent(productImage -> {
                    product.getImages().remove(productImage);
                    productRepository.save(product);
                });

        ProductImage mainImage = new ProductImage();
        mainImage.setImageUrl(url);
        mainImage.setImageType(ProductImage.ImageType.MAIN);
        mainImage.setProduct(product);

        product.getImages().add(mainImage);
        productRepository.save(product);

        return ProductImagePublic.of(mainImage);
    }

    public ProductImagePublic uploadSubImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        FileServer fileServer = fileServerService.uploadFile(file);
        String url = "/api/v1/files/" + fileServer.getId();

        ProductImage subImage = new ProductImage();
        subImage.setImageUrl(url);
        subImage.setImageType(ProductImage.ImageType.SUB);
        subImage.setProduct(product);

        productImageRepository.save(subImage);
        return ProductImagePublic.of(subImage);
    }

    public void deleteSubImage(Long productId, Long imageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        ProductImage productImage = product.getImages().stream()
                .filter(image -> image.getImageId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy ảnh với id " + imageId));

        product.getImages().remove(productImage);
        productRepository.save(product);
    }
}
