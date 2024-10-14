package com.project.groupqueue.services;

import com.project.groupqueue.models.enums.RoleType;
import com.project.groupqueue.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
	private final RoleRepository roleRepository;

	public Long getRoleIdByType(RoleType roleType) {
		return roleRepository.getRoleIdByType(roleType);
	}
}
