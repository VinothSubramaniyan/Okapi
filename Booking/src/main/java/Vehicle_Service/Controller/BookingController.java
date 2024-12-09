package Vehicle_Service.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Vehicle_Service.DTO.BookingDto;
import Vehicle_Service.Service.BookingService;

@RestController
@RequestMapping("/VA2/Booking")
public class BookingController {

	@Autowired
	BookingService bookingService;
	
	@PostMapping(path="/serviceRegister")
	public ResponseEntity <Map<String,Object>>ServiceRegister (@RequestBody BookingDto bookingDto){
		return bookingService.bookingEntity(bookingDto);
		
	}
	
	@GetMapping(path="id/{id}")
	public ResponseEntity<Map<String,Object>>getDataById(@PathVariable Long id){
		return bookingService.getBookingDataById(id,null,null,null);
		
	}
	@GetMapping(path="service/{serviceNo}")
	public ResponseEntity<Map<String,Object>>getDataByServiceNo(@PathVariable String serviceNo){
		String phoneNo=null;
		String vehicleNo=null;
		
		return bookingService.getBookingDataByServiceNo(serviceNo,null,null);
	}	
	@GetMapping(path="vehicle/{vehicleNo}")
	public ResponseEntity<Map<String,Object>>getDataByVehicleNo(@PathVariable String vehicleNo){
		String phoneNo=null;
		String serviceNo=null;
		return bookingService.getBookingDataByVehicleNo(vehicleNo,null,null);
	}	
}
