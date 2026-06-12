/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.Serializable;

/**
 *
 * @author Student
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    String username;
    String password;
    String phone;
    String securityAnswer;
    String securityQuestion;
    
    // Original constructor for backward compatibility
    User(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.securityQuestion = null;
        this.securityAnswer = null;
    }
    
    // New constructor with security questions
    User(String username, String password, String phone, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getSecurityQuestion() { return securityQuestion; }
    public String getSecurityAnswer() { return securityAnswer; }
    
    // Setters
    public void setPassword(String password) { this.password = password; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }
}