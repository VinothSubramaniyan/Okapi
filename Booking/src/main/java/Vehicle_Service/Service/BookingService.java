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
import org.springframework.stereotype.Service;

import Vehicle_Service.DTO.BookingDto;
import Vehicle_Service.Entity.BookingEntity;
import Vehicle_Service.Model.BookingModel;
import Vehicle_Service.Repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    
   
    public boolean isValidPhoneNumber(String s) {
    	System.out.println(s);
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
    public static boolean isValidVehicleNumber(String vehicleNo) {
        String regex = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$";
        return vehicleNo.matches(regex);
    }

    public static boolean isValidVehicleModel(String model) {
        String regex = "^[A-Za-z0-9 ]{1,30}$";
        return model.matches(regex);
    }

    public ResponseEntity<Map<String, Object>> bookingEntity(BookingDto bookingDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        
        System.out.println(bookingDto.getPhoneNo());
        List<Map<String, Object>> dataList = new ArrayList<>();

        String vehicleNo = bookingDto.getVehicleNo();
        String phoneNo = bookingDto.getPhoneNo();
        String model = bookingDto.getModel();

        Map<String, Object> BookingData = new LinkedHashMap<>();

        if (!isValidPhoneNumber(phoneNo)) {
            BookingData.put("status_code", 400);
            BookingData.put("boolean", false);
            BookingData.put("message", "Invalid phone number");
            dataList.add(BookingData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
        }else if (!isValidVehicleNumber(vehicleNo)) {
            BookingData.put("status_code", 400);
            BookingData.put("boolean", false);
            BookingData.put("message", "Invalid vehicle number");
            dataList.add(BookingData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());    
        }else if (!isValidVehicleModel(model)) {
            BookingData.put("status_code", 400);
            BookingData.put("boolean", false);
            BookingData.put("message", "Invalid vehicle model");
            dataList.add(BookingData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
        } else {
        BookingEntity booking = new BookingEntity();
        booking.setPhoneNo(phoneNo);
        booking.setVehicleNo(vehicleNo);
        booking.setModel(model);
        booking.setServiceNo(generatedServiceNo());
        bookingRepository.save(booking);

        BookingData.put("serviceNo", booking.getServiceNo());
        BookingData.put("vehicleNo", booking.getVehicleNo());
        BookingData.put("Model", booking.getModel());
        BookingData.put("boolean", true);
        dataList.add(BookingData);

        BookingModel bookingModel = new BookingModel();
        bookingModel.setPhoneNo(bookingDto.getPhoneNo());
        bookingModel.setVehicleNo(bookingDto.getVehicleNo());
        bookingModel.setModel(bookingDto.getModel());

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("status_code", 200);
        meta.put("message", "Booking confirmed");

        response.put("data", dataList);
        response.put("meta", meta);
        response.put("pagination", new LinkedHashMap<>());
        
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private static String generatedServiceNo() {
        String Characters = "0123456789";
        StringBuilder ServiceNo = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = (int) (Math.random() * Characters.length());
            ServiceNo.append(Characters.charAt(index));
        }
        return ServiceNo.toString();
    }

    public ResponseEntity<Map<String, Object>> getBookingDataById(Long id, String serviceNo, String phoneNo, String vehicleNo) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            BookingEntity booking = null;

            if ( id > 0) {
                booking = bookingRepository.findById((Long)id).orElse(null);
            } else if (vehicleNo != null) {
                booking = bookingRepository.findByVehicleNo(vehicleNo);
            } else if (serviceNo != null) {
                booking = bookingRepository.findByServiceNo(serviceNo);
            } else if (phoneNo != null) {
            	booking = bookingRepository.findByPhoneNo(phoneNo);
            }

            if (booking != null) {
                Map<String, Object> BookingData = new LinkedHashMap<>();
                BookingData.put("Id", booking.getId());
                BookingData.put("serviceNo", booking.getServiceNo());
                BookingData.put("vehicleNo", booking.getVehicleNo());
                BookingData.put("phoneNo", booking.getPhoneNo());

                dataList.add(BookingData);
                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("status_code", 200);
                meta.put("message", "Successfully retrieved");

                response.put("data", dataList);
                response.put("meta", meta);
                response.put("pagination", new LinkedHashMap<>());
                return ResponseEntity.ok(response);
            } else {
                response.put("status_code", 404);
                response.put("boolean", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status_code", 500);
            response.put("boolean", false);
            response.put("message", "Error fetching user data");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getBookingDataByVehicleNo(String vehicleNo, String phoneNo , String serviceNo) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            BookingEntity booking = null;

            // Validate vehicleNo before searching
            if (vehicleNo != null) {
                booking = bookingRepository.findByVehicleNo(vehicleNo);
            } else if (serviceNo != null) {
                booking = bookingRepository.findByServiceNo(serviceNo);
            } else if (phoneNo != null) {
                booking = bookingRepository.findByPhoneNo(phoneNo);	
            }

            if (booking != null) {
                Map<String, Object> BookingData = new LinkedHashMap<>();
                BookingData.put("serviceNo", booking.getServiceNo());
                BookingData.put("vehicleNo", booking.getVehicleNo());
                BookingData.put("phoneNo", booking.getPhoneNo());

                dataList.add(BookingData);
                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("status_code", 200);
                meta.put("message", "Successfully retrieved");

                response.put("data", dataList);
                response.put("meta", meta);
                response.put("pagination", new LinkedHashMap<>());
                return ResponseEntity.ok(response);
            } else {
                response.put("status_code", 404);
                response.put("boolean", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status_code", 500);
            response.put("boolean", false);
            response.put("message", "Error fetching user data");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getBookingDataByServiceNo(String serviceNo, String phoneNo, String vehicleNo) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            BookingEntity booking = null;

            if (serviceNo != null ) {
                booking = bookingRepository.findByServiceNo(serviceNo);
            } else if (vehicleNo != null) {
                booking = bookingRepository.findByVehicleNo(vehicleNo);
            } else if (phoneNo != null) {
                booking = bookingRepository.findByPhoneNo(phoneNo);	
            }

            if (booking != null) {
                Map<String, Object> BookingData = new LinkedHashMap<>();
                BookingData.put("serviceNo", booking.getServiceNo());
                BookingData.put("vehicleNo", booking.getVehicleNo());
                BookingData.put("phoneNo", booking.getPhoneNo());

                dataList.add(BookingData);
                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("status_code", 200);
                meta.put("message", "Successfully retrieved");

                response.put("data", dataList);
                response.put("meta", meta);
                response.put("pagination", new LinkedHashMap<>());
                return ResponseEntity.ok(response);
            } else {
                response.put("status_code", 404);
                response.put("boolean", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status_code", 500);
            response.put("boolean", false);
            response.put("message", "Error fetching user data");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
