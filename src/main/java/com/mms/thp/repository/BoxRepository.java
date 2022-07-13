package com.mms.thp.repository;

import com.mms.thp.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
    //TODO Extra logic to do db operation
    //TODO Not in use as of now

    Box findByNumberAndName(int number, String name);
    Box findByNumber(String number);
}
