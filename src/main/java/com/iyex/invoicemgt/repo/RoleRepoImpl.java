package com.iyex.invoicemgt.repo;

import com.iyex.invoicemgt.domain.Role;
import com.iyex.invoicemgt.exception.ApiException;
import com.iyex.invoicemgt.rowMapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import static com.iyex.invoicemgt.enumeration.RoleType.ROLE_USER;
import static com.iyex.invoicemgt.query.RoleQuery.*;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepoImpl<T extends Role> implements RoleRepo<T> {

    private final NamedParameterJdbcTemplate jdbc;
    @Override
    public T create(T data) {
        return null;
    }

    @Override
    public Collection<T> list(int page, int pageSize) {
        return null;
    }

    @Override
    public T get(Long id) {
        return null;
    }

    @Override
    public T update(T data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("adding role {} to user id: {}",roleName,userId);
        try{
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, of("name",roleName), new RoleRowMapper());
            jdbc.update(INSERT_ROLE_TO_USER_QUERY, of("userId",userId,"roleId", requireNonNull(role).getId()));
        }catch(EmptyResultDataAccessException exception ){
            throw new ApiException("No role found by name "+ROLE_USER.name());
        }
        catch(Exception exception){
            throw new ApiException("An error occurred. Please try again");
        }

    }

    @Override
    public Role getRoleByUserId(Long userId) {
        return null;
    }

    @Override
    public Role getRoleByUserEmail(Long email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
