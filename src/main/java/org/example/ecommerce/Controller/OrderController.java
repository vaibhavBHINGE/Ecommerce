package org.example.ecommerce.Controller;

import org.example.ecommerce.Model.DTO.OrderRequest;
import org.example.ecommerce.Model.DTO.OrderResponse;
import org.example.ecommerce.Model.Product;
import org.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/placeOrder")
    public ResponseEntity<OrderResponse> orderProduct(@RequestBody OrderRequest orderRequest){
        OrderResponse response=orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order/fetchOrder")
    public ResponseEntity<List<OrderResponse>> fetchOrder(){
        List<OrderResponse> fetchResponse=orderService.getAllOrderResponse();
        return new ResponseEntity<>(fetchResponse,HttpStatus.OK);
    }
}
