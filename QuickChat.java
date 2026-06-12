import java.util.*;
import java.io.*;

public class QuickChat {
   
    private static List<Message> messages = new ArrayList<>();
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIds = new ArrayList<>();
    
    private static Scanner scanner = new Scanner(System.in);
    private static int messageCounter = 0;
    private static int totalMessagesSent = 0;
    private static String currentUser = "";
    private static String currentUserPhone = "";
    
    public static void main(String[] args) {
        if (args.length >= 2) {
            currentUser = args[0];
            currentUserPhone = args[1];
            System.out.println("\n======================================");
            System.out.println("WELCOME TO QUICKCHAT, " + currentUser.toUpperCase() + "!");
            System.out.println("======================================");
            loadMessages();
            showMessagingMenu();
        } else {
            System.out.println("ERROR: You must login through the Registration System first!");
            System.exit(0);
        }
    }
    
    private static void showMessagingMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n========== QUICKCHAT MESSAGING ==========");
            System.out.println("User: " + currentUser);
            System.out.println("1. Send Message");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. View Total Messages Sent");
            System.out.println("4. Search Message by ID");
            System.out.println("5. View All Message IDs");
            System.out.println("6. Stored Messages Menu");
            System.out.println("7. View Deleted Messages");
            System.out.println("8. Exit");
            System.out.print("Choose option: ");
            
            int choice = 0;
            try {
                String input = scanner.nextLine();
                if (input.isEmpty()) {
                    System.out.println("Invalid option! Please enter a number between 1-8.");
                    continue;
                }
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option! Please enter a number between 1-8.");
                continue;
            }
            
