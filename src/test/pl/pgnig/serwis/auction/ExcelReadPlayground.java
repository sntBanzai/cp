package pl.pgnig.serwis.auction;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

public class ExcelReadPlayground {

	@Test
	public void testExcelSheet() throws IOException {
		
		String text = "Version 1.0";
		
		InputStream resourceAsStream = ExcelReadPlayground.class.getResourceAsStream("/Aukcja_Sprzet_201903.xlsx");
		Workbook workbook = WorkbookFactory.create(resourceAsStream);
		System.out.println(workbook);

		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		System.out.println("Retrieving name of sheet");
		workbook.forEach(sheet -> {
			System.out.println("=> " + sheet.getSheetName());
		});

		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
		System.out.println("\n\nRows and columns in excel file\n");
		sheet.forEach(row -> {
			row.forEach(cell -> {
				String cellValue = dataFormatter.formatCellValue(cell);
				System.out.println(cellValue + "\t");
			});
			System.out.println();
		});
		workbook.close();
	}

}
