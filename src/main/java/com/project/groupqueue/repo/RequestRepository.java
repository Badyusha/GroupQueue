package com.project.groupqueue.repo;

import com.project.groupqueue.models.dto.Request;
import com.project.groupqueue.models.entities.RequestEntity;
import com.project.groupqueue.models.enums.RequestType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<RequestEntity, Long> {
	@Query(value = """
					SELECT new com.project.groupqueue.models.dto.Request(
					s.id,
					s.lastName,
					s.firstName,
					s.username,
					role.name,
					g.number,
					r.requestType)
					FROM RequestEntity r
					INNER JOIN StudentEntity s
					ON s.id = r.studentId
					INNER JOIN GroupEntity g
					ON g.id = s.groupId
					INNER JOIN RoleEntity role
					ON s.roleId = role.id
				""")
	List<Request> getRequests();

	@Query(value = """
					SELECT r.id
					FROM RequestEntity r
					WHERE r.requestType = ?1 AND r.studentId = ?2
					""")
	long getRequestIdByRequestTypeStudentId(RequestType requestType, long studentId);
}
