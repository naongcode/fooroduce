package com.foodu.Event.Service;

import com.foodu.Event.Dto.ApplicationStatusUpdateRequest;
import com.foodu.entity.TruckApplication;
import com.foodu.entity.User;
import com.foodu.repository.TruckApplicationRepository;
import com.foodu.util.SendGridUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final TruckApplicationRepository truckApplicationRepository;
    private final SendGridUtil sendGridUtil;

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

            //이메일 전송
            User owner = app.getTruck().getOwner();
            String email = owner.getEmail();
            String truckName = app.getTruck().getName(); // 트럭 이름 가져오기
            String eventName = app.getEvent().getEventName(); // 이벤트 이름 가져오기
            String templateId = (status == TruckApplication.Status.ACCEPTED)
                    ? sendGridUtil.getAcceptedTemplateId()
                    : sendGridUtil.getRejectedTemplateId();

            try {
                sendGridUtil.sendDynamicTemplateEmail(email, truckName, eventName, templateId);
            } catch (IOException e) {
                log.error("SendGrid 이메일 전송 실패: {}", e.getMessage());
            }
        }
    }
}
