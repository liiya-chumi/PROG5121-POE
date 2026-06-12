/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.Serializable;

/**
 *
 * @author Student
 */
public class Message implements Serializable {
    String messageId;
    int messageNum;
    String recipient;
    String content;
    String messageHash;
    String sender;
    String status = "PENDING";
    
    Message(String messageId, int messageNum, String recipient, String content, String messageHash, String sender) {
        this.messageId = messageId;
        this.messageNum = messageNum;
        this.recipient = recipient;
        this.content = content;
        this.messageHash = messageHash;
        this.sender = sender;
    }
    
    // Getters
    public String getMessageId() { return messageId; }
    public int getMessageNum() { return messageNum; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
    public String getMessageHash() { return messageHash; }
    public String getSender() { return sender; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}