            switch(choice) {
                case 1:
                    sendMessage();
                    break;
                case 2:
                    printMessages();
                    break;
                case 3:
                    System.out.println("\nTotal messages sent: " + returnTotalMessages());
                    break;
                case 4:
                    searchMessageById();
                    break;
                case 5:
                    viewAllMessageIds();
                    break;
                case 6:
                    showStoredMessagesMenu();
                    break;
                case 7:
                    viewDeletedMessages();
                    break;
                case 8:
                    System.out.println("\nLogging out " + currentUser + "...");
                    System.out.println("Returning to Registration System...");
                    running = false;
                    return;
                default:
                    System.out.println("Invalid option! Please choose 1-8.");
            }
        }
    }
    
    private static void showStoredMessagesMenu() {
        boolean inMenu = true;
        
        while (inMenu) {
            System.out.println("\n========== STORED MESSAGES MENU ==========");
            System.out.println("a. Display sender and recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID and display recipient and message");
            System.out.println("d. Search for all messages for a particular recipient");
            System.out.println("e. Delete a message using message hash");
            System.out.println("f. Display full report of all stored messages");
            System.out.println("g. Return to Main Menu");
            System.out.print("Choose option (a-g): ");
            
            String choice = scanner.nextLine().toLowerCase();
            
            switch(choice) {
                case "a":
                    displaySendersAndRecipients();
                    break;
                case "b":
                    displayLongestMessage();
                    break;
                case "c":
                    searchMessageByIdStored();
                    break;
                case "d":
                    searchMessagesByRecipient();
                    break;
                case "e":
                    deleteMessageByHash();
                    break;
                case "f":
                    displayFullReport();
                    break;
                case "g":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option! Please choose a-g.");
            }
        }
    }
    
    private static void sendMessage() {
        System.out.print("\nHow many messages do you want to send? ");
        int numMessages = 0;
        try {
            numMessages = Integer.parseInt(scanner.nextLine());
            if (numMessages <= 0) {
                System.out.println("Please enter a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
            return;
        }
        
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " ---");
            
            String recipient = "";
            boolean validRecipient = false;
            
            while (!validRecipient) {
                System.out.println("\nHow would you like to specify the recipient?");
                System.out.println("   1. By Phone Number");
                System.out.println("   2. By Username");
                System.out.print("Choose (1-2): ");
                
                int recipientType = 0;
                try {
                    recipientType = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice! Please enter 1 or 2.");
                    continue;
                }
                
                if (recipientType == 1) {
                    System.out.print("Enter recipient phone number (+ international code): ");
                    recipient = scanner.nextLine();
                    
                    if (recipient.equalsIgnoreCase("exit") || recipient.equalsIgnoreCase("quit")) {
                        System.out.println("Message sending cancelled.");
                        return;
                    }
                    
                    if (recipient.matches("^\\+[0-9]{10,13}$")) {
                        if (isPhoneNumberRegistered(recipient)) {
                            System.out.println("Success: Valid recipient!");
                            validRecipient = true;
                        } else {
                            System.out.println("ERROR: Phone number not registered!");
                            System.out.println("[TIP] Use option 4 in Registration System to view all registered users.");
                        }
                    } else {
                        System.out.println("ERROR: Invalid phone number format. Use + and 10-13 digits.");
                    }
                    
                } else if (recipientType == 2) {
                    System.out.print("Enter recipient username: ");
                    String username = scanner.nextLine();
                    
                    if (username.equalsIgnoreCase("exit") || username.equalsIgnoreCase("quit")) {
                        System.out.println("Message sending cancelled.");
                        return;
                    }
                    
                    String phoneNumber = getPhoneNumberByUsername(username);
                    if (phoneNumber != null) {
                        recipient = phoneNumber;
                        System.out.println("Success: Username found! Sending to: " + username + " (" + phoneNumber + ")");
                        validRecipient = true;
                    } else {
                        System.out.println("ERROR: Username not found!");
                        System.out.println("[TIP] Use option 4 in Registration System to view all registered users.");
                    }
                } else {
                    System.out.println("Invalid choice! Please enter 1 or 2.");
                }
            }
            
            String content = "";
            boolean validMessage = false;
            
            while (!validMessage) {
                System.out.print("Enter your message (max 250 characters): ");
                content = scanner.nextLine();
                
                if (content.equalsIgnoreCase("exit") || content.equalsIgnoreCase("quit")) {
                    System.out.println("Message sending cancelled.");
                    return;
                }
                
                if (content.length() > 250) {
                    int excess = content.length() - 250;
                    System.out.println("Message exceeds 250 characters by " + excess + " characters; please reduce the size.");
                } else if (content.trim().isEmpty()) {
                    System.out.println("Message cannot be empty! Please enter a message.");
                } else {
                    System.out.println("Message ready to send.");
                    validMessage = true;
                }
            }
            
            messageCounter++;
            String messageId = generateMessageId();
            messageIds.add(messageId);
            
            String messageHash = createMessageHash(messageId, messageCounter, content);
            messageHashes.add(messageHash);
            
            Message msg = new Message(messageId, messageCounter, recipient, content, messageHash, currentUser);
            messages.add(msg);
            
            System.out.println("\n========== MESSAGE CREATED ==========");
            System.out.println("MESSAGE ID: " + messageId);
            System.out.println("Message Hash: " + messageHash);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + content);
            System.out.println("======================================");
            
            System.out.println("\nOptions:");
            System.out.println("   1. Send Message");
            System.out.println("   2. Disregard Message");
            System.out.println("   3. Store Message");
            System.out.print("Choose (1-3): ");
            
            int option = 0;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                option = 3;
            }
            
            if (option == 1) {
                totalMessagesSent++;
                msg.setStatus("SENT");
                sentMessages.add(msg);
                System.out.println("\n[SUCCESS] Message marked as SENT");
            } else if (option == 2) {
                msg.setStatus("DISREGARDED");
                disregardedMessages.add(msg);
                System.out.println("\n[DELETED] Message disregarded and marked as DELETED.");
            } else {
                totalMessagesSent++;
                msg.setStatus("STORED");
                storedMessages.add(msg);
                System.out.println("\n[STORED] Message marked as STORED");
            }
            
            storeMessage();
            System.out.println("\n[SUCCESS] Message " + (i + 1) + " processed successfully!");
        }
        
        System.out.println("\n[SUMMARY] Total messages processed: " + totalMessagesSent);
        System.out.println("\nReturning to main menu...");
    }
    
    private static boolean isPhoneNumberRegistered(String phoneNumber) {
        try {
            File file = new File("users.dat");
            if (!file.exists()) return false;
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
                List<User> users = (List<User>) ois.readObject();
                for (User user : users) {
                    if (user.getPhone().equals(phoneNumber)) return true;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
        return false;
    }
    
    private static String getPhoneNumberByUsername(String username) {
        try {
            File file = new File("users.dat");
            if (!file.exists()) return null;
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
                List<User> users = (List<User>) ois.readObject();
                for (User user : users) {
                    if (user.getUsername().equalsIgnoreCase(username)) {
                        return user.getPhone();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
        return null;
    }
    
    private static void viewDeletedMessages() {
        if (disregardedMessages.isEmpty()) {
            System.out.println("\n[INFO] No deleted messages found.");
            return;
        }
        
        System.out.println("\n========== DELETED MESSAGES ==========");
        System.out.println("Total deleted messages: " + disregardedMessages.size());
        System.out.println("---------------------------------------");
        
        for (int i = 0; i < disregardedMessages.size(); i++) {
            Message m = disregardedMessages.get(i);
            System.out.println("\n--- Deleted Message " + (i + 1) + " ---");
            System.out.println("    Message ID: " + m.getMessageId());
            System.out.println("    Message Hash: " + m.getMessageHash());
            System.out.println("    Sender: " + m.getSender());
            System.out.println("    Recipient: " + m.getRecipient());
            System.out.println("    Content: " + m.getContent());
            System.out.println("    Status: " + m.getStatus());
            System.out.println("    -----------------------------------------");
        }
        
        System.out.println("\n=========================================");
        System.out.print("Do you want to restore a deleted message? (y/n): ");
        String answer = scanner.nextLine().toLowerCase();
        
        if (answer.equals("y")) {
            restoreDeletedMessage();
        }
        System.out.println("=========================================");
    }
    
    private static void restoreDeletedMessage() {
        if (disregardedMessages.isEmpty()) {
            System.out.println("[ERROR] No deleted messages to restore.");
            return;
        }
        
        System.out.println("\nSelect a message to restore:");
        for (int i = 0; i < disregardedMessages.size(); i++) {
            Message m = disregardedMessages.get(i);
            String preview = m.getContent().length() > 50 ? m.getContent().substring(0, 50) + "..." : m.getContent();
            System.out.println("   " + (i + 1) + ". " + preview);
        }
        
        System.out.print("\nEnter message number to restore (1-" + disregardedMessages.size() + "): ");
        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > disregardedMessages.size()) {
                System.out.println("[ERROR] Invalid choice.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input.");
            return;
        }
        
        Message restoredMsg = disregardedMessages.get(choice - 1);
        
        System.out.println("\nMessage to restore:");
        System.out.println("   Content: " + restoredMsg.getContent());
        System.out.println("   Recipient: " + restoredMsg.getRecipient());
        
        System.out.println("\nRestore options:");
        System.out.println("   1. Restore to SENT messages");
        System.out.println("   2. Restore to STORED messages");
        System.out.println("   3. Keep deleted (cancel restore)");
        System.out.print("Choose (1-3): ");
        
        int option = 0;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid option. Restore cancelled.");
            return;
        }
        
        if (option == 1) {
            restoredMsg.setStatus("SENT");
            sentMessages.add(restoredMsg);
            messages.add(restoredMsg);
            disregardedMessages.remove(choice - 1);
            totalMessagesSent++;
            System.out.println("\n[SUCCESS] Message restored to SENT messages!");
            storeMessage();
        } else if (option == 2) {
            restoredMsg.setStatus("STORED");
            storedMessages.add(restoredMsg);
            messages.add(restoredMsg);
            disregardedMessages.remove(choice - 1);
            totalMessagesSent++;
            System.out.println("\n[SUCCESS] Message restored to STORED messages!");
            storeMessage();
        } else if (option == 3) {
            System.out.println("\n[INFO] Message kept as deleted. No changes made.");
        } else {
            System.out.println("\n[ERROR] Invalid option. Restore cancelled.");
        }
    }
    
    private static void displaySendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            System.out.println("\n[ERROR] No stored messages found.");
            return;
        }
        
        System.out.println("\n========== SENDERS AND RECIPIENTS OF STORED MESSAGES ==========");
        System.out.printf("%-5s %-20s %-20s%n", "No.", "Sender", "Recipient");
        System.out.println("--------------------------------------------------------------");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            System.out.printf("%-5d %-20s %-20s%n", (i + 1), m.getSender(), m.getRecipient());
        }
        System.out.println("=================================================================");
    }
    
    private static void displayLongestMessage() {
        if (storedMessages.isEmpty()) {
            System.out.println("\n[ERROR] No stored messages found.");
            return;
        }
        
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.getContent().length() > longest.getContent().length()) {
                longest = m;
            }
        }
        
        System.out.println("\n========== LONGEST STORED MESSAGE ==========");
        System.out.println("Message: " + longest.getContent());
        System.out.println("Length: " + longest.getContent().length() + " characters");
        System.out.println("Sender: " + longest.getSender());
        System.out.println("Recipient: " + longest.getRecipient());
        System.out.println("Message ID: " + longest.getMessageId());
        System.out.println("Message Hash: " + longest.getMessageHash());
        System.out.println("=============================================");
    }
    
    private static void searchMessageByIdStored() {
        if (messageIds.isEmpty()) {
            System.out.println("\n[ERROR] No messages have been sent yet.");
            return;
        }
        
        System.out.print("\nEnter Message ID to search: ");
        String searchId = scanner.nextLine().trim();
        
        boolean found = false;
        
        // Search in stored messages
        for (Message m : storedMessages) {
            if (m.getMessageId().equals(searchId)) {
                System.out.println("\n========== MESSAGE FOUND (STORED) ==========");
                System.out.println("Recipient: " + m.getRecipient());
                System.out.println("Message: " + m.getContent());
                System.out.println("Sender: " + m.getSender());
                System.out.println("Message Hash: " + m.getMessageHash());
                System.out.println("Status: " + m.getStatus());
                System.out.println("=============================================");
                found = true;
                break;
            }
        }
        
        // Search in sent messages
        if (!found) {
            for (Message m : sentMessages) {
                if (m.getMessageId().equals(searchId)) {
                    System.out.println("\n========== MESSAGE FOUND (SENT) ==========");
                    System.out.println("Recipient: " + m.getRecipient());
                    System.out.println("Message: " + m.getContent());
                    System.out.println("Sender: " + m.getSender());
                    System.out.println("Message Hash: " + m.getMessageHash());
                    System.out.println("Status: " + m.getStatus());
                    System.out.println("===========================================");
                    found = true;
                    break;
                }
            }
        }
        
        // Search in disregarded messages
        if (!found) {
            for (Message m : disregardedMessages) {
                if (m.getMessageId().equals(searchId)) {
                    System.out.println("\n========== MESSAGE FOUND (DELETED) ==========");
                    System.out.println("Recipient: " + m.getRecipient());
                    System.out.println("Message: " + m.getContent());
                    System.out.println("Sender: " + m.getSender());
                    System.out.println("Message Hash: " + m.getMessageHash());
                    System.out.println("Status: " + m.getStatus());
                    System.out.println("==============================================");
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            System.out.println("\n[ERROR] Message with ID '" + searchId + "' not found.");
            System.out.println("[TIP] Use option 5 to view all available Message IDs.");
        }
    }
    
    private static void searchMessagesByRecipient() {
        System.out.println("\nSearch by:");
        System.out.println("   1. Phone Number");
        System.out.println("   2. Username");
        System.out.print("Choose (1-2): ");
        
        int searchType = 0;
        try {
            searchType = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid choice.");
            return;
        }
        
        String searchValue = "";
        if (searchType == 1) {
            System.out.print("Enter recipient phone number: ");
            searchValue = scanner.nextLine();
            if (!isPhoneNumberRegistered(searchValue)) {
                System.out.println("[ERROR] Phone number not registered!");
                return;
            }
        } else if (searchType == 2) {
            System.out.print("Enter recipient username: ");
            String username = scanner.nextLine();
            searchValue = getPhoneNumberByUsername(username);
            if (searchValue == null) {
                System.out.println("[ERROR] Username not found!");
                return;
            }
            System.out.println("Found phone number: " + searchValue);
        } else {
            System.out.println("[ERROR] Invalid choice.");
            return;
        }
        
        List<Message> foundMessages = new ArrayList<>();
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(searchValue)) {
                foundMessages.add(m);
            }
        }
        
        if (foundMessages.isEmpty()) {
            System.out.println("\n[ERROR] No stored messages found for: " + searchValue);
        } else {
            System.out.println("\n========== MESSAGES FOR RECIPIENT ==========");
            for (int i = 0; i < foundMessages.size(); i++) {
                Message m = foundMessages.get(i);
                System.out.println((i + 1) + ". " + m.getContent());
            }
            System.out.println("============================================");
        }
    }
    
    private static void deleteMessageByHash() {
        if (messageHashes.isEmpty()) {
            System.out.println("\n[ERROR] No messages available to delete.");
            return;
        }
        
        System.out.println("\nAvailable Message Hashes:");
        for (int i = 0; i < messageHashes.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + messageHashes.get(i));
        }
        
        System.out.print("\nEnter Message Hash to delete: ");
        String hashToDelete = scanner.nextLine();
        
        boolean found = false;
        Message deletedMessage = null;
        
        Iterator<Message> iter = storedMessages.iterator();
        while (iter.hasNext()) {
            Message m = iter.next();
            if (m.getMessageHash().equals(hashToDelete)) {
                deletedMessage = m;
                iter.remove();
                found = true;
                break;
            }
        }
        
        if (!found) {
            Iterator<Message> sentIter = sentMessages.iterator();
            while (sentIter.hasNext()) {
                Message m = sentIter.next();
                if (m.getMessageHash().equals(hashToDelete)) {
                    deletedMessage = m;
                    sentIter.remove();
                    found = true;
                    break;
                }
            }
        }
        
        if (found && deletedMessage != null) {
            deletedMessage.setStatus("DISREGARDED");
            disregardedMessages.add(deletedMessage);
            System.out.println("\n[SUCCESS] Message moved to Deleted Messages.");
            storeMessage();
        } else {
            System.out.println("\n[ERROR] Message with hash '" + hashToDelete + "' not found.");
        }
    }
    
    private static void displayFullReport() {
        if (storedMessages.isEmpty()) {
            System.out.println("\n[ERROR] No stored messages found.");
            return;
        }
        
        System.out.println("\n========== FULL STORED MESSAGES REPORT ==========");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            System.out.println("\n--- Message " + (i + 1) + " ---");
            System.out.println("    Message ID: " + m.getMessageId());
            System.out.println("    Message Hash: " + m.getMessageHash());
            System.out.println("    Sender: " + m.getSender());
            System.out.println("    Recipient: " + m.getRecipient());
            System.out.println("    Content: " + m.getContent());
            System.out.println("    Status: " + m.getStatus());
            System.out.println("    ----------------------------------------");
        }
        System.out.println("================================================");
    }
    
    private static void viewAllMessageIds() {
        if (messageIds.isEmpty()) {
            System.out.println("\n[ERROR] No messages sent yet.");
        } else {
            System.out.println("\n========== ALL MESSAGE IDs ==========");
            for (int i = 0; i < messageIds.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + messageIds.get(i));
            }
            System.out.println("=====================================");
        }
    }
    
    private static String createMessageHash(String msgId, int msgNum, String content) {
        if (content == null || content.trim().isEmpty()) {
            return "00:0:EMPTY";
        }
        
        content = content.trim().replace("\"", "");
        String[] words = content.split(" ");
        
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        String prefix = msgId.length() >= 2 ? msgId.substring(0, 2) : "00";
        String combined = (firstWord + lastWord).toUpperCase();
        
        return prefix + ":" + msgNum + ":" + combined;
    }
    
    private static void printMessages() {
        if (messages.isEmpty()) {
            System.out.println("\n[ERROR] No messages sent yet.");
        } else {
            System.out.println("\n========== RECENT MESSAGES ==========");
            int displayCount = Math.min(10, messages.size());
            
            for (int i = messages.size() - displayCount; i < messages.size(); i++) {
                Message m = messages.get(i);
                System.out.println("\n--- Message " + (i + 1) + " ---");
                System.out.println("Message ID: " + m.getMessageId());
                System.out.println("Recipient: " + m.getRecipient());
                System.out.println("Message: " + m.getContent());
                System.out.println("From: " + m.getSender());
                System.out.println("Status: " + m.getStatus());
                System.out.println("------------------------");
            }
        }
    }
    
    private static void searchMessageById() {
        if (messageIds.isEmpty()) {
            System.out.println("\n[ERROR] No messages sent yet.");
            return;
        }
        
        System.out.print("\nEnter Message ID to search: ");
        String searchId = scanner.nextLine().trim();
        
        boolean found = false;
        for (Message m : messages) {
            if (m.getMessageId().equals(searchId)) {
                System.out.println("\n========== MESSAGE FOUND ==========");
                System.out.println("Message ID: " + m.getMessageId());
                System.out.println("Message Hash: " + m.getMessageHash());
                System.out.println("Recipient: " + m.getRecipient());
                System.out.println("Message: " + m.getContent());
                System.out.println("From: " + m.getSender());
                System.out.println("Status: " + m.getStatus());
                System.out.println("===================================");
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("\n[ERROR] Message with ID '" + searchId + "' not found.");
            System.out.println("[TIP] Use option 5 to view all available Message IDs.");
        }
    }
    
    private static int returnTotalMessages() {
        return sentMessages.size() + storedMessages.size();
    }
    
    private static void storeMessage() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("messages.json"))) {
            pw.println("[");
            for (int i = 0; i < messages.size(); i++) {
                Message m = messages.get(i);
                pw.println("  {");
                pw.println("    \"messageId\": \"" + m.getMessageId() + "\",");
                pw.println("    \"messageNum\": " + m.getMessageNum() + ",");
                pw.println("    \"sender\": \"" + m.getSender() + "\",");
                pw.println("    \"recipient\": \"" + m.getRecipient() + "\",");
                pw.println("    \"content\": \"" + escapeJson(m.getContent()) + "\",");
                pw.println("    \"messageHash\": \"" + m.getMessageHash() + "\",");
                pw.println("    \"status\": \"" + m.getStatus() + "\"");
                pw.print("  }");
                if (i < messages.size() - 1) pw.println(",");
                else pw.println();
            }
            pw.println("]");
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
    
    private static String generateMessageId() {
        Random rand = new Random();
        int id = 1000000 + rand.nextInt(9000000);
        return String.valueOf(id);
    }
    
    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\"", "\\\"");
    }
    
    private static void loadMessages() {
        messages = new ArrayList<>();
        sentMessages = new ArrayList<>();
        disregardedMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        messageIds = new ArrayList<>();
        messageHashes = new ArrayList<>();
        
        File file = new File("messages.json");
        if (!file.exists()) {
            System.out.println("No existing messages found. Starting fresh.");
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            String msgId = "", sender = "", recipient = "", content = "", hash = "", status = "";
            int msgNum = 0;
            
            while ((line = br.readLine()) != null) {
                if (line.contains("\"messageId\"")) {
                    msgId = line.split("\"")[3];
                    messageIds.add(msgId);
                } else if (line.contains("\"messageNum\"")) {
                    String[] parts = line.split(":");
                    msgNum = Integer.parseInt(parts[1].trim().replace(",", ""));
                } else if (line.contains("\"sender\"")) {
                    sender = line.split("\"")[3];
                } else if (line.contains("\"recipient\"")) {
                    recipient = line.split("\"")[3];
                } else if (line.contains("\"content\"")) {
                    content = line.split("\"")[3];
                } else if (line.contains("\"messageHash\"")) {
                    hash = line.split("\"")[3];
                    messageHashes.add(hash);
                } else if (line.contains("\"status\"")) {
                    status = line.split("\"")[3];
                    Message m = new Message(msgId, msgNum, recipient, content, hash, sender);
                    m.setStatus(status);
                    messages.add(m);
                    
                    if (status.equals("SENT")) {
                        sentMessages.add(m);
                    } else if (status.equals("STORED")) {
                        storedMessages.add(m);
                    } else if (status.equals("DISREGARDED")) {
                        disregardedMessages.add(m);
                    }
                    
                    if (msgNum > messageCounter) messageCounter = msgNum;
                    if (status.equals("SENT") || status.equals("STORED")) totalMessagesSent++;
                }
            }
            System.out.println("Loaded " + messages.size() + " existing messages.");
            System.out.println("  - Sent: " + sentMessages.size());
            System.out.println("  - Stored: " + storedMessages.size());
            System.out.println("  - Deleted: " + disregardedMessages.size());
        } catch (IOException e) {
            System.out.println("Error loading messages: " + e.getMessage());
        }
    }
}