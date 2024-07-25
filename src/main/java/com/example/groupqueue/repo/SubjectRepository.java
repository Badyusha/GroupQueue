package com.example.groupqueue.repo;

import com.example.groupqueue.models.Schedule;
import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends CrudRepository<Schedule, Long> {

}
