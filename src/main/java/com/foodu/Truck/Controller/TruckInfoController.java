package com.foodu.Truck.Controller;

import com.foodu.Truck.Dto.*;
import com.foodu.Truck.Service.TruckInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/truck")
public class TruckInfoController {

    private final TruckInfoService truckInfoService;

    /** 트럭 기본정보 등록 */
    @PostMapping
    public ResponseEntity<TruckInfoResponse> saveOrUpdateTruck(@RequestBody TruckInfoRequest requestDto,
                                                             HttpServletRequest request) {
        String ownerId = request.getAttribute("userId").toString();
        Integer truckId = truckInfoService.saveOrUpdateTruck(requestDto, ownerId);
        return ResponseEntity
                .created(URI.create("/api/truck/" + truckId))
                .body(new TruckInfoResponse(truckId));
    }

    /**트럭 기본정보 수정*/
    @PutMapping("/{truckId}")
    public ResponseEntity<?> updateTruck(@PathVariable Integer truckId, @RequestBody TruckInfoRequest request) {
        truckInfoService.updateTruck(truckId, request);
        return ResponseEntity.ok().build();
    }

    /** 메뉴 등록 */
    @PostMapping("/menus")
    public ResponseEntity<MenuInfoResponse> saveMenu(@RequestBody MenuInfoRequest menuRequestDto,
                                                     HttpServletRequest request) {
        try {
            String ownerId = request.getAttribute("userId").toString();
            Integer menuId = truckInfoService.saveMenu(menuRequestDto, ownerId);
            return ResponseEntity
                    .created(URI.create("/api/truck/menus/" + menuId))
                    .body(new MenuInfoResponse(menuId));
        } catch (Exception e) {
            e.printStackTrace();  // 에러 스택 트레이스 출력
            return ResponseEntity.status(500).build();
        }
    }

    /**메뉴 수정*/
    @PutMapping("/menus/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable Integer menuId,
                                        @RequestBody MenuInfoRequest request,
                                        HttpServletRequest httpRequest) {
        String userId = httpRequest.getAttribute("userId").toString();
        truckInfoService.updateMenu(menuId, request, userId);
        return ResponseEntity.ok().build();
    }

    /**조회*/
    @GetMapping("/my")
    public ResponseEntity<List<TruckWithMenusResponse>> getMyTrucks(HttpServletRequest request) {
        String ownerId = request.getAttribute("userId").toString();
        List<TruckWithMenusResponse> trucks = truckInfoService.getTrucksWithMenusByOwner(ownerId);
        return ResponseEntity.ok(trucks);
    }
}