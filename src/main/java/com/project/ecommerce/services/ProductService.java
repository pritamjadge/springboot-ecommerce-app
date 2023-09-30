package com.project.ecommerce.services;

import com.project.ecommerce.dto.PaginationPageResponse;
import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.models.Product;
import com.project.ecommerce.models.ProductImages;
import com.project.ecommerce.repository.ProductImageRepo;
import com.project.ecommerce.repository.ProductRepo;
import com.project.ecommerce.utility.ImageUploadUtility;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Value("${product.images_path}")
    private String imagePath;

    public ProductService(ProductImageRepo productImageRepo, ProductRepo productRepo, ModelMapper modelMapper) {
        this.productImageRepo = productImageRepo;
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
    }

    private ProductDTO convertProductToProductDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    @Transactional
    public void addProduct(Product product, MultipartFile imageFile) {
        Product _product = productRepo.save(product);
        Path filePath = ImageUploadUtility.productImageUpload(imageFile, imagePath);
        ProductImages productImages = ProductImages.builder().
                imageUrl(filePath.toString().substring(47)).
                imageName(imageFile.getOriginalFilename()).
                product(_product).
                build();
        productImageRepo.save(productImages);
    }

    //not in use - will remove in future
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepo.findAll();
        return products.stream().map(this::convertProductToProductDTO).collect(Collectors.toList());
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
        List<ProductDTO> productDTOList = products.stream().map(this::convertProductToProductDTO).collect(Collectors.toList());
        return new PaginationPageResponse<>(productDTOList, products.getNumber(), products.getTotalElements(), products.getTotalPages());
    }

    public ProductDTO getProductDetail(Long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        return productOptional.map(this::convertProductToProductDTO).orElse(null);
    }
}
