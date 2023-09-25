package com.project.springbootangularcrud.services;

import com.project.springbootangularcrud.dto.ProductDTO;
import com.project.springbootangularcrud.dto.ProductImagesDTO;
import com.project.springbootangularcrud.models.Product;
import com.project.springbootangularcrud.models.ProductImages;
import com.project.springbootangularcrud.repository.ProductImageRepo;
import com.project.springbootangularcrud.repository.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public void addProduct(Product product, MultipartFile imageFile) throws Exception {
        Product _product = productRepo.save(product);
        logger.info("Product saved: {}", _product);

        String originalFilename = imageFile.getOriginalFilename();
        Path filePath = Paths.get(imagePath, originalFilename).toAbsolutePath().normalize();
        logger.info("Image file path: {}", filePath);

        imageFile.transferTo(filePath.toFile());

        ProductImages productImages = ProductImages.builder().imageUrl(filePath.toString()).imageName(originalFilename).product(_product).build();

        productImageRepo.save(productImages);
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = productRepo.getAllProducts();

        for (ProductDTO product : products) {
            List<ProductImagesDTO> productImages = productImageRepo.findAllByProductId(product.getProductId());
            product.setProductImages(productImages);
        }
        logger.debug("getAllProducts {} :" + products.toString());
        return products;
    }

    public List<Product> findAllProducts() {
        List<Product> productList = productRepo.findAll();

        // Group products by productId
        Map<Long, List<Product>> productGroups = productList.stream()
                .collect(Collectors.groupingBy(Product::getProductId));

        productGroups.forEach((aLong, products) -> System.out.println(aLong + "__" + products.toString()));

        // Transform grouped products into the desired format
        List<Product> result = productGroups.values().stream()
                .map(products -> {
                    // Merge productImages lists
                    List<ProductImages> productImages = products.stream()
                            .flatMap(product -> product.getProductImages().stream())
                            .collect(Collectors.toList());

                    // Pick any product from the group to get common fields
                    Product sampleProduct = products.get(0);

                    // Create a new product with merged productImages
                    return new Product(
                            sampleProduct.getProductId(),
                            sampleProduct.getProductName(),
                            sampleProduct.getProductDescription(),
                            sampleProduct.getProductQty(),
                            sampleProduct.getProductPrice(),
                            productImages,
                            sampleProduct.getCreatedAt(),
                            sampleProduct.getCreatedBy()
                    );
                })
                .collect(Collectors.toList());

        // Output the result
        result.forEach(System.out::println);
        return result;
    }
}
