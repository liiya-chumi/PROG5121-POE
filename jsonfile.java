import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class jsonfile {
    
    public static void main(String[] args) {
        try {
            List<Message> messages = new ArrayList<>();
            
            // Add messages with test data
            Message msg1 = new Message("msg_001", 1, "+27834557896", 
                "Did you get the cake?", "12:1:DIDYOUGETTHECAKE?", "user_123");
            msg1.setStatus("SENT");
            messages.add(msg1);
            
            Message msg2 = new Message("msg_002", 2, "+27838884567", 
                "Where are you? You are late! I have asked you to be on time.", 
                "12:2:WHEREONTIME", "user_456");
            msg2.setStatus("STORED");
            messages.add(msg2);
            
            Message msg3 = new Message("msg_003", 3, "+27834484567", 
                "Yohoooo, I am at your gate.", "12:3:YOHOOGATE", "user_123");
            msg3.setStatus("DISREGARDED");
            messages.add(msg3);
            
            Message msg4 = new Message("msg_004", 4, "+27838884567", 
                "It is dinner time!", "12:4:ITDINNERTIME", "user_789");
            msg4.setStatus("SENT");
            messages.add(msg4);
            
            Message msg5 = new Message("msg_005", 5, "+27838884567", 
                "Ok, I am leaving without you.", "12:5:OKWITHOUTYOU", "user_456");
            msg5.setStatus("STORED");
            messages.add(msg5);
            
            // Save to JSON file using Jackson
            ObjectMapper mapper = new ObjectMapper();
            
            mapper.writeValue(new File("messages.json"), messages);
            
            System.out.println("JSON file created successfully with test data!");
            System.out.println("Created " + messages.size() + " test messages.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    