package com.ilt.cms.inventory.db.service;

import com.ilt.cms.inventory.db.repository.spring.inventory.LocationRepository;
import com.ilt.cms.inventory.db.service.interfaces.LocationDatabaseService;
import com.ilt.cms.inventory.model.inventory.Location;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoLocationDatabaseService implements LocationDatabaseService {

    private LocationRepository locationRepository;

    public MongoLocationDatabaseService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }
    @Override
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Optional<Location> findLocationById(String locationId) {
        return locationRepository.findById(locationId);
    }

    @Override
    public Optional<Location> findLocationByClinicId(String clinicId) {
        return locationRepository.findByClinicId(clinicId);
    }

}
