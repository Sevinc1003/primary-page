package com.example.demo.utils.log;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service 
public class ActionLogger {

    private static ActionLogRepository repo;


    public static void logSuccess(String methodName, int statusCode, String fallbackEmail) {
        ActionLogEntity log = new ActionLogEntity();
        log.setEmail(getExecutorEmail(fallbackEmail));
        log.setMethodName(methodName);
        log.setStatusCode(statusCode);
        log.setErrorMessage(null); // Xəta yoxdur
        
        repo.save(log);
    }

    public static void logError(String methodName, int statusCode, String errorMessage, String fallbackEmail) {
        ActionLogEntity log = new ActionLogEntity();
        log.setEmail(getExecutorEmail(fallbackEmail));
        log.setMethodName(methodName);
        log.setStatusCode(statusCode);
        log.setErrorMessage(errorMessage);
        
        repo.save(log);
    }


    //-------------------------------------------------------------------------

    private static String getExecutorEmail(String fallbackEmail) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return auth.getName();
        }
        return fallbackEmail != null ? fallbackEmail : "Anonim / Sistem";
    }

}
