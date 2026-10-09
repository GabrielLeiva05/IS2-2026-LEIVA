package com.kindsonthegenius.spring_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kindsonthegenius.spring_api.entity.Product;
import com.kindsonthegenius.spring_api.service.ProductService;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") Long id){
        return productService.getProduct(id);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@RequestBody() Product product, @PathVariable("id") Long id){
        product.setId(id);
        return productService.updateProduct(product);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addNew(@RequestBody() Product product){
        Product newProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
    }

}
