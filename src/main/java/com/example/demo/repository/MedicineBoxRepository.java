package com.example.demo.repository;

import com.example.demo.model.Box;
import com.example.demo.model.Medicine;
import com.example.demo.model.MedicineBoxes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineBoxRepository extends JpaRepository<MedicineBoxes, Long> {
    MedicineBoxes findMedicineBoxesByMedicineAndBox(Medicine medicine, Box box);
}
