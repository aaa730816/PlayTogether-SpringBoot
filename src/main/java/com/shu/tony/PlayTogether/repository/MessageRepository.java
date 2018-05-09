package com.shu.tony.PlayTogether.repository;

import com.shu.tony.PlayTogether.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message,ObjectId> {
    Page<Message> findByEventIdAndTypeEquals(String eventId, String type, Pageable pageable);

    void deleteAllByEventIdAndTypeEquals(String eventId, String type);
}
