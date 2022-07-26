package com.image.product.service;

import com.image.product.model.Product;
import com.image.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
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
    public Optional<Product> getUserById(Long id) {
        return productRepository.findById(id);
    }
    public void deleteProductById(Long id)
    {
        productRepository.deleteById(id);

    }

    public Product updateProduct(Long  id, Product product){
        product.setId(id);
        return productRepository.save(product);
    }


    public String saveProductToDB(MultipartFile file, String brand, String model, String performance,
                                  String location, int price, String description, Date createDate) {

        Product p = new Product();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(fileName.contains(".."))
        {
            System.out.println("not a a valid file");
        }
        try {
            p.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        p.setDescription(description);
        p.setBrand(brand);
        p.setLocation(location);
        p.setModel(model);
        p.setPrice(price);
        p.setPerformance(performance);
        p.setCreateDate(createDate);
        productRepository.save(p);
        return fileName;

    }
}
