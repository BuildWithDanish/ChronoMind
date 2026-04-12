package com.danish.chronoMind.repository;

import com.danish.chronoMind.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryImpl {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").exists(true).ne(null).ne(""));
        query.addCriteria(Criteria.where("weeklyReport").is(true));

        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }

}

