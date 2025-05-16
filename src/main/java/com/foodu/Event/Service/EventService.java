package com.foodu.Event.Service;

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

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final TruckApplicationRepository truckApplicationRepository;
    private final TruckMenuRepository truckMenuRepository;

//    public EventService(EventRepository eventRepository)
//    {
//        this.eventRepository = eventRepository;
//    }

    // 진행중 이벤트
    public List<OngoingEventResponse> getOngoingEvents() {
        return eventRepository.findOngoingEvents(LocalDateTime.now())  // 여기 수정
                .stream()
                .map(event -> new OngoingEventResponse(
                        event.getEventId(),
                        event.getEventName(),
                        event.getVoteEnd()
                ))
                .sorted(Comparator.comparing(OngoingEventResponse::getVoteEnd)) // 오름차순 정렬
                .collect(Collectors.toList());
    }

    // 마감된 이벤트
    public List<ClosedEventResponse> getClosedEvents() {
        return eventRepository.findClosedEvents(LocalDateTime.now())
                .stream()
                .map(event -> new ClosedEventResponse(
                        event.getEventId(),
                        event.getEventName(),
                        event.getEventEnd()
                ))
                .sorted(Comparator.comparing(ClosedEventResponse::getEventEnd).reversed()) // 내림차순 정렬
                .collect(Collectors.toList());
    }


    public EventResponse getEventDetail(Integer eventId) {
        // 1. 행사 정보 조회
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 이벤트가 존재하지 않습니다."));

        // 2. 행사에 참여 신청한 트럭 신청서 목록 조회
        List<TruckApplication> applications = truckApplicationRepository.findByEvent_EventId(eventId);

        // 3. 신청서를 통해 트럭과 메뉴 정보 구성
        List<EventResponse.TruckWithMenu> truckDtos = applications.stream()
                .map(app -> {
                    Truck truck = app.getTruck();

                    if (truck == null) {
                        throw new IllegalStateException("트럭 신청서에 연결된 트럭 정보가 없습니다.");
                    }

                    // 해당 트럭의 메뉴 목록 조회
                    List<TruckMenu> menus = truckMenuRepository.findByTruck_TruckId(truck.getTruckId());

                    return EventResponse.TruckWithMenu.builder()
                            .truckId(truck.getTruckId())
                            .truckName(truck.getName())
                            .description(truck.getDescription()) // 🟡 트럭 설명 포함
                            .menus(menus.stream().map(menu ->
                                    EventResponse.TruckWithMenu.Menu.builder()
                                            .menuName(menu.getMenuName())
                                            .menuPrice(menu.getMenuPrice())
                                            .menuImage(menu.getMenuImage())
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
                .build();
    }

}
