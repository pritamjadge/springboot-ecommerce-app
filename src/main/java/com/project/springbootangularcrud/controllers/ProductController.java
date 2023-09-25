package com.project.springbootangularcrud.controllers;

import com.project.springbootangularcrud.dto.ProductDTO;
import com.project.springbootangularcrud.models.Product;
import com.project.springbootangularcrud.services.ProductService;
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
                return ResponseEntity.badRequest().body("product is missing.");
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
    public ResponseEntity<List<Product>> findAllProducts() {
        logger.info("findAllProducts");
        List<Product> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
