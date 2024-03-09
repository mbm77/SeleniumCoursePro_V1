package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.Uninterruptibles;

public class DataProviderTest {

	@Test(dataProvider = "userData")
	public void dataProviderTest(Map<String, String> data) {
		//FirefoxOptions options = new FirefoxOptions();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		WebDriver driver = new ChromeDriver(options);
		
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		WebElement username = new WebDriverWait(driver,Duration.ofSeconds(10))
		.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
		username.sendKeys(data.get("username"));
		WebElement password = new WebDriverWait(driver,Duration.ofSeconds(10))
				.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
		password.sendKeys(data.get("password"));
		
		WebElement login = new WebDriverWait(driver,Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
		login.click();
		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
		driver.quit();
	}

	@DataProvider(name = "userData")
	public Object[][] dataProviderMethod() {
		File excelFile = new File(System.getProperty("user.dir") + "/src/test/resources/excel/users_data.xlsx");
		Object[][] objArr = null;
		try (FileInputStream fis = new FileInputStream(excelFile); XSSFWorkbook workbook = new XSSFWorkbook(fis);) {

			XSSFSheet sheet = workbook.getSheet("Sheet1");
			int rows = sheet.getLastRowNum();
			int cols = sheet.getRow(0).getLastCellNum();
			System.out.println(rows+" "+cols);
			objArr = new Object[rows][1];
			Map<String, String> map = null;
			for (int i = 1; i <= rows; i++) {
				map = new HashMap<>();
				for (int j = 0; j < cols; j++) {
					String key = sheet.getRow(0).getCell(j).getStringCellValue();
					String value = sheet.getRow(i).getCell(j).getStringCellValue();
					map.put(key, value);
				}
				objArr[i - 1][0] = map;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return objArr;
	}
}
