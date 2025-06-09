package com.foodu.Event.Service;

import com.foodu.Event.Dto.ApplicationStatusUpdateRequest;
import com.foodu.entity.TruckApplication;
import com.foodu.repository.TruckApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final TruckApplicationRepository truckApplicationRepository;

    @Transactional
    public void updateApplicationStatuses(Integer eventId, List<ApplicationStatusUpdateRequest> requests) {
        for (ApplicationStatusUpdateRequest req : requests) {
            Optional<TruckApplication> applicationOpt =
                    truckApplicationRepository.findByApplicationIdAndEvent_EventId(req.getApplicationId(), eventId);

            if (applicationOpt.isEmpty()) {
                throw new IllegalArgumentException("Application not found: ID = " + req.getApplicationId());
            }

            TruckApplication app = applicationOpt.get();

            TruckApplication.Status status = req.getStatus();
            if (status != TruckApplication.Status.ACCEPTED && status != TruckApplication.Status.REJECTED) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }

            app.setStatus(status);
            truckApplicationRepository.save(app);
        }
    }


}
