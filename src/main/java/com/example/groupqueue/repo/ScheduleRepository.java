package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.WeekType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<ScheduleEntity, Long> {
	@Query(value = "SELECT COUNT(schedule.id) " +
			"FROM ScheduleEntity schedule " +
			"INNER JOIN GroupEntity group " +
			"ON group.id = schedule.groupEntity.id " +
			"WHERE group.number = ?1 and schedule.weekType = ?2 " +
			"ORDER BY schedule.startTime ASC")
	Long getScheduleIdCountByGroupNumber(int groupNumber, WeekType currentWeek);

	default boolean isScheduleExistByGroupNumber(int groupNumber, WeekType currentWeek) {
		return getScheduleIdCountByGroupNumber(groupNumber, currentWeek) != 0;
	}

	@Query(value = "select schedule " +
			"from ScheduleEntity schedule " +
			"inner join GroupEntity group " +
			"on group.id = schedule.groupId " +
			"where group.number = ?1 and schedule.weekType = ?2")
	List<ScheduleEntity> getScheduleEntityByGroupNumberWeekType(long groupId, WeekType week);
}
