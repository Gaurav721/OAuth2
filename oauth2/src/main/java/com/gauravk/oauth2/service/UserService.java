package com.gauravk.oauth2.service;

import com.gauravk.oauth2.enums.AuthProvider;
import com.gauravk.oauth2.model.User;
import com.gauravk.oauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUserLocal(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuth_provide(AuthProvider.LOCAL);
        return userRepository.save(user);
    }

    public User loginUserLocal(User user){
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(existingUser != null){
            if(!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())){
                throw new RuntimeException("User Id or Password does not match");
            }
            return existingUser;
        }
        throw new RuntimeException("User not found");
    }

    public User loginRegisterByGoogleOAuth2(OAuth2AuthenticationToken auth2AuthenticationToken){
        OAuth2User oAuth2User = auth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("User email from Google is :"+email+" Name"+name);

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null){
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setAuth_provide(AuthProvider.GOOGLE);
            return userRepository.save(user);
        }
        return user;
    }
}

