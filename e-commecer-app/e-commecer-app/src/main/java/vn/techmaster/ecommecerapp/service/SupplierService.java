package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.FileServer;
import vn.techmaster.ecommecerapp.entity.Supplier;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.SupplierPublic;
import vn.techmaster.ecommecerapp.model.response.ImageResponse;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;
import vn.techmaster.ecommecerapp.repository.TransactionRepository;
import vn.techmaster.ecommecerapp.utils.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final FileServerService fileServerService;

    public List<SupplierPublic> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream().map(SupplierPublic::of).toList();
    }

    public SupplierPublic getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy nhà cung cấp với id: " + id));
        return SupplierPublic.of(supplier);
    }

    public SupplierPublic createSupplier(Supplier supplier) {
        supplier.setThumbnail(StringUtils.generateLinkImage(supplier.getName()));
        Supplier newSupplier = supplierRepository.save(supplier);
        return SupplierPublic.of(newSupplier);
    }

    public SupplierPublic updateSupplier(Long id, Supplier supplier) {
        Supplier updatedSupplier = supplierRepository.findById(id)
                .map(s -> {
                    s.setName(supplier.getName());
                    s.setAddress(supplier.getAddress());
                    s.setEmail(supplier.getEmail());
                    s.setPhone(supplier.getPhone());
                    return supplierRepository.save(s);
                })
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy nhà cung cấp với id: " + supplier.getSupplierId()));
        return SupplierPublic.of(updatedSupplier);
    }

    public void deleteSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy nhà cung cấp với id: " + id));

        // Kiểm tra xem nhà cung cấp đã nhập hàng chưa?
        if(transactionRepository.existsBySupplier_SupplierId(supplier.getSupplierId())) {
            throw new ResouceNotFoundException("Nhà cung cấp đã nhập hàng, không thể xóa");
        }

        // Kiểm tra xem nhà cung cấp có sản phẩm nào không?
        if(productRepository.existsBySupplier_SupplierId(supplier.getSupplierId())) {
            throw new ResouceNotFoundException("Nhà cung cấp có sản phẩm, không thể xóa");
        }

        supplierRepository.deleteById(id);
    }

    public String updateThumbnail(Long id, MultipartFile file) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy nhà cung cấp với id: " + id));

        ImageResponse imageResponse = fileServerService.uploadFile(file);
        String thumbnail = imageResponse.getUrl();
        supplier.setThumbnail(thumbnail);
        supplierRepository.save(supplier);
        return supplier.getThumbnail();
    }
}
