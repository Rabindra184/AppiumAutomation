import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * @author Rabindra Biswal
 * @since 09/09/23 7:09 pm
 */

public class OTPTest {

    private static final Duration TIMEOUT = Duration.ofMinutes(5);
    private static final Duration POLLING_INTERVAL = Duration.ofSeconds(10);

    @SneakyThrows
    private static String waitForOTP(AndroidDriver driver, String otpPattern, Duration timeout) {
        Instant startTime = Instant.now();
        while (Duration.between(startTime, Instant.now()).compareTo(timeout) <= 0) {
            String data = String.valueOf(driver.executeScript("mobile: listSms", 1));
            Matcher matcher = Pattern.compile(otpPattern).matcher(data);

            if (matcher.find()) {
                return matcher.group(1);
            }

            sleep(POLLING_INTERVAL.toMillis());
        }

        return null;
    }

    @Test
    @SneakyThrows
    public void shouldRetrieveOTPFromApp() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        options.setDeviceName("PIXEL_5_API");
        options.setPlatformName(MobilePlatform.ANDROID);
        options.setAppActivity("com.swaglabsmobileapp.MainActivity");
        options.setApp("/Users/rabindrabiswal/Workspace/AppiumAutomation/src/main/resources/sauce.apk");

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.openNotifications();

        String otpPattern = "Your OTP for DummyApp: (\\d+)";

        String otp = waitForOTP(driver, otpPattern, TIMEOUT);

        if (otp != null) {
            System.out.println("OTP: " + otp);
        } else {
            System.out.println("OTP not found.");
        }
    }
}




