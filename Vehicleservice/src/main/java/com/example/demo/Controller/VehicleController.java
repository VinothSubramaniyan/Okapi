package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.VehicleDto;
import com.example.demo.Service.VehicleService;

@RestController
@RequestMapping("/api/Vehicle")
public class VehicleController {

	@Autowired
	VehicleService vehicleService;
	
	@PostMapping(path="/VehicleData")
	public ResponseEntity<Map<String,Object>>VehicleRegister(@RequestBody VehicleDto vehicleDto){	
		return vehicleService.VehicleRegister(vehicleDto);	
	}
	
	@GetMapping (path="/{id}")
	public ResponseEntity <Map<String, Object>>getDetailsById(@PathVariable Long id){
		return vehicleService.getDetailsById(id, null, null, null, null, null);
		
	}
	@GetMapping (path="/vehicleNo/{vehicleNo}")
	public ResponseEntity <Map<String, Object>> getDetailsByVehicleNo (@PathVariable String vehicleNo){
		
		String serviceNo = null;
		String vehicleType = null;
		String model = null;
		String brand = null;
		
		return vehicleService.getDetailsByVehicleNo(vehicleNo, serviceNo, model, vehicleType, brand);
	}	
	@GetMapping(path="/model/{model}")
	public ResponseEntity <Map<String,Object>> getDetailsByModel (@PathVariable String model){
		 System.out.println("Model received: " + model);
		String vehicleNo=null;
		String serviceNo=null;
		String vehicleType=null;
		String brand=null;
		return vehicleService.getDetailsByModel(model, vehicleNo, serviceNo, vehicleType, brand);
	
	}
	@GetMapping(path="/serviceNo/{serviceNo}")
	public ResponseEntity <Map<String,Object>> getDetailsByServiceNo (@PathVariable String serviceNo){
		
		String vehicleNo = null;
		String model = null;
		String vehicleType = null;
		String brand = null;
		
		return vehicleService.getDetailsByServiceNo(serviceNo, vehicleNo, model, vehicleType, brand);
	}

}
