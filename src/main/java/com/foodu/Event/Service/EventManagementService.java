package com.foodu.Event.Service;

import com.foodu.Event.Dto.EventCreateRequest;
import com.foodu.Event.Dto.EventCreateResponse;
import com.foodu.Event.Dto.EventUpdateRequest;
import com.foodu.Event.Dto.EventUpdateResponse;
import com.foodu.Map.Dto.GeocodeResponse;
import com.foodu.Map.Service.MapService;
import com.foodu.entity.Event;
import com.foodu.entity.User;
import com.foodu.repository.EventRepository;
import com.foodu.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class EventManagementService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final MapService mapService;

    LocalDateTime now = LocalDateTime.now();
    
    //행사 등록 로직
    public EventCreateResponse createEvent(EventCreateRequest req, String userId) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        GeocodeResponse geo = mapService.geocodeAddress(req.getLocation());

        Event tempEvent = Event.builder()
                .eventName(req.getEventName())
                .eventHost(req.getEventHost())
                .description(req.getDescription())
                .recruitStart(req.getRecruitStart())
                .recruitEnd(req.getRecruitEnd())
                .voteStart(req.getVoteStart())
                .voteEnd(req.getVoteEnd())
                .eventStart(req.getEventStart())
                .eventEnd(req.getEventEnd())
                .location(req.getLocation())
                .latitude(geo.getLatitude())
                .longitude(geo.getLongitude())
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .truckCount(0)
                .build();

        Event savedEvent = eventRepository.save(tempEvent);

        //동일한 이미지의 다른 버전들일때의 이름 변경 로직
        String originalFilename = req.getEventImage();


        // DB에 저장
        savedEvent.setEventImage(originalFilename);
        eventRepository.save(savedEvent);

        // 응답 반환
        return EventCreateResponse.builder()
                .eventName(savedEvent.getEventName())
                .eventHost(savedEvent.getEventHost())
                .eventImage(savedEvent.getEventImage())
                .description(savedEvent.getDescription())
                .location(savedEvent.getLocation())
                .recruitStart(savedEvent.getRecruitStart())
                .recruitEnd(savedEvent.getRecruitEnd())
                .voteStart(savedEvent.getVoteStart())
                .voteEnd(savedEvent.getVoteEnd())
                .eventStart(savedEvent.getEventStart())
                .eventEnd(savedEvent.getEventEnd())
                .truckCount(savedEvent.getTruckCount())
                .build();
    }
    
    //행사 수정 로직
    public EventUpdateResponse updateEvent(EventUpdateRequest req, String userId) {
        Event event = eventRepository.findById(req.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 행사가 존재하지 않습니다."));

        if (!event.getCreatedBy().equals(userId)) {
            throw new SecurityException("자신이 등록한 행사만 수정할 수 있습니다.");
        }

        if (req.getEventName() != null) event.setEventName(req.getEventName());
        if (req.getEventHost() != null) event.setEventHost(req.getEventHost());
        if (req.getEventImage() != null) event.setEventImage(req.getEventImage());
        if (req.getDescription() != null) event.setDescription(req.getDescription());
        if (req.getLocation() != null) {
            event.setLocation(req.getLocation());
            GeocodeResponse geo = mapService.geocodeAddress(req.getLocation());
            event.setLatitude(geo.getLatitude());
            event.setLongitude(geo.getLongitude());
        }
        if (req.getRecruitStart() != null) event.setRecruitStart(req.getRecruitStart());
        if (req.getRecruitEnd() != null) event.setRecruitEnd(req.getRecruitEnd());
        if (req.getVoteStart() != null) event.setVoteStart(req.getVoteStart());
        if (req.getVoteEnd() != null) event.setVoteEnd(req.getVoteEnd());
        if (req.getEventStart() != null) event.setEventStart(req.getEventStart());
        if (req.getEventEnd() != null) event.setEventEnd(req.getEventEnd());
        if (req.getTruckCount() != null) event.setTruckCount(req.getTruckCount());

        eventRepository.save(event);

        return EventUpdateResponse.builder()
                .eventId(event.getEventId())
                .eventName(event.getEventName())
                .eventHost(event.getEventHost())
                .eventImage(event.getEventImage())
                .description(event.getDescription())
                .location(event.getLocation())
                .recruitStart(event.getRecruitStart())
                .recruitEnd(event.getRecruitEnd())
                .voteStart(event.getVoteStart())
                .voteEnd(event.getVoteEnd())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .truckCount(event.getTruckCount())
                .build();
    }

    //행사 취소 로직
    public void cancelEvent(Integer eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 행사가 존재하지 않습니다."));

        if (!event.getCreatedBy().equals(userId)) {
            throw new SecurityException("자신이 등록한 행사만 취소할 수 있습니다.");
        }

        if (event.isCanceled()) {
            throw new IllegalArgumentException("이미 취소된 행사입니다.");
        }

        event.setCanceled(true);
        eventRepository.save(event);
    }

}