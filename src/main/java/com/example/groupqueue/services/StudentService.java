package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.Student;
import com.example.groupqueue.models.entities.StudentEntity;
import com.example.groupqueue.models.enums.RoleType;
import com.example.groupqueue.repo.StudentRepository;
import com.example.groupqueue.utils.CookieUtil;
import com.example.groupqueue.utils.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
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
		studentRepository.save(student.toStudentEntity());
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

		List<Object[]> studentList = studentRepository.getStudentInfoByStudentId(studentId);
		Object[] student = studentList.getFirst();

		String username = (String) student[0];
		String firstName = (String) student[1];
		String lastName = (String) student[2];
		RoleType roleType = (RoleType) student[3];
		Integer groupNumber = (Integer) student[4];
		String password = (String) student[5];

		return new Student(
				null,
				firstName,
				lastName,
				username,
				password,
				null,
				groupNumber,
				roleType
		);
	}

	public void editProfile(HttpServletRequest request, Student student) {
		long studentId = CookieUtil.getStudentId(request);
		StudentEntity studentEntity = studentRepository.getStudentEntityByStudentId(studentId);
		long roleId = studentEntity.getRoleId();
		long groupId = studentEntity.getGroupId();

		fillStudent(student, studentId, roleId, groupId);
		if(!student.getPassword().isEmpty()) {
			studentRepository.save(student.toStudentEntityWithPasswordEncryption());
			return;
		}
		student.setPassword(studentEntity.getPassword());
		studentRepository.save(student.toStudentEntityWithOutPasswordEncryption());
	}

	public void deleteStudentByStudentId(HttpServletRequest request) {
		long studentId = CookieUtil.getStudentId(request);
		studentRepository.deleteById(studentId);
	}

	public String getStudentRoleByStudentId(HttpServletRequest request) {
		long studentId = CookieUtil.getStudentId(request);
		return studentRepository.getStudentRoleByStudentId(studentId);
	}

	private void fillStudent(Student student, long studentId, long roleId, long groupId) {
		student.setStudentId(studentId);
		student.setRoleId(roleId);
		student.setGroupId(groupId);
	}
}
