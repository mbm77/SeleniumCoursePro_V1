package utils;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

public class TempDataIntoExcel {
	@Test
	public void writeDataIntiExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Temp Info");
		Object tempData[][] = { { "Emp Id", "Name", "Job" }, { 101, "David", "Engineer" }, { 102, "mbm", "Manager" },
				{ 103, "Scott", "Analyst" } };
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
