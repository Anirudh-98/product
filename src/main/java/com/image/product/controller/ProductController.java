package com.image.product.controller;

import com.image.product.model.Product;
import com.image.product.repository.ProductRepository;
import com.image.product.service.ProductService;
import com.image.product.service.ResponseMessage;
import com.image.product.util.ImageUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductController {


    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/productList.html")
    List<Product> products() {
        return productService.getAllProduct();
    }


//    public String showContent(@PathVariable(value = "id") String id, Model model) throws
//            UnsupportedEncodingException {
//
//        if (!productRepository.existsById(id)) {
//            return "redirect:/post_not_exist";
//        }
//        Optional<Product> post =productRepository.findById(id);
//        ArrayList<Product> content = new ArrayList<>();
//        post.ifPresent(content::add);
//        model.addAttribute("post", content);
//
//
//        byte[] encodeBase64 = Base64.getEncoder().encode(post.get().getPicByte());
//        String base64Encoded = new String(encodeBase64, "UTF-8");
//        model.addAttribute("contentImage", base64Encoded );
//        return "/productList.html";
//    }
//      @GetMapping("/list/{id}")
//    public Optional<Product> getAdd(@PathVariable("id") String id){
//        return productService.getUserById(id);
//    }
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

    @GetMapping("/image/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") String id, HttpServletResponse response, Optional<Product> image)
            throws ServletException, IOException {
        log.info("Id :: " + id);
        image = productService.getUserById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(image.get().getPicByte());
        response.getOutputStream().close();
    }

    @GetMapping(path = {"/get/image/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {

        final Optional<Product> dbImage = productRepository.findByName(name);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(ImageUtility.decompressImage(dbImage.get().getPicByte()));
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

    @PostMapping("/image/saveImageDetails")
    public @ResponseBody ResponseEntity<?> createProduct(@RequestParam("name") String name,
                                                         @RequestParam("price") double price, @RequestParam("description") String description, Model model, HttpServletRequest request
            ,final @RequestParam("image") MultipartFile file) {
        try {
            //String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            log.info("uploadDirectory:: " + uploadDirectory);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            log.info("FileName: " + file.getOriginalFilename());
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            String[] names = name.split(",");
            String[] descriptions = description.split(",");
            java.util.Date createDate = new Date();
            log.info("Name: " + names[0]+" "+filePath);
            log.info("description: " + descriptions[0]);
            log.info("price: " + price);
            try {
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    log.info("Folder Created");
                    dir.mkdirs();
                }
                // Save the file locally
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception e) {
                log.info("in catch");
                e.printStackTrace();
            }
            byte[] imageData = file.getBytes();
            ImageGallery imageGallery = new ImageGallery();
            imageGallery.setName(names[0]);
            imageGallery.setImage(imageData);
            imageGallery.setPrice(price);
            imageGallery.setDescription(descriptions[0]);
            imageGallery.setCreateDate(createDate);
            imageGalleryService.saveImage(imageGallery);
            log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


