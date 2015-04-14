package excel;

import model.Book;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.BeforeClass;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private static Connection connection;

    @BeforeClass
    public static void before() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String dbUrl = "jdbc:mysql://voyaging.iptime.org:3636/siis";
        String dbUser = "siis";
        String dbPass = "8cfebf05178170f6ef35e88e8cef3c5c484702d0";
        connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);

        System.out.println("Db connection initialize");
    }

    @Test
    public void testReadExcel2007File() throws URISyntaxException, IOException, InvalidFormatException, SQLException {
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
            if (excelFile.toString().contains("제주도서관") && excelFile.toString().contains("xls")) {
                InputStream excelFileStream = new FileInputStream(excelFile.toString());
                Workbook workbook = WorkbookFactory.create(excelFileStream);

                Sheet sheet = workbook.getSheetAt(0);

                Row row;
                Cell cell;
                Iterator<Row> rows = sheet.rowIterator();
                int rowCount = 0;
                List<Book> newBooks = new ArrayList<>(2000);
                while (rows.hasNext()) {
                    row = rows.next();

                    if (rowCount == 0) {
                        rowCount++;
                        continue;
                    }

                    Iterator<Cell> cells = row.cellIterator();

                    int cellCount = 0;
                    Book newBook = new Book();
                    boolean noError = true;
                    while (cells.hasNext()) {
                        cell = cells.next();
                        if (cellCount == 0 || cellCount == 5) {
                            cellCount++;
                            continue;
                        }
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String cellValue = cell.getStringCellValue().split("=")[0];
                            cellValue = cellValue.trim();
                            if (cellValue.lastIndexOf(".") > -1) {
                                cellValue = StringUtils.substringBeforeLast(cellValue, ".");
                            }
                            if (cellValue.lastIndexOf(",") > -1) {
                                cellValue = StringUtils.substringBeforeLast(cellValue, ",");
                            }
                            switch (cellCount) {
                                case 1:
                                    newBook.setName(cellValue);
                                    break;
                                case 2:
                                    if (cellValue.startsWith(";")) {
                                        String newName = newBook.getName();
                                        String newAuthor = null;
                                        if (newName.contains("/") && newName.split("/").length > 1) {
                                            String newBookName = newName.split("/")[0];
                                            newAuthor = newName.split("/")[1];

                                            newBookName = newBookName.trim();
                                            if (newBookName.lastIndexOf(".") > -1) {
                                                newBookName = StringUtils.substringBeforeLast(newBookName, ".");
                                            }

                                            newBook.setName(newBookName);

                                            newAuthor = newAuthor.trim();
                                            if (newAuthor.lastIndexOf(".") > -1) {
                                                newAuthor = StringUtils.substringBeforeLast(newAuthor, ".");
                                            }
                                        }

                                        if (StringUtils.isNotBlank(newAuthor)) {
                                            cellValue = newAuthor + cellValue;
                                        } else {
                                            cellValue = StringUtils.substringAfter(cellValue, ";");
                                        }
                                    }

                                    if (cellValue.contains(" ;")) {
                                        cellValue = cellValue.replace(" ;", ";");
                                    }

                                    newBook.setAuthor(cellValue);
                                    break;
                                case 3:
                                    newBook.setPublisher(cellValue);
                                    break;
                                case 4:
                                    newBook.setYear(cellValue);
                                    break;
                                default:
                                    break;
                            }
                        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            log.error("Cell type is numeric : " + cell.getNumericCellValue() + " : " + newBook.toString());
                            noError = false;
                        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
//                            log.error("Blank Cell : " + cell.getCellType() + " Index : " + cellCount + " : " + newBook.toString());
                            if (cellCount == 2) {
                                String newName = newBook.getName();
                                String newAuthor = StringUtils.EMPTY;
                                if (newName.contains("/") && newName.split("/").length > 1) {
                                    String newBookName = newName.split("/")[0];
                                    newAuthor = newName.split("/")[1];

                                    newBookName = newBookName.trim();
                                    if (newBookName.lastIndexOf(".") > -1) {
                                        newBookName = StringUtils.substringBeforeLast(newBookName, ".");
                                    }

                                    newBook.setName(newBookName);

                                    newAuthor = newAuthor.trim();
                                    if (newAuthor.lastIndexOf(".") > -1) {
                                        newAuthor = StringUtils.substringBeforeLast(newAuthor, ".");
                                    }
                                    newBook.setAuthor(newAuthor);
                                } else {
                                    newBook.setAuthor(newAuthor);
                                }
                            } else if (cellCount == 4) {
                                if (StringUtils.isBlank(newBook.getYear()) && StringUtils.isNumeric(newBook.getPublisher())) {
                                    newBook.setYear(newBook.getPublisher());
                                }
                            }
//                            log.info("New Book data : " + newBook.toString());
                        }
                        cellCount++;
                    }
                    if (noError) {
                        newBooks.add(newBook);
                        newBook = null;
                    }
                    if (newBooks.size() == 2000) {
                        writeToDatabase(newBooks);
                        newBooks.clear();
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

    private void writeToDatabase(List<Book> newBooks) {
        String query = "INSERT INTO book SET name = ?, author = ?, publisher = ?, year = ?, created = NOW()";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (Book newBook : newBooks) {
                String publisher = newBook.getPublisher();
                if (publisher != null && publisher.contains("제주")) {
                    continue;
                }
                preparedStatement.setString(1, newBook.getName());
                preparedStatement.setString(2, newBook.getAuthor());
                preparedStatement.setString(3, newBook.getPublisher());
                preparedStatement.setString(4, newBook.getYear());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testReadExcel2003File() {

    }
}
