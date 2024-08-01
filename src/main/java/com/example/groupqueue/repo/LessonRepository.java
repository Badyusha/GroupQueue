package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.LessonEntity;
import org.springframework.data.repository.CrudRepository;

public interface LessonRepository extends CrudRepository<LessonEntity, Long> {
}
