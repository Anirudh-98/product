package com.image.product.service;

import com.image.product.model.Product;
import com.image.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProduct()
    {
        return productRepository.findAll();
    }
    public Optional<Product> getUserById(String id) {
        return productRepository.findById(id);
    }
    public void deleteProductById(String id)
    {
        productRepository.deleteById(id);

    }

    public Product updateProduct(String  id, Product product){
        product.setId(id);
        return productRepository.save(product);
    }


    public Product saveProductToDB(MultipartFile file, String brand, String model, String performance,
                                   String location, Integer price, String description) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Product product = new Product(fileName, file.getContentType(), file.getBytes());
        product.setBrand(brand);
        product.setModel(model);
        product.setPerformance(performance);
        product.setLocation(location);
        product.setPrice(price);
        product.setDescription(description);
        return productRepository.save(product);

    }

}
