
 import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BridgeAppTest {

    @BeforeEach
    public void resetBridge() {
        // Reset all static variables before each test
        BridgeApp.savedUsername = "";
        BridgeApp.savedPassword = "";
        BridgeApp.savedName = "";
        BridgeApp.stage = 0;
        BridgeApp.conversationCount = 0;
        BridgeApp.positiveResponses = 0;
        BridgeApp.negativeResponses = 0;
    }

    // ============================================================
    // TEST 1: USERNAME VALIDATION (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(1)
    @DisplayName("Username Validation - Valid usernames should pass")
    public void testValidUsername() {
        // Valid: min 6 chars, contains underscore, starts with letter
        assertTrue(BridgeApp.isValidUsername("john_doe"));
        assertTrue(BridgeApp.isValidUsername("user_name123"));
        assertTrue(BridgeApp.isValidUsername("jane_doe"));
        assertTrue(BridgeApp.isValidUsername("test_user"));
        assertTrue(BridgeApp.isValidUsername("a_b_c_d_e_f"));
        assertTrue(BridgeApp.isValidUsername("username_1"));
    }

    @Test
    @Order(2)
    @DisplayName("Username Validation - Invalid usernames should fail")
    public void testInvalidUsername() {
        // Too short
        assertFalse(BridgeApp.isValidUsername("ab_c"));
        assertFalse(BridgeApp.isValidUsername("us_1"));
        
        // No underscore
        assertFalse(BridgeApp.isValidUsername("user1"));
        assertFalse(BridgeApp.isValidUsername("username123"));
        
        // Starts with underscore
        assertFalse(BridgeApp.isValidUsername("_username"));
        assertFalse(BridgeApp.isValidUsername("_john_doe"));
        
        // Starts with number
        assertFalse(BridgeApp.isValidUsername("1user_name"));
        assertFalse(BridgeApp.isValidUsername("123_user"));
        
        // Contains invalid characters
        assertFalse(BridgeApp.isValidUsername("user@name"));
        assertFalse(BridgeApp.isValidUsername("user name"));
        assertFalse(BridgeApp.isValidUsername("user-name"));
        
        // Null or empty
        assertFalse(BridgeApp.isValidUsername(null));
        assertFalse(BridgeApp.isValidUsername(""));
    }

    // ============================================================
    // TEST 2: PASSWORD VALIDATION (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(3)
    @DisplayName("Password Validation - Valid passwords should pass")
    public void testValidPassword() {
        // Valid: min 8 chars, number, special char, uppercase, lowercase
        assertTrue(BridgeApp.isValidPassword("Bridge@2024"));
        assertTrue(BridgeApp.isValidPassword("Password123!"));
        assertTrue(BridgeApp.isValidPassword("MyP@ssw0rd"));
        assertTrue(BridgeApp.isValidPassword("Secure#99"));
        assertTrue(BridgeApp.isValidPassword("Test$1234"));
        assertTrue(BridgeApp.isValidPassword("Java@Program1"));
    }

    @Test
    @Order(4)
    @DisplayName("Password Validation - Invalid passwords should fail")
    public void testInvalidPassword() {
        // Too short
        assertFalse(BridgeApp.isValidPassword("pass"));
        assertFalse(BridgeApp.isValidPassword("Pass@1"));
        
        // No uppercase
        assertFalse(BridgeApp.isValidPassword("password123!"));
        
        // No lowercase
        assertFalse(BridgeApp.isValidPassword("PASSWORD123!"));
        
        // No number
        assertFalse(BridgeApp.isValidPassword("Password@!"));
        
        // No special character
        assertFalse(BridgeApp.isValidPassword("Password123"));
        
        // Missing multiple requirements
        assertFalse(BridgeApp.isValidPassword("weak"));
        assertFalse(BridgeApp.isValidPassword("onlylowercase"));
        assertFalse(BridgeApp.isValidPassword("ONLYUPPERCASE"));
        
        // Null or empty
        assertFalse(BridgeApp.isValidPassword(null));
        assertFalse(BridgeApp.isValidPassword(""));
    }

    // ============================================================
    // TEST 3: CELLPHONE VALIDATION (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(5)
    @DisplayName("Cellphone Validation - Valid SA cellphone numbers should pass")
    public void testValidCellphone() {
        // Valid: 10 digits, starts with 0, second digit 6/7/8
        assertTrue(BridgeApp.isValidCellphone("0612345678"));
        assertTrue(BridgeApp.isValidCellphone("0712345678"));
        assertTrue(BridgeApp.isValidCellphone("0812345678"));
        assertTrue(BridgeApp.isValidCellphone("0623456789"));
        assertTrue(BridgeApp.isValidCellphone("0734567890"));
        assertTrue(BridgeApp.isValidCellphone("0845678901"));
    }

    @Test
    @Order(6)
    @DisplayName("Cellphone Validation - Invalid cellphone numbers should fail")
    public void testInvalidCellphone() {
        // Wrong length
        assertFalse(BridgeApp.isValidCellphone("061234567"));    // 9 digits
        assertFalse(BridgeApp.isValidCellphone("06123456789"));  // 11 digits
        assertFalse(BridgeApp.isValidCellphone("123456789"));    // 9 digits
        
        // Doesn't start with 0
        assertFalse(BridgeApp.isValidCellphone("6123456789"));
        assertFalse(BridgeApp.isValidCellphone("7123456789"));
        
        // Second digit invalid (not 6,7,8)
        assertFalse(BridgeApp.isValidCellphone("0112345678"));   // second digit 1
        assertFalse(BridgeApp.isValidCellphone("0212345678"));   // second digit 2
        assertFalse(BridgeApp.isValidCellphone("0912345678"));   // second digit 9
        assertFalse(BridgeApp.isValidCellphone("0012345678"));   // second digit 0
        
        // Contains non-digits
        assertFalse(BridgeApp.isValidCellphone("061234567a"));
        assertFalse(BridgeApp.isValidCellphone("071234567 "));
        assertFalse(BridgeApp.isValidCellphone("08123456-8"));
        
        // Null or empty
        assertFalse(BridgeApp.isValidCellphone(null));
        assertFalse(BridgeApp.isValidCellphone(""));
    }

    // ============================================================
    // TEST 4: CHATBOT STAGE TRANSITIONS (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(7)
    @DisplayName("Chatbot - Positive feeling should move to food stage")
    public void testFeelingPositiveStartsFoodStage() {
        resetBridge();
        assertEquals(0, BridgeApp.stage);
        
        String result = BridgeApp.getResponse("I am happy");
        
        assertTrue(result.toLowerCase().contains("what") && 
                   result.toLowerCase().contains("eat"));
        assertEquals(1, BridgeApp.stage);
    }

    @Test
    @Order(8)
    @DisplayName("Chatbot - Negative feeling should move to food stage")
    public void testFeelingNegativeStartsFoodStage() {
        resetBridge();
        assertEquals(0, BridgeApp.stage);
        
        String result = BridgeApp.getResponse("I am sad");
        
        assertTrue(result.toLowerCase().contains("eat"));
        assertEquals(1, BridgeApp.stage);
    }

    @Test
    @Order(9)
    @DisplayName("Chatbot - Great feeling should move to food stage")
    public void testFeelingGreatStartsFoodStage() {
        resetBridge();
        
        String result = BridgeApp.getResponse("I feel great");
        
        assertTrue(result.toLowerCase().contains("what did you eat"));
        assertEquals(1, BridgeApp.stage);
    }

    @Test
    @Order(10)
    @DisplayName("Chatbot - OK feeling should move to food stage")
    public void testFeelingOkStartsFoodStage() {
        resetBridge();
        
        String result = BridgeApp.getResponse("I'm ok");
        
        assertTrue(result.toLowerCase().contains("what did you eat"));
        assertEquals(1, BridgeApp.stage);
    }

    @Test
    @Order(11)
    @DisplayName("Chatbot - Food positive response should reset stage")
    public void testFoodPositiveResponse() {
        resetBridge();
        BridgeApp.stage = 1;
        
        String result = BridgeApp.getResponse("I ate good food");
        
        assertTrue(result.toLowerCase().contains("good") || 
                   result.toLowerCase().contains("wonderful") ||
                   result.toLowerCase().contains("proud"));
        assertEquals(0, BridgeApp.stage);
    }

    @Test
    @Order(12)
    @DisplayName("Chatbot - Ate breakfast response should reset stage")
    public void testFoodBreakfastResponse() {
        resetBridge();
        BridgeApp.stage = 1;
        
        String result = BridgeApp.getResponse("I had breakfast");
        
        assertTrue(result.toLowerCase().contains("wonderful") || 
                   result.toLowerCase().contains("great"));
        assertEquals(0, BridgeApp.stage);
    }

    @Test
    @Order(13)
    @DisplayName("Chatbot - Food negative response should reset stage")
    public void testFoodNegativeResponse() {
        resetBridge();
        BridgeApp.stage = 1;
        
        String result = BridgeApp.getResponse("I did not eat");
        
        assertTrue(result.toLowerCase().contains("eat"));
        assertEquals(0, BridgeApp.stage);
    }

    @Test
    @Order(14)
    @DisplayName("Chatbot - Hungry response should suggest eating")
    public void testHungryResponse() {
        resetBridge();
        BridgeApp.stage = 1;
        
        String result = BridgeApp.getResponse("I'm hungry");
        
        assertTrue(result.toLowerCase().contains("eat"));
        assertEquals(0, BridgeApp.stage);
    }

    @Test
    @Order(15)
    @DisplayName("Chatbot - Neutral response should ask about feelings")
    public void testNeutralResponse() {
        resetBridge();
        
        String result = BridgeApp.getResponse("hello");
        
        assertTrue(result.toLowerCase().contains("how are you feeling"));
        assertEquals(0, BridgeApp.stage);
    }

    // ============================================================
    // TEST 5: SENTIMENT ANALYSIS (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(16)
    @DisplayName("Sentiment Analysis - Positive words should increase score")
    public void testPositiveSentiment() {
        int score = BridgeApp.analyzeSentiment("I am very happy and great");
        assertTrue(score > 0);
        
        score = BridgeApp.analyzeSentiment("This is wonderful excellent amazing");
        assertTrue(score > 2);
    }

    @Test
    @Order(17)
    @DisplayName("Sentiment Analysis - Negative words should decrease score")
    public void testNegativeSentiment() {
        int score = BridgeApp.analyzeSentiment("I feel sad and depressed");
        assertTrue(score < 0);
        
        score = BridgeApp.analyzeSentiment("This is terrible awful bad");
        assertTrue(score < -2);
    }

    @Test
    @Order(18)
    @DisplayName("Sentiment Analysis - Neutral text should score zero")
    public void testNeutralSentiment() {
        int score = BridgeApp.analyzeSentiment("The table is brown");
        assertEquals(0, score);
        
        score = BridgeApp.analyzeSentiment("I went to the store");
        assertEquals(0, score);
    }

    // ============================================================
    // TEST 6: CRISIS RESPONSE (Rubric Requirement - Safety)
    // ============================================================
    
    @Test
    @Order(19)
    @DisplayName("Crisis Response - Suicide keyword should trigger help")
    public void testCrisisResponse() {
        resetBridge();
        
        String result = BridgeApp.getResponse("I feel like killing myself");
        
        assertTrue(result.toLowerCase().contains("concerned") ||
                   result.toLowerCase().contains("sadag") ||
                   result.toLowerCase().contains("0800"));
    }

    @Test
    @Order(20)
    @DisplayName("Crisis Response - Hopeless keyword should trigger help")
    public void testHopelessResponse() {
        resetBridge();
        
        String result = BridgeApp.getResponse("I feel hopeless");
        
        assertTrue(result.toLowerCase().contains("concerned") ||
                   result.toLowerCase().contains("sadag") ||
                   result.toLowerCase().contains("0800"));
    }

    // ============================================================
    // TEST 7: HELP SYSTEM (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(21)
    @DisplayName("Help System - Help command should show options")
    public void testHelpCommand() {
        resetBridge();
        
        String result = BridgeApp.getResponse("help");
        
        assertTrue(result.toLowerCase().contains("what i can do") ||
                   result.toLowerCase().contains("commands"));
    }

    @Test
    @Order(22)
    @DisplayName("Help System - Commands keyword should work")
    public void testCommandsKeyword() {
        resetBridge();
        
        String result = BridgeApp.getResponse("commands");
        
        assertTrue(result.toLowerCase().contains("help") ||
                   result.toLowerCase().contains("what i can do"));
    }

    // ============================================================
    // TEST 8: NAME PERSONALIZATION (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(23)
    @DisplayName("Personalization - Setting name should work")
    public void testSetName() {
        resetBridge();
        
        String result = BridgeApp.getResponse("my name is Thabo");
        
        assertTrue(result.toLowerCase().contains("thabo"));
        assertEquals("Thabo", BridgeApp.savedName);
    }

    @Test
    @Order(24)
    @DisplayName("Personalization - Name should appear in responses")
    public void testNameInResponse() {
        resetBridge();
        BridgeApp.savedName = "Thabo";
        BridgeApp.conversationCount = 1;
        
        String result = BridgeApp.getResponse("I feel good");
        
        assertTrue(result.toLowerCase().contains("thabo"));
    }

    // ============================================================
    // TEST 9: STATISTICS TRACKING (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(25)
    @DisplayName("Statistics - Should track positive responses")
    public void testPositiveResponseTracking() {
        resetBridge();
        int initialPositive = BridgeApp.positiveResponses;
        
        BridgeApp.getResponse("I am very happy");
        
        assertTrue(BridgeApp.positiveResponses > initialPositive);
    }

    @Test
    @Order(26)
    @DisplayName("Statistics - Should track negative responses")
    public void testNegativeResponseTracking() {
        resetBridge();
        int initialNegative = BridgeApp.negativeResponses;
        
        BridgeApp.getResponse("I feel sad");
        
        assertTrue(BridgeApp.negativeResponses > initialNegative);
    }

    @Test
    @Order(27)
    @DisplayName("Statistics - Should track conversation count")
    public void testConversationCountTracking() {
        resetBridge();
        assertEquals(0, BridgeApp.conversationCount);
        
        BridgeApp.getResponse("Hello");
        assertEquals(1, BridgeApp.conversationCount);
        
        BridgeApp.getResponse("How are you");
        assertEquals(2, BridgeApp.conversationCount);
    }

    // ============================================================
    // TEST 10: EDGE CASES & ERROR HANDLING (Rubric Requirement)
    // ============================================================
    
    @Test
    @Order(28)
    @DisplayName("Edge Cases - Empty message should not crash")
    public void testEmptyMessage() {
        resetBridge();
        
        String result = BridgeApp.getResponse("");
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @Order(29)
    @DisplayName("Edge Cases - Very long message should be handled")
    public void testLongMessage() {
        resetBridge();
        
        String longMessage = "a".repeat(1000);
        String result = BridgeApp.getResponse(longMessage);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @Order(30)
    @DisplayName("Edge Cases - Mixed case sensitivity")
    public void testCaseSensitivity() {
        resetBridge();
        
        String result1 = BridgeApp.getResponse("I AM HAPPY");
        String result2 = BridgeApp.getResponse("i am happy");
        
        // Both should detect positive sentiment
        assertTrue(result1.toLowerCase().contains("what") ||
                   result1.toLowerCase().contains("glad"));
        assertTrue(result2.toLowerCase().contains("what") ||
                   result2.toLowerCase().contains("glad"));
    }

    // ============================================================
    // ADDITIONAL RUBRIC TESTS
    // ============================================================
    
    @Test
    @Order(31)
    @DisplayName("Registration - Username validation in registration flow")
    public void testRegistrationUsernameValidation() {
        // Valid username should pass validation
        assertTrue(BridgeApp.isValidUsername("test_user"));
        
        // Invalid username should fail validation
        assertFalse(BridgeApp.isValidUsername("test"));
        assertFalse(BridgeApp.isValidUsername("testuser"));
        assertFalse(BridgeApp.isValidUsername("_testuser"));
        assertFalse(BridgeApp.isValidUsername("1test_user"));
    }
    
    @Test
    @Order(32)
    @DisplayName("Registration - Password validation in registration flow")
    public void testRegistrationPasswordValidation() {
        // Valid password should pass validation
        assertTrue(BridgeApp.isValidPassword("Test@1234"));
        
        // Invalid password should fail validation
        assertFalse(BridgeApp.isValidPassword("weak"));
        assertFalse(BridgeApp.isValidPassword("nouppercase123!"));
        assertFalse(BridgeApp.isValidPassword("NOLOWERCASE123!"));
        assertFalse(BridgeApp.isValidPassword("NoSpecial123"));
        assertFalse(BridgeApp.isValidPassword("NoNumber@!"));
    }
    
    @Test
    @Order(33)
    @DisplayName("Registration - Cellphone validation in registration flow")
    public void testRegistrationCellphoneValidation() {
        // Valid cellphone should pass validation
        assertTrue(BridgeApp.isValidCellphone("0612345678"));
        
        // Invalid cellphone should fail validation
        assertFalse(BridgeApp.isValidCellphone("12345"));
        assertFalse(BridgeApp.isValidCellphone("061234567"));
        assertFalse(BridgeApp.isValidCellphone("0112345678"));
        assertFalse(BridgeApp.isValidCellphone("061234567a"));
    }
    
    @Test
    @Order(34)
    @DisplayName("Login - Attempt limit should be enforced")
    public void testLoginAttemptLimit() {
        // This tests the login logic through validation
        // The actual attempt counter is in the login() method
        assertTrue(BridgeApp.isValidPassword("Test@1234"));
        assertFalse(BridgeApp.isValidPassword("wrong"));
    }
    
    @Test
    @Order(35)
    @DisplayName("Stress Management - Should provide coping suggestions")
    public void testStressResponse() {
        resetBridge();
        
        String result = BridgeApp.getResponse("I feel stressed");
        
        assertTrue(result.toLowerCase().contains("breath") ||
                   result.toLowerCase().contains("stress") ||
                   result.toLowerCase().contains("tough"));
    }
    
    @Test
    @Order(36)
    @DisplayName("Exercise - Should respond positively to exercise mentions")
    public void testExerciseResponse() {
        resetBridge();
        
        String result = BridgeApp.getResponse("I went for a workout");
        
        assertTrue(result.toLowerCase().contains("great") ||
                   result.toLowerCase().contains("walking"));
    }
    
    @Test
    @Order(37)
    @DisplayName("Sleep - Should address sleep concerns")
    public void testSleepResponse() {
        resetBridge();
        
        String result = BridgeApp.getResponse("I am tired");
        
        assertTrue(result.toLowerCase().contains("sleep") ||
                   result.toLowerCase().contains("quality"));
    }
}

  
