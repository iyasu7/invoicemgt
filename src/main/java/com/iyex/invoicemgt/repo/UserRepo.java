package com.iyex.invoicemgt.repo;

import com.iyex.invoicemgt.domain.User;

import java.util.Collection;

public interface UserRepo <T extends User> {
    //    basic CRUD Opration
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);
}
