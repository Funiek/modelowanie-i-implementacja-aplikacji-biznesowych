package com.example.bootdata.service;

import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    @Override
    public String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }
}
