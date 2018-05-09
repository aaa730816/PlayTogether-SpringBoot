package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.entity.Activity;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findByTypeEqualsAndLocationGeoCodeIn(String type, List<String> getCodes, Pageable pageable);

    Page<Activity> findAllByType(String type,Pageable pageable);
    List<Activity> findByCreator(String userId);
    @Query(value = "SELECT * FROM Activity ORDER BY ROUND(6378.138 * 2 \" +\n" +
            "            \"* ASIN(SQRT(POW(SIN((?1 * PI() / 180 - latitude * PI() / 180) / 2), 2) \" +\n" +
            "            \"+ COS(?1 * PI() / 180) * COS(latitude * PI() / 180) * POW(SIN((?2 * PI() / 180 \" +\n" +
            "            \"- longitude * PI() / 180) / 2), 2))) * 1000) ASC", nativeQuery = true)
    List<ActivityVo> findNearest(Double latitude, Double longitude);
}
