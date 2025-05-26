package com.foodu.Event.Service;

import com.foodu.Event.Dto.EventCreateRequest;
import com.foodu.Event.Dto.EventCreateResponse;
import com.foodu.Map.Dto.GeocodeResponse;
import com.foodu.Map.Service.MapService;
import com.foodu.entity.Event;
import com.foodu.entity.User;
import com.foodu.repository.EventRepository;
import com.foodu.repository.UserRepository;
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
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일명이 없습니다.");
        }

        // 확장자 분리
        String ext = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            ext = originalFilename.substring(dotIndex);
            originalFilename = originalFilename.substring(0, dotIndex);
        }

        // 같은 이름 존재 개수 세기 -> 버전 번호
        int version = eventRepository.countByEventImagePrefix(savedEvent.getEventId() + "_" + originalFilename + "_v");
        version++;

        String newFileName = savedEvent.getEventId() + "_" + originalFilename + "_v" + version + ext;

        // S3 URL 재조합 (고정된 URL prefix + newFileName)
        String s3Url = "https://naong2-s3.s3.amazonaws.com/image/" + newFileName;

        // DB에 저장
        savedEvent.setEventImage(s3Url);
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
}