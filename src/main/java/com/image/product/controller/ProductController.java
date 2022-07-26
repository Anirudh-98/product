package com.image.product.controller;

import com.image.product.model.Product;
import com.image.product.service.ProductService;
import com.image.product.service.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
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
    public Optional<Product> getAdd(@PathVariable("id") String id){
        return productService.getUserById(id);
    }
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam String brand, @RequestParam String model,
                                                      @RequestParam String performance, @RequestParam String location, @RequestParam Integer price,
                                                      @RequestParam String description) {
        String message = "";
        try {
            productService.saveProductToDB(file,brand,model,performance,location,price,description);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable("id") String id,@RequestBody  Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable("id") String  id) {
        productService.deleteProductById(id);
    }
}



