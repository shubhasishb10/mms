package com.mms.thp.repository;

import com.mms.thp.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long>, MedicineExtendedRepository {

    //TODO extra logic to do db operation
    List<Medicine> findMedicineByName(String name);
    List<Medicine> findMedicineByCompanyStringName(String company);
    List<Medicine> findMedicineByNameStartingWith(String name);
    //List<Medicine> findMedicineByBoxNumber(int boxNumber);
    Medicine findMedicineByNameAndCompanyStringNameAndVolume(String name, String company, int volume);
}
