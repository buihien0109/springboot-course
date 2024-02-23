package vn.techmaster.ecommecerapp.service;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.Category;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.ProductAttribute;
import vn.techmaster.ecommecerapp.entity.Supplier;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.dto.CategorySeparateDto;
import vn.techmaster.ecommecerapp.model.dto.ProductAdminDto;
import vn.techmaster.ecommecerapp.model.dto.ProductNormalAdminDto;
import vn.techmaster.ecommecerapp.model.dto.ProductNormalDto;
import vn.techmaster.ecommecerapp.model.projection.ProductAttributePublic;
import vn.techmaster.ecommecerapp.model.projection.product.ProductPublic;
import vn.techmaster.ecommecerapp.model.request.CreateProductRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateProductRequest;
import vn.techmaster.ecommecerapp.model.request.UpsertProductAttributeRequest;
import vn.techmaster.ecommecerapp.model.response.ImageResponse;
import vn.techmaster.ecommecerapp.model.response.PageResponse;
import vn.techmaster.ecommecerapp.model.response.PageResponseImpl;
import vn.techmaster.ecommecerapp.repository.CategoryRepository;
import vn.techmaster.ecommecerapp.repository.ProductAttributeRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;
import vn.techmaster.ecommecerapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final Slugify slugify;
    private final ProductAttributeRepository productAttributeRepository;
    private final FileServerService fileServerService;

    public List<ProductNormalAdminDto> getAllProductsAdmin() {
        return productRepository.getAllProductsAdmin();
    }

    public List<ProductPublic> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductPublic::of).toList();
    }

    public List<ProductAdminDto> getAllAvailabelProductsAdminDtoByAdmin() {
        return productRepository.getAllAvailabelProductsAdminDtoByAdmin();
    }

    public List<ProductNormalAdminDto> getAllAvailabelProductsNormalAdminDtoByAdmin() {
        return productRepository.getAllAvailabelProductsNormalAdminDtoByAdmin();
    }

    public ProductPublic findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find product by id " + id));
        return ProductPublic.of(product);
    }

    public ProductPublic findByIdAndSlug(Long id, String slug) {
        Product product = productRepository.findByProductIdAndSlug(id, slug)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find product by id " + id + " and slug " + slug));
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

    // get products list same category with product id and get by limit
    public List<ProductNormalDto> getRelatedProducts(Long categoryId, Long productId, int limit) {
        return productRepository.getRelatedProducts(categoryId, productId, limit);
    }

    // get all product by parent category slug, page, size, sub category
    // return Page<ProductPublic>
    // page, size, sub category can be null
    public PageResponse<ProductNormalDto> findAllProductByParentCategorySlug(String slug, Integer page, Integer size, String sub) {
        List<ProductNormalDto> productList;
        if (sub != null && !sub.isEmpty()) {
            log.info("Sub category: {}", sub);
            productList = productRepository.getProductsByCategorySlug(sub);
        } else {
            log.info("Parent category: {}", slug);
            productList = productRepository.getProductsByParentCategorySlug(slug);
        }

        log.info("Product list size: {}", productList.size());
        return new PageResponseImpl<>(productList, page, size);
    }

    // get all product has discount valid date (not expies) and pagination with param page and size
    public Map<String, Object> findAllProductHasDiscountValidDate(Integer page, Integer size) {
        List<ProductNormalDto> productList = productRepository.getAllDiscountProducts();
        int startIndex = (page - 1) * size;
        int endIndex = page * size;
        return Map.of(
                "name", "Sản phẩm giảm giá",
                "slug", "",
                "products", productList.size() >= endIndex
                        ? productList.subList(startIndex, endIndex)
                        : productList.subList(startIndex, productList.size()),
                "totalElements", productList.size(),
                "remain", productList.size() - (long) page * size < 0 ? 0 : productList.size() - (long) page * size,
                "currentPage", page
        );
    }

    // get all product by parent category and pagination with param page and size with product
    public List<Map<String, Object>> findAllProductCombineCategory(Integer page, Integer size) {
        List<CategorySeparateDto> categories = categoryService.findAllByParentCategoryIsNull();
        List<Map<String, Object>> data = new ArrayList<>();
        categories.forEach(category -> {
            List<ProductNormalDto> productList = productRepository.getProductsByParentCategorySlug(category.getMainCategory().getSlug());
            int startIndex = (page - 1) * size;
            int endIndex = page * size;
            Map<String, Object> map = Map.of(
                    "id", category.getMainCategory().getCategoryId(),
                    "name", category.getMainCategory().getName(),
                    "slug", category.getMainCategory().getSlug(),
                    "products", productList.size() >= endIndex
                            ? productList.subList(startIndex, endIndex)
                            : productList.subList(startIndex, productList.size()),
                    "totalElements", productList.size(),
                    "remain", productList.size() - (long) page * size < 0 ? 0 : productList.size() - (long) page * size,
                    "currentPage", page
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
        List<ProductNormalDto> productList = productRepository.getProductsByParentCategorySlug(categorySlug);
        int startIndex = (page - 1) * size;
        int endIndex = page * size;
        return Map.of(
                "slug", categorySlug,
                "products", productList.size() >= endIndex
                        ? productList.subList(startIndex, endIndex)
                        : productList.subList(startIndex, productList.size()),
                "totalElements", productList.size(),
                "remain", productList.size() - (long) page * size < 0 ? 0 : productList.size() - (long) page * size,
                "currentPage", page
        );
    }

    public ProductPublic createProduct(CreateProductRequest request) {
        // check category id is existed
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy danh mục với id " + request.getCategoryId()));

        // check supplier id is existed
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy nhà cung cấp với id " + request.getSupplierId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setSlug(slugify.slugify(request.getName()));
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());
        product.setPrice(request.getPrice());
        product.setMainImage(StringUtils.generateLinkImage(request.getName()));
        product.setStatus(request.getStatus());
        product.setCategory(category);
        product.setSupplier(supplier);

        productRepository.save(product);
        return ProductPublic.of(product);
    }

    public ProductPublic updateProductById(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + id));

        // check category id is existed
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy danh mục với id " + request.getCategoryId()));

        product.setName(request.getName());
        product.setSlug(slugify.slugify(request.getName()));
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());
        product.setCategory(category);

        if (request.getSupplierId() != null) {
            // check supplier id is existed
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy nhà cung cấp với id " + request.getSupplierId()));
            product.setSupplier(supplier);
        }

        // update attribute for request
        request.getAttributes().forEach(attributeRequest -> {
            // check attribute id is existed
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

    public ImageResponse uploadMainImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        // TODO: Xóa ảnh cũ

        ImageResponse imageResponse = fileServerService.uploadFile(file);
        product.setMainImage(imageResponse.getUrl());
        productRepository.save(product);

        return imageResponse;
    }

    public ImageResponse uploadSubImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        ImageResponse imageResponse = fileServerService.uploadFile(file);
        List<String> subImages = product.getSubImages() == null ? new ArrayList<>() : product.getSubImages();
        subImages.add(imageResponse.getUrl());
        product.setSubImages(subImages);
        productRepository.save(product);
        return imageResponse;
    }

    public void deleteSubImage(Long productId, String imageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        // TODO: Xóa ảnh cũ

        product.getSubImages().removeIf(image -> image.toLowerCase().contains(imageId.toLowerCase()));
        productRepository.save(product);
    }
}
