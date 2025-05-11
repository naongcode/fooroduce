package com.foodu.repository;

import com.foodu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email); //해당 유저의 이메일 찾기
    
    boolean existsByUserId(String userId); //해당 유저 아이디가 이미 존재하는지 여부를 반환
    Optional<User> findByUserId(String userId); //해당 유저 아이디 찾기


}
