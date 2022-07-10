package com.example.demo.repository;

import com.example.demo.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    //TODO extra logic to do db operation
    List<Medicine> findMedicineByName(String name);
    List<Medicine> findMedicineByCompany(String company);
    List<Medicine> findMedicineByNameStartingWith(String name);
    List<Medicine> findMedicineByBoxNumber(int boxNumber);
    @Modifying
    @Query("update Medicine m set m.sellDate = :date where m.medicineId = :medicineId")
    int sellMedicineOnDate(@Param("date") Date date, @Param("medicineId") long medicineId);
}
