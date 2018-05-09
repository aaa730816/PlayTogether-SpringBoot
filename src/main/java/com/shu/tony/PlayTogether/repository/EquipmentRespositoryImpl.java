package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentCriteria;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentVo;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;

@Repository
public class EquipmentRespositoryImpl {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public PageImpl<EquipmentVo> findNearest(EquipmentCriteria equipmentCriteria) {
        Query query = entityManager.createNativeQuery("select id,creator,num,type,other_type as otherType,address,title,description,tenant,cost,unit,guarantee,\n" +
                "ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((:latitude * PI() / 180 - latitude * PI() / 180) / 2), 2) + COS(:latitude * PI() / 180) * COS(latitude * PI() / 180) * POW(SIN((:longitude * PI() / 180 - longitude * PI() / 180) / 2), 2))) * 1000) \n" +
                "AS distance from equipment WHERE type=:equipmentType and isnull(tenant) ORDER BY distance asc");
        query.setParameter("latitude", Double.valueOf(equipmentCriteria.getLocation().getLatitude()));
        query.setParameter("longitude", Double.valueOf(equipmentCriteria.getLocation().getLongitude()));
        query.setParameter("equipmentType", equipmentCriteria.getType());
        Query countQuery = entityManager.createNativeQuery("select count(*) from equipment where isnull(tenant) ");
        Long count = ((BigInteger) countQuery.getSingleResult()).longValue();
        PageRequest pageRequest = new PageRequest(equipmentCriteria.getPage(), equipmentCriteria.getSize());
        query.setFirstResult(Math.toIntExact(pageRequest.getOffset()));
        query.setMaxResults(pageRequest.getPageSize());
        query=query.unwrap(SQLQuery.class).addScalar("id", StandardBasicTypes.STRING)
                .addScalar("creator", StandardBasicTypes.STRING)
                .addScalar("num", StandardBasicTypes.INTEGER)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar("otherType", StandardBasicTypes.STRING)
                .addScalar("address", StandardBasicTypes.STRING)
                .addScalar("title", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("tenant", StandardBasicTypes.STRING)
                .addScalar("cost", StandardBasicTypes.DOUBLE)
                .addScalar("unit", StandardBasicTypes.STRING)
                .addScalar("guarantee", StandardBasicTypes.DOUBLE)
                .addScalar("distance", StandardBasicTypes.DOUBLE)
                .setResultTransformer(Transformers.aliasToBean(EquipmentVo.class));
        return new PageImpl<>(query.getResultList(), pageRequest, count);
    }
}
