
 import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BridgeAppTest {

    // ===== USERNAME =====
    @Test
    public void testValidUsername() {
        assertTrue(BridgeApp.isValidUsername("user_1"));
    }

    @Test
    public void testInvalidUsername() {
        assertFalse(BridgeApp.isValidUsername("user1"));
    }

    // ===== PASSWORD =====
    @Test
    public void testValidPassword() {
        assertTrue(BridgeApp.isValidPassword("Pass@123"));
    }

    @Test
    public void testInvalidPassword() {
        assertFalse(BridgeApp.isValidPassword("pass"));
    }

    // ===== CELLPHONE =====
    @Test
    public void testValidCellphone() {
        assertTrue(BridgeApp.isValidCellphone("0747474547"));
    }

    @Test
    public void testInvalidCellphone() {
        assertFalse(BridgeApp.isValidCellphone("123"));
    }

    // ===== CHATBOT =====
   
    @Test
    public void testFeelingPositiveStartsFoodStage() {
        resetBridge();

        String result = BridgeApp.getResponse("I am happy");

        assertTrue(result.toLowerCase().contains("what did you eat"));
        assertEquals(1, BridgeApp.stage);
    }

    @Test
    public void testFeelingNegativeStartsFoodStage() {
        resetBridge();

        String result = BridgeApp.getResponse("I am sad");

        assertTrue(result.toLowerCase().contains("eat"));
        assertEquals(1, BridgeApp.stage);
    }

    @Test
    public void testFoodPositiveResponse() {
        resetBridge();
        BridgeApp.stage = 1;

        String result = BridgeApp.getResponse("I ate good food");

        assertTrue(result.toLowerCase().contains("good"));
        assertEquals(0, BridgeApp.stage);
    }

    @Test
    public void testFoodNegativeResponse() {
        resetBridge();
        BridgeApp.stage = 1;

        String result = BridgeApp.getResponse("I did not eat");

        assertTrue(result.toLowerCase().contains("eat"));
        assertEquals(0, BridgeApp.stage);
    }

    @Test
    public void testNeutralResponse() {
        resetBridge();

        String result = BridgeApp.getResponse("hello");

        assertTrue(result.toLowerCase().contains("how are you feeling"));
    }
}
  
