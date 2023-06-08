package com.iyex.invoicemgt.service;

import com.iyex.invoicemgt.domain.User;
import com.iyex.invoicemgt.dto.UserDTO;
import com.iyex.invoicemgt.dtomapper.UserDTOMapper;
import com.iyex.invoicemgt.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo<User> userRepo;
    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepo.create(user));
    }
}
