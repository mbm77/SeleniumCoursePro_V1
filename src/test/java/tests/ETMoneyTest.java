package tests;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.Uninterruptibles;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ETMoneyTest {

	@Test
	public void etMoneyPPFCalculator() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://www.etmoney.com/tools-and-calculators/ppf-calculator");
		WebElement amount = driver.findElement(By.name("amount"));
		amount.clear();
		amount.sendKeys("10,000");
//		amount.clear();
//		amount.sendKeys("Ten thousand only");

		WebElement interest = driver.findElement(By.name("interest"));
		boolean isEnabled = interest.isEnabled();
		Assert.assertFalse(isEnabled);
		String interestRate = interest.getAttribute("value");
		Assert.assertEquals(interestRate, "7.1", "Interest reate mismatched");
		WebElement tableView = driver.findElement(By.xpath(
				"//span[text()='View output as:']/following-sibling::ul[contains(@class,'custom-switch')]/li[text()='Table']"));
		tableView.click();
		String period = driver.findElement(By.name("period")).getAttribute("value");

		Assert.assertEquals(period, "15", "Default investment period mismatched");

		List<WebElement> rowsInTable = driver.findElements(By.xpath("//table[@id='ppfTable']/tbody/tr"));
		int rows = rowsInTable.size();
		Assert.assertEquals(rows, 15, "Table doesn't have 15 rows");

		WebElement yearEndBalance = driver.findElement(By.xpath("(//table[@id='ppfTable']/tbody/tr/td)[60]"));
		String balanceStr = yearEndBalance.getText();

		balanceStr = balanceStr.replaceAll("[-+^:,₹]", "");
		int balance = Integer.parseInt(balanceStr);
		double bal = balance / 100000.00;
		DecimalFormat df = new DecimalFormat("####0.00");
		String approxBal = df.format(bal);
		WebElement maturityAmtEle = driver.findElement(By.xpath("//span[@id='maturityAmount']/span"));
		String maturityAmt = maturityAmtEle.getText().replaceAll("[^0-9.]+", "");
		Assert.assertEquals(maturityAmt, approxBal, "Year End Balance not matching with Maturity Amount approximately");
		// System.out.println(balance);

		int maturityAmount = (int) (Double.parseDouble(maturityAmt) * 100000);
		// System.out.println(maturityAmount);
		Assert.assertTrue(maturityAmount < balance,
				"Year End Balance not matching with Maturity Amount approximately (yearendbal less)");
		Assert.assertTrue(balance < (maturityAmount + 500),
				"Year End Balance not matching with Maturity Amount approximately (yearendbal more)");

		int periodInt = Integer.parseInt(period);

		for (int i = periodInt; i < 20; i++) {

			WebElement numPlay = driver.findElement(By.xpath("//div[@class='increase-num-ctrl']/span"));
			numPlay.click();
			
			Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
		}

	}

}
