package com.michael.fu.hsf301assigment1.service.interfaces;

public interface NotificationService {
    public void sendVerification(String type, String data);
    public void sendResetPasswordEmail(String email, String resetToken) ;

    }
