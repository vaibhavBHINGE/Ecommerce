package org.example.ecommerce.Controller;

import org.example.ecommerce.Model.Product;
import org.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> product() {
        return new ResponseEntity<>(productService.getProduct(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> productById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(product, HttpStatus.NOT_FOUND);

        }
    }

    //Adding image
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile image) {
        try {
            Product saveProduct = productService.saveProduct(product, image);
            return new ResponseEntity<>(saveProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productID}/image")
    public ResponseEntity<byte[]> productImage(@PathVariable int productID) {
        Product product = productService.getProductById(productID);
        return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);

//        if (product.getId() > 0) {
//
//        } else {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile image) {
        try {
            Product upProduct=productService.updateOrAddProduct(id,product,image);
            return new ResponseEntity<>(upProduct, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }
    @DeleteMapping("/deleteProduct/{id}")
     public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        Product delProduct = productService.getProductById(id);
        // here we need to add logic for if id/product is not preset it should catch this
        if (delProduct != null)
        {
            productService.deleteProductById(id);
            return new ResponseEntity<>("Product deleted!", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Error while deleting ", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
