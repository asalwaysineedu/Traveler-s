package com.example.demo.controller.member;

import com.example.demo.entity.member.Role;
import com.example.demo.entity.member.Code;
import com.example.demo.entity.member.User;
import com.example.demo.response.MemberResponse;
import com.example.demo.service.member.UserService;
import com.example.demo.utility.customUserDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String index(Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("**********" +oAuth2User.getAttributes());

        return "세션 확인" + oAuth2User.getAttributes();

    }

   /* @GetMapping("/googleLogin")
    public MemberResponse OAuth(Authentication authentication) {



        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setEmail(customUserDetails.getUser().getEmail());
        memberResponse.setName(customUserDetails.getUser().getName());

        return memberResponse;
    }*/

    @GetMapping("/listall")
    public List<User> login(){
        return userService.listAll();
    }

    @PostMapping("/register")
    public void register(){
        User user = new User("admin", "admin@gmail.com", "password");
        Role role = new Role("ADMIN");
        userService.addUser(user);

        userService.addRoleToUser(user, role);
    }

    @GetMapping("/kakaoLogin")
    public String kakaoLogin(String code){

        RestTemplate restTemplate = new RestTemplate();

        Code authorizeCode  = new Code(code);

        String result = restTemplate.postForObject(
                "http://localhost:5000/kakao-login",
                authorizeCode,
                String.class
        );

        log.info("result = " + result);

        return result;
    }

}