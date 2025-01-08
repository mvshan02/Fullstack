package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Seller;
import com.example.ElectroMart.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public Seller addSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(String id) {
        return sellerRepository.findById(id).orElse(null);
    }

    public Seller getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email);
    }

    public Seller updateSeller(String id, Seller updatedSeller) {
        Seller existingSeller = sellerRepository.findById(id).orElse(null);
        if (existingSeller != null) {
            existingSeller.setName(updatedSeller.getName());
            existingSeller.setShopName(updatedSeller.getShopName());
            existingSeller.setAddress(updatedSeller.getAddress());
            return sellerRepository.save(existingSeller);
        }
        return null;
    }

    public void deleteSeller(String id) {
        sellerRepository.deleteById(id);
    }
}
