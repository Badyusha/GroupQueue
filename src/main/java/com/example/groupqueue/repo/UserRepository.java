package com.example.groupqueue.repo;

import com.example.groupqueue.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {}
