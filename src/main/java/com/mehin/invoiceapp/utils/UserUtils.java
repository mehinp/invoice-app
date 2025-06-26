
package com.mehin.invoiceapp.utils;

import com.mehin.invoiceapp.domain.User;
import com.mehin.invoiceapp.domain.UserPrincipal;
import com.mehin.invoiceapp.dto.UserDTO;
import org.springframework.security.core.Authentication;

public class UserUtils {

    public static UserDTO getAuthenticatedUser(Authentication authentication) {
        return ((UserDTO) authentication.getPrincipal());
    }

    public static UserDTO getLoggedInUser(Authentication authentication) {
        return (UserDTO) authentication.getPrincipal();
    }

}