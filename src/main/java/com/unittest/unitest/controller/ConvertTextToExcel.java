package com.unittest.unitest.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unittest.unitest.dao.ExcelEntitiesDao;
import com.unittest.unitest.model.ExcelEntities;

/**
 * @author Supriya Srivastava
 *
 */
@RestController
@RequestMapping("/convertexcel")
public class ConvertTextToExcel {

	@Autowired
	private ExcelEntitiesDao excelEntitiesDao;

	private static final Logger logger = LogManager.getLogger(ConvertTextToExcel.class);

	@RequestMapping("/read")
	private String readTextFile() throws IOException {

		String folderPath = "C:\\Users\\Supriya.Srivastava\\Desktop\\MavenTest";

		/* Path -Take input from the text file */
		File file = new File(folderPath + "\\test.txt");
		/* Path for the output excel file */
		File statText = new File(folderPath + "\\MavenTestOutput.xlsx");

		String outputFileName = "MavenTestOutput.xlsx";
		String name = "Tests run:";
		Pattern p = Pattern.compile("\\d+");

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Maven Test Output");

		Row header = sheet.createRow(0);
		removeOtherSheets(workbook, outputFileName);

		/*
		 * This method will generate the header of excel
		 */ generateHeaderRow(workbook, sheet, header);
		if (file != null) {
			BufferedReader br = new BufferedReader(new FileReader(file));

			try (Stream<String> stream = br.lines().parallel()) {

				List<String> listOfStream = stream.filter(line -> line.contains(" " + name + " "))
						.collect(Collectors.toList());

				int sizeOfList = listOfStream.size() > 0 ? listOfStream.size() : 0;

				int rowCount = 0;
				for (String lis : listOfStream) {

					String arr[] = lis.split(",");
					Row row = sheet.createRow(++rowCount);

					createExcelSheetSerialNumber(row, sheet, sizeOfList, rowCount);

					int columnCount = 0;

					for (int i = 0; i < arr.length; i++) {

						if (i < arr.length - 1) {
							Cell cell = row.createCell(++columnCount);

							if (arr[i] instanceof String) {

								Matcher m = p.matcher(arr[i]);

								while (m.find()) {

									String digitStr = m.group();
									Integer digit = Integer.parseInt(digitStr);

									cell.setCellValue(digit);
								}

							}

						}

						else {
							Cell cell = row.createCell(++columnCount);

							String lastColumnValue[] = lis.split("-");
							for (String lastStr : lastColumnValue) {
								if (lastStr instanceof String) {

									cell.setCellValue((String) lastStr);

								}
							}
						}

					}

					System.out.println(lis);
					countExcelSheetTestOutput(sheet, sizeOfList, arr.length - 1);
				}

				// workbook.close();
				generateExcel(workbook, outputFileName, statText);

				saveExcelDataToDb(sizeOfList, folderPath, statText);
			} catch (Exception e) {
				e.printStackTrace();
			}
			br.close();
		}
		return "Data Save Successfully!!!";
	}

