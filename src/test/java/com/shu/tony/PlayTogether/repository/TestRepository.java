package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.entity.Activity;
import com.shu.tony.PlayTogether.entity.GeoLocation;
import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRepository {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityRepositoryImpl activityRepositoryImpl;
    @Test
    @Transactional
    @Rollback(value = false)
    public void testUserRepository() {
        User user = new User();
        user.setUsername("Tony");
        userRepository.save(user);
    }

}
