import java.util.Scanner;

public class Bridge {

    // USERNAME VALIDATION
    public static boolean isValidUsername(String username) {
        return username.length() >= 6 && username.contains("_");
    }

    // PASSWORD VALIDATION
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

    // CELLPHONE VALIDATION
    public static boolean isValidCellphone(String cellphone) {

        if (cellphone.length() != 10) {
            return false;
        }

        if (cellphone.charAt(0) != '0') {
            return false;
        }

        for (int i = 0; i < cellphone.length(); i++) {
            if (!Character.isDigit(cellphone.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String username;
        String password;
        String cellphone;
        String firstName;
        String lastName;

        boolean validUsername = false;
        boolean validPassword = false;
        boolean validCell = false;

        // USERNAME LOOP
        do {
            System.out.print("Enter username: ");
            username = input.nextLine();

            if (isValidUsername(username)) {
                validUsername = true;
            } else {
                System.out.println("ERROR: Username must be at least 6 characters and contain an underscore (_)");
            }

        } while (!validUsername);

        // PASSWORD LOOP
        do {
            System.out.print("Enter password: ");
            password = input.nextLine();

            if (isValidPassword(password)) {
                validPassword = true;
            } else {
                System.out.println("ERROR: Password must be at least 8 characters, include a number and a special character.");
            }

        } while (!validPassword);

        // CELLPHONE LOOP
        do {
            System.out.print("Enter cellphone number: ");
            cellphone = input.nextLine();

            if (isValidCellphone(cellphone)) {
                validCell = true;
                System.out.println("Cellphone number added successfully");
            } else {
                System.out.println("Cellphone number incorrectly formatted");
            }

        } while (!validCell);

        // OTHER DETAILS
        System.out.print("Enter first name: ");
        firstName = input.nextLine();

        System.out.print("Enter last name: ");
        lastName = input.nextLine();

        // WELCOME MESSAGE
        System.out.println("\nWelcome " + firstName + " " + lastName + ", it is great to see you again!");

        // CHAT SYSTEM
        String[] chatHistory = new String[10];
        int chatIndex = 0;
        String message;

        System.out.println("\nStart chatting (type 'exit' to quit):");

        do {
            System.out.print("You: ");
            message = input.nextLine();

            if (!message.equalsIgnoreCase("exit")) {

                if (chatIndex < chatHistory.length) {
                    chatHistory[chatIndex] = message;
                    chatIndex++;

                    System.out.println("Bot: I hear you said -> " + message);
                } else {
                    System.out.println("Chat history is full.");
                    break;
                }
            }

        } while (!message.equalsIgnoreCase("exit"));

        // DISPLAY CHAT HISTORY
        System.out.println("\nChat History:");
        for (int i = 0; i < chatIndex; i++) {
            System.out.println((i + 1) + ": " + chatHistory[i]);
        }

        System.out.println("Goodbye!");

        input.close();
    }
}
