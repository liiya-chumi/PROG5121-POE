/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
public class MainProgram {
  
    public static void main(String[] args) {
        System.out.println("Starting combined program...\n");
        
        // Run File 1 first
        RegistrationAndLogin.createMessages();
        
        // Then run File 2
        QuickChat.readMessages();
        
        System.out.println("\nProgram finished!");
    }
}  

