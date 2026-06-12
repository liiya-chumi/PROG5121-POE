/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 *
 * @author Student
 */
public class RegistrationAndLogin {
    private static List<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;
    
    private static final String[] SECURITY_QUESTIONS = {
        "What was your childhood best friend's name?",
        "What is your dream car?",
        "What was the name of your first pet?",
        "What is your mother's maiden name?",
        "What city were you born in?"
    };
    
    public static void main(String[] args) {
        loadUsers();
        
        while (true) {
            if (loggedInUser == null) {
                showWelcomeScreen();
            } else {
                System.out.println("\n======================================");
                System.out.println("     Launching QuickChat...");
                System.out.println("======================================");
                
                QuickChat.main(new String[]{loggedInUser.getUsername(), loggedInUser.getPhone()});
                
                loggedInUser = null;
                System.out.println("\n======================================");
                System.out.println("     Logged out successfully!");
                System.out.println("======================================");
            }
        }
    }
    
    private static void showWelcomeScreen() {
        System.out.println("\n======================================");
        System.out.println("     REGISTRATION & LOGIN SYSTEM");
        System.out.println("======================================");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Forgot Password");
        System.out.println("4. View All Registered Users");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 1) {
            register();
        } else if (choice == 2) {
            login();
        } else if (choice == 3) {
            forgotPassword();
        } else if (choice == 4) {
            viewAllUsers();
        } else if (choice == 5) {
            System.out.println("Goodbye!");
            System.exit(0);
        } else {
            System.out.println("Invalid choice! Please try again.");
        }
    }
    
    private static void register() {
        System.out.println("\n--- REGISTRATION ---");
        
        String username = "";
        boolean validUsername = false;
        
        while (!validUsername) {
            System.out.print("Enter username (must contain underscore _ and max 5 characters): ");
            username = scanner.nextLine();
            
            if (!username.contains("_")) {
                System.out.println("INVALID: Username must contain an underscore (_)");
                continue;
            }
            
            if (username.length() > 5) {
                System.out.println("INVALID: Username cannot be more than 5 characters");
                continue;
            }
            
            boolean exists = false;
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    exists = true;
                    break;
                }
            }
            
            if (exists) {
                System.out.println("INVALID: Username already exists!");
                continue;
            }
            
            validUsername = true;
        }
        
        String password = "";
        boolean validPassword = false;
        
        while (!validPassword) {
            System.out.print("Enter password (min 8 chars, 1 uppercase, 1 special character): ");
            password = scanner.nextLine();
            
            if (password.length() < 8) {
                System.out.println("INVALID: Password must be at least 8 characters");
                continue;
            }
            
            boolean hasUpper = false;
            boolean hasSpecial = false;
            
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    hasUpper = true;
                }
                if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))) {
                    hasSpecial = true;
                }
            }
            
            if (!hasUpper) {
                System.out.println("INVALID: Password must contain at least 1 uppercase letter");
                continue;
            }
            
            if (!hasSpecial) {
                System.out.println("INVALID: Password must contain at least 1 special character");
                continue;
            }
            
            validPassword = true;
        }
        
        System.out.print("Enter cell phone number (+ international code): ");
        String phone = scanner.nextLine();
        
        while (!phone.matches("^\\+[0-9]{10,13}$")) {
            System.out.println("ERROR: Invalid phone number format!");
            System.out.println("Phone must start with + and have 10-13 digits");
            System.out.println("Example: +27718693002");
            System.out.print("Enter cell phone number (+ international code): ");
            phone = scanner.nextLine();
        }
        
        // Check if phone number already exists
        boolean phoneExists = false;
        for (User u : users) {
            if (u.getPhone().equals(phone)) {
                phoneExists = true;
                break;
            }
        }
        
        if (phoneExists) {
            System.out.println("ERROR: This phone number is already registered to another user!");
            System.out.println("Each phone number can only be used once.");
            return;
        }
        
        System.out.println("SUCCESS: Cell phone number captured.");
        
        System.out.println("\n--- SECURITY SETUP FOR PASSWORD RECOVERY ---");
        for (int i = 0; i < SECURITY_QUESTIONS.length; i++) {
            System.out.println((i + 1) + ". " + SECURITY_QUESTIONS[i]);
        }
        System.out.print("Choose question (1-5): ");
        int questionChoice = 0;
        while (true) {
            try {
                questionChoice = Integer.parseInt(scanner.nextLine());
                if (questionChoice >= 1 && questionChoice <= 5) break;
            } catch (NumberFormatException e) {}
            System.out.print("Invalid choice. Select 1-5: ");
        }
        
        String securityQuestion = SECURITY_QUESTIONS[questionChoice - 1];
        System.out.print("Enter your answer: ");
        String securityAnswer = scanner.nextLine().toLowerCase().trim();
        
        User newUser = new User(username, password, phone, securityQuestion, securityAnswer);
        users.add(newUser);
        saveUsers();
        
        System.out.println("\n======================================");
        System.out.println("REGISTRATION SUCCESSFUL!");
        System.out.println("Welcome " + username + "!");
        System.out.println("Phone: " + phone);
        System.out.println("======================================");
        
        System.out.println("\nLogging you in automatically...");
        loggedInUser = newUser;
    }
    
    private static void login() {
        System.out.println("\n--- LOGIN ---");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                System.out.println("\n======================================");
                System.out.println("WELCOME " + user.getUsername().toUpperCase() + "!");
                System.out.println("Login successful!");
                System.out.println("======================================");
                return;
            }
        }
        
        System.out.println("ERROR: Invalid username or password!");
    }
    
    private static void viewAllUsers() {
        if (users.isEmpty()) {
            System.out.println("\nNo registered users found.");
            return;
        }
        
        System.out.println("\n========== ALL REGISTERED USERS ==========");
        System.out.printf("%-3s %-20s %-20s%n", "No.", "Username", "Phone Number");
        System.out.println("-----------------------------------------------");
        
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            System.out.printf("%-3d %-20s %-20s%n", (i + 1), u.getUsername(), u.getPhone());
        }
        System.out.println("===============================================");
        System.out.println("\n[TIP] Use these usernames or phone numbers to send messages!");
    }
    
    private static void forgotPassword() {
        System.out.println("\n--- PASSWORD RECOVERY ---");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        
        User foundUser = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                foundUser = user;
                break;
            }
        }
        
        if (foundUser == null) {
            System.out.println("ERROR: Username not found!");
            System.out.println("[TIP] Use option 4 to view all registered usernames.");
            return;
        }
        
        System.out.println("\nAccount found for: " + foundUser.getUsername());
        System.out.println("Phone number: " + foundUser.getPhone());
        System.out.println("\nSecurity Question: " + foundUser.getSecurityQuestion());
        System.out.print("Your answer: ");
        String answer = scanner.nextLine().toLowerCase().trim();
        
        if (answer.equals(foundUser.getSecurityAnswer())) {
            System.out.println("\n======================================");
            System.out.println("SECURITY ANSWER CORRECT!");
            System.out.println("You can now reset your password.");
            System.out.println("======================================");
            
            String newPassword = "";
            boolean validPassword = false;
            
            while (!validPassword) {
                System.out.print("\nEnter new password (min 8 chars, 1 uppercase, 1 special character): ");
                newPassword = scanner.nextLine();
                
                if (newPassword.length() < 8) {
                    System.out.println("INVALID: Password must be at least 8 characters");
                    continue;
                }
                
                boolean hasUpper = false;
                boolean hasSpecial = false;
                
                for (int i = 0; i < newPassword.length(); i++) {
                    char c = newPassword.charAt(i);
                    if (c >= 'A' && c <= 'Z') hasUpper = true;
                    if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))) hasSpecial = true;
                }
                
                if (!hasUpper) {
                    System.out.println("INVALID: Password must contain at least 1 uppercase letter");
                    continue;
                }
                if (!hasSpecial) {
                    System.out.println("INVALID: Password must contain at least 1 special character");
                    continue;
                }
                
                validPassword = true;
            }
            
            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();
            
            if (newPassword.equals(confirmPassword)) {
                foundUser.setPassword(newPassword);
                saveUsers();
                System.out.println("\n======================================");
                System.out.println("PASSWORD RESET SUCCESSFULLY!");
                System.out.println("You can now login with your new password.");
                System.out.println("======================================");
            } else {
                System.out.println("\nERROR: Passwords do not match! Password not reset.");
            }
        } else {
            System.out.println("\nERROR: Security answer incorrect!");
            System.out.println("Password recovery failed.");
        }
    }
    
    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    private static void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (List<User>) ois.readObject();
            System.out.println("Loaded " + users.size() + " existing users.");
        } catch (IOException | ClassNotFoundException e) {
            users = new ArrayList<>();
            System.out.println("No existing users found. Starting fresh.");
        }
    }
}