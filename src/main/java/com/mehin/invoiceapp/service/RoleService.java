package com.mehin.invoiceapp.service;

import com.mehin.invoiceapp.domain.Role;

import java.util.Collection;


public interface RoleService {
    Role getRoleByUserId(Long id);
    Collection<Role> getAllRoles();
}
