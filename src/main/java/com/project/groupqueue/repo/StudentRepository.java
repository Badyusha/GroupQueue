package com.project.groupqueue.repo;

import com.project.groupqueue.models.dto.Student;
import com.project.groupqueue.models.entities.StudentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<StudentEntity, Long> {
	@Query(value = """
					SELECT s
					FROM StudentEntity s
					WHERE s.personEntity.username = ?1
				""")
	StudentEntity getStudentByUsername(String username);

	@Query(value = """
					SELECT s.roleId
					FROM StudentEntity s
					WHERE s.id = ?1
					""")
	long getRoleIdByStudentId(long studentId);

	@Query(value = """
					SELECT count(1)
					FROM StudentEntity s
					WHERE s.personEntity.username = ?1
				""")
	Integer usernameCountByName(String username);

	default boolean isUsernameExist(String username) {
		return usernameCountByName(username) != 0;
	}

	@Query(value = """
					SELECT s.roleEntity.name
					FROM StudentEntity s
					WHERE s.id = ?1
				""")
	String getRoleNameByStudentId(long studentId);

	default boolean isRoleMatchByStudentIdRoleName(long studentId, String roleType) {
		return getRoleNameByStudentId(studentId).equals(roleType);
	}

	@Query(value = """
			SELECT count(1)
			FROM StudentEntity s
			WHERE (s.personEntity.username = ?1 and s.personEntity.password = ?2)
			""")
	Integer studentsCountByUsernamePassword(String username, String password);

	default boolean isStudentExistByUsernamePassword(String username, String password) {
		return studentsCountByUsernamePassword(username, password) != 0;
	}

	@Query(value = """
					SELECT s.id
					FROM StudentEntity s
					WHERE s.personEntity.username = ?1
				""")
	Long getIdByUsername(String username);

	@Query(value = """
					SELECT new com.project.groupqueue.models.dto.Student(
						null,
						p.firstName,
						p.lastName,
						p.username,
						p.password,
						null,
						group.number,
						role.name
					)
					FROM StudentEntity s
					INNER JOIN PersonEntity p
						ON s.personId = p.id
					INNER JOIN GroupEntity group
						ON group.id = s.groupId
					INNER JOIN RoleEntity role
						ON role.id = s.roleId
					WHERE s.id = ?1
				""")
	Student getStudentDtoByStudentId(long studentId);

	@Query(value = """
					SELECT s.personEntity.password
					FROM StudentEntity s
					WHERE s.id = ?1
				""")
	String getPasswordByStudentId(long studentId);

	@Query(value = """
					SELECT s
					FROM StudentEntity s
					WHERE s.id = ?1
				""")
	StudentEntity getStudentEntityByStudentId(long studentId);

	@Query(value = """
					SELECT r.name
					FROM RoleEntity r
					JOIN StudentEntity s
					ON s.roleId = r.id
					WHERE s.id = ?1
				""")
	String getStudentRoleByStudentId(long studentId);
}
