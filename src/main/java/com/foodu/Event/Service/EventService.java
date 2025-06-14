package com.foodu.Event.Service;

import com.foodu.Event.Dto.AllEventResponse;
import com.foodu.Event.Dto.ClosedEventResponse;
import com.foodu.Event.Dto.EventResponse;
import com.foodu.Event.Dto.OngoingEventResponse;
import com.foodu.entity.Event;
import com.foodu.entity.Truck;
import com.foodu.entity.TruckApplication;
import com.foodu.entity.TruckMenu;
import com.foodu.repository.EventRepository;
import com.foodu.repository.TruckApplicationRepository;
import com.foodu.repository.TruckMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final TruckApplicationRepository truckApplicationRepository;
    private final TruckMenuRepository truckMenuRepository;

    //전체 행사
    public List<AllEventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream()
                .map(event -> new AllEventResponse(
                        event.getEventId(),
                        event.getEventName(),
                        event.getEventHost(),
                        event.getEventImage(),
                        event.getEventStart(),
                        event.getEventEnd(),
                        event.getRecruitStart(),
                        event.getRecruitEnd(),
                        event.getVoteStart(),
                        event.getVoteEnd()
                ))
                .collect(Collectors.toList());
    }


    // 진행중 이벤트
    public List<OngoingEventResponse> getOngoingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("voteEnd").ascending());
        Page<Event> eventPage = eventRepository.findOngoingEvents(LocalDateTime.now(), pageable);

        return eventPage.getContent().stream()
                .map(event -> new OngoingEventResponse(
                        event.getEventId(),
                        event.getEventName(),
                        event.getEventHost(),
                        event.getEventImage(),
                        event.getEventStart(),
                        event.getEventEnd(),
                        event.getRecruitStart(),
                        event.getRecruitEnd(),
                        event.getVoteStart(),
                        event.getVoteEnd()
                ))
                .collect(Collectors.toList());
    }

    // 마감된 이벤트
    public List<ClosedEventResponse> getClosedEvents() {
        return eventRepository.findClosedEvents(LocalDateTime.now())
                .stream()
                .map(event -> new ClosedEventResponse(
                        event.getEventId(),
                        event.getEventName(),
                        event.getEventHost(),
                        event.getEventImage(),
                        event.getEventStart(),
                        event.getEventEnd(),
                        event.getRecruitStart(),
                        event.getRecruitEnd(),
                        event.getVoteStart(),
                        event.getVoteEnd()
                ))
                .sorted(Comparator.comparing(ClosedEventResponse::getEventEnd).reversed()) // 내림차순 정렬
                .collect(Collectors.toList());
    }

    public EventResponse getEventDetail(Integer eventId, int page, int size) {
        // 1. 행사 정보 조회
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 이벤트가 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());

        // 2. 행사에 참여 신청한 트럭 신청서 목록 조회
//        List<TruckApplication> applications = truckApplicationRepository.findByEvent_EventId(eventId);
        Page<TruckApplication> truckPage =
                truckApplicationRepository.findByEvent_EventId(eventId, pageable);

        // 3. 신청서를 통해 트럭과 메뉴 정보 구성
        List<EventResponse.TruckWithMenu> truckDtos = truckPage.stream()
                .map(app -> {Truck truck = app.getTruck();

                    if (truck == null) {
                        throw new IllegalStateException("트럭 신청서에 연결된 트럭 정보가 없습니다.");
                    }

                    // 해당 트럭의 메뉴 목록 조회
                    List<TruckMenu> menus = truckMenuRepository.findByTruck_TruckId(truck.getTruckId());

                    return EventResponse.TruckWithMenu.builder()
                            .truckId(truck.getTruckId())
                            .truckName(truck.getName())
                            .description(truck.getDescription()) // 🟡 트럭 설명 포함
                            .phoneNumber(truck.getPhoneNumber())
                            .status(app.getStatus().toString()) // ✅ 여기서 status 포함
                            .applicationId(app.getApplicationId()) // ✅ 추가
                            .menus(menus.stream().map(menu ->
                                    EventResponse.TruckWithMenu.Menu.builder()
                                            .menuName(menu.getMenuName())
                                            .menuPrice(menu.getMenuPrice())
                                            .menuImage(menu.getMenuImage())
                                            .menuType(menu.getMenuType())
                                            .build()
                            ).collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        // 4. 최종 EventResponseDto로 반환
        return EventResponse.builder()
                .eventId(event.getEventId())
                .eventName(event.getEventName())
                .eventHost(event.getEventHost())
                .eventImage(event.getEventImage())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .description(event.getDescription())
                .truckCount(event.getTruckCount())
                .recruitStart(event.getRecruitStart())
                .recruitEnd(event.getRecruitEnd())
                .voteStart(event.getVoteStart())
                .voteEnd(event.getVoteEnd())
                .location(event.getLocation())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .createdBy(event.getCreatedBy())
                .createdAt(event.getCreatedAt())
                .trucks(truckDtos)
                .currentPage(truckPage.getNumber())
                .totalPages(truckPage.getTotalPages())
                .totalElements(truckPage.getTotalElements())
                .build();
    }

}
