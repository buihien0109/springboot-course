package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.FileServer;
import vn.techmaster.ecommecerapp.entity.Supplier;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.SupplierPublic;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
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
        supplierRepository.deleteById(id);
    }

    public String updateThumbnail(Long id, MultipartFile file) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy nhà cung cấp với id: " + id));

        FileServer fileServer = fileServerService.uploadFile(file);
        String thumbnail = "/api/v1/files/" + fileServer.getId();
        supplier.setThumbnail(thumbnail);
        supplierRepository.save(supplier);
        return supplier.getThumbnail();
    }
}
