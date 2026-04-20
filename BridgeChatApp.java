
import java.util.Scanner;


public class BridgeApp {

    static Scanner input = new Scanner(System.in);

    // ===== SIMPLE STORAGE =====
    static String savedUsername = "";
    static String savedPassword = "";
     static stage=0;
//stage 0 ask how user feels 
// stage 1 ask if they have eaten 
    // ===== USERNAME VALIDATION =====
    public static boolean isValidUsername(String username) {
        return username.length() >= 6 && username.contains("_");
    }

    // ===== PASSWORD VALIDATION =====
    public static boolean isValidPassword(String password) {
        boolean hasNumber = false;
        boolean hasSpecial = false;
        boolean hasLength = false;

        if (password.length() >= 8) {
            hasLength = true;
        }

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (Character.isDigit(ch)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(ch)) {
                hasSpecial = true;
            }
        }

        return hasLength && hasNumber && hasSpecial;
    }

    // ===== CELLPHONE VALIDATION =====
    public static boolean isValidCellphone(String cellphone) {
        if (cellphone.length() != 10) return false;
        if (cellphone.charAt(0) != '0') return false;

        for (int i = 0; i < cellphone.length(); i++) {
            if (!Character.isDigit(cellphone.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // ===== SIMPLE CHATBOT RESPONSE =====
    public static String getResponse(String message) {

    message = message.toLowerCase();

    // ===== STAGE 0: ASK FEELINGS =====
    if (stage == 0) {

        if (message.contains("good") || message.contains("happy") || message.contains("great") || message.contains("ok")) {
            stage = 1;
            return "Bridge: I'm glad you're feeling good , What did you eat today?";
        }

        if (message.contains("sad") || message.contains("bad") || message.contains("no")) {
            stage = 1;
            return "Bridge: I'm sorry to hear that Did you eat anything today?";
        }

        return "Bridge: How are you feeling today?";
    }

    // ===== STAGE 1: ASK FOOD =====
    if (stage == 1) {

        if (message.contains("ate") || message.contains("food")) {
            stage = 0; // reset conversation
            return "Bridge: That's good ,Eating well is important for your health!";
        }

        if (message.contains("hungry") || message.contains("did not eat")) {
            stage = 0; // reset
            return "Bridge: You should try to eat something,It really helps your energy.";
        }

        stage = 0; // reset anyway
        return "Bridge: Thank you for sharing, Take care of yourself!";
    }

    return "Bridge: Tell me more...";
}
    // ===== REGISTER =====
    public static void Register() {
        String username;
        String password;
        String cellphone;

        System.out.println("\n--- REGISTER ---");

        do {
            System.out.print("Enter username: ");
            username = input.nextLine();

            if (!isValidUsername(username)) {
                System.out.println("Invalid username (min 6 chars + underscore) please try again");
            }

        } while (!isValidUsername(username));

        do {
            System.out.print("Enter password: ");
            password = input.nextLine();

            if (!isValidPassword(password)) {
                System.out.println("Invalid password (min 8 chars + number + special char)please try again");
            }

        } while (!isValidPassword(password));

        do {
            System.out.print("Enter cellphone: ");
            cellphone = input.nextLine();

            if (!isValidCellphone(cellphone)) {
                System.out.println("Cellphone number incorrectly formatted");
            } else {
                System.out.println("Cellphone number added successfully");
            }

        } while (!isValidCellphone(cellphone));

        savedUsername = username;
        savedPassword = password;

        System.out.println("Registration successful!");
    }

    // ===== LOGIN =====
    public static void login() {

        if (savedUsername.equals("")) {
            System.out.println("No users registered yet. Please register first.");
            return;
        }

        System.out.println("\n--- LOGIN ---");

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        if (username.equals(savedUsername) && password.equals(savedPassword)) {
            System.out.println("Login successful! Welcome to Bridge ");
            chat();
        } else {
            System.out.println("Invalid login details");
        }
    }

    // ===== CHAT =====
    public static void chat() {

        String message;

        System.out.println("\nStart chatting (type 'exit' to quit):");

        do {
            System.out.print("You: ");
            message = input.nextLine();

            if (!message.equalsIgnoreCase("exit")) {
                System.out.println(getResponse(message));
            }

        } while (!message.equalsIgnoreCase("exit"));
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("       WELCOME TO BRIDGE");
            System.out.println("       Sidibanisa Ubomi");
            System.out.println("==============================");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("Choose option: ");

            int choice;

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> register();

                case 2 -> login();

                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}
  
   
