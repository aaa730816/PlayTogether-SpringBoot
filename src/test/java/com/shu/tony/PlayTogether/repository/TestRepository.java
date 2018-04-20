package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRepository {
    @Autowired
    private UserRepository userRepository;
    @Test
    @Transactional
    @Rollback(value = false)
    public void testUserRepository() {
        User user = new User();
        user.setUsername("Tony");
        userRepository.save(user);
    }
}
