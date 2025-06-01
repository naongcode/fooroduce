package com.foodu.Truck.Service;

import com.foodu.Truck.Dto.TruckResponse;
import com.foodu.entity.TruckApplication;
import com.foodu.repository.TruckApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TruckService {

    private final TruckApplicationRepository truckApplicationRepository;

    public List<TruckResponse> getApplicationsByOwner(String ownerId) {
        List<TruckApplication> applications = truckApplicationRepository.findByTruck_Owner_UserId(ownerId);

        return applications.stream()
                .map(TruckResponse::from)
                .collect(Collectors.toList());
    }
}