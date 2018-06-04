package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.nonentity.activity.ActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
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
public class ActivityRepositoryImpl {
    @Autowired
    private EntityManager entityManager;
    @Transactional
    public PageImpl<ActivityVo> getNearest(ActivityCriteria activityCriteria) {
        Query query = entityManager.createNativeQuery("SELECT id,title,address, start_time as startTime,cost,num_of_people as numOfPeople,need_bring_equipment as needBringEquipment," +
                "description,type,creator,game,\n" +
                "ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((:latidude * PI() / 180 - latitude * PI() / 180) / 2), 2) + COS(:latidude * PI() / 180) * COS(latitude * PI() / 180) * POW(SIN((:longitude * PI() / 180 - longitude * PI() / 180) / 2), 2))) * 1000) \n" +
                "AS distance FROM activity where type=:activityType and num_of_people>(select COUNT(*) from activity_user WHERE activity_id=id) ORDER BY distance ASC");
        query.setParameter("latidude", Double.valueOf(activityCriteria.getLocation().getLatitude()));
        query.setParameter("longitude", Double.valueOf(activityCriteria.getLocation().getLongitude()));
        query.setParameter("activityType", activityCriteria.getType());
        Query countQuery = entityManager.createNativeQuery("SELECT count(*) from activity where type=:activityType and num_of_people>(select COUNT(*) from activity_user WHERE activity_id=id)");
        countQuery.setParameter("activityType", activityCriteria.getType());
        Long count = ((BigInteger)countQuery.getSingleResult()).longValue();
        PageRequest pageRequest = new PageRequest(activityCriteria.getPage(), activityCriteria.getSize());
        query=query.unwrap(SQLQuery.class).addScalar("id", StandardBasicTypes.LONG)
                .addScalar("title", StandardBasicTypes.STRING)
                .addScalar("startTime", StandardBasicTypes.TIMESTAMP)
                .addScalar("cost", StandardBasicTypes.DOUBLE)
                .addScalar("numOfPeople", StandardBasicTypes.INTEGER)
                .addScalar("needBringEquipment", StandardBasicTypes.BOOLEAN)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar("distance", StandardBasicTypes.DOUBLE)
                .addScalar("address",StandardBasicTypes.STRING)
                .addScalar("creator",StandardBasicTypes.STRING)
                .addScalar("game",StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(ActivityVo.class));
        query.setFirstResult(Math.toIntExact(pageRequest.getOffset()));
        query.setMaxResults(pageRequest.getPageSize());
        PageImpl<ActivityVo> result = new PageImpl<>(query.getResultList(), pageRequest, count);
        return result;
    }
}
