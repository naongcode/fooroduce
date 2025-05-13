package com.foodu.Membership.Service;

import com.foodu.Membership.Dto.VoteRequestDto;
import com.foodu.entity.*;
import com.foodu.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Transactional
    public void vote(VoteRequestDto dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Truck truck = truckRepository.findById(dto.getTruckId())
                .orElseThrow(() -> new IllegalArgumentException("Truck not found"));

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate today = LocalDate.now();
        String startOfDay = today.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endOfDay = today.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        User user = null;

        if (dto.getUserId() != null && !dto.getUserId().isBlank()) {
            // 회원 로직
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            int voteCount = voteRepository.countByUserAndEventAndVotedAtBetween(user, event, startOfDay, endOfDay);
            if (voteCount >= 3) {
                throw new IllegalStateException("회원은 하루 최대 3번까지만 투표할 수 있습니다.");
            }

        } else if (dto.getFingerprint() != null && !dto.getFingerprint().isBlank()) {
            // 비회원 로직
            int voteCount = voteRepository.countByFingerprintAndEventAndVotedAtBetween(
                    dto.getFingerprint(), event, startOfDay, endOfDay);
            if (voteCount >= 1) {
                throw new IllegalStateException("비회원은 하루 최대 3번까지만 투표할 수 있습니다.");
            }

        } else {
            throw new IllegalArgumentException("회원 ID 또는 fingerprint가 필요합니다.");
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

        // 투표 결과 저장 or 업데이트
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

    public List<VoteResult> getVoteResults(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));
        return voteResultRepository.findAllByEventOrderByVoteCountDesc(event);
    }



}
