package tests;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ReadTextFromBarcodeTest {

	@Test
	public void barcodeTest() throws IOException, NotFoundException {
		// System.setProperty("webdriver.chrome.driver",
		// System.getProperty("user.dir") +
		// "\\src\\test\\resources\\executables\\chromedriver.exe");
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		//https://barcode.tec-it.com/en
		driver.get("https://barcode.tec-it.com/barcode.ashx?data=ABC-abc-1234&code=Code128&translate-esc=on");
		String barcodeURL = driver.findElement(By.xpath("//img[contains(@style,'display: block')]"))
				.getAttribute("src");

		try {
			URL url = new URL(barcodeURL);
			BufferedImage bufferedImage = ImageIO.read(url);
			LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
			Result result = new MultiFormatReader().decode(binaryBitmap);
			System.out.println(result.getText());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}
}
