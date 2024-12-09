package Vehicle_Service.Controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
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

import Vehicle_Service.DTO.AdminDto;
import Vehicle_Service.DTO.LoginDto;
import Vehicle_Service.Service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/a1/api")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	
	@PostMapping(path="/register")
	public ResponseEntity<Map<String, Object>> registerAdmin (@RequestBody AdminDto adminDto){
		return adminService.AdminRegister(adminDto);
	}
	@PostMapping(path="/login")
	public ResponseEntity<Map<String, Object>> AdminLogin (@RequestBody LoginDto loginDto){
		return adminService.Adminlogin(loginDto);
	}
	@GetMapping(path="/id/{id}")
	public ResponseEntity<Map<String,Object>> getAdminData(@PathVariable Long id){
		return adminService.getAdminData(id, null, null, null, null);
	}
	@GetMapping(path="/admin/{adminId}")
	public ResponseEntity<Map<String,Object>> getAdminDataByAdminId (@PathVariable String adminId){
		
		String adminName = null;
		String emailId = null;
		String phoneNo = null;
		
		return adminService.getAdminDataByAdminId(adminId, emailId, adminName,phoneNo);
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
