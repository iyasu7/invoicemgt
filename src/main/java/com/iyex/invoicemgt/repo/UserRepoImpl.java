package com.iyex.invoicemgt.repo;

import com.iyex.invoicemgt.domain.Role;
import com.iyex.invoicemgt.domain.User;
import com.iyex.invoicemgt.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.UUID;

import static com.iyex.invoicemgt.enumeration.RoleType.ROLE_USER;
import static com.iyex.invoicemgt.enumeration.VerificationType.ACCOUNT;
import static com.iyex.invoicemgt.query.UserQuery.*;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepoImpl implements UserRepo<User> {

    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepo<Role> roleRepo;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User create(User user) {
        //check the email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase())>0)
            throw new ApiException("Email already in us, Please use another email address");
        //save new user
        try{
            KeyHolder holder = new GeneratedKeyHolder();
            log.info("holder "+ holder);
            SqlParameterSource parameters = getSqlParameterSource(user);
            log.info("parameters "+ parameters);
            jdbc.update(INSERT_USER_QUERY,parameters,holder);
            log.info("jdbc updated with parameters and holders");
            user.setId((requireNonNull(holder.getKey()).longValue()));
            log.info("userId is Set");
        //add role to the user
            roleRepo.addRoleToUser(user.getId(),ROLE_USER.name());
            log.info("role is added to the user");
        //send verification url
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(),ACCOUNT.getType());
            log.info("verificationUrl is added to the user"+verificationUrl);

            //save url in a verification table
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, of("userId",user.getId(),"url",verificationUrl));
            log.info("jdbc verificationUrl");

            //send email to user with verification URL
//            emailService.sendVerificationUrl(user.getFirstName(),user.getEmail(),verificationUrl,ACCOUNT);

            user.setEnabled(false);
            log.info("setEnabled false");

            user.setNotLocked(true);
            log.info("setNotLocked true");
        //return the newly created user
        return user;
        //Is any error, throw exception with proper

        }catch(Exception exception){
            throw new ApiException("An error occurred. Please try again");
        }

    }


    @Override
    public Collection<User> list(int page, int pageSize) {
     return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }
    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, of("email",email),Integer.class);
    }
    private SqlParameterSource getSqlParameterSource(User user) {


        return new MapSqlParameterSource()
                .addValue("firstName",user.getFirstName())
                .addValue("lastName",user.getLastName())
                .addValue("email",user.getEmail())
                .addValue("password",encoder.encode(user.getPassword()));
    }
    private String getVerificationUrl(String key,String type){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/"+type+"/"+key).toUriString();
    }

}
