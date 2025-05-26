package com.foodu.Membership.Service;

import com.foodu.Membership.Dto.*;
import com.foodu.repository.UserRepository;
import com.foodu.util.JwtTokenProvider;
import com.foodu.util.PasswordUtil;
import com.foodu.entity.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;


    //회원가입 로직
    public User register(SignupRequest request) {

        //bycrpt 해싱
        String hashedPassword = PasswordUtil.hashPassword(request.getPassword());

        //유저의 권한(일반 사용자, 행사담당자, 푸드트럭주인)
        User.Role role;
        try{
            role = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않는 역할입니다.");
        }


        User user = User.builder()
                .userId(request.getUser_id()) //userid 저장
                .email(request.getEmail()) //email 저장
                .password(hashedPassword) //password 저장
                .role(role) //role 저장
                .createdAt(LocalDateTime.now())  //createAt저장
                .build();

        return userRepository.save(user); //DB에 user객체 저장
    }
    
    //로그인 로직
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserId(request.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 잘못되었습니다."));

        String hashedPassword = PasswordUtil.hashPassword(request.getPassword());

        // bcrypt 비밀번호 비교
        if (!PasswordUtil.verifyPassword(request.getPassword(), user.getPassword())) {//수정
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        // JWT 생성(아이디정보를 가지고 있음)
        String token = JwtTokenProvider.createToken(user.getUserId(), user.getRole().name());
        
        //Role 문자열로 반환
        String role = user.getRole().name(); //enum을 문자열로 변환
        
        return new LoginResponse(token, role,"로그인 성공");

    }


    //카카오 로그인 로직

    //1. 인가 코드 -> Access Token로 받음
    private static final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    public String getAccessTokenFromKakao(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                KakaoTokenResponse.class
        );

        KakaoTokenResponse tokenResponse = response.getBody();
        if (tokenResponse == null) {
            throw new RuntimeException("카카오로부터 토큰을 받아오지 못했습니다.");
        }

        log.info("Access Token: {}", tokenResponse.getAccessToken());
        return tokenResponse.getAccessToken();
    }

    //2.Access Token -> 사용자 정보 요청
    public KakaoProfile getKakaoProfile(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoProfile> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                KakaoProfile.class
        );

        KakaoProfile profile = response.getBody();
        if (profile == null) {
            throw new RuntimeException("카카오 사용자 정보를 받아오지 못했습니다.");
        }

        log.info("카카오 사용자 정보: id={}, email={}",
                profile.getId(), profile.getKakao_account().getEmail());

        return profile;
    }

    //3. 사용자 존재 여부 확인 + 회원가입 + JWT 발급 반환
    public LoginResponse kakaoLogin(String code) {
        String accessToken = getAccessTokenFromKakao(code);
        KakaoProfile profile = getKakaoProfile(accessToken);

        String kakaoId = "kakao_" + profile.getId();
        Optional<User> optionalUser = userRepository.findByUserId(kakaoId);

        User user;

        // 이메일을 카카오 프로필에서 가져오기
        String email = profile.getKakao_account().getEmail();

        // 이메일이 null이면 기본 이메일 설정
        if (email == null) {
            email = "no1-email@kakao.com"; // 기본 이메일 설정
        }

        if (optionalUser.isEmpty()) {
            // 회원가입
            user = User.builder()
                    .userId(kakaoId)
                    .role(User.Role.GENERAL) // 기본 권한
                    .password("kakao") // 의미 없는 값
                    .email(email) // 이메일 설정
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        String token = JwtTokenProvider.createToken(user.getUserId(), user.getRole().name());
        return new LoginResponse(token, user.getRole().name(), "카카오 로그인 성공");
    }

    // 아이디 중복 확인 - 해당 아이디가 존재하면 false (사용 불가), 존재하지 않으면 true (사용 가능)
    public boolean isUsableId(String userId) {
        try {
            return !userRepository.existsByUserId(userId); // 존재하면 false, 존재하지 않으면 true
        } catch (Exception e) {
            log.error("Error checking duplicate user ID: ", e);
            throw new RuntimeException("Database error occurred");
        }
    }


}

