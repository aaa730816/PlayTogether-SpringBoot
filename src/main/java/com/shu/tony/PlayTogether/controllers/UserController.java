package com.shu.tony.PlayTogether.controllers;

import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.user.RegisterCriteria;
import com.shu.tony.PlayTogether.nonentity.user.RegisterResult;
import com.shu.tony.PlayTogether.repository.UserRepository;
import com.shu.tony.PlayTogether.service.user.UserService;
import com.shu.tony.PlayTogether.utils.SHAEncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("user")
@Scope("request")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @RequestMapping("check")
    public boolean check(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    @RequestMapping("register")
    public RegisterResult register(@RequestBody RegisterCriteria criteria) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.updateUser(criteria);
    }

}