	/* This method will genrate the excel after reading text file */
	private static void generateExcel(XSSFWorkbook workbook, String outputExcelSheetName, File file) {
		logger.traceEntry("saveDataToExcel method of ConvertTextToExcel class");
		removeOtherSheets(workbook, outputExcelSheetName);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {

			workbook.write(outputStream);
			// workbook.close();
			outputStream.close();
		}

		catch (POIXMLException | IOException e) {
			logger.debug("Exception Occured While Writing Excel  as the file is OPEN{} ",
					ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}

		finally {

			closeExcel(workbook);
		}
		logger.traceExit();
	}

	/* This method will create the excel header */
	private static void generateHeaderRow(XSSFWorkbook workbookInput, XSSFSheet sheet, Row headerRow) {
		logger.traceEntry("generateHeaderRow method of ConvertTextToExcel class");
		String[] headers = new String[] { "S.No", "Tests Run", "Failures", "Errors", "Skipped", "Class Name" };
		try {

			Row row = sheet.createRow(0);
			CellStyle style = workbookInput.createCellStyle();
			Font font = workbookInput.createFont();
			font.setBold(true);
			style.setFont(font);

			for (int i = 0; i <= headers.length - 1; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* This method will remove the same excel sheet present on same location. */

	private static void removeOtherSheets(XSSFWorkbook workbook, String excelSheetNames) {
		logger.traceEntry("removeOtherSheets method of ConvertTextToExcel class");
		int sheetCount = workbook.getNumberOfSheets();

		try {

			for (int i = sheetCount - 1; i >= 0; i--) {
				boolean isPresent = excelSheetNames.equals(excelSheetNames);
				if (!isPresent) {
					workbook.removeSheetAt(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.traceExit();
	}

	/* Create serial Number for new Excel */

	private static void createExcelSheetSerialNumber(Row row, XSSFSheet sheet, int sizeOfList, int rowCount) {
		logger.traceEntry("createExcelSheetSerialNumber method of ConvertTextToExcel class");
		try {
			row.createCell(0).setCellValue(rowCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.traceExit();

	}

	private static void countExcelSheetTestOutput(XSSFSheet sheet, int sizeOfList, int arrLength) {
		logger.traceEntry("countExcelSheetTestOutput method of ConvertTextToExcel class");
		try {
			Row rowTotal = sheet.createRow(sizeOfList + 2);
			Cell cellTotalText = rowTotal.createCell(0);
			cellTotalText.setCellValue("Total:");

			Cell cellTotal = rowTotal.createCell(1);

			cellTotal.setCellFormula("SUM(B1:B" + sizeOfList + ")");
			rowTotal.createCell(2).setCellFormula("SUM(C1:C" + sizeOfList + ")");
			rowTotal.createCell(3).setCellFormula("SUM(D1:D" + sizeOfList + ")");
			rowTotal.createCell(4).setCellFormula("SUM(E1:E" + sizeOfList + ")");
		} catch (Exception e) {
			e.printStackTrace();

		}
		logger.traceExit();
	}

	private static void closeExcel(XSSFWorkbook workbookInput) {
		try {
			workbookInput.close();
		} catch (IOException e) {
			logger.error("Exception Occured While Closing Excel", e);
		}
	}

	@SuppressWarnings("resource")
	private void saveExcelDataToDb(int sizeOfList, String folderPath, File statText) throws IOException {
		logger.traceEntry("saveExcelDataToDb method of ConvertTextToExcel class");

		String status = "";

		Integer testsRun, failures, errors, skipped;

		String className = "";
		try (FileInputStream fileInputStream = new FileInputStream(statText)) {

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			Iterator<Row> row = workbook.getSheetAt(0).iterator();

			while (row.hasNext()) {

				ExcelEntities excelEntities = new ExcelEntities();

				org.apache.poi.ss.usermodel.Row rows = row.next();
				if (rows.getRowNum() == 0) {
					continue;// skip first row, as it contains column names
				}

				rows.getCell(2);

				if (!rows.getCell(1).toString().equals("SUM(B1:B" + sizeOfList + ")")) {
					testsRun = ((Double) Double.parseDouble(rows.getCell(1).toString())).intValue();
					failures = ((Double) Double.parseDouble(rows.getCell(2).toString())).intValue();
					errors = ((Double) Double.parseDouble(rows.getCell(3).toString())).intValue();
					skipped = ((Double) Double.parseDouble(rows.getCell(4).toString())).intValue();
					className = rows.getCell(5).getStringCellValue().trim();

					excelEntities.setTestRun(testsRun);
					excelEntities.setFaliures(failures);
					excelEntities.setErrors(errors);
					excelEntities.setSkipped(skipped);
					excelEntities.setClassName(className);
					excelEntities.setDate(new Date());

				}

				try {
					status = (excelEntities.getId() == null) ? "success" : "updateSuccess";

					if (excelEntities.getClassName() != null) {
						excelEntitiesDao.save(excelEntities);
					}
					// status = "success";
				} catch (Exception err) {
					err.printStackTrace();
					status = "error";

				}
				logger.traceExit();
			}

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		logger.traceExit();
	}

	@GetMapping(path = "/allRecordOfTestCase", produces = "application/json")
	List<ExcelEntities> all() {
		return excelEntitiesDao.findAll();
	}

	/*@GetMapping("/recordBaseOfDate/{2020-03-20 16:13:23.621}")
	Optional<ExcelEntities> one(@PathVariable Date id) {

		return excelEntitiesDao.findById(id);
		 .orElseThrow(() -> new EmployeeNotFoundException(id)); 
	}*/

}