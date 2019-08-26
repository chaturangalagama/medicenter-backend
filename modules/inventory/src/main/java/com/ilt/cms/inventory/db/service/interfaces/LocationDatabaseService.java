package com.ilt.cms.inventory.db.service.interfaces;

import com.ilt.cms.inventory.model.inventory.Location;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface LocationDatabaseService {

    Location saveLocation(Location location);

    Optional<Location> findLocationById(String locationId);

    Optional<Location> findLocationByClinicId(String clinicId);

    //List<Location> findLocationByClinicIdAndItemIds(int page, int size, String clinicId, List<String> itemIds);

    //List<Location> findLocationByLocationIdAndInventory(String locationId, String itemRefId);

}
