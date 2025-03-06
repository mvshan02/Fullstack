package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Shipping;
import com.example.ElectroMart.Repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    public Shipping addShipping(Shipping shipping) {
        return shippingRepository.save(shipping);
    }

    public List<Shipping> getShippingByUserId(String userId) {
        return shippingRepository.findByUserId(userId);
    }

    public void updateShippingStatus(String id, String status) {
        Shipping shipping = shippingRepository.findById(id).orElse(null);
        if (shipping != null) {
            shipping.setStatus(status);
            shippingRepository.save(shipping);
        }
    }

    public void deleteShipping(String id) {
        shippingRepository.deleteById(id);
    }
}
