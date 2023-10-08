package vn.techmaster.ecommecerapp.rest.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpsertUserAddressRequest;
import vn.techmaster.ecommecerapp.service.UserAddressService;

@RestController
@RequestMapping("/api/v1/users/address")
@RequiredArgsConstructor
public class UserAddressResources {
    private final UserAddressService userAddressService;

    @PostMapping
    public ResponseEntity<?> createNewUserAddress(@RequestBody UpsertUserAddressRequest request) {
        return ResponseEntity.ok(userAddressService.createNewUserAddress(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserAddress(@PathVariable Long id, @RequestBody UpsertUserAddressRequest request) {
        return ResponseEntity.ok(userAddressService.updateUserAddress(id, request));
    }

    @PutMapping("/{id}/set-default")
    public ResponseEntity<?> setDefaultUserAddress(@PathVariable Long id) {
        return ResponseEntity.ok(userAddressService.setDefaultUserAddress(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable Long id) {
        userAddressService.deleteUserAddressById(id);
        return ResponseEntity.ok().build();
    }
}
