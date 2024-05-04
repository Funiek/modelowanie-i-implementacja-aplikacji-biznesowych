package pl.cudanawidelcu.microservices.recipes.service;

public interface PasswordService {
    String hashPassword(String password);
}
