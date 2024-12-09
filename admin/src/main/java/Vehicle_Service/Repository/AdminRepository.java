package Vehicle_Service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Vehicle_Service.Entity.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity,Long>{

	AdminEntity findByAdminId (String adminId);
	AdminEntity findByEmailId (String emailId);
	AdminEntity findByPhoneNo(String phoneNo);
	AdminEntity findByAdminName(String adminName);
}
