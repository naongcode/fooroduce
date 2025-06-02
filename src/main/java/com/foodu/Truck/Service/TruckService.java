package com.foodu.Truck.Service;

import com.foodu.Truck.Dto.TruckApplicationRequest;
import com.foodu.Truck.Dto.TruckApplicationResponse;
import com.foodu.Truck.Dto.TruckResponse;
import com.foodu.entity.Event;
import com.foodu.entity.Truck;
import com.foodu.entity.TruckApplication;
import com.foodu.repository.EventRepository;
import com.foodu.repository.TruckApplicationRepository;
import com.foodu.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TruckService {

    private final TruckApplicationRepository truckApplicationRepository;
    private final TruckRepository truckRepository;
    private final EventRepository eventRepository;


    public List<TruckResponse> getApplicationsByOwner(String ownerId) {
        List<TruckApplication> applications = truckApplicationRepository.findByTruck_Owner_UserId(ownerId);

        return applications.stream()
                .map(TruckResponse::from)
                .collect(Collectors.toList());
    }




    public TruckApplicationResponse applyToEvent(Integer eventId, Integer truckId) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (truckApplicationRepository.existsByTruck_TruckIdAndEvent_EventId(truck.getTruckId(), event.getEventId())) {
            throw new RuntimeException("이미 신청한 트럭입니다.");
        }

        TruckApplication application = new TruckApplication();
        application.setTruck(truck);
        application.setEvent(event);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(TruckApplication.Status.PENDING);

        TruckApplication saved = truckApplicationRepository.save(application);

        return new TruckApplicationResponse(
                saved.getApplicationId(),
                saved.getTruck().getTruckId(),
                saved.getEvent().getEventId(),
                saved.getStatus().name()
        );
    }
}