package com.mehin.invoiceapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import static com.mehin.invoiceapp.constant.Constants.USER_AGENT;
import static com.mehin.invoiceapp.constant.Constants.X_FORWARDED_FOR;

public class RequestUtils {

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = "Unknown ip";
        if (request != null) {
            ipAddress = request.getHeader(X_FORWARDED_FOR);
            if(ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr();
            }
        }
        return ipAddress;
    }

    public static String getDevice(HttpServletRequest request) {
        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(1000).build();
        UserAgent userAgent = userAgentAnalyzer.parse(request.getHeader(USER_AGENT));
        System.out.println(userAgent);
        return userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME) + " - " + userAgent.getValue(UserAgent.AGENT_NAME) + " - " + userAgent.getValue(UserAgent.DEVICE_NAME);
    }
}
