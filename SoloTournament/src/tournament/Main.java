package tournament;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class Main {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Server\\Desktop\\Eclipse\\DokumenteZumEinlesen\\Turnierliste.xlsx";
        String sheetName = "Tabelle1";
        int[] columnsToRead = {0}; // Die Spalten, die ausgelesen werden sollen (0-basiert)
        ArrayList<String> playerFromExcel = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(new File(filePath)))) {
            Sheet sheet = workbook.getSheet(sheetName);
            
            for (Row row : sheet) {
                for (int columnIndex : columnsToRead) {
                    Cell cell = row.getCell(columnIndex, MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null) {
                        System.out.print("Empty");
                    } else {
                        DataFormatter formatter = new DataFormatter();
                        String cellValue = formatter.formatCellValue(cell);
                        playerFromExcel.add(cellValue);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
//        for (String string : playerFromExcel) {
//			System.out.println(string);
//		}
        
        
        //creating Starting Groups
        List<List<String>> splittedLists = splitList(playerFromExcel, 3);

        GenerateJSON gJson = new GenerateJSON(splittedLists);
        gJson.initTournament();
        
        
		String pathToWatch = "C:\\Users\\Server\\Desktop\\Eclipse\\PathToWatch"; 
		String pathToMove = "C:\\Users\\Server\\Desktop\\Eclipse\\PathToMove"; 
		
		
		ReadFiles.watchDirectoryPath(pathToWatch, pathToMove,gJson);
    }
        
        
        
       
	
	private static List<List<String>> splitList(ArrayList<String> playerFromExcel, int parts) {
        List<List<String>> splittedLists = new ArrayList<>();

        int listSize = playerFromExcel.size();
        int chunkSize = listSize / parts;
        int remainingElements = listSize % parts;
        int currentIndex = 0;

        for (int i = 0; i < parts; i++) {
            int chunkCount = chunkSize;
            if (remainingElements > 0) {
                chunkCount++;
                remainingElements--;
            }

            List<String> chunk = playerFromExcel.subList(currentIndex, currentIndex + chunkCount);
            splittedLists.add(chunk);
            currentIndex += chunkCount;
        }

        return splittedLists;
    }
    
}