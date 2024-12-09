package com.example.demo.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.VehicleDto;
import com.example.demo.Entity.VehicleEntity;
import com.example.demo.Model.VehicleModel;
import com.example.demo.Repository.VehicleRepository;

@Service
public class VehicleService {

	@Autowired
	VehicleRepository vehicleRepository;
	
	public boolean vehicleAlredyExists (String vehicleNo) {
		VehicleEntity VehicleData = vehicleRepository.findByVehicleNo(vehicleNo);
		if(VehicleData!=null) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isValidServiceNo(String serviceNo) {
        String regex = "^[0-9]{4}$";
        return serviceNo.matches(regex);
    }  
    
    public boolean isValidVehicleNo(String vehicleNo) {
        String unRegex = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$";
        Pattern unPattern = Pattern.compile(unRegex);
        Matcher unMatcher = unPattern.matcher(vehicleNo.toUpperCase());
        return unMatcher.matches();
    }
    
    public static boolean isValidVehicleModel(String model) {
        String regex = "^[A-Za-z0-9 ]{1,30}$";
        return model.matches(regex);
    }
    
    public static boolean isValidVehicleType(String vehicleType) {
        String regex = "^[A-Za-z0-9 ]{1,30}$";
        return vehicleType.matches(regex);
    }
    
    public static boolean isValidBrand(String brand) {
        String regex = "^[A-Za-z0-9 ]{1,30}$";
        return brand.matches(regex);
    }
    
    public ResponseEntity <Map<String, Object>> VehicleRegister (VehicleDto vehicleDto){
		Map<String,Object> response = new LinkedHashMap<>();
    	
    	List<Map<String,Object>>dataList= new ArrayList<>();
    	
    	String vehicleNo = vehicleDto.getVehicleNo();
    	String serviceNo = vehicleDto.getServiceNo();
    	String vehicleType = vehicleDto.getVehicleType();
    	String model = vehicleDto.getModel();
    	String brand = vehicleDto.getBrand();
    	
    	Map<String,Object>VehicleData = new LinkedHashMap<>();
    	
    	if(vehicleAlredyExists(vehicleNo)) {
    		
    		VehicleData.put("status_code", 400);
    		VehicleData.put("boolean",false);
    		VehicleData.put("message", "vehicleAlredyExists");
    		dataList.add(VehicleData);
    		response.put("data",dataList);
    		response.put("meta", new LinkedHashMap<>());
    		response.put("pagination",new LinkedHashMap<>());
    		
    	}else if(!isValidServiceNo(serviceNo)) {
    		
    		VehicleData.put("status_code", 400);
    		VehicleData.put("boolean",false);
    		VehicleData.put("message", "invalid Service No");
    		dataList.add(VehicleData);
    		response.put("data",dataList);
    		response.put("meta", new LinkedHashMap<>());
    		response.put("pagination",new LinkedHashMap<>());
    		
    	}else if(!isValidVehicleModel(model)) {
    		
    		VehicleData.put("status_code", 400);
    		VehicleData.put("boolean",false);
    		VehicleData.put("message", "vehicleAlredyExists");
    		dataList.add(VehicleData);
    		response.put("data",dataList);
    		response.put("meta", new LinkedHashMap<>());
    		response.put("pagination",new LinkedHashMap<>());
    		
    	}else if(!isValidVehicleNo(vehicleNo)) {
    		
    		VehicleData.put("status_code", 400);
    		VehicleData.put("boolean",false);
    		VehicleData.put("message", "vehicleAlredyExists");
    		dataList.add(VehicleData);
    		response.put("data",dataList);
    		response.put("meta", new LinkedHashMap<>());
    		response.put("pagination",new LinkedHashMap<>());
    		
    	}else if(!isValidVehicleType(vehicleType)) {
    		
    		VehicleData.put("status_code", 400);
    		VehicleData.put("boolean",false);
    		VehicleData.put("message", "vehicleAlredyExists");
    		dataList.add(VehicleData);
    		response.put("data",dataList);
    		response.put("meta", new LinkedHashMap<>());
    		response.put("pagination",new LinkedHashMap<>());
    		
    	}else if(!isValidBrand(brand)) {
    		
    		VehicleData.put("status_code", 400);
    		VehicleData.put("boolean",false);
    		VehicleData.put("message", "vehicleAlredyExists");
    		dataList.add(VehicleData);
    		response.put("data",dataList);
    		response.put("meta", new LinkedHashMap<>());
    		response.put("pagination",new LinkedHashMap<>());
    		
    	}else {
    		VehicleEntity Details = new VehicleEntity();
    		Details.setServiceNo(serviceNo);
    		Details.setVehicleNo(vehicleNo);
    		Details.setVehicleType(vehicleType);
    		Details.setModel(model);
    		Details.setBrand(brand);
    		vehicleRepository.save(Details);
    		
    		VehicleData.put("serviceNo", Details.getServiceNo());
    		VehicleData.put("vehicleNo", Details.getVehicleNo());
    		VehicleData.put("vehicleType", Details.getVehicleType());
    		VehicleData.put("model", Details.getModel());
    		VehicleData.put("brand", Details.getBrand());
    		VehicleData.put("boolean", true);
    		dataList.add(VehicleData);
    		
    		VehicleModel vehicleModel = new VehicleModel();
    		vehicleModel.setServiceNo(serviceNo);
    		vehicleModel.setVehicleNo(vehicleNo);
    		vehicleModel.setVehicleType(vehicleType);
    		vehicleModel.setModel(model);
    		vehicleModel.setBrand(brand);
    		
    		Map<String,Object> meta = new LinkedHashMap<>();
    		meta.put("status_code", 200);
    		meta.put("message", "Vehical Details uploaded successfully");
    		
    		response.put("data",dataList);
    		response.put("meta", meta);
    		response.put("pagination", new LinkedHashMap<>());
    		
    	}
    	
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    public ResponseEntity <Map<String,Object>> getDetailsById(Long id, String vehicleNo, String vehicleType, String model, String brand, String serviceNo){
		Map<String,Object> response = new LinkedHashMap<>();
		List<Map<String,Object>> dataList = new ArrayList<>();
		
    	try {
    		VehicleEntity Details = null;
    		
    		if(id>0) {
    			
    			Details = vehicleRepository.findById((Long)id).orElse(null);	
    		}else if (vehicleNo != null){
    			Details = vehicleRepository.findByVehicleNo(vehicleNo);
    		}else if (serviceNo != null) {
    			Details = vehicleRepository.findByServiceNo(serviceNo);
    		}else if (model != null) {
    			Details = vehicleRepository.findByModel(model);
    		}
    		
    		if(Details != null) {
    			Map<String, Object> VehicleData = new LinkedHashMap<>();
    			VehicleData.put("Id",Details.getId());
    			VehicleData.put("ServiceNo", Details.getServiceNo());
    			VehicleData.put("VehicleNo", Details.getVehicleNo());
    			VehicleData.put("Model", Details.getModel());
    			VehicleData.put("vehicleType", Details.getVehicleType());
    			VehicleData.put("brand", Details.getBrand());
    			
    			dataList.add(VehicleData);
    			Map<String, Object> meta = new LinkedHashMap<>();
    			meta.put("status_code", 200);
    			meta.put("message", "Successfully retrieved");
    			
    			response.put("Data",dataList);
    			response.put("meta",meta);
    			response.put("pagination", new LinkedHashMap<>());
    			return ResponseEntity.ok(response);
    		}else {
    			response.put("status_code",404);
    			response.put("boolean",false);
    			response.put("message", "VehicleData not found");
    			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    		}
    		
    	}catch(Exception e) {
    		response.put("status_code",404);
			response.put("boolean",false);
			response.put("message", "Error Fetching VehicleData");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    	}
    }
    public ResponseEntity<Map<String, Object>> getDetailsByVehicleNo(String vehicleNo, String serviceNo, String model, String brand, String vehicleType){
		Map<String,Object> response = new LinkedHashMap<>();
		List<Map<String,Object>> dataList = new ArrayList<>();
		
		try {
			VehicleEntity Details = null;
			
			if(vehicleNo!=null) {
				Details = vehicleRepository.findByVehicleNo(vehicleNo);
			}else if(serviceNo!= null) {
				Details = vehicleRepository.findByServiceNo(serviceNo);
			}else if (model != null) {
				Details = vehicleRepository.findByModel(model);
			}
			
			if(Details != null) {
				Map<String, Object> VehicleData = new LinkedHashMap<>();
				VehicleData.put("serviceNo", Details.getServiceNo());
				VehicleData.put("vehicleNo", Details.getVehicleNo());
				VehicleData.put("model", Details.getModel());
				VehicleData.put("vehicleType", Details.getVehicleType());
    			VehicleData.put("brand", Details.getBrand());
				dataList.add(VehicleData);
				Map<String,Object> meta = new LinkedHashMap<>();
				meta.put("status_code",200);
				meta.put("message", "Successfully Retrieved");
				response.put("data", dataList);
				response.put("meta", meta);
				response.put("pagination", new LinkedHashMap<>());
				return ResponseEntity.ok(response);
			}else {
    			response.put("status_code",404);
    			response.put("boolean",false);
    			response.put("message", "VehicleData not found");
    			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    		}
			
		}catch(Exception e) {
    		response.put("status_code",404);
			response.put("boolean",false);
			response.put("message", "Error Fetching VehicleData");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    	}
    	
    	
    }
    
    public ResponseEntity<Map<String, Object>> getDetailsByModel(String model, String serviceNo, String vehicleNo, String brand, String vehicleType){
		Map<String,Object> response = new LinkedHashMap<>();
		System.out.println(model);
		List<Map<String,Object>> dataList = new ArrayList<>();
		
		try {
			VehicleEntity Details = null;
			
			if (model != null) {
				Details = vehicleRepository.findByModel(model);
			}else if(vehicleNo!=null) {
				Details = vehicleRepository.findByVehicleNo(vehicleNo);
			}else if(serviceNo!= null) {
				Details = vehicleRepository.findByServiceNo(serviceNo);
			}
			
			if(Details != null) {
				Map<String, Object> VehicleData = new LinkedHashMap<>();
				VehicleData.put("serviceNo", Details.getServiceNo());
				VehicleData.put("vehicleNo", Details.getVehicleNo());
				VehicleData.put("model", Details.getModel());
				VehicleData.put("vehicleType", Details.getVehicleType());
    			VehicleData.put("brand", Details.getBrand());
				dataList.add(VehicleData);
				Map<String,Object> meta = new LinkedHashMap<>();
				meta.put("status_code",200);
				meta.put("message", "Successfully Retrieved");
				response.put("data", dataList);
				response.put("meta", meta);
				response.put("pagination", new LinkedHashMap<>());
				return ResponseEntity.ok(response);
			}else {
    			response.put("status_code",404);
    			response.put("boolean",false);
    			response.put("message", "VehicleData not found");
    			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    		}
			
		}catch(Exception e) {
    		response.put("status_code",404);
			response.put("boolean",false);
			response.put("message", "Error Fetching VehicleData");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    	}
    	
    	
    }
    public ResponseEntity<Map<String, Object>> getDetailsByServiceNo(String serviceNo , String vehicleNo, String model, String brand, String vehicleType){
		Map<String,Object> response = new LinkedHashMap<>();
		List<Map<String,Object>> dataList = new ArrayList<>();
		
		try {
			VehicleEntity Details = null;
			if(serviceNo!= null) {
				Details = vehicleRepository.findByServiceNo(serviceNo);
			}else if(vehicleNo!=null) {
				Details = vehicleRepository.findByVehicleNo(vehicleNo);
			}else if (model != null) {
				Details = vehicleRepository.findByModel(model);
			}
			
			if(Details != null) {
				Map<String, Object> VehicleData = new LinkedHashMap<>();
				VehicleData.put("serviceNo", Details.getServiceNo());
				VehicleData.put("vehicleNo", Details.getVehicleNo());
				VehicleData.put("model", Details.getModel());
				VehicleData.put("vehicleType", Details.getVehicleType());
    			VehicleData.put("brand", Details.getBrand());
				dataList.add(VehicleData);
				Map<String,Object> meta = new LinkedHashMap<>();
				meta.put("status_code",200);
				meta.put("message", "Successfully Retrieved");
				response.put("data", dataList);
				response.put("meta", meta);
				response.put("pagination", new LinkedHashMap<>());
				return ResponseEntity.ok(response);
			}else {
    			response.put("status_code",404);
    			response.put("boolean",false);
    			response.put("message", "VehicleData not found");
    			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    		}
			
		}catch(Exception e) {
    		response.put("status_code",404);
			response.put("boolean",false);
			response.put("message", "Error Fetching VehicleData");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    	}
    	
    	
    }
    
}
