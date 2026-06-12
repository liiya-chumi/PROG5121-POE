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
            System.out.println("Please login through the Registration System first.");
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
            System.out.println("7. View Deleted/Disregarded Messages");
            System.out.println("8. Logout");
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
                    System.out.println("Goodbye " + currentUser + "!");
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
                System.out.print("Recipient number (+ international code): ");
                recipient = scanner.nextLine();
                
                if (recipient.equalsIgnoreCase("exit") || recipient.equalsIgnoreCase("quit")) {
                    System.out.println("Message sending cancelled.");
                    return;
                }
                
                String recipientCheck = checkRecipientCell(recipient);
                System.out.println(recipientCheck);
                
                if (recipientCheck.contains("Success")) {
                    validRecipient = true;
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
            System.out.println(">>> MESSAGE ID: " + messageId + " <<<");
            System.out.println("Message Hash: " + messageHash);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + content);
            System.out.println("From: " + currentUser);
            System.out.println("======================================");
            
            String sentResult = sentMessage();
            System.out.println(sentResult);
            
            if (sentResult.equals("Message successfully sent.")) {
                totalMessagesSent++;
                msg.setStatus("SENT");
                sentMessages.add(msg);
                System.out.println("\n[SUCCESS] Message marked as SENT");
            } else if (sentResult.equals("Press 0 to delete the message.")) {
                msg.setStatus("DISREGARDED");
                disregardedMessages.add(msg);
                messages.remove(msg);
                messageIds.remove(messageId);
                messageHashes.remove(messageHash);
                messageCounter--;
                System.out.println("\n[DELETED] Message disregarded and deleted.");
            } else if (sentResult.equals("Message successfully stored.")) {
                totalMessagesSent++;
                msg.setStatus("STORED");
                storedMessages.add(msg);
                System.out.println("\n[STORED] Message marked as STORED");
            }
            
            storeMessage();
            System.out.println("\n[SUCCESS] Message " + (i + 1) + " processed successfully!");
        }
        
        System.out.println("\n[SUMMARY] Total messages sent this session: " + totalMessagesSent);
        System.out.println("Return total number sent: " + returnTotalMessages());
        System.out.println("\nReturning to main menu...");
    }
    
    // New method to view deleted/disregarded messages
    private static void viewDeletedMessages() {
        if (disregardedMessages.isEmpty()) {
            System.out.println("\n[ERROR] No deleted/disregarded messages found.");
            System.out.println("Messages are deleted when you choose 'Disregard Message' option.");
            return;
        }
        
        System.out.println("\n========== DELETED/DISREGARDED MESSAGES ==========");
        System.out.println("Total deleted messages: " + disregardedMessages.size());
        System.out.println("---------------------------------------------------");
        
        for (int i = 0; i < disregardedMessages.size(); i++) {
            Message m = disregardedMessages.get(i);
            System.out.println("\n--- Deleted Message " + (i + 1) + " ---");
            System.out.println("    Message ID: " + m.getMessageId());
            System.out.println("    Message Hash: " + m.getMessageHash());
            System.out.println("    Sender: " + m.getSender());
            System.out.println("    Recipient: " + m.getRecipient());
            System.out.println("    Content: " + m.getContent());
            System.out.println("    Status: " + m.getStatus());
            System.out.println("    Message Number: " + m.getMessageNum());
            System.out.println("    -----------------------------------------");
        }
        
        // Option to recover a deleted message
        System.out.println("\n---------------------------------------------------");
        System.out.print("Would you like to recover a deleted message? (y/n): ");
        String answer = scanner.nextLine().toLowerCase();
        
        if (answer.equals("y")) {
            recoverDeletedMessage();
        }
        
        System.out.println("===================================================");
    }
    
    // Method to recover a deleted message
    private static void recoverDeletedMessage() {
        if (disregardedMessages.isEmpty()) {
            System.out.println("[ERROR] No deleted messages to recover.");
            return;
        }
        
        System.out.println("\nAvailable deleted messages:");
        for (int i = 0; i < disregardedMessages.size(); i++) {
            Message m = disregardedMessages.get(i);
            System.out.println("   " + (i + 1) + ". ID: " + m.getMessageId() + " - " + m.getContent());
        }
        
        System.out.print("\nEnter message number to recover (1-" + disregardedMessages.size() + "): ");
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
        
        Message recoveredMsg = disregardedMessages.get(choice - 1);
        
        System.out.println("\nRecovery Options:");
        System.out.println("   1. Move to SENT messages");
        System.out.println("   2. Move to STORED messages");
        System.out.println("   3. Keep as deleted");
        System.out.print("Choose option (1-3): ");
        
        int option = 0;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid option. Message not recovered.");
            return;
        }
        
        if (option == 1) {
            recoveredMsg.setStatus("SENT");
            sentMessages.add(recoveredMsg);
            messages.add(recoveredMsg);
            disregardedMessages.remove(choice - 1);
            totalMessagesSent++;
            System.out.println("\n[SUCCESS] Message moved to SENT messages.");
            storeMessage();
        } else if (option == 2) {
            recoveredMsg.setStatus("STORED");
            storedMessages.add(recoveredMsg);
            messages.add(recoveredMsg);
            disregardedMessages.remove(choice - 1);
            totalMessagesSent++;
            System.out.println("\n[SUCCESS] Message moved to STORED messages.");
            storeMessage();
        } else if (option == 3) {
            System.out.println("\n[INFO] Message remains deleted.");
        } else {
            System.out.println("\n[ERROR] Invalid option. Message not recovered.");
        }
    }
    
    // ========== STORED MESSAGES FEATURES ==========
    
    // a. Display the sender and recipient of all stored messages
    private static void displaySendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            System.out.println("\n[ERROR] No stored messages found.");
            System.out.println("Send a message and choose 'Store Message' option to see it here.");
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
    
    // b. Display the longest stored message
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
    
    // c. Search for a message ID and display recipient and message
    private static void searchMessageByIdStored() {
        if (storedMessages.isEmpty() && sentMessages.isEmpty()) {
            System.out.println("\n[ERROR] No messages have been sent yet.");
            return;
        }
        
        System.out.println("\nAvailable Message IDs in STORED messages:");
        if (storedMessages.isEmpty()) {
            System.out.println("   No stored messages available.");
        } else {
            for (Message m : storedMessages) {
                System.out.println("   * " + m.getMessageId());
            }
        }
        
        System.out.print("\nEnter Message ID to search: ");
        String searchId = scanner.nextLine();
        
        boolean found = false;
        
        // Search in stored messages first
        for (Message m : storedMessages) {
            if (m.getMessageId().equals(searchId)) {
                System.out.println("\n========== MESSAGE FOUND ==========");
                System.out.println("Recipient: " + m.getRecipient());
                System.out.println("Message: " + m.getContent());
                System.out.println("Sender: " + m.getSender());
                System.out.println("Message Hash: " + m.getMessageHash());
                System.out.println("====================================");
                found = true;
                break;
            }
        }
        
        // If not found in stored, check sent messages
        if (!found) {
            for (Message m : sentMessages) {
                if (m.getMessageId().equals(searchId)) {
                    System.out.println("\n========== MESSAGE FOUND (in SENT messages) ==========");
                    System.out.println("Recipient: " + m.getRecipient());
                    System.out.println("Message: " + m.getContent());
                    System.out.println("Sender: " + m.getSender());
                    System.out.println("Message Hash: " + m.getMessageHash());
                    System.out.println("=====================================================");
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            System.out.println("\n[ERROR] Message with ID '" + searchId + "' not found.");
        }
    }
    
    // d. Search for all messages stored for a particular recipient
    private static void searchMessagesByRecipient() {
        System.out.print("\nEnter recipient number to search for: ");
        String recipient = scanner.nextLine();
        
        List<Message> foundMessages = new ArrayList<>();
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(recipient)) {
                foundMessages.add(m);
            }
        }
        
        if (foundMessages.isEmpty()) {
            System.out.println("\n[ERROR] No stored messages found for recipient: " + recipient);
        } else {
            System.out.println("\n========== MESSAGES FOR RECIPIENT " + recipient + " ==========");
            for (int i = 0; i < foundMessages.size(); i++) {
                Message m = foundMessages.get(i);
                System.out.println((i + 1) + ". " + m.getContent());
                System.out.println("   (ID: " + m.getMessageId() + ", Hash: " + m.getMessageHash() + ")");
            }
            System.out.println("============================================================");
        }
    }
    
    // e. Delete a message using the message hash
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
        
        // Search in stored messages
        Iterator<Message> iter = storedMessages.iterator();
        while (iter.hasNext()) {
            Message m = iter.next();
            if (m.getMessageHash().equals(hashToDelete)) {
                deletedMessage = m;
                iter.remove();
                messageHashes.remove(hashToDelete);
                messageIds.remove(m.getMessageId());
                found = true;
                break;
            }
        }
        
        // If not found in stored, check sent messages
        if (!found) {
            Iterator<Message> sentIter = sentMessages.iterator();
            while (sentIter.hasNext()) {
                Message m = sentIter.next();
                if (m.getMessageHash().equals(hashToDelete)) {
                    deletedMessage = m;
                    sentIter.remove();
                    messageHashes.remove(hashToDelete);
                    messageIds.remove(m.getMessageId());
                    found = true;
                    break;
                }
            }
        }
        
        // Also remove from main messages list
        if (deletedMessage != null) {
            messages.remove(deletedMessage);
        }
        
        if (found && deletedMessage != null) {
            // Add to disregarded messages
            deletedMessage.setStatus("DISREGARDED");
            disregardedMessages.add(deletedMessage);
            System.out.println("\n[SUCCESS] Message: \"" + deletedMessage.getContent() + "\" successfully deleted.");
            storeMessage();
        } else if (!found) {
            System.out.println("\n[ERROR] Message with hash '" + hashToDelete + "' not found.");
        }
    }
    
    // f. Display a report that lists the full details of all the stored messages
    private static void displayFullReport() {
        if (storedMessages.isEmpty()) {
            System.out.println("\n[ERROR] No stored messages found.");
            return;
        }
        
        System.out.println("\n========== FULL STORED MESSAGES REPORT ==========");
        System.out.printf("%-3s %-12s %-12s %-12s %-30s %-10s%n", 
                         "#", "Message ID", "Message Hash", "Sender", "Message Content", "Status");
        System.out.println("================================================================================================");
        
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            String shortContent = m.getContent().length() > 25 ? 
                                 m.getContent().substring(0, 25) + "..." : 
                                 m.getContent();
            String shortHash = m.getMessageHash().length() > 12 ? 
                              m.getMessageHash().substring(0, 12) : 
                              m.getMessageHash();
            System.out.printf("%-3d %-12s %-12s %-12s %-30s %-10s%n", 
                             (i + 1), 
                             m.getMessageId(), 
                             shortHash,
                             m.getSender(), 
                             shortContent, 
                             m.getStatus());
        }
        
        // Detailed view
        System.out.println("\n================================================================================");
        System.out.println("\nDETAILED VIEW:");
        System.out.println("--------------------------------------------------------------------------------");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            System.out.println("\n--- Message " + (i + 1) + " ---");
            System.out.println("    Message ID: " + m.getMessageId());
            System.out.println("    Message Hash: " + m.getMessageHash());
            System.out.println("    Sender: " + m.getSender());
            System.out.println("    Recipient: " + m.getRecipient());
            System.out.println("    Content: " + m.getContent());
            System.out.println("    Status: " + m.getStatus());
            System.out.println("    Message Number: " + m.getMessageNum());
            System.out.println("    ----------------------------------------");
        }
        System.out.println("================================================");
    }
    
    // ========== HELPER METHODS ==========
    
    private static void viewAllMessageIds() {
        if (messageIds.isEmpty()) {
            System.out.println("\n[ERROR] No messages sent yet. Send a message first to get a Message ID.");
        } else {
            System.out.println("\n========== ALL MESSAGE IDs ==========");
            System.out.println("You have " + messageIds.size() + " message(s) stored.");
            System.out.println("\nMessage IDs you can search for:");
            for (int i = 0; i < messageIds.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + messageIds.get(i));
            }
            System.out.println("\nUse option 4 and enter any of these IDs to search.");
            System.out.println("=====================================");
        }
    }
    
    private static String checkRecipientCell(String recipient) {
        if (recipient == null || recipient.isEmpty()) {
            return "Failure: Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        if (recipient.matches("^\\+[0-9]{10,13}$")) {
            return "Success: Cell phone number successfully captured.";
        } else {
            return "Failure: Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
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
        
        String prefix = msgId.length() >= 2 ? msgId.substring(0, 2) : msgId;
        if (prefix.length() < 2) {
            prefix = "00";
        }
        
        String combined = (firstWord + lastWord).toUpperCase();
        
        return prefix + ":" + msgNum + ":" + combined;
    }
    
    private static String sentMessage() {
        System.out.println("\nOptions:");
        System.out.println("   1. Send Message");
        System.out.println("   2. Disregard Message");
        System.out.println("   3. Store Message");
        System.out.print("Choose (1-3): ");
        
        int option = 0;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid option! Defaulting to Store.");
            return "Message successfully stored.";
        }
        
        if (option == 1) {
            return "Message successfully sent.";
        } else if (option == 2) {
            return "Press 0 to delete the message.";
        } else if (option == 3) {
            return "Message successfully stored.";
        } else {
            System.out.println("Invalid option! Defaulting to Store.");
            return "Message successfully stored.";
        }
    }
    
    private static void printMessages() {
        if (messages.isEmpty()) {
            System.out.println("\n[ERROR] No messages sent yet.");
        } else {
            System.out.println("\n========== RECENT MESSAGES ==========");
            int displayCount = Math.min(10, messages.size());
            System.out.println("Showing " + displayCount + " most recent messages:");
            
            for (int i = messages.size() - displayCount; i < messages.size(); i++) {
                Message m = messages.get(i);
                System.out.println("\n--- Message " + (i + 1) + " ---");
                System.out.println("Message ID: " + m.getMessageId());
                System.out.println("Message Hash: " + m.getMessageHash());
                System.out.println("Recipient: " + m.getRecipient());
                String shortContent = m.getContent().length() > 50 ? 
                                     m.getContent().substring(0, 50) + "..." : 
                                     m.getContent();
                System.out.println("Message: " + shortContent);
                System.out.println("From: " + m.getSender());
                System.out.println("Status: " + m.getStatus());
                System.out.println("------------------------");
            }
        }
    }
    
    private static void searchMessageById() {
        if (messageIds.isEmpty()) {
            System.out.println("\n[ERROR] No messages have been sent yet. Send a message first to get a Message ID.");
            return;
        }
        
        System.out.println("\nAvailable Message IDs: " + messageIds);
        System.out.print("\nEnter Message ID to search: ");
        String searchId = scanner.nextLine();
        
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
            System.out.println("Available IDs are: " + messageIds);
        }
    }
    
    private static int returnTotalMessages() {
        return totalMessagesSent;
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
            System.out.println("  - Disregarded: " + disregardedMessages.size());
        } catch (IOException e) {
            System.out.println("Error loading messages: " + e.getMessage());
        }
    }

    static void readMessages() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}