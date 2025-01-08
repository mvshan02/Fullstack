package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.Seller;
import com.example.ElectroMart.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/register")
    public Seller registerSeller(@RequestBody Seller seller) {
        return sellerService.addSeller(seller);
    }

    @GetMapping
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}")
    public Seller getSellerById(@PathVariable String id) {
        return sellerService.getSellerById(id);
    }

    @GetMapping("/email/{email}")
    public Seller getSellerByEmail(@PathVariable String email) {
        return sellerService.getSellerByEmail(email);
    }

    @PutMapping("/{id}")
    public Seller updateSeller(@PathVariable String id, @RequestBody Seller updatedSeller) {
        return sellerService.updateSeller(id, updatedSeller);
    }

    @DeleteMapping("/{id}")
    public void deleteSeller(@PathVariable String id) {
        sellerService.deleteSeller(id);
    }
}
