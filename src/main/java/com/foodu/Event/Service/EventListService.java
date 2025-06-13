package com.foodu.Event.Service;

import com.foodu.entity.Truck;
import com.foodu.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.foodu.entity.Event;
import com.foodu.repository.EventRepository;
import com.foodu.Event.Dto.EventResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventListService {

    private final EventRepository eventRepository;
    private final TruckRepository truckRepository; // 추가

    public List<EventResponse> getEventsByManager(String userId) {
        List<Event> events = eventRepository.findByCreatedBy(userId);
        return events.stream()
                .map(event -> {
                    // 트럭 목록 조회
                    List<Truck> trucks = truckRepository.findTrucksByEventId(event.getEventId());

                    // 트럭 + 메뉴 변환
                    List<EventResponse.TruckWithMenu> truckWithMenus = trucks.stream()
                            .map(truck -> EventResponse.TruckWithMenu.builder()
                                    .truckId(truck.getTruckId())
                                    .truckName(truck.getName())
                                    .description(truck.getDescription())
                                    .menus(
                                            truck.getTruckMenus().stream()
                                                    .map(menu -> EventResponse.TruckWithMenu.Menu.builder()
                                                            .menuName(menu.getMenuName())
                                                            .menuPrice(menu.getMenuPrice())
                                                            .menuImage(menu.getMenuImage())
                                                            .menuType(menu.getMenuType())
                                                            .build()
                                                    ).collect(Collectors.toList())
                                    )
                                    .build())
                            .collect(Collectors.toList());

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
                            .trucks(truckWithMenus)
                            .build();
                })
                .collect(Collectors.toList());
    }
}