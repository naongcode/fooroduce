package com.foodu.Event.Service;

import com.foodu.Event.Dto.ClosedEventResponse;
import com.foodu.Event.Dto.OngoingEventResponse;
import com.foodu.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {


    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository)
    {
        this.eventRepository = eventRepository;
    }

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
}
