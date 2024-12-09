package Vehicle_Service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Vehicle_Service.Entity.BookingEntity;


@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,Long>{

	BookingEntity findByServiceNo(String serviceNo);
	BookingEntity findByPhoneNo(String phoneNo);
	BookingEntity findByVehicleNo(String vehicleNo);

}
