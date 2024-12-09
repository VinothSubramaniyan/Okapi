package com.example.demo.Utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.demo.Entity.UserEntity;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import jakarta.mail.internet.MimeMessage;




@Service
public class EmailMessageService {
	
	    private final TemplateEngine templateEngine;

	    private final JavaMailSender javaMailSender;
	    
	    @Autowired
	    UserRepository userRepository;
	    
	    @Autowired
	    PasswordEncoder passwordEncoder;
		

	    
	    @Autowired
	    public EmailMessageService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
	        this.templateEngine = templateEngine;
	        this.javaMailSender = javaMailSender;
	    }
	    
	   
	    
	    public void sendMail(User user) {
	        try {
	            String message = "Successfully Registered\n\n" +
	                    "NAME: " + user.getUsername() + "\n" +
	                    "EMAIL: " + user.getEmail() + "\n" +
	                    "MOBILE NUMBER: " + user.getMobile() + "\n\n" +
	                    "Successful registration, an email has been sent to your email address for verification\n\n" +
	                    "Best regards,\n" +
	                    "Inventory Management Services";

	            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
	            helper.setSubject("Welcome To Inventory Management System " + user.getUsername());
	            helper.setText(message);
	            helper.setTo(user.getEmail());
	            javaMailSender.send(mimeMessage);
	        } catch (Exception e) {
	            System.err.print(e);
	        }
	    }

	    
	  public void sendOtpEmail(String toEmail, String otp) {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(toEmail);
      mailMessage.setSubject("OTP Verification");
      mailMessage.setText("Your OTP is: " + otp);
      javaMailSender.send(mailMessage);
      
      UserEntity user = userRepository.findByEmail(toEmail);
      if (user != null) {
          user.setOtp(otp);
          userRepository.save(user);
      }
  }
  
  
  public boolean verifyOTP(String email, String userOTP) {
	  UserEntity user = userRepository.findByEmail(email);
      if (user != null) {
          String storedOTP = user.getOtp();
          if (storedOTP != null && storedOTP.equals(userOTP)) {
              user.setOtp(null);
              userRepository.save(user);
              return true;
          }
      }
      return false;
  }
  
  public void updatePassword(String email, String newPassword) throws Exception {
	  UserEntity user = userRepository.findByEmail(email);
	    if (user != null) {
	        String encodedPassword = passwordEncoder.encode(newPassword);
	        user.setPassword(encodedPassword);
	        userRepository.save(user);
	    } else {
	        throw new Exception("User not found for email: " + email);
	    }
	}



public ResponseEntity<String> resetPassword(String email, String userOTP, String newPassword, String confirmPassword) {
	UserEntity user = userRepository.findByEmail(email);

      if (user != null && user.getOtp().equals(userOTP)) {
          if (newPassword.equals(confirmPassword)) {
              if (isValidPassword(newPassword)) {
                  // Encode and update the user's password
                  String encodedPassword = passwordEncoder.encode(newPassword);
                  user.setPassword(encodedPassword);
                  userRepository.save(user);
                  return ResponseEntity.ok("Password changed successfully.");
              } else {
                  return ResponseEntity.badRequest().body("Invalid password format. Password must meet the criteria.");
              }
          } else {
              return ResponseEntity.badRequest().body("New password and confirm password do not match.");
          }
      } else {
          return ResponseEntity.badRequest().body("Invalid OTP.");
      }
  }


  private boolean isValidPassword(String password) {
      String regex = "^(?=.*[0-9])"
                     + "(?=.*[a-z])(?=.*[A-Z])"
                     + "(?=.*[@#$%^&+=])"
                     + "(?=\\S+$).{8,20}$";
      return password.matches(regex);
  }



}

