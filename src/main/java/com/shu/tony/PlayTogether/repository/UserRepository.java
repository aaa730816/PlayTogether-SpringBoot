package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByNickName(String nickName);
}
