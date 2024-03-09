package tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.Uninterruptibles;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MaxHealthcareTest {

	@Test
	public void maxHealthcareTest() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://www.maxhealthcare.in/");
		WebElement subscribe = driver.findElement(By.xpath("//button[normalize-space()='No, Thanks']"));

		try {
			if (subscribe.isDisplayed()) {
				subscribe.click();
			}
		} catch (NoSuchElementException e) {

		}

		WebElement search = driver.findElement(
				By.xpath("//div[contains(text(),'Search for')]/following-sibling::div/input[@type='text']"));
		search.sendKeys("CARD");
		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(5));
		List<WebElement> speciality = driver.findElements(By.xpath(
				"(//div/label/strong[contains(text(),'Select ')])[2]/preceding::div/label/input[@type='checkbox']/parent::label"));
		Actions actions = new Actions(driver);
		for (int i = 0; i < speciality.size(); i++) {
			String special = speciality.get(i).getText().trim();
			if (special.equalsIgnoreCase("Cardiac Surgery (CTVS)") || special.equalsIgnoreCase("Cardiology")
					|| special.equalsIgnoreCase("Paediatric (Ped) Cardiology")) {
				actions.moveToElement(speciality.get(i)).click().build().perform();
				Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
			}
		}
		// "//div/child::span[text()='+ 2 more...']"
		WebElement fieldValue = driver.findElement(By.xpath("//div/child::div[3]/child::span"));
		String text = fieldValue.getText();
		// System.out.println(text);
		Assert.assertTrue(text.equalsIgnoreCase("+ 2 more..."));
		WebElement clearField = driver.findElement(By.xpath("//span[@title='Clear all']/span[@class='ng-clear']"));
		actions.moveToElement(clearField).click().build().perform();
		// System.out.println(speciality.size());

		boolean flag = false;
		for (int i = 0; i < speciality.size(); i++) {
			WebElement specialEle = speciality.get(i);
			if (specialEle.isSelected()) {
				flag = true;
				break;
			}
		}
		Assert.assertFalse(flag);
		for (int i = 0; i < speciality.size(); i++) {
			String special = speciality.get(i).getText().trim();
			if (special.equalsIgnoreCase("Cardiac Surgery (CTVS)")) {
				actions.moveToElement(speciality.get(i)).click().build().perform();
			}
		}

		WebElement applay = driver.findElement(By.xpath("//div[contains(@class,'search-header')]/child::button"));
		actions.moveToElement(applay).click().build().perform();

		WebElement locationField = driver
				.findElement(By.xpath("//div[text()='Select Location']/following-sibling::div/child::input"));
		locationField.click();
		
		WebElement city = driver.findElement(By.xpath("//input[@type='checkbox' and @id='item-0']"));
		actions.moveToElement(city).click().build().perform();
		List<WebElement> hospitals = driver.findElements(By.xpath(
				"//input[@id='item-7']/preceding::div/label/input[@type='checkbox' and not(contains(@id,'item-0'))]/parent::label/child::input"));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(4));
		boolean hospitalFlag = true;
		
		for (int i = 0; i < hospitals.size(); i++) {
			WebElement hospital = hospitals.get(i);
			//System.out.println("Is checkbox checked: "+jse.executeScript("return arguments[0].checked", hospital));
			if ((hospital.isSelected()) == false) {
				hospitalFlag = false;
				break;
			}
		}
		Assert.assertTrue(hospitalFlag);
	}
}
