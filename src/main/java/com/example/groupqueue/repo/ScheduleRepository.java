package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.WeekType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<ScheduleEntity, Long> {
	@Query(value = "select count(schedule.id) " +
			"from ScheduleEntity schedule " +
			"inner join GroupEntity group " +
			"on group.id = schedule.groupEntity.id " +
			"where group.number = ?1 and schedule.weekType = ?2")
	Long getScheduleIdCountByGroupNumber(Integer groupNumber, WeekType currentWeek);

	default boolean isScheduleInDbByGroupNumber(Integer groupNumber, WeekType currentWeek) {
		return getScheduleIdCountByGroupNumber(groupNumber, currentWeek) != 0;
	}

	@Query(value = "select schedule " +
			"from ScheduleEntity schedule " +
			"inner join GroupEntity group " +
			"on group.id = schedule.groupEntity.id " +
			"where group.number = ?1 and schedule.weekType = ?2")
	List<ScheduleEntity> getScheduleEntityByGroupNumber(Integer groupId, WeekType currentWeek);
}
