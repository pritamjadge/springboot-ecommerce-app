package com.project.ecommerce.controllers;

import com.project.ecommerce.dto.PaginationPageResponse;
import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.models.Product;
import com.project.ecommerce.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/add_product", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> addProduct(@RequestPart("productImage") MultipartFile imageFile, @RequestPart(value = "product") Product product) {

        try {
            if (imageFile != null && !imageFile.isEmpty() && product != null) {
                productService.addProduct(product, imageFile);
                return ResponseEntity.ok("Product added successfully.");
            } else if (product == null) {
                return ResponseEntity.badRequest().body("Product is missing.");
            } else {
                return ResponseEntity.badRequest().body("Image File is missing.");
            }
        } catch (Exception e) {
            // Handle the exception gracefully, log it, and return an error response.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding product: " + e.getMessage());
        }
    }

    @GetMapping(value = "/get_products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        logger.info("getAllProducts");
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping(value = "/find_products")
    public ResponseEntity<?> findAllProducts(@RequestParam(value = "page", defaultValue = "0", required = false) int pageNo, @RequestParam(value = "size", defaultValue = "2", required = false) int pageSize) {

        PaginationPageResponse<ProductDTO> products = productService.findAllProducts(pageNo, pageSize);
       /* if (products.getContent().isEmpty()) {
            return new ResponseEntity<>("No products exist.", HttpStatus.NOT_FOUND);
        } else {*/
        return new ResponseEntity<>(products, HttpStatus.OK);
        //}
    }

    @GetMapping(value = "/find_products_by_name")
    public ResponseEntity<?> findProductsByNameOrCategory(@RequestParam(value = "productName", required = false) String productName,
                                                          @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                          @RequestParam(value = "page", defaultValue = "0", required = false) Integer pageNo,
                                                          @RequestParam(value = "size", defaultValue = "2", required = false) Integer pageSize) {

        System.out.println("productName {} :" + productName);
        System.out.println("categoryId {} :" + categoryId);
        PaginationPageResponse<ProductDTO> products = productService.findProductsByNameOrCategory(productName, categoryId, pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/product_details/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable("id") Long productId) {

        ProductDTO productDetail = productService.getProductDetail(productId);

        if (productDetail != null) {
            return ResponseEntity.ok(productDetail);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
        }
    }
}
