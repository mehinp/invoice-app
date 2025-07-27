package com.mehin.invoiceapp.repository.implementation;

import com.mehin.invoiceapp.domain.Role;
import com.mehin.invoiceapp.domain.User;
import com.mehin.invoiceapp.domain.UserPrincipal;
import com.mehin.invoiceapp.dto.UserDTO;
import com.mehin.invoiceapp.enumeration.VerificationType;
import com.mehin.invoiceapp.exception.ApiException;
import com.mehin.invoiceapp.form.UpdateForm;
import com.mehin.invoiceapp.repository.RoleRepository;
import com.mehin.invoiceapp.repository.UserRepository;
import com.mehin.invoiceapp.rowmapper.UserRowMapper;
import com.mehin.invoiceapp.service.EmailService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mehin.invoiceapp.enumeration.RoleType.ROLE_USER;
import static com.mehin.invoiceapp.enumeration.VerificationType.ACCOUNT;
import static com.mehin.invoiceapp.enumeration.VerificationType.PASSWORD;
import static com.mehin.invoiceapp.query.UserQuery.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;

    @Override
    public User create(User user) {
        // Check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new ApiException("Email already in use. Please use a different email and try again.");
        // Save new user
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(requireNonNull(holder.getKey()).longValue());
            // Add role to the user
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            // Send verification URL
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // Save URL in verification table
            jdbc.update(INSERT_VERIFICATION_URL_QUERY, of("userId", user.getId(), "url", verificationUrl));
            sendEmail(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            // Send email to user with verification URL
           // emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);
            // Return the newly created user
            return user;
            // If any errors, throw exception with proper message
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");

        }


    }


    @Override
    public Collection<User> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public User get(Long id) {
       try {
           return jdbc.queryForObject(SELECT_USER_BY_ID_QUERY, of ("id", id), new UserRowMapper());
       } catch (EmptyResultDataAccessException exception){
           throw new ApiException("No user found by id: " + id);
        } catch (Exception exception) {
           log.error(exception.getMessage());
           throw new ApiException("An error occurred. Please try again.");
       }

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
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type) {
         return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if(user == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");

        } else {
            log.info("User found in the database: {}", email);
            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission() );
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No user found with email: " + email);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
        String verificationCode = randomAlphabetic(8).toUpperCase();

        try {
            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, of("id", user.getId()));
            jdbc.update(INSERT_VERIFICATION_CODE_QUERY, of("userId", user.getId(), "code", verificationCode, "expirationDate", expirationDate));
          //  sendSMS(user.getPhone(), "From: Invoice App \nVerification code: \n" + verificationCode);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public void resetPassword(String email) {
        if (getEmailCount(email.trim().toLowerCase()) <= 0) throw new ApiException("There is no account for this email address.");

        try {
                String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
                User user = getUserByEmail(email);
                String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), PASSWORD.getType());
                jdbc.update(DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, of("userId", user.getId()));
                jdbc.update(INSERT_PASSWORD_VERIFICATION_QUERY, of("userId", user.getId(), "url", verificationUrl, "expirationDate", expirationDate));
                sendEmail(user.getFirstName(), email, verificationUrl, PASSWORD);
                log.info("Verification URL: {}", verificationUrl);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public User verifyPasswordKey(String key) {
        if (isLinkExpired(key)) throw new ApiException("Link has expired. Please reset your password again.");
        try {
            User user = jdbc.queryForObject(SELECT_USER_BY_PASSWORD_URL_QUERY, of("url", getVerificationUrl(key, PASSWORD.getType())), new UserRowMapper());
            return user;
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("This link is not valid. Please reset your password again.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    private Boolean isLinkExpired(String key) {
        try {
            return jdbc.queryForObject(SELECT_EXPIRATION_BY_URL, of("url", getVerificationUrl(key, PASSWORD.getType())), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("This link is not  valid. Please reset your password again.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }


    @Override
    public void renewPassword(String key, String newPassword, String confirmPassword) {
        if(!newPassword.equals(confirmPassword)) throw new ApiException("Passwords do not match. Please try again.");
        String verificationUrl = getVerificationUrl(key, PASSWORD.getType());
        try {
            jdbc.update(UPDATE_USER_PASSWORD_BY_URL_QUERY, of("url", verificationUrl, "password", encoder.encode(newPassword)));
            jdbc.update(DELETE_VERIFICATION_BY_URL_QUERY, of("url", verificationUrl));
        } catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public void renewPassword(Long userId, String newPassword, String confirmPassword) {
        if(!newPassword.equals(confirmPassword)) throw new ApiException("Passwords do not match. Please try again.");
        try {
            jdbc.update(UPDATE_USER_PASSWORD_BY_USER_ID_QUERY, of("userId", userId, "password", encoder.encode(newPassword)));
            jdbc.update(DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, of("userId", userId));
        } catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public User verifyAccountKey(String key) {
        String verificationUrl = getVerificationUrl(key, ACCOUNT.getType());
        try {
            User user = jdbc.queryForObject(SELECT_USER_BY_ACCOUNT_URL_QUERY, of ("url", verificationUrl), new UserRowMapper());
            jdbc.update(UPDATE_USER_ENABLED_QUERY, of("enabled",  true , "id" , user.getId()));
            return user;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("This link is not valid.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public User updateUserDetails(UpdateForm user) {
        try {
            jdbc.update(UPDATE_USER_DETAILS_QUERY, of("id", user.getId(), "firstName", user.getFirstName(), "lastName", user.getLastName(), "email", user.getEmail(),
                    "phone", user.getPhone(), "address", user.getAddress(), "title", user.getTitle(), "bio", user.getBio()));
            return get(user.getId());
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No user found by id: " + user.getId());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new ApiException("Passwords do not match. Please try again.");
        }
        User user = get(id);
        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new ApiException("Incorrect current password. Please try again or reset your password.");
        }
        try {
            jdbc.update(UPDATE_USER_PASSWORD_BY_ID_QUERY, of("id", id, "newPassword", encoder.encode(newPassword)));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked) {
        try {
            jdbc.update(UPDATE_USER_ACCOUNT_SETTINGS_QUERY, of("enabled", enabled, "userId", userId, "notLocked", notLocked));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public void updateImage(UserDTO user, MultipartFile image) {
        String userImageUrl = setUserImageUrl(user.getEmail());
       // user.setImageUrl(userImageUrl);
        saveImage(user.getEmail(), image);
        jdbc.update(UPDATE_USER_IMAGE_QUERY, of("imageUrl", userImageUrl, "userId", user.getId()));
    }

    private void saveImage(String email, MultipartFile image) {
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Downloads/images/").toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException("Unable to create directory.");
            }
            log.info("Created image at: {}", fileStorageLocation);
        }
        try {
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(email + ".png"), REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new ApiException(exception.getMessage());
        }

        log.info("Saved image at: {}", fileStorageLocation);
    }

    private String setUserImageUrl(String email) {
       return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/" + email + ".png").toUriString();
    }

    private void sendEmail(String firstName, String email, String verificationUrl, VerificationType verificationType) {
        CompletableFuture.runAsync(() -> emailService.sendVerificationEmail(firstName, email, verificationUrl, verificationType));
    }

}
