package com.mukham.sendemail.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mukham.sendemail.model.Mail;

import java.sql.Timestamp;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {

    @Query(nativeQuery = true, value =" SELECT * FROM mail m WHERE m.otp= :otp and m.created_date >=:t1 and m.created_date <=:t2 order by id desc limit 1")
    Mail findByOtp(@Param("otp") String otp, @Param("t1") Timestamp t1, @Param("t2")Timestamp t2);


}
