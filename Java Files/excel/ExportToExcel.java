package excel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportToExcel {

	/********************************************
	 ** MEMBER VARIABLES
	 ********************************************/

	private Map<Integer, List<Double>> data;
	private int position;
	private String fileName;
	private String sheetName;

	/********************************************
	 ** CONSTRUCTOR
	 ********************************************/

	public ExportToExcel() {
		this.data = new HashMap<Integer, List<Double>>();
		this.position = 0;
		this.fileName = null;
		this.sheetName = null;
	}

	/********************************************
	 ** SETTER
	 ********************************************/

	// specify where we want out files saved
	public void setFileName(String file) {
		this.fileName = file;
	}

	// specify sheet name
	public void setSheetName(String sheet) {
		this.sheetName = sheet;
	}

	/********************************************
	 ** METHODS
	 ********************************************/

	// adds data from our EC System to the data map
	public void addTestingData(List<Double> testingData) {
		this.data.put(position, testingData);
		position++;
	}
	
	// adds data from a map to be written in excel
	public void addMapForTesting(Map<Integer, List<Double>> testingData) {
		this.data = testingData;
	}

	// where we actually write our data to Excel
	public void writeDataToExcel() {

		// create a new workbook and sheet
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(this.sheetName);

		// iterate over data and write to sheet
		Set<Integer> keyset = this.data.keySet();
		// keep track of what row we are on
		int rownum = 0;

		// loop over the each key in the data map
		for (Integer key : keyset) {

			// create a row
			Row row = sheet.createRow(rownum++);

			// make a list from the data in the key
			List<Double> dataPoints = this.data.get(key);

			// keep track of what cell we are on
			int cellnum = 0;

			// loop over all the data points in the list
			for (Double eachDouble : dataPoints) {

				// create a cell and write to that cell
				Cell cell = row.createCell(cellnum++);
				cell.setCellValue(eachDouble);
			}
		}

		try {
			// write the workbook in file system
			FileOutputStream toExcel = new FileOutputStream(new File("//Users//RossWeinstein//Desktop//" + this.fileName + ".xlsx"));
			workbook.write(toExcel);

			// close streams
			toExcel.close();
			workbook.close();

			// print message if everything is ok
			System.out.println("Excel file written successfully.");

		} catch (Exception e) {
			// if things went wrong, where did they go wrong
			e.printStackTrace();
		}
	}
}
