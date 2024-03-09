package tests;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.Uninterruptibles;

public class NDTVWeatherTest {

	@Test
	public void getTempInfoOfCity() {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://social.ndtv.com/static/Weather/report/#pfrom=home-ndtv_topsubnavigation");

		WebElement search = driver.findElement(By.id("searchBox")); 
		search.sendKeys("Ahmedabad");
		WebElement city = driver.findElement(By.id("Ahmedabad"));
		city.click();
		search.clear();

		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));

		search.sendKeys("Ajmer");
		WebElement city2 = driver.findElement(By.id("Ajmer"));
		city2.click();
		search.clear();

		String tempInDegreeOfAhmedabad = driver
				.findElement(By.xpath(
						"//div[normalize-space()='Ahmedabad']/preceding-sibling::div/span[@class='tempRedText']"))
				.getText();
		tempInDegreeOfAhmedabad = tempInDegreeOfAhmedabad.replace("℃", "");
		String tempInForeignOfAhmedabad = driver
				.findElement(By.xpath(
						"//div[normalize-space()='Ahmedabad']/preceding-sibling::div/span[@class='tempWhiteText']"))
				.getText();
		tempInForeignOfAhmedabad = tempInForeignOfAhmedabad.replace("℉", "");
		String tempInDegreeOfAjmer = driver
				.findElement(By.xpath(
						"//div[normalize-space()='Ajmer']/preceding-sibling::div/span[@class='tempRedText']"))
				.getText();
		tempInDegreeOfAjmer = tempInDegreeOfAjmer.replace("℃", "");
		String tempInForeignOfAjmer = driver
				.findElement(By.xpath(
						"//div[normalize-space()='Ajmer']/preceding-sibling::div/span[@class='tempWhiteText']"))
				.getText();
		tempInForeignOfAjmer = tempInForeignOfAjmer.replace("℉", "");
		//System.out.println("Ahmedabad Weather In Celsius "+tempInDegreeOfAhmedabad +" And In Fahrenheit "+tempInForeignOfAhmedabad);
		//System.out.println("Ajmer Weather In Celsius "+tempInDegreeOfAjmer +" And In Fahrenheit "+tempInForeignOfAjmer);
		System.out.println(city.getAttribute("id"));
		System.out.println(city2.getAttribute("id"));
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Temp Info");
		Object tempData[][] = { 
				{ "City", "Temparature In Celsius", "Temparature In Fahrenheit" }, 
				{ city.getAttribute("id"), tempInDegreeOfAhmedabad, tempInForeignOfAhmedabad }, 
				{ city2.getAttribute("id"), tempInDegreeOfAjmer, tempInForeignOfAjmer }
				 };
		int rows = tempData.length;
		int cols = tempData[0].length;
		for (int i = 0; i < rows; i++) {
			XSSFRow row = sheet.createRow(i);
			for (int j = 0; j < cols; j++) {
				XSSFCell cell = row.createCell(j);
				Object value = tempData[i][j];
				
				if(value instanceof String)
					cell.setCellValue((String) value);
				if(value instanceof Integer)
					cell.setCellValue((Integer) value);
				if(value instanceof Boolean)
					cell.setCellValue((Boolean) value);
			}
		}
		
		String filepath = ".\\datafiles\\city_temprature.xlsx";
		try {
			FileOutputStream fos = new FileOutputStream(filepath);
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
