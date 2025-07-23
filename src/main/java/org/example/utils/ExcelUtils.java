package org.example.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.data.leapfrog.GameData;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static List<GameData> readGameDataFromExcel(String filePath) {
        List<GameData> games = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> columnIndexMap = mapHeaderNameToColumnIndex(sheet);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String title = getCellValue(row.getCell(columnIndexMap.get("title")));
                String age = getCellValue(row.getCell(columnIndexMap.get("age")));
                String price = getCellValue(row.getCell(columnIndexMap.get("price")));
                games.add(new GameData(title, age, price));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return games;
    }

    private static String getCellValue(Cell cell) {
        return cell == null ? null : cell.getStringCellValue().trim();
    }

    private static Map<String, Integer> mapHeaderNameToColumnIndex(Sheet sheet) {
        Map<String, Integer> columnIndexMap = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            String header = cell.getStringCellValue().trim();
            columnIndexMap.put(header, cell.getColumnIndex());
        }
        return columnIndexMap;
    }
}
