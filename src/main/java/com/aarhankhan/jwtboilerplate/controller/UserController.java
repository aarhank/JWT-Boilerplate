package com.aarhankhan.jwtboilerplate.controller;

import com.aarhankhan.jwtboilerplate.model.JWTResponse;
import com.aarhankhan.jwtboilerplate.model.User;
import com.aarhankhan.jwtboilerplate.service.UserService;
import com.aarhankhan.jwtboilerplate.utility.JWTutility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTutility jwtUtility;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(){
        return "somehow worked";
    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody User user) throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
        }
        catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS",e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        final String token = jwtUtility.generateToken(userDetails);

        return new JWTResponse(token);
    }
}
