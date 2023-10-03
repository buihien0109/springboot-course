package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Banner;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.BannerPublic;
import vn.techmaster.ecommecerapp.model.request.SortBannerRequest;
import vn.techmaster.ecommecerapp.model.request.UpsertBannerRequest;
import vn.techmaster.ecommecerapp.repository.BannerRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;


    public List<BannerPublic> getAllBanners() {
        List<Banner> banners = bannerRepository.findAll(Sort.by("createdAt").descending());
        return banners.stream().map(BannerPublic::of).toList();
    }

    public BannerPublic getBannerById(Integer id) {
        return bannerRepository.findById(id).map(BannerPublic::of)
                .orElseThrow(() -> new RuntimeException("Cannot find banner by id: " + id));
    }

    public BannerPublic createBanner(UpsertBannerRequest request) {
        // create banner
        Banner banner = Banner.builder()
                .name(request.getName())
                .url(request.getUrl())
                .linkRedirect(request.getLinkRedirect())
                .status(request.getStatus())
                .build();

        // check banner status = true => set displayOrder = length of banners status = true + 1
        if (request.getStatus()) {
            List<Banner> banners = bannerRepository.findAllByStatus(true);
            banner.setDisplayOrder(banners.size() + 1);
        }

        bannerRepository.save(banner);
        return BannerPublic.of(banner);
    }

    public BannerPublic updateBanner(Integer id, UpsertBannerRequest request) {
        log.info("updateBanner");
        log.info("id: " + id);
        log.info("request: " + request);

        // check if banner status change true -> false
        boolean isUpdateDisplayOrder = false;

        // find banner by id
        Banner bannerToUpdate = bannerRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy banner với id: " + id));

        // if banner status = false and request status = true => set displayOrder = length of banners status = true + 1
        if (!bannerToUpdate.getStatus() && request.getStatus()) {
            log.info("Truong hop 1");

            List<Banner> banners = bannerRepository.findAllByStatus(true);
            bannerToUpdate.setDisplayOrder(banners.size() + 1);
        }

        // if banner status = true and request status = false => set displayOrder = null and update displayOrder of banners status = true
        if (bannerToUpdate.getStatus() && !request.getStatus()) {
            log.info("Truong hop 2");

            bannerToUpdate.setDisplayOrder(null);
            isUpdateDisplayOrder = true;
        }

        // update banner
        bannerToUpdate.setName(request.getName());
        bannerToUpdate.setUrl(request.getUrl());
        bannerToUpdate.setLinkRedirect(request.getLinkRedirect());
        bannerToUpdate.setStatus(request.getStatus());

        log.info("bannerToUpdate: " + bannerToUpdate);
        bannerRepository.save(bannerToUpdate);

        // check if banner status change true -> false => update displayOrder of banners status = true
        if(isUpdateDisplayOrder) {

            // update displayOrder of banners status = true
            List<Banner> banners = bannerRepository.findAllByStatus(true);

            // Sort banners by displayOrder ascending
            List<Banner> bannersSorted = banners.stream().sorted(Comparator.comparingInt(Banner::getDisplayOrder)).toList();

            // update displayOrder of banners status = true
            for (int i = 0; i < bannersSorted.size(); i++) {
                bannersSorted.get(i).setDisplayOrder(i + 1);
                bannerRepository.save(bannersSorted.get(i));
            }
        }

        return BannerPublic.of(bannerToUpdate);
    }

    // delete banner check status
    public void deleteBanner(Integer id) {
        // find banner by id
        Banner bannerToDelete = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy banner với id: " + id));

        // banner status = true -> throw exception
        if (bannerToDelete.getStatus()) {
            throw new RuntimeException("Không thể xóa banner đang được hiển thị");
        }

        // delete banner
        bannerRepository.delete(bannerToDelete);
    }

    // delete banner dont check status and update displayOrder of banners status = true
    public void deleteBannerOther(Integer id) {
        // find banner by id
        Banner bannerToDelete = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy banner với id: " + id));

        // delete banner
        bannerRepository.delete(bannerToDelete);

        // check status of banner update
        if (bannerToDelete.getStatus()) {
            // update displayOrder of banners status = true
            List<Banner> banners = bannerRepository.findAllByStatus(true);

            // Sort banners by displayOrder ascending
            List<Banner> bannersSorted = banners.stream().sorted(Comparator.comparingInt(Banner::getDisplayOrder)).toList();

            // update displayOrder of banners status = true
            List<Banner> bannersToUpdate = bannersSorted.subList(bannerToDelete.getDisplayOrder() - 1, banners.size());

            for (Banner banner : bannersToUpdate) {
                banner.setDisplayOrder(banner.getDisplayOrder() - 1);
                bannerRepository.save(banner);
            }
        }
    }

    // sort banners follow displayOrder
    public void sortBanners(List<Integer> ids) {
        // Sort banners by displayOrder ascending
        for (int i = 0; i < ids.size(); i++) {
            Integer bannerId = ids.get(i);
            Banner banner = bannerRepository.findById(bannerId)
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy banner với id: " + bannerId));

            if (!banner.getStatus()) {
                throw new RuntimeException("Không thể sắp xếp banner đang bị vô hiệu hóa");
            }
            banner.setDisplayOrder(i + 1);
            bannerRepository.save(banner);
        }
    }

    public List<BannerPublic> sortBannersOther(List<SortBannerRequest> request) {
        // Sort banners by displayOrder ascending
        List<Banner> banners = bannerRepository.findByStatusTrueOrderByDisplayOrderAsc();

        // update displayOrder of banners
        for (SortBannerRequest sortBannerRequest : request) {
            Banner banner = banners.stream().filter(b -> Objects.equals(b.getId(), sortBannerRequest.getId())).findFirst()
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy banner với id: " + sortBannerRequest.getId()));
            banner.setDisplayOrder(sortBannerRequest.getDisplayOrder());
            bannerRepository.save(banner);
        }

        return banners.stream().map(BannerPublic::of).toList();
    }

    public List<BannerPublic> getAllBannersActive() {
        List<Banner> banners = bannerRepository.findAllByStatusOrderByDisplayOrderAsc(true);
        return banners.stream().map(BannerPublic::of).toList();
    }
}
