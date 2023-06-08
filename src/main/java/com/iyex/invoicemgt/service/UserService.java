package com.iyex.invoicemgt.service;

import com.iyex.invoicemgt.domain.User;
import com.iyex.invoicemgt.dto.UserDTO;

public interface UserService {
    UserDTO createUser(User user);
}
