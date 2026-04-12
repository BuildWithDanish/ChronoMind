package com.danish.chronoMind.repository;

import com.danish.chronoMind.entity.ConfigJournalAppEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppConfigRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId> {

}
