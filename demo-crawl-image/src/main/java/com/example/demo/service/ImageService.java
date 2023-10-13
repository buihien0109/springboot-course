package com.example.demo.service;

import com.example.demo.entity.Image;
import com.example.demo.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Image getImageById(Integer id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found file"));
    }

    public Image saveImage(byte[] data) {
        Image image = new Image();
        image.setData(data);
        image.setType("image/png");
        imageRepository.save(image);

        return image;
    }
}
