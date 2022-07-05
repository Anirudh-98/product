package com.image.product.controller;

import com.image.product.model.Product;
import com.image.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductController {


    @Autowired
    private ProductService productService;
    private String createDate;

    @GetMapping("/")
    public String showAddProduct()
    {
        return "/addProduct.html";
    }

    @GetMapping("/productList.html")
     List<Product> products() {
        return productService.getAllProduct();
    }
    @PostMapping("/addProduct")
    public String saveProduct(@RequestParam("file") MultipartFile file,
                              @RequestParam("brand") String brand,
                              @RequestParam("model") String model,
                              @RequestParam("performance") String performance,
                              @RequestParam("location") String location,
                              @RequestParam("price") int price,
                              @RequestParam("description") String description
                              )
    {
        productService.saveProductToDB(file,brand,model,performance,location,price,description);
        return "redirect:/listProducts.html";
    }
}



