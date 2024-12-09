package com.example.demo.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.LoginDto;
import com.example.demo.DTO.UserDto;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Utils.EmailMessageService;
import com.example.demo.Utils.JwtKeyProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class UserService  {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private JwtKeyProvider jwtSecretKeyProvider;
	
	@Autowired
    private EmailMessageService emailService;
	
    
    
	public boolean checkAlreadyRegistered(String email) {
		UserEntity carbageData = userRepository.findByEmail(email);
        if(carbageData != null) {
        	return true;
        }else {
        	return false;
        }
    }
    private boolean checkMobileNumberAlreadyExists(String mobile) {
    	UserEntity existingUser = userRepository.findByMobile(mobile);
        return existingUser != null;
    }
    
    public boolean isValidPhoneNumber(String s) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }  
    
    public boolean isValidUsername(String username) {
        String unRegex = "^[A-Z0-9._%+-/!#$%&'*=?^_`{|}~]+$";
        Pattern unPattern = Pattern.compile(unRegex);
        Matcher unMatcher = unPattern.matcher(username.toUpperCase());
        return unMatcher.matches();
    }
    
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])"
                       + "(?=.*[a-z])(?=.*[A-Z])"
                       + "(?=.*[@#$%^&+=])"
                       + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public ResponseEntity<Map<String, Object>> UserUpdate(UserDto customerDto) { 
    	Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        String email = customerDto.getEmail();
        String mobileNumber = customerDto.getMobile();
        String username = customerDto.getUsername();
        String password = customerDto.getPassword();
        String confirmPassword = customerDto.getConfirmPassword();

        Map<String, Object> userData = new LinkedHashMap<>();

        if (!isValidEmail(email)) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Invalid Email Id");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (checkAlreadyRegistered(email)) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Email already registered");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else if (!isValidPhoneNumber(mobileNumber)) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Invalid Mobile number");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
        }else if (checkMobileNumberAlreadyExists(mobileNumber)) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "mobile number already exit");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else if (!isValidUsername(username)) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Invalid username");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (!isValidPassword(password)) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Invalid password");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (!password.equals(confirmPassword)) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Password and confirmation do not match");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
        	UserEntity register = new UserEntity();
        	register.setUsername(username);
        	register.setEmail(email);
        	register.setMobile(mobileNumber);

            
        	register.setUserId(generateUserId());
            
        	register.setPassword(passwordEncoder.encode(password));
            
            String jwtToken = generateJwtToken(username);
            register.setToken(jwtToken);
            userRepository.save(register);

            userData.put("userName", register.getUsername());
            userData.put("userId", register.getUserId());
            userData.put("token", register.getToken());
            userData.put("boolean", true);
            dataList.add(userData);
            
            User user = new User();
            user.setEmail(customerDto.getEmail());
            user.setUsername(customerDto.getUsername());
            user.setMobile(customerDto.getMobile());
            emailService.sendMail(user);

            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("status_code", 200);
            meta.put("message", "Successfully registered");

            response.put("data", dataList);
            response.put("meta", meta);
            response.put("pagination", new LinkedHashMap<>());

        }
        
            return ResponseEntity.status(HttpStatus.OK).body(response);
      
    }
    
    private String generateUserId() {
        String characters = "0123456789";
        StringBuilder userId = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = (int) (Math.random() * characters.length());
            userId.append(characters.charAt(index));
        }

        return userId.toString();
    }
    
    private String generateJwtToken(String username) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecretKeyProvider.getSecretKey().getBytes());

        return Jwts.builder()
            .setSubject(username)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }
    
    
	public ResponseEntity<Map<String, Object>> UserLogin(LoginDto customerDtoLoginDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        
        UserEntity user = null;
        
        if (customerDtoLoginDto.getEmail() != null) {
        	user = userRepository.findByEmail(customerDtoLoginDto.getEmail());
        } else if (customerDtoLoginDto.getMobile() != null) {
        	user = userRepository.findByMobile(customerDtoLoginDto.getMobile());
        }

        if (user != null) {
            String password = customerDtoLoginDto.getPassword();
            String encodedPassword = user.getPassword();
            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);

            if (isPwdRight) {
                Map<String, Object> userData = new LinkedHashMap<>();
                userData.put("userName", user.getUsername());
                userData.put("userId", user.getUserId());
                userData.put("boolean", true);
                dataList.add(userData);

                
                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("status_code", 200);
                meta.put("message", "Successfully Login");
               
                response.put("data", dataList);
                response.put("meta", meta);
                response.put("pagination", new LinkedHashMap<>());

                return ResponseEntity.ok(response);
            }
        }

        Map<String, Object> userData = new LinkedHashMap<>();
        userData.put("status_code", 401);
        userData.put("boolean", false);
        userData.put("message", "Login Failed");
        dataList.add(userData);

        response.put("data", dataList);
        response.put("meta", new LinkedHashMap<>());
        response.put("pagination", new LinkedHashMap<>());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
	
	 public ResponseEntity<Map<String, Object>> getUserData(Long id, String username, String mobile, String email) {
	        Map<String, Object> response = new LinkedHashMap<>();
	        List<Map<String, Object>> dataList = new ArrayList<>();

	        try {
	        	UserEntity userEntity = null;

	            if (id > 0) {
	            	userEntity = userRepository.findById((Long) id).orElse(null);
	            } else if (username != null) {
	            	userEntity = userRepository.findByUsername(username);
	            } else if (mobile != null) {
	            	userEntity = userRepository.findByMobile(mobile);
	            } else if (email != null) {
	            	userEntity = userRepository.findByEmail(email);
	            }

	            if (userEntity != null) {
	                Map<String, Object> userData = new LinkedHashMap<>();
	                userData.put("userId", userEntity.getId()); 
	                userData.put("userName", userEntity.getUsername());
	                userData.put("email", userEntity.getEmail());
	                userData.put("mobile", userEntity.getMobile());

	                dataList.add(userData);
	                
	                Map<String, Object> meta = new LinkedHashMap<>();
	                meta.put("status_code", 200);
	                meta.put("message", "Successfully registered");
	                
	                response.put("data", dataList);
	                response.put("meta", meta);
	                response.put("pagination", new LinkedHashMap<>());
	                
	                return ResponseEntity.ok(response); 
	                
	            } else {
	                response.put("status_code", 404);
	                response.put("boolean", false);
	                response.put("message", "User not found");
	                
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Return a 404 Not Found response for user not found

	            }
	        } catch (Exception e) {
	            response.put("status_code", 500);
	            response.put("boolean", false);
	            response.put("message", "Error fetching user data");
	            
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Return a 500 Internal Server Error response for errors

	        }

	    }
	    
	 public ResponseEntity<Map<String, Object>> getUserDataByUserId(String userId, String email, String username, String mobile) {
	        Map<String, Object> response = new LinkedHashMap<>();
	        List<Map<String, Object>> dataList = new ArrayList<>();

	        try {
	        	UserEntity userEntity = null;

	            if (email != null) {
	            	userEntity = userRepository.findByEmail(email);
	            } else if (userId != null) {
	            	userEntity = userRepository.findByUserId(userId);
	            } else if (username != null) {
	            	userEntity = userRepository.findByUsername(username);
	            } else if (mobile != null) {
	            	userEntity = userRepository.findByMobile(mobile);
	            }

	            if (userEntity != null) {
	                Map<String, Object> userData = new LinkedHashMap<>();
	                userData.put("userId", userEntity.getUserId());
	                userData.put("userName", userEntity.getUsername());
	                userData.put("email", userEntity.getEmail());
	                userData.put("mobile", userEntity.getMobile());

	                dataList.add(userData);

	                Map<String, Object> meta = new LinkedHashMap<>();
	                meta.put("status_code", 200);
	                meta.put("message", "Successfully fetched user data");

	                response.put("data", dataList);
	                response.put("meta", meta);
	                response.put("pagination", new LinkedHashMap<>());

	                return ResponseEntity.ok(response); // Return a 200 OK response for success

	            } else {
	                response.put("status_code", 404);
	                response.put("boolean", false);
	                response.put("message", "User not found");

	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Return a 404 Not Found response for user not found
	            }
	        } catch (Exception e) {
	            response.put("status_code", 500);
	            response.put("boolean", false);
	            response.put("message", "Error fetching user data");

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Return a 500 Internal Server Error response for errors
	        }
	    }

}
