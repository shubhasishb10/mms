package com.mms.thp.repository;

import com.mms.thp.model.Box;
import com.mms.thp.model.Medicine;
import com.mms.thp.model.MedicineBoxes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineBoxRepository extends JpaRepository<MedicineBoxes, Long> {
    MedicineBoxes findMedicineBoxesByMedicineAndBox(Medicine medicine, Box box);
}
