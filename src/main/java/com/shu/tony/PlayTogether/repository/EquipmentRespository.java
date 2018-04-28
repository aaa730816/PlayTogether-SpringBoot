package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRespository extends JpaRepository<Equipment,Long> {
}
