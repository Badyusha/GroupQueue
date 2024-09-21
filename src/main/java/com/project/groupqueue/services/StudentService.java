package com.project.groupqueue.services;

import com.project.groupqueue.models.dto.Student;
import com.project.groupqueue.models.entities.PersonEntity;
import com.project.groupqueue.models.entities.StudentEntity;
import com.project.groupqueue.models.enums.RoleType;
import com.project.groupqueue.repo.PersonRepository;
import com.project.groupqueue.repo.StudentRepository;
import com.project.groupqueue.utils.CookieUtil;
import com.project.groupqueue.utils.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
	private final PersonRepository personRepository;
	private final StudentRepository studentRepository;
	private final RoleService roleService;

	public long getRoleIdByStudentId(long studentId) {
		return studentRepository.getRoleIdByStudentId(studentId);
	}

	public void updateStudentRoleType(long studentId) {
		StudentEntity student = studentRepository.getStudentEntityByStudentId(studentId);
		long roleId = roleService.getRoleIdByType(RoleType.GROUP_ADMIN);
		student.setRoleId(roleId);
		studentRepository.save(student);
	}

	public boolean isItStudentRole(HttpServletRequest request, String roleType) {
		long studentId = CookieUtil.getStudentId(request);
		return studentRepository.isRoleMatchByStudentIdRoleName(studentId, roleType);
	}

	public void saveStudent(Student student) {
		long roleId = roleService.getRoleIdByType(RoleType.USER);
		student.setRoleId(roleId);
		Long personId = personRepository.save(new PersonEntity(student)).getId();
		studentRepository.save(student.toStudentEntity(personId));
	}

	public boolean isUsernameExist(String username) {
		return studentRepository.isUsernameExist(username);
	}

	public boolean isPasswordMatches(HttpServletRequest request, String password) {
		String hashedPassword = EncryptionUtil.hashData(password);
		String studentPassword = studentRepository.getPasswordByStudentId(CookieUtil.getStudentId(request));
		return hashedPassword.equals(studentPassword);
	}

	public Long getStudentIdByStudent(Student student) {
		return studentRepository.getIdByUsername(student.getUsername());
	}

	public boolean isStudentExistByUsernamePassword(Student student) {
		return studentRepository.isStudentExistByUsernamePassword(student.getUsername(),
															EncryptionUtil.hashData(student.getPassword()));
	}

	public void fillInStudent(Student student) {
		StudentEntity studentEntity = studentRepository.getStudentByUsername(student.getUsername());
		student.copyFromStudentEntity(studentEntity);
	}

	public Student getStudentInfo(HttpServletRequest request) {
		long studentId = CookieUtil.getStudentId(request);
		return studentRepository.getStudentDtoByStudentId(studentId);
	}

	public void editProfile(HttpServletRequest request, Student student) {
		long studentId = CookieUtil.getStudentId(request);
		PersonEntity personEntity = personRepository.getPersonByStudentId(studentId);

		fillInPerson(personEntity, student);

		if(!student.getPassword().isEmpty()) {
			personEntity.setPassword(EncryptionUtil.hashData(student.getPassword()));
		}

		personRepository.save(personEntity);
	}

	public void deleteStudentByStudentId(HttpServletRequest request) {
		long studentId = CookieUtil.getStudentId(request);
		studentRepository.deleteById(studentId);
	}

	public String getStudentRoleByStudentId(HttpServletRequest request) {
		long studentId = CookieUtil.getStudentId(request);
		return studentRepository.getStudentRoleByStudentId(studentId);
	}

	private void fillInPerson(PersonEntity personEntity, Student student) {
		personEntity.setUsername(student.getUsername());
		personEntity.setFirstName(student.getFirstName());
		personEntity.setLastName(student.getLastName());
	}

}
