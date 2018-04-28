package com.shu.tony.PlayTogether.controllers;

import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.user.NickNameVo;
import com.shu.tony.PlayTogether.nonentity.user.UserVo;
import com.shu.tony.PlayTogether.nonentity.user.UserResult;
import com.shu.tony.PlayTogether.repository.UserRepository;
import com.shu.tony.PlayTogether.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
        return user == null;
    }

    @RequestMapping("changeNickName")
    public ResultBase checkNickName(@RequestBody NickNameVo vo) {
        return userService.changeNickName(vo);
    }
    @RequestMapping("register")
    public UserResult register(@RequestBody UserVo criteria) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.updateUser(criteria);
    }

    @RequestMapping("login")
    public UserResult login(@RequestBody UserVo criteria) {
        return userService.login(criteria);
    }

    @RequestMapping("myjoin")
    public List<ActivityVo> getJoinActivities(@RequestParam String userId) {
        return userService.getJoinActivities(userId);
    }

    @RequestMapping("mycreate")
    public List<ActivityVo> getCreateActivities(@RequestParam String userId) {
        return userService.getCreateActivities(userId);
    }

}
