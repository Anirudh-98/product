package com.image.product.controller;

import com.image.product.model.Product;
import com.image.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductController {


    @Autowired
    private ProductService productService;



    @GetMapping("/productList.html")
    List<Product> products() {
        return productService.getAllProduct();
    }

    @GetMapping("/list/{id}")
    public Optional<Product> getAdd(@PathVariable("id") Long id){
        return productService.getUserById(id);
    }
    @PostMapping("/addProduct.html")
    public String saveProduct(@RequestParam("file") MultipartFile file,
                              @RequestParam("brand") String brand,
                              @RequestParam("model") String model,
                              @RequestParam("performance") String performance,
                              @RequestParam("location") String location,
                              @RequestParam("price") int price,
                              @RequestParam("description") String description,
                              @RequestParam("createDate") Date createDate
                              )
    {
        productService.saveProductToDB(file,brand,model,performance,location,price,description,createDate);
        return "/productList.html";
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable("id") Long id,@RequestBody  Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable("id") Long  id) {
        productService.deleteProductById(id);
    }
}



