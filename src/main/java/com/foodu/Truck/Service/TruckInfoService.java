package com.foodu.Truck.Service;

import com.foodu.Truck.Dto.MenuInfoResponse;
import com.foodu.Truck.Dto.TruckInfoRequest;
import com.foodu.Truck.Dto.MenuInfoRequest;
import com.foodu.Truck.Dto.TruckWithMenusResponse;
import com.foodu.entity.Truck;
import com.foodu.entity.TruckMenu;
import com.foodu.entity.User;
import com.foodu.repository.TruckRepository;
import com.foodu.repository.TruckMenuRepository;
import com.foodu.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TruckInfoService {

    private final TruckRepository truckRepository;
    private final TruckMenuRepository truckMenuRepository;
    private final UserRepository userRepository;  // 추가

    /**기본정보 등록, 수정*/
    @Transactional
    public Integer saveOrUpdateTruck(TruckInfoRequest requestDto, String ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Truck truck;
        try {
            if (requestDto.getTruckId() != null) {
                truck = truckRepository.findById(requestDto.getTruckId())
                        .map(existing -> {
                            if (!existing.getOwner().getUserId().equals(ownerId)) {
                                throw new SecurityException("본인 소유의 트럭만 수정할 수 있습니다.");
                            }
                            existing.setName(requestDto.getName());
                            existing.setPhoneNumber(requestDto.getPhoneNumber());
                            existing.setDescription(requestDto.getDescription());
                            return existing;
                        })
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 트럭입니다."));
            } else {
                truck = Truck.builder()
                        .name(requestDto.getName())
                        .phoneNumber(requestDto.getPhoneNumber())
                        .description(requestDto.getDescription())
                        .owner(owner)
                        .createdAt(LocalDateTime.now())
                        .build();
            }

            return truckRepository.save(truck).getTruckId();

        } catch (DataIntegrityViolationException e) {
            // 중복으로 인한 제약 위반 에러 처리
            throw new IllegalArgumentException("이미 동일한 이름의 트럭이 존재합니다.");
        }
    }

    /** 메뉴저장 */
    @Transactional
    public Integer saveMenu(MenuInfoRequest menuRequestDto, String ownerId) {
        Truck truck = truckRepository.findById(menuRequestDto.getTruckId())
                .orElseThrow(() -> new IllegalArgumentException("해당 트럭이 존재하지 않습니다."));

        if (!truck.getOwner().getUserId().equals(ownerId)) {
            throw new SecurityException("트럭 소유자만 메뉴를 등록할 수 있습니다.");
        }

        TruckMenu menu = TruckMenu.builder()
                .truck(truck)
                .menuName(menuRequestDto.getMenuName())
                .menuPrice(menuRequestDto.getMenuPrice())
                .menuImage(menuRequestDto.getMenuImage())
                .menuType(menuRequestDto.getMenuType())
                .build();

        return truckMenuRepository.save(menu).getMenuId();
    }

    /**정보 조회*/
    @Transactional(readOnly = true)
    public List<TruckWithMenusResponse> getTrucksWithMenusByOwner(String ownerId) {
        List<Truck> trucks = truckRepository.findByOwner_UserId(ownerId);

        return trucks.stream().map(truck -> {
            List<MenuInfoResponse> menus = truck.getTruckMenus().stream()
                    .map(menu -> new MenuInfoResponse(menu.getMenuId(), menu.getMenuName(), menu.getMenuPrice(), menu.getMenuImage(), menu.getMenuType()))
                    .collect(Collectors.toList());

            return new TruckWithMenusResponse(
                    truck.getTruckId(),
                    truck.getName(),
                    truck.getPhoneNumber(),
                    truck.getDescription(),
                    menus
            );
        }).collect(Collectors.toList());
    }

    /**트럭 기본정보 수정*/
    public void updateTruck(Integer truckId, TruckInfoRequest request) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("트럭을 찾을 수 없습니다."));

        truck.setName(request.getName());
        truck.setPhoneNumber(request.getPhoneNumber());
        truck.setDescription(request.getDescription());

        truckRepository.save(truck); // JPA에서 필요시 생략 가능하지만 명시적으로 작성
    }

    /** 메뉴 수정 */
    public void updateMenu(Integer menuId, MenuInfoRequest request, String userId) {
        TruckMenu menu = truckMenuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당 메뉴를 찾을 수 없습니다."));

        // 본인 확인
        if (!menu.getTruck().getOwner().getUserId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        menu.setMenuName(request.getMenuName());
        menu.setMenuPrice(request.getMenuPrice());

        if (request.getMenuImage() != null && !request.getMenuImage().isEmpty()) {
            // 프론트에서 이미 S3에 업로드한 이미지 URL이 넘어온다고 가정
            menu.setMenuImage(request.getMenuImage());
        }

        //추가 : 메뉴의 카테고리 수정
        menu.setMenuType(request.getMenuType());

        truckMenuRepository.save(menu);
    }
}