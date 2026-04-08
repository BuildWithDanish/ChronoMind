package com.danish.journalApp.repository;

import com.danish.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryImpl {

    Query query = new Query();

    @Autowired
    MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {
        query.addCriteria(Criteria.where("email").exists(true));
        query.addCriteria(Criteria.where("weeklyReport").exists(true));

        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }

}

