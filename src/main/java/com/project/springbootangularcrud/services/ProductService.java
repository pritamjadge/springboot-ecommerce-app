package com.project.springbootangularcrud.services;

import com.project.springbootangularcrud.dto.PaginationPageResponse;
import com.project.springbootangularcrud.dto.ProductDTO;
import com.project.springbootangularcrud.dto.ProductImagesDTO;
import com.project.springbootangularcrud.models.Product;
import com.project.springbootangularcrud.models.ProductImages;
import com.project.springbootangularcrud.repository.ProductImageRepo;
import com.project.springbootangularcrud.repository.ProductRepo;
import com.project.springbootangularcrud.utility.ImageUploadUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductImageRepo productImageRepo;
    private final ProductRepo productRepo;

    @Value("${product.images_path}")
    private String imagePath;

    public ProductService(ProductImageRepo productImageRepo, ProductRepo productRepo) {
        this.productImageRepo = productImageRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public void addProduct(Product product, MultipartFile imageFile) {
        Product _product = productRepo.save(product);
        Path filePath = ImageUploadUtility.productImageUpload(imageFile, imagePath);
        ProductImages productImages = ProductImages.builder().imageUrl(filePath.toString().substring(47)).imageName(imageFile.getOriginalFilename()).product(_product).build();
        productImageRepo.save(productImages);
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = productRepo.getAllProducts();

        for (ProductDTO product : products) {
            List<ProductImagesDTO> productImages = productImageRepo.findAllByProductId(product.getProductId());
            product.setProductImages(productImages);
        }
        return products;
    }

    public PaginationPageResponse<ProductDTO> findAllProducts(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Product> products = productRepo.findAll(paging);

        return getProductDTOPaginationPageResponse(products);
    }

    public PaginationPageResponse<ProductDTO> findProductsByName(String productName, int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Product> products = productRepo.findByProductNameContaining(productName, paging);

        return getProductDTOPaginationPageResponse(products);
    }

    public PaginationPageResponse<ProductDTO> getProductDTOPaginationPageResponse(Page<Product> products) {
        List<ProductDTO> productDTOList = products.stream().map(prod -> {
            List<ProductImages> productImagesList = prod.getProductImages();
            List<ProductImagesDTO> productImagesDTOList = productImagesList.stream().map(prodImages -> new ProductImagesDTO(prodImages.getId(), prodImages.getImageUrl())).collect(Collectors.toList());
            return new ProductDTO(prod.getProductId(), prod.getProductName(), prod.getProductDescription(), prod.getProductQty(), prod.getProductPrice(), productImagesDTOList);
        }).collect(Collectors.toList());

        return new PaginationPageResponse<>(productDTOList, products.getNumber(), products.getTotalElements(), products.getTotalPages());
    }

    public ProductDTO getProductDetail(Long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<ProductImagesDTO> productImagesDTOList = product.getProductImages().stream()
                    .map(productImage -> new ProductImagesDTO(productImage.getId(), productImage.getImageUrl())).toList();

            return ProductDTO.builder()
                    .productId(product.getProductId())
                    .productPrice(product.getProductPrice())
                    .productQty(product.getProductQty())
                    .productDescription(product.getProductDescription())
                    .productName(product.getProductName())
                    .productImages(productImagesDTOList)
                    .build();
        } else {
            return null;
        }
    }
}
