package com.example.groupqueue.repo;

import com.example.groupqueue.models.dto.Request;
import com.example.groupqueue.models.entities.RequestEntity;
import com.example.groupqueue.models.enums.RequestType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<RequestEntity, Long> {
	@Query(value = """
					SELECT new com.example.groupqueue.models.dto.Request(
					u.id,
					u.lastName,
					u.firstName,
					u.username,
					role.name,
					g.number,
					r.requestType)
					FROM RequestEntity r
					INNER JOIN UserEntity u
					ON u.id = r.userId
					INNER JOIN GroupEntity g
					ON g.id = u.groupId
					INNER JOIN RoleEntity role
					ON u.roleId = role.id
				""")
	List<Request> getRequests();

	@Query(value = """
					SELECT r.id
					FROM RequestEntity r
					WHERE r.requestType = ?1 AND r.userId = ?2
					""")
	long getRequestIdByRequestTypeUserId(RequestType requestType, long userId);
}
