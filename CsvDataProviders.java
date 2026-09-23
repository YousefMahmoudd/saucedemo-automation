package com.saucedemo.dataproviders;
import com.opencsv.CSVReader;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CsvDataProviders {

    private CsvDataProviders() {
    }

    @DataProvider(name = "loginData")
    public static Object[][] loginData() throws IOException {
        return readCsv("src/test/resources/testdata/users.csv");
    }

    @DataProvider(name = "checkoutData")
    public static Object[][] checkoutData() throws IOException {
        return readCsv("src/test/resources/testdata/checkout_data.csv");
    }

    private static Object[][] readCsv(String path) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> rows = reader.readAll();
            List<String[]> dataRows = new ArrayList<>(rows.subList(1, rows.size())); // skip header
            Object[][] data = new Object[dataRows.size()][];
            for (int i = 0; i < dataRows.size(); i++) {
                data[i] = dataRows.get(i);
            }
            return data;
        } catch (com.opencsv.exceptions.CsvException e) {
            throw new IOException("Failed to parse CSV: " + path, e);
        }
    }
}
