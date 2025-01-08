package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.Shipping;
import com.example.ElectroMart.Service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping
    public Shipping addShipping(@RequestBody Shipping shipping) {
        return shippingService.addShipping(shipping);
    }

    @GetMapping("/user/{userId}")
    public List<Shipping> getShippingByUserId(@PathVariable String userId) {
        return shippingService.getShippingByUserId(userId);
    }

    @PutMapping("/{id}")
    public void updateShippingStatus(@PathVariable String id, @RequestParam String status) {
        shippingService.updateShippingStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void deleteShipping(@PathVariable String id) {
        shippingService.deleteShipping(id);
    }
}
