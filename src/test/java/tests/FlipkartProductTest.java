package tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.Uninterruptibles;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FlipkartProductTest {

	@Test
	public void flipkartProductTest() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.flipkart.com/");
		WebElement search = driver.findElement(By.name("q"));
		String product = "fridge";
		for (int i = 0; i < product.length(); i++) {
			StringBuilder sb = new StringBuilder();
			String strChar = sb.append(product.charAt(i)).toString();
			search.sendKeys(strChar);
		}

		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(5));
		List<WebElement> proElements = driver.findElements(By.xpath("//li//a/child::div[2]/child::span/parent::div"));
		// System.out.println(proElements.size());
		for (WebElement pro : proElements) {
			String productName = pro.getAttribute("innerHTML").replaceAll("<[^>]*>", " ").replace("  ", " ").trim();
			// System.out.println(productName);
			if (productName.contains("fridge single door in Refrigerators")) {
				pro.click();
				break;
			}
		}
		// System.exit(0);
		driver.findElement(By.xpath("(//div[@style='height: 200px; width: 200px;']/img)[1]")).click();
		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(5));
		String parentWin = driver.getWindowHandle();
		Set<String> windowHandles = driver.getWindowHandles();

		for (String win : windowHandles) {
			if (!win.equals(parentWin)) {
				driver.switchTo().window(win);
				WebElement pincode = driver.findElement(By.id("pincodeInputId"));
				pincode.sendKeys("110092");
				driver.findElement(By.xpath("//span[text()='Check']")).click();
				Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));
				driver.close();
			}
		}

		driver.switchTo().window(parentWin);
		Actions actions = new Actions(driver);
		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));
		WebElement brand = driver.findElement(By.xpath("//div[@title='SAMSUNG']//input[@type='checkbox']"));
		actions.moveToElement(brand).click().build().perform();

		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
		driver.findElement(By.xpath("//div[text()='Capacity']")).click();
		WebElement capacity = driver.findElement(By.xpath("//div[text()='301 - 400 L']/preceding-sibling::input"));
		actions.moveToElement(capacity).click().build().perform();
		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
		String resultStr = driver.findElement(By.xpath("//span[contains(text(),'Showing')]")).getText();
		String totalProductsStr = resultStr.substring(resultStr.lastIndexOf("of") + 3,
				resultStr.lastIndexOf("results") - 1);
		int totalProducts = Integer.parseInt(totalProductsStr);
		System.out.println("Total Products : " + totalProducts);

		WebElement notDeliverProduct = driver.findElement(By.xpath(
				"(//img/parent::div[@style='height: 200px; width: 200px;']/parent::div/following-sibling::div/span)[1]"));
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript("arguments[0].scrollIntoView(true)", notDeliverProduct);
		actions.moveToElement(notDeliverProduct).build().perform();

		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		String encodedString = takesScreenshot.getScreenshotAs(OutputType.BASE64);
		byte[] byteArr = Base64.getDecoder().decode(encodedString);
		try {
			FileOutputStream fos = new FileOutputStream(
					new File(System.getProperty("user.dir") + "/screenshots/product.png"));
			fos.write(byteArr);
		} catch (IOException e) {
			e.printStackTrace();
		}

		WebElement lastProduct = driver.findElement(By.xpath(
				"((//img/parent::div[@style='height: 200px; width: 200px;']/parent::div/following-sibling::div/span)[1]/parent::div/preceding-sibling::div/child::div/preceding::div[@style='height: 200px; width: 200px;'])[last()]"));
		File source = lastProduct.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(source, new File(System.getProperty("user.dir") + "/screenshots/last_product.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(System.getProperty("user.dir") + "/screenshots/last_product.png");

		Assert.assertTrue(file.exists());

		WebElement productName = driver.findElement(By
				.xpath("(//div[@style='width: 100%;']//div[contains(@class,'row')]/child::div[1]/child::div[1])[23]"));
		String product_name = productName.getText();
		System.out.println("Product Name : " + product_name);
		Assert.assertTrue(product_name.contains("SAMSUNG"));
		Assert.assertTrue(product_name.contains("301 L"));

		WebElement price = driver.findElement(By.xpath(
				"(//div[@style='width: 100%;']//div[contains(@class,'row')]/child::div[2]/child::div[1]/child::div/child::div[1])[23]"));
		String productPrice = price.getText();
		System.out.println("Product Price : " + productPrice);
	}

}
