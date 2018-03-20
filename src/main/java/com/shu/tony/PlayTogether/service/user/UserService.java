package com.shu.tony.PlayTogether.service.user;

import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.user.RegisterCriteria;
import com.shu.tony.PlayTogether.nonentity.user.RegisterResult;
import com.shu.tony.PlayTogether.repository.UserRepository;
import com.shu.tony.PlayTogether.utils.SHAEncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public RegisterResult updateUser(RegisterCriteria criteria) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        User user = new User();
        user.setUsername(criteria.getUsername());
        user.setPassword(SHAEncodeUtil.shaEncode(criteria.getPassword()));
        user.setLoginType(criteria.getLoginType());
        userRepository.save(user);
        RegisterResult result = new RegisterResult();
        result.setUserOid(user.getId());
        result.setUsername(user.getUsername());
        return result;
    }
}
