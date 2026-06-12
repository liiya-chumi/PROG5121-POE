/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/**
 *
 * @author Student
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("\n======================================");
        System.out.println("     WELCOME TO QUICKCHAT SYSTEM");
        System.out.println("======================================");
        
        // Step 1: Run Registration and Login System
        System.out.println("\n[PHASE 1] Starting Registration and Login System...");
        System.out.println("======================================");
        RegistrationAndLogin.main(args);
        
        // After RegistrationAndLogin finishes, run QuickChat
        System.out.println("\n======================================");
        System.out.println("[PHASE 2] Starting QuickChat Messenger...");
        System.out.println("======================================");
        QuickChat.main(args);
        
        System.out.println("\n======================================");
        System.out.println("     THANK YOU FOR USING QUICKCHAT");
        System.out.println("======================================");
    }
}