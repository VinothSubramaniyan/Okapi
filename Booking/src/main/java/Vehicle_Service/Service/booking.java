package Vehicle_Service.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import Vehicle_Service.Repository.BookingRepository;

public class booking {
	
	@Autowired 
	BookingRepository bookingRepository;

	public boolean isValidPhoneNumber(String s) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
