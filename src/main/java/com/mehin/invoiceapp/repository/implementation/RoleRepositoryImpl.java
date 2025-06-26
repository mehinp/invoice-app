package com.mehin.invoiceapp.repository.implementation;

import com.mehin.invoiceapp.domain.Role;
import com.mehin.invoiceapp.exception.ApiException;
import com.mehin.invoiceapp.repository.RoleRepository;
import com.mehin.invoiceapp.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.mehin.invoiceapp.enumeration.RoleType.ROLE_USER;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static com.mehin.invoiceapp.query.RoleQuery.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {

        log.info("Adding role {} to user id: {}", roleName, userId);

        try {
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, of("name", roleName), new RoleRowMapper());
            jdbc.update(INSERT_ROLE_TO_USER_QUERY, of("userId", userId, "roleId", requireNonNull(role).getId()));

        } catch (EmptyResultDataAccessException exception) { throw new ApiException("No role found by name: " + roleName);

        } catch (Exception exception) { throw new ApiException("An error occurred. Please try again.");

        }


    }


        @Override
    public Role getRoleByUserId(Long userId) {
            log.info("Fetching role with user id {}", userId);

            try {
                return jdbc.queryForObject(SELECT_ROLE_BY_ID_QUERY, of("id", userId), new RoleRowMapper());

            } catch (EmptyResultDataAccessException exception) { throw new ApiException("User with id " + userId + " not found with a role.");

            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException("An error occurred. Please try again.");

            }

        }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
