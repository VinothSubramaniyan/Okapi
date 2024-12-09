package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.LoginDto;
import com.example.demo.DTO.UserDto;
import com.example.demo.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootApplication

@RestController
@RequestMapping("/VS1/User")
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping(path="/update")
	public ResponseEntity<Map<String,Object>>userUpdate(@RequestBody UserDto userDto){
		return userService.UserUpdate(userDto);
	}
		
	@PostMapping(path="/login")
	public ResponseEntity<Map<String,Object>>userLogin(@RequestBody LoginDto loginDto){
		return userService.UserLogin(loginDto);
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<Map<String,Object>>GetUserData(@PathVariable Long id){
		return userService.getUserData(id,null,null,null);
		
	}
	@GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserDataByUserId(@PathVariable String userId) {
        String email = null;
        String username = null;
        String mobile = null;

        return userService.getUserDataByUserId(userId, email, username, mobile);
    }
	
	 @GetMapping("/logout")
	    public String logoutApi(HttpServletRequest request, HttpServletResponse response) {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null) {
	            new SecurityContextLogoutHandler().logout(request, response, auth);
	        }
	        return "Logged out successfully";
	    }
	  
	
}
