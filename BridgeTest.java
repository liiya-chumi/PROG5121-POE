
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
    public void testPositiveResponse() {
        String result = BridgeApp.getResponse("I am happy");
        assertTrue(result.toLowerCase().contains("glad"));
    }

    @Test
    public void testNegativeResponse() {
        String result = BridgeApp.getResponse("I am sad");
        assertTrue(result.toLowerCase().contains("sorry"));
    }

    @Test
    public void testNeutralResponse() {
        String result = BridgeApp.getResponse("hello");
        assertTrue(result.toLowerCase().contains("tell me more"));
    }
}
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
