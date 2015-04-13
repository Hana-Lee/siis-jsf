package excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lee Hana on 2015-04-13 오전 2:20.
 *
 * @author Lee Hana
 * @since 2015-04-13 오전 2:20
 */
public class ExcelReadTest {

    private Log log = LogFactory.getLog(getClass());

    @Test
    public void testReadExcel2007File() throws URISyntaxException, IOException, InvalidFormatException {
        final List<Path> excelFiles = new ArrayList<>();
        URI excelFileUri = new URI("file:/Users/voyaging/Dropbox/dev/문서/도서목록");
        Path excelFileDirectory = Paths.get(excelFileUri);
        DirectoryStream<Path> excelFileDirectoryStream = Files.newDirectoryStream(excelFileDirectory);

        for (Path fileEntry : excelFileDirectoryStream) {
            excelFiles.add(fileEntry);
        }
        excelFileDirectoryStream.close();

        int totalRowCount = 0;
        for (Path excelFile : excelFiles) {
            if (StringUtils.contains(excelFile.toString(), "xls")) {
                InputStream excelFileStream = new FileInputStream(excelFile.toString());
                Workbook workbook = WorkbookFactory.create(excelFileStream);

                Sheet sheet = workbook.getSheetAt(0);

                Row row;
                Cell cell;
                Iterator<Row> rows = sheet.rowIterator();
                int rowCount = 0;
                while (rows.hasNext()) {
                    row = rows.next();

                    if (rowCount == 0) {
                        continue;
                    }
                    Iterator<Cell> cells = row.cellIterator();

                    int cellCount = 0;
                    while (cells.hasNext()) {
                        cell = cells.next();
                        if (cellCount == 0) {
                            continue;
                        }
                        if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {

                        } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                            
                        }
                        cellCount++;
                    }
                    rowCount++;
                }
                log.info("============================================================");
                log.info(excelFile.toString());
                log.info("Row count : " + rowCount);
                log.info("============================================================");

                excelFileStream.close();
                workbook.close();

                totalRowCount += rowCount;
            }
        }

        log.info("Total Row Count : " + totalRowCount);
    }

    @Test
    public void testReadExcel2003File() {

    }
}
