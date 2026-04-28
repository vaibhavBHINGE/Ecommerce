package org.example.ecommerce.service;

import org.example.ecommerce.Model.Product;
import org.example.ecommerce.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public List<Product> getProduct(){
        return productRepo.findAll();
    }

    public Product getProductById(int id){
        return productRepo.findById(id).orElse(null);
    }

    public  Product saveProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getContentType());
        product.setImageType(image.getOriginalFilename());
        product.setImageData(image.getBytes());

        return productRepo.save(product);
    }

    public Product updateOrAddProduct(int id,Product product,MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());
        return productRepo.save(product);
    }
    public void deleteProductById(int id){
        productRepo.deleteById(id);
    }
}
