package com.app.security.service;

public interface PreAuthService {

    boolean userHasPowerOnSubject(Long subjectId, String permissionName );


}
