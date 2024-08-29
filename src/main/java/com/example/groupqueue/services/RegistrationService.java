package com.example.groupqueue.services;

import com.example.groupqueue.external.api.BsuirAPI;
import com.example.groupqueue.models.dto.Student;
import com.example.groupqueue.models.entities.GroupEntity;
import com.example.groupqueue.repo.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
	private final StudentService studentService;
	private final GroupRepository groupRepository;
	private final ScheduleService scheduleService;

	public void registerStudent(Student student) {
		long groupId = groupRepository.getGroupIdByNumber(student.getGroupNumber());
		student.setGroupId(groupId);
		studentService.saveStudent(student);
		long studentId = studentService.getStudentIdByStudent(student);
		student.setStudentId(studentId);
	}

	public void registerGroup(int groupNumber) {
		if(!groupRepository.isGroupExist(groupNumber) && BsuirAPI.isGroupExist(groupNumber)) {
			groupRepository.save(new GroupEntity(groupNumber));
			scheduleService.addRecordsForNewGroupByGroupNumber(groupNumber);
		}
	}
}
