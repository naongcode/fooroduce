package com.foodu.Vote;

import com.foodu.Vote.Dto.VoteRequest;
import com.foodu.Vote.Dto.VoteResponse;
import com.foodu.Vote.Dto.VoteStatusResponse;
import com.foodu.entity.*;
import com.foodu.repository.*;
import com.foodu.util.ExtractInfoFromToken;
import com.foodu.util.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteResultRepository voteResultRepository;
    private final UserRepository userRepository;
    private final TruckRepository truckRepository;
    private final EventRepository eventRepository;

    public VoteService(VoteRepository voteRepository,
                       VoteResultRepository voteResultRepository,
                       UserRepository userRepository,
                       TruckRepository truckRepository,
                       EventRepository eventRepository) {
        this.voteRepository = voteRepository;
        this.voteResultRepository = voteResultRepository;
        this.userRepository = userRepository;
        this.truckRepository = truckRepository;
        this.eventRepository = eventRepository;
    }

    public void vote(VoteRequest dto, String token, String fingerprint) {
        System.out.println(">>> EventId: " + dto.getEventId());
        System.out.println(">>> TruckId: " + dto.getTruckId());

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Truck truck = truckRepository.findById(dto.getTruckId())
                .orElseThrow(() -> new IllegalArgumentException("Truck not found"));

        // 현재 시간과 오늘의 시작/끝 시간을 LocalDateTime으로 설정
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        User user = null;

        // JWT 토큰이 있으면 여기서 userId 추출
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7); // "Bearer " 부분 제거
            if (JwtTokenProvider.isTokenValid(jwt)) {
                String userId = ExtractInfoFromToken.getUserIdFromToken(jwt);
                user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // 하루 최대 투표 횟수 확인
                int voteCount = voteRepository.countByUserAndEventAndVotedAtBetween(user, event, startOfDay, endOfDay);
                if (voteCount >= 100) {
                    throw new IllegalStateException("회원은 하루 최대 100번까지만 투표할 수 있습니다.");
                }
                // 중복 투표 확인 (회원)
                boolean alreadyVoted = voteRepository.existsByUserAndEventAndTruck(user, event, truck);
                if (alreadyVoted) {
                    throw new IllegalStateException("이미 해당 트럭에 투표하셨습니다.");
                }

            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
        } else if (dto.getFingerprint() != null && !dto.getFingerprint().isBlank()) {
            // 비회원 로직
            int voteCount = voteRepository.countByFingerprintAndEventAndVotedAtBetween(
                    dto.getFingerprint(), event, startOfDay, endOfDay);
            if (voteCount >= 100) {
                throw new IllegalStateException("비회원은 하루 최대 100번까지만 투표할 수 있습니다.");
            }

            // 중복 투표 확인 (비회원)
            boolean alreadyVoted = voteRepository.existsByFingerprintAndEventAndTruck(dto.getFingerprint(), event, truck);
            if (alreadyVoted) {
                throw new IllegalStateException("이미 해당 트럭에 투표하셨습니다.");
            }

        } else {
            throw new IllegalArgumentException("회원 토큰 또는 fingerprint가 필요합니다.");
        }

        // 투표 저장
        Vote vote = Vote.builder()
                .user(user)
                .truck(truck)
                .event(event)
                .fingerprint(dto.getFingerprint())
                .votedAt(now)
                .build();
        voteRepository.save(vote);

        // 투표 결과 저장 또는 업데이트
        VoteResult result = voteResultRepository
                .findByEventAndTruck(event, truck)
                .orElse(VoteResult.builder()
                        .event(event)
                        .truck(truck)
                        .voteCount(0)
                        .savedAt(now)
                        .build());

        result.setVoteCount(result.getVoteCount() + 1);
        result.setSavedAt(now);
        voteResultRepository.save(result);
    }

    // 투표 결과 가져오기
    public List<VoteResponse> getVoteResponses(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));

        List<VoteResult> voteResults = voteResultRepository.findAllByEventOrderByVoteCountDesc(event);

        return voteResults.stream()
                .map(result -> {
                    Truck truck = result.getTruck();
                    String truckName = truck.getName();
                    Integer truckId = truck.getTruckId();

                    // 대표 이미지 가져오기 (예: 첫 번째 메뉴의 이미지)
                    String menuImage = null;
                    List<TruckMenu> menus = truck.getTruckMenus();
                    if (menus != null && !menus.isEmpty()) {
                        menuImage = menus.get(0).getMenuImage(); // 첫 번째 메뉴 이미지
                    }

                    return new VoteResponse(truckId, truckName, menuImage, result.getVoteCount());
                })
                .collect(Collectors.toList());
    }

    public List<VoteStatusResponse> checkVoteStatus(Integer eventId, String userId, String fingerprint) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));

        List<Truck> trucks = truckRepository.findTrucksByEventId(eventId);

        List<VoteStatusResponse> responses = new ArrayList<>();

        for (Truck truck : trucks) {
            boolean alreadyVoted = false;

            if (userId != null) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
                alreadyVoted = voteRepository.existsByUserAndEventAndTruck(user, event, truck);
            } else if (fingerprint != null) {
                alreadyVoted = voteRepository.existsByFingerprintAndEventAndTruck(fingerprint, event, truck);
            }
            // userId, fingerprint 둘 다 없으면 그냥 alreadyVoted = false

            responses.add(new VoteStatusResponse(truck.getTruckId(), alreadyVoted));
        }

        return responses;
    }

}
