package com.mehin.invoiceapp.constant;

public class Constants {
    // Security

    public static final String[] PUBLIC_URLS = {
            "/user/login/**",
            "/user/register/**",
            "/user/resetpassword/**",
            "/user/verify/password/**",
            "/user/verify/account/**",
            "/user/refresh/token**",
            "/user/image/**",
            "/user/new/password"
    };

    public static final String EMPTY = "";
    public static final String TOKEN_PREFIX = "Bearer " ;
    public static final String[] PUBLIC_ROUTES = {"/user/login", "/user/register", "/user/refresh/token", "user/image", "/user/new/password"};
    public static final String HTTP_OPTIONS_METHOD = "OPTIONS";

    public static final String MEHIN_APPLICATION = "MEHIN_APPLICATION";
    public static final String CUSTOMER_MANAGEMENT_SERVICE = "CUSTOMER_MANAGEMENT_SERVICE";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 432_000_000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME =  864_000_000;

    // Request

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String USER_AGENT = "user-agent";

    // Date

    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
}
