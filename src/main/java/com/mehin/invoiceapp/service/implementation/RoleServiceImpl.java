package com.mehin.invoiceapp.service.implementation;

import com.mehin.invoiceapp.domain.Role;
import com.mehin.invoiceapp.repository.RoleRepository;
import com.mehin.invoiceapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

   private final RoleRepository<Role> roleRepository;
   @Override
    public Role getRoleByUserId(Long id) {
        return roleRepository.getRoleByUserId(id);
    }
}
