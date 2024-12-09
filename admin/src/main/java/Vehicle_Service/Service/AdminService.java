package Vehicle_Service.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Vehicle_Service.DTO.AdminDto;
import Vehicle_Service.DTO.LoginDto;
import Vehicle_Service.Entity.AdminEntity;
import Vehicle_Service.Model.Author;
import Vehicle_Service.Repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	AdminRepository adminRepository;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	public boolean checkAdminAlreadyExists (String emailId) {
		AdminEntity AdminData=adminRepository.findByEmailId(emailId);
		if (AdminData!=null) {
		return true;
		}else {
		return false;
		}
	}
	
	public boolean checkPhoneNoAlreadyExists (String phoneNo) {
		AdminEntity ExistingAdmin = adminRepository.findByPhoneNo(phoneNo);
			return ExistingAdmin != null;
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
    
    public boolean isValidAdminName(String adminName) {
        String unRegex = "^[A-Z0-9._%+-/!#$%&'*=?^_`{|}~]+$";
        Pattern unPattern = Pattern.compile(unRegex);
        Matcher unMatcher = unPattern.matcher(adminName.toUpperCase());
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

    public ResponseEntity<Map<String,Object>> AdminRegister (AdminDto adminDto){
		Map<String,Object>response= new LinkedHashMap<>();
    	List<Map<String,Object>>dataList = new ArrayList<>();
    	
    	String emailId =adminDto.getEmailId();
    	String phoneNo = adminDto.getPhoneNo();
    	String adminName= adminDto.getAdminName();
    	String password = adminDto.getPassword();
    	String confirmPassword = adminDto.getConfirmPassword();

    	Map<String,Object> AdminData = new LinkedHashMap<>();
    	
    	if(!isValidEmail(emailId)) {
    		AdminData.put("status_code",400);
    		AdminData.put("boolean", false);
            AdminData.put("message", "Invalid Email Id");
            dataList.add(AdminData);
            response.put("data",dataList);
            response.put("meta",new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}else if(checkAdminAlreadyExists(emailId)) {
    		AdminData.put("status_code",400);
    		AdminData.put("boolean", false);
            AdminData.put("message", "Email Id already registered");
            dataList.add(AdminData);
            response.put("data",dataList);
            response.put("meta",new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}else if (!isValidPhoneNumber(phoneNo)) {
    		AdminData.put("status_code",400);
    		AdminData.put("boolean", false);
            AdminData.put("message", "invalid phone number");
            dataList.add(AdminData);
            response.put("data",dataList);
            response.put("meta",new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}else if (checkPhoneNoAlreadyExists(phoneNo)) {
    		AdminData.put("status_code",400);
    		AdminData.put("boolean", false);
            AdminData.put("message", "PHONE  NO already registered");
            dataList.add(AdminData);
            response.put("data",dataList);
            response.put("meta",new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}else if(!isValidAdminName(adminName)) {
    		AdminData.put("status_code",400);
    		AdminData.put("boolean", false);
            AdminData.put("message", "INVALID ADMIN NAME");
            dataList.add(AdminData);
            response.put("data",dataList);
            response.put("meta",new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}else if(!isValidPassword(password)) {
    		AdminData.put("status_code",400);
    		AdminData.put("boolean", false);
            AdminData.put("message", "INVALID PASSWORD");
            dataList.add(AdminData);
            response.put("data",dataList);
            response.put("meta",new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}else if (!password.equals(confirmPassword)) {
    		AdminData.put("status_code",400);
    		AdminData.put("boolean", false);
            AdminData.put("message", "INCORRECT PASSWORD");
            dataList.add(AdminData);
            response.put("data",dataList);
            response.put("meta",new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}else {
    		AdminEntity Admin = new AdminEntity();
    		Admin.setAdminId(generateAdminId());
    		Admin.setAdminName(adminName);
    		Admin.setEmailId(emailId);
    		Admin.setPhoneNo(phoneNo);
    		Admin.setPassword(passwordEncoder.encode(password));
    		adminRepository.save(Admin);
    		
    		AdminData.put("adminName", Admin.getAdminName());
    		AdminData.put("adminId", Admin.getAdminId());
    		AdminData.put("boolean", true);
    		dataList.add(AdminData);
    		
    		Author auth = new Author();
    		auth.setAdminName(adminDto.getAdminName());
    		auth.setEmailId(adminDto.getEmailId());
    		auth.setPhoneNo(adminDto.getPhoneNo());
    		
    		
    		Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("status_code", 200);
            meta.put("message", "Successfully registered");
    		response.put("data", dataList);
            response.put("meta", meta);
            response.put("pagination", new LinkedHashMap<>());

    	}
    
        return ResponseEntity.status(HttpStatus.OK).body(response);
    	
    }

	private String generateAdminId() {
		String characters= "0123456789";
		StringBuilder adminId = new StringBuilder();
		for(int i =0;i<4;i++) {
			int use = (int)(Math.random()*characters.length());
			adminId.append(characters.charAt(use));
		}
		return adminId.toString();
		
	}
	public ResponseEntity<Map<String,Object>>Adminlogin(LoginDto loginDto){
		Map<String,Object> response = new LinkedHashMap<>();
		List <Map<String,Object>>dataList = new ArrayList<>();
		
		AdminEntity admin= null;
		
		if(loginDto.getEmailId()!= null) {
			admin = adminRepository.findByEmailId(loginDto.getEmailId());
		}else if (loginDto.getPhoneNo()!=null) {
			admin = adminRepository.findByPhoneNo(loginDto.getPhoneNo());
		}
		if(admin != null) {
			String password = loginDto.getPassword();
			String encodedPassword = admin.getPassword();
			boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
			
			if(isPwdRight) {
				Map<String, Object> AdminData = new LinkedHashMap<>();
				AdminData.put("adminName", admin.getAdminName());
				AdminData.put("adminId", admin.getAdminId());
				AdminData.put("boolean", true);
				dataList.add(AdminData);
				 
				Map<String, Object> meta = new LinkedHashMap<>();
	            meta.put("status_code", 200);
	            meta.put("message", "Successfully Login");  
	            response.put("data", dataList);
	            response.put("meta", meta);
	            response.put("pagination", new LinkedHashMap<>());
	            
	            return ResponseEntity.ok(response);
			}
		}
			Map<String, Object> AdminData = new LinkedHashMap<>();
			AdminData.put("status_code", 401);
			AdminData.put("boolean", false);
			AdminData.put("message", "Login Failed");
	        dataList.add(AdminData);

	        response.put("data", dataList);
	        response.put("meta", new LinkedHashMap<>());
	        response.put("pagination", new LinkedHashMap<>());

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		
	}
	public ResponseEntity<Map<String,Object>>getAdminData(Long id,String adminId,String phoneNo,String emailId,String adminName){
		Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
		
        try {
        	AdminEntity admin = null;
        	if(id >0) {
        		admin = adminRepository.findById((Long)id).orElse(null);
        	}
        	else if(adminName !=null) {
        		admin= adminRepository.findByAdminName(adminName);
        	}else if(emailId != null) {
        		admin = adminRepository.findByEmailId(emailId);
        	}else if(phoneNo != null) {
        		admin = adminRepository.findByPhoneNo(phoneNo);
        	}else if (adminId != null) {
        		admin = adminRepository.findByAdminId(adminId);
        	}
        	if(admin != null) {
        		Map<String,Object> AdminData = new LinkedHashMap<>();
        		AdminData.put("adminId",admin.getAdminId());
        		AdminData.put("emailId", admin.getEmailId());
        		AdminData.put("phoneNo", admin.getPhoneNo());
        		AdminData.put("adminName", admin.getAdminName());
        		
        		dataList.add(AdminData);
        		
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
        }catch (Exception e) {
        	response.put("status_code", 500);
            response.put("boolean", false);
            response.put("message", "Error fetching user data");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Return a 500 Internal Server Error response for errors

        }
	}
public ResponseEntity <Map<String,Object>>getAdminDataByAdminId(String adminId,String adminName,String phoneNo, String emailId){
	 Map<String, Object> response = new LinkedHashMap<>();
     List<Map<String, Object>> dataList = new ArrayList<>();

     try {
    	 
    	 AdminEntity admin= null;
    	 
    	 if(adminName != null) {
    		 admin = adminRepository.findByAdminName(adminName);
     	}
     	else if(adminId !=null) {
     		admin= adminRepository.findByAdminId(adminId);
     	}else if(emailId != null) {
     		admin = adminRepository.findByEmailId(emailId);
     	}else if(phoneNo != null) {
     		admin = adminRepository.findByPhoneNo(phoneNo);
     	}
    	 if(admin != null) {
     		Map<String,Object> AdminData = new LinkedHashMap<>();
     		AdminData.put("adminId",admin.getAdminId());
     		AdminData.put("emailId", admin.getEmailId());
     		AdminData.put("phoneNo", admin.getPhoneNo());
     		AdminData.put("adminName", admin.getAdminName());
     		
     		dataList.add(AdminData);
     		
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
     }catch (Exception e) {
     	response.put("status_code", 500);
         response.put("boolean", false);
         response.put("message", "Error fetching user data");
         
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Return a 500 Internal Server Error response for errors
     }
}
}