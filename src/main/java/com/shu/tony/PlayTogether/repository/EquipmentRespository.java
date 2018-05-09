package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRespository extends JpaRepository<Equipment, Long> {
    Page<Equipment> findByLocationGeoCodeInAndTypeEqualsAndTenantIsNull(List<String> geoCodes, String type, Pageable pageable);

    Page<Equipment> findAllByTenantIsNull(Pageable pageable);
    List<Equipment> findByCreatorEquals(String userId);
}
