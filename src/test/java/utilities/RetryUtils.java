package utilities;


import org.openqa.selenium.StaleElementReferenceException;

public class RetryUtils {


    public static void retryOnStale(Runnable action, int retryCount) {
        int attempts = 0;
        while (attempts < retryCount) {
            try {
                action.run();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            }
        }
        throw new RuntimeException("Failed due to repeated stale element");
    }
	
	
}
