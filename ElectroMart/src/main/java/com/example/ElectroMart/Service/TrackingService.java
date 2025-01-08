package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Tracking;
import com.example.ElectroMart.Repository.TrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;

    public Tracking addTracking(Tracking tracking) {
        return trackingRepository.save(tracking);
    }

    public Tracking getTrackingByOrderId(String orderId) {
        return trackingRepository.findByOrderId(orderId);
    }

    public void updateTracking(String id, String status, String currentLocation) {
        Tracking tracking = trackingRepository.findById(id).orElse(null);
        if (tracking != null) {
            tracking.setStatus(status);
            tracking.setCurrentLocation(currentLocation);
            trackingRepository.save(tracking);
        }
    }
}
