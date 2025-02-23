package com.gauravk.oauth2.controller;

import com.gauravk.oauth2.model.User;
import com.gauravk.oauth2.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Validated User user){
        return ResponseEntity.ok(userService.registerUserLocal(user));
    }

    @PostMapping("/login/local")
    public ResponseEntity<User> loginLocal(@RequestBody @Validated User user){
        return ResponseEntity.ok(userService.loginUserLocal(user));
    }

    @GetMapping("/login/google")
    public ResponseEntity<String> loginGoogleAuth(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
        return ResponseEntity.ok("Redirecting ...");
    }

    @GetMapping("/loginSuccess")
    public ResponseEntity<String> handleGoogleSuccess(OAuth2AuthenticationToken oAuth2AuthenticationToken) throws IOException {
        User user = userService.loginRegisterByGoogleOAuth2(oAuth2AuthenticationToken);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/home")).build();
    }

}
