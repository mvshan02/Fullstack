package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.Tracking;
import com.example.ElectroMart.Service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @PostMapping
    public Tracking addTracking(@RequestBody Tracking tracking) {
        return trackingService.addTracking(tracking);
    }

    @GetMapping("/order/{orderId}")
    public Tracking getTrackingByOrderId(@PathVariable String orderId) {
        return trackingService.getTrackingByOrderId(orderId);
    }

    @PutMapping("/{id}")
    public void updateTracking(@PathVariable String id, @RequestParam String status, @RequestParam String location) {
        trackingService.updateTracking(id, status, location);
    }
}
