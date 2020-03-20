/**
 * 
 */
package com.unittest.unitest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.unittest.unitest.dao.ExcelEntitiesDao;
import com.unittest.unitest.model.ExcelEntities;

/**
 * @author Supriya.Srivastava
 *
 */
@Controller
public class ExcelReadAndSave {

	@Autowired
	private static ExcelEntitiesDao excelEntitiesDao;

	public static void main(String[] args) throws IOException {

		String folderPath = "C:\\Users\\Supriya.Srivastava\\Desktop\\MavenTest";
		/* File statText = new File(folderPath + "\\MavenTestOutput.xlsx"); */

		String status = "";

		Integer testsRun, failures, errors, skipped;
		ExcelEntities excelEntities = new ExcelEntities();
		String className = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(folderPath + "\\MavenTestOutput.xlsx");
 			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			Iterator<Row> row = workbook.getSheetAt(0).iterator();

			while (row.hasNext()) {

				org.apache.poi.ss.usermodel.Row rows = row.next();
				if (rows.getRowNum() == 0) {
					continue;// skip first row, as it contains column names
				}

				Iterator<Cell> cellIterator = rows.cellIterator();

				rows.getCell(2);
				
				if(!rows.getCell(1).toString().equals("SUM(B1:B45)"))
				{
				testsRun = ((Double) Double.parseDouble(rows.getCell(1).toString())).intValue();
				failures = ((Double) Double.parseDouble(rows.getCell(2).toString())).intValue();
				errors = ((Double) Double.parseDouble(rows.getCell(3).toString())).intValue();
				skipped = ((Double)Double.parseDouble(rows.getCell(4).toString())).intValue();
				className = rows.getCell(5).getStringCellValue().trim();

				excelEntities.setTestRun(testsRun);
				excelEntities.setFaliures(failures);
				excelEntities.setErrors(errors);
				excelEntities.setSkipped(skipped);
				excelEntities.setClassName(className);
				}
			}
		}

		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			status = (excelEntities.getId() == null) ? "success" : "updateSuccess";
			excelEntitiesDao.save(excelEntities);
			// status = "success";
		} catch (Exception err) {
			err.printStackTrace();
			status = "error";

		}

	}

}
