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
                    truckApplicationRepository.findByApplicationIdAndEvent_EventId(req.getApplication_id(), eventId);

            if (applicationOpt.isEmpty()) {
                throw new IllegalArgumentException("Application not found: ID = " + req.getApplication_id());
            }

            TruckApplication app = applicationOpt.get();
            if (!req.getStatus().equals("ACCEPTED") && !req.getStatus().equals("REJECTED")) {
                throw new IllegalArgumentException("Invalid status: " + req.getStatus());
            }

            app.setStatus(TruckApplication.Status.valueOf(req.getStatus()));
            truckApplicationRepository.save(app);
        }
    }


}
