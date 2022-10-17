package com.mms.thp.service.impl;

import com.mms.thp.model.Medicine;
import com.mms.thp.model.Order;
import com.mms.thp.repository.MedicineRepository;
import com.mms.thp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Shubhasish
 */
@Service
public class OrderServiceImpl implements OrderService {

    private Order currentOrderContext;

    private MedicineRepository medicineRepository;

    @Override
    public void initiatePurchaseContext(String customerName) {
        currentOrderContext = Order.prepareOrderBucket(customerName);
    }

    @Override
    public boolean isPurchaseActive() {
        return Order.isPurchaseActive();
    }

    @Override
    public void destroyPurchaseSession() {
        Order.destroyPurchaseSession();
    }

    @Override
    public void addToOrder(long medicineId) {
        if(Objects.nonNull(currentOrderContext)) {
            Optional<Medicine> theMedicine = medicineRepository.findById(medicineId);
            theMedicine.ifPresent(medicine -> currentOrderContext.getMedicines().add(medicine));
        }
    }

    public Order getCurrentOrderContext(){
        return currentOrderContext;
    }

    @Autowired
    public void setMedicineRepository(final MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }
}
