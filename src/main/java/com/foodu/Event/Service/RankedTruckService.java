package com.foodu.Event.Service;

import com.foodu.Event.Dto.RankedTruckResponse;
import com.foodu.entity.TruckApplication;
import com.foodu.entity.TruckMenu;
import com.foodu.repository.TruckApplicationRepository;
import com.foodu.repository.TruckMenuRepository;
import com.foodu.repository.VoteResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RankedTruckService {

    private final TruckApplicationRepository truckApplicationRepository;
    private final TruckMenuRepository truckMenuRepository;
    private final VoteResultRepository voteResultRepository;

    public List<RankedTruckResponse> getTop3RankedTrucks(Integer eventId) {
        List<TruckApplication> applications = truckApplicationRepository.findByEvent_EventId(eventId);

        // 1. voteCount 맵 구성 (truckId → voteCount)
        Map<Integer, Integer> voteCountMap = new HashMap<>();
        List<Object[]> voteCounts = voteResultRepository.findVoteCountsByEventId(eventId);
        for (Object[] row : voteCounts) {
            Integer truckId = (Integer) row[0];
            Long count = (Long) row[1]; // SUM()은 Long으로 나올 수 있음
            voteCountMap.put(truckId, count.intValue());
        }

        // 2. voteCount 내림차순 정렬 후 상위 3개
        return applications.stream()
                .sorted((a, b) -> {
                    int aVotes = voteCountMap.getOrDefault(a.getTruck().getTruckId(), 0);
                    int bVotes = voteCountMap.getOrDefault(b.getTruck().getTruckId(), 0);
                    return Integer.compare(bVotes, aVotes); // 내림차순
                })
                .limit(3)
                .map(app -> {
                    int voteCount = voteCountMap.getOrDefault(app.getTruck().getTruckId(), 0);
                    return toDto(app, voteCount);
                })
                .toList();
    }

    private RankedTruckResponse toDto(TruckApplication app, Integer voteCount) {
        var truck = app.getTruck();

        if (truck == null) {
            throw new IllegalStateException("트럭 신청서에 연결된 트럭 정보가 없습니다.");
        }

        List<TruckMenu> menus = truckMenuRepository.findByTruck_TruckId(truck.getTruckId());

        List<RankedTruckResponse.Menu> menuDtos = menus.stream()
                .map(menu -> RankedTruckResponse.Menu.builder()
                        .menuName(menu.getMenuName())
                        .menuPrice(menu.getMenuPrice())
                        .menuImage(menu.getMenuImage())
                        .menuType(menu.getMenuType())
                        .build())
                .toList();

        return RankedTruckResponse.builder()
                .truckId(truck.getTruckId())
                .truckName(truck.getName())
                .description(truck.getDescription())
                .phoneNumber(truck.getPhoneNumber())
                .status(app.getStatus().toString())
                .applicationId(app.getApplicationId())
                .menus(menuDtos)
                .voteCount(voteCount)
                .build();
    }
}
