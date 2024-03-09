package tests;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.Uninterruptibles;

public class ScrollUpDemo {
	
	@Test
	public void scrollTest() {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://google.com");
		driver.findElement(By.name("q")).sendKeys("Bala Murali", Keys.ENTER);
		String tab = Keys.chord(Keys.CONTROL,Keys.RETURN);
		driver.findElement(By.xpath("//a[text()='Wikipedia']")).sendKeys(tab);
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_2);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_2);
			Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_1);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_1);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
}
