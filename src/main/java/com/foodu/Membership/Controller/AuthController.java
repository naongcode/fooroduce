package com.foodu.Membership.Controller;

import com.foodu.Membership.Dto.*;
import com.foodu.Membership.Service.UserService;
import com.foodu.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    //회원가입 엔드포인트
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        User user = userService.register(request);
        SignupResponse response = new SignupResponse(
                user.getUserId(),
                user.getEmail(),
                user.getRole().name() //enum을 문자열로 변환
        );
        return ResponseEntity.ok(response);
    }

    //로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류") + e.getMessage());
        }
    }

    //카카오 로그인
    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaologin(@RequestParam("code") String code) {
        // 카카오 로그인 처리
        LoginResponse loginResponse = userService.kakaoLogin(code);

        // 로그인 응답 반환 (JWT 토큰과 함께)
        return ResponseEntity.ok(loginResponse);
    }


    //아이디 중복확인 엔드포인트
    @GetMapping("/id-check")
    public ResponseEntity<?> checkUserisUsableId(HttpServletRequest request, @RequestParam("user_id") String pathUserId) { //쿼리 파라미터로 user_id 받음 형식(id-check?user_id=test)
        try {
            //아이디 중복확인 로직
            boolean isDuplicate = userService.isUsableId(pathUserId);
            return ResponseEntity.ok(new CheckUserIdResponse(isDuplicate));

        } catch (Exception e) {
            log.error("아이디 중복 확인 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CheckUserIdResponse(false));
        }
    }

}
