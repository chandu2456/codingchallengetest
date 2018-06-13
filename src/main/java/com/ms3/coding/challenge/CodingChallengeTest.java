package com.ms3.coding.challenge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import com.mysql.cj.util.StringUtils;

/**
 * Hello world!
 *
 */
public class CodingChallengeTest {
	private static final String NEW_LINE_SEPARATOR = "\n";

	public static void main(String[] args) {
		int goodRecords = 0;
		int badRecords = 0;
		int totalRecords = 0;
		FileWriter writer = null;
		FileHandler fh;
		Reader reader = null;
		Connection connection = null;
		PreparedStatement st = null;
		// logger
		Logger logger = Logger.getLogger("MyLog");

		// Csv printer to write bad data to the sheet
		CSVPrinter csvFilePrinter = null;
		// csv format
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

		try {

			// Log file to store the number of records are successful, fail and total
			// records.
			fh = new FileHandler("/Users/chandu/LogFile.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			// create file to store bad data
			File fi = new File("/Users/chandu/bad-data-" + Calendar.getInstance().getTime() + ".csv");
			fi.createNewFile();
			writer = new FileWriter(fi);
			csvFilePrinter = new CSVPrinter(writer, csvFileFormat);

			// Reader to read file from the system
			reader = Files.newBufferedReader(Paths.get("/Users/chandu/ms3Interview.csv"));
			/* FileInputStream fileInput = new FileInputStream(fi); */
			@SuppressWarnings("resource")
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/chandu/sqlite/test.db");
			for (CSVRecord csvRecord : csvParser) {
				// Accessing Values by Column Index
				totalRecords = totalRecords + 1;
				// get data from each cell of each row.
				String a = csvRecord.get(0);
				String b = csvRecord.get(1);
				String c = csvRecord.get(2);
				String d = csvRecord.get(3);
				String e = csvRecord.get(4);
				String f = csvRecord.get(5);
				String g = csvRecord.get(6);
				String h = csvRecord.get(7);
				String i = csvRecord.get(8);
				String j = csvRecord.get(9);

				// Check comma elements
				a = checkCommaElement(a);
				b = checkCommaElement(b);
				c = checkCommaElement(c);
				d = checkCommaElement(d);
				e = checkCommaElement(e);
				f = checkCommaElement(f);
				g = checkCommaElement(g);
				h = checkCommaElement(h);
				i = checkCommaElement(i);
				j = checkCommaElement(j);

				// Checking bad records
				if (StringUtils.isNullOrEmpty(a) || StringUtils.isNullOrEmpty(b) || StringUtils.isNullOrEmpty(c)
						|| StringUtils.isNullOrEmpty(d) || StringUtils.isNullOrEmpty(e) || StringUtils.isNullOrEmpty(f)
						|| StringUtils.isNullOrEmpty(g) || StringUtils.isNullOrEmpty(h) || StringUtils.isNullOrEmpty(i)
						|| StringUtils.isNullOrEmpty(j)) {
					Object[] badData = { a, b, c, d, e, f, g, h, i, j };
					csvFilePrinter.printRecord(badData);
					badRecords = badRecords + 1;
				} else {
					// Good records
					String sql = "insert into testtable values(?,?,?,?,?,?,?,?,?,?)";
					st = connection.prepareStatement(sql);
					st.setString(1, a);
					st.setString(2, b);
					st.setString(3, c);
					st.setString(4, d);
					st.setString(5, e);
					st.setString(6, f);
					st.setString(7, g);
					st.setString(8, h);
					st.setString(9, i);
					st.setString(10, j);
					st.executeUpdate();
					connection.commit();
					goodRecords = goodRecords + 1;

				}

			}
			// logging the records in log file
			logger.info("Total number of records received : " + totalRecords);
			logger.info("Total number of good records received : " + goodRecords);
			logger.info("Total number of bad records received : " + badRecords);
			System.out.println(goodRecords);
			System.out.println(badRecords);
			System.out.println(totalRecords);
		} catch (IOException e) {
			if (csvFilePrinter != null) {
				try {
					csvFilePrinter.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				connection.rollback();
				connection.close();
				if (st != null) {
					st.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {

			try {

				writer.flush();
				writer.close();
				reader.close();
				st.close();
				connection.close();
				csvFilePrinter.close();

			} catch (IOException e) {

				System.out.println("Error while flushing/closing fileWriter or reader !!!");

				e.printStackTrace();

			} catch (SQLException e) {
				System.out.println("Error while closing connnection!!!");
			}

		}

	}

	public static String checkCommaElement(String element) {
		if (!StringUtils.isNullOrEmpty(element)) {
			if (element.contains(",")) {
				element = "\"" + element + "\"";
			}
		}
		return element;
	}

}
