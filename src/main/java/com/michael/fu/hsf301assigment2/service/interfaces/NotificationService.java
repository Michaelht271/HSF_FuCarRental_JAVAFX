package com.michael.fu.hsf301assigment2.service.interfaces;

public interface NotificationService {
    void sendVerification(String type, String data);
    void sendResetPasswordEmail(String email, String resetToken) ;

    }
