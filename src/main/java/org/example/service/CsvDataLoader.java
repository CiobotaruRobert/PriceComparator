package org.example.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.model.DiscountEntry;
import org.example.model.PriceEntry;
import org.example.model.Product;
import org.example.repository.DiscountEntryRepository;
import org.example.repository.PriceEntryRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CsvDataLoader {

    private static final String DATA_DIRECTORY = "src/main/resources/data";
    private static final char CSV_SEPARATOR = ';';

    private final ProductService productService;
    private final PriceEntryRepository priceEntryRepository;
    private final DiscountEntryRepository discountEntryRepository;

    @PostConstruct
    public void loadCsvData() {
        File folder = new File(DATA_DIRECTORY);
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            System.err.println("No CSV files found in directory: " + DATA_DIRECTORY);
            return;
        }

        for (File file : files) {
            try {
                processCsvFile(file);
            } catch (Exception e) {
                System.err.println("Failed to process file: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    private void processCsvFile(File file) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withCSVParser(new CSVParserBuilder().withSeparator(CSV_SEPARATOR).build())
                .build()) {

            reader.readNext();
            String fileName = file.getName();
            String storeName = extractStoreName(fileName);
            LocalDate date = extractDateFromFileName(fileName);

            if (!fileName.contains("discount")) {
                processPriceEntries(reader, storeName, date);
            } else {
                processDiscountEntries(reader, storeName, date);
            }

            System.out.println("Processed file: " + fileName);
        }
    }

    private void processPriceEntries(CSVReader reader, String storeName, LocalDate date) throws IOException, CsvValidationException {
        String[] line;
        while ((line = reader.readNext()) != null) {
            Product product = productService.getProductById(line[0]);

            if (product == null) {
                product = new Product(line[0], line[1], line[2], line[3], Double.parseDouble(line[4]), line[5]);
            }

            Optional<PriceEntry> existing = priceEntryRepository.findByProductAndDateAndStore(product, date, storeName);

            if (existing.isEmpty()) {
                PriceEntry priceEntry = new PriceEntry(storeName, date, Double.parseDouble(line[6]));
                priceEntry.setProduct(product);
                product.addPriceEntry(priceEntry);
                productService.addOrUpdateProduct(product);
            }
        }
    }

    private void processDiscountEntries(CSVReader reader, String storeName, LocalDate uploadDate) throws IOException, CsvValidationException {
        String[] line;
        while ((line = reader.readNext()) != null) {
            Product product = productService.getProductById(line[0]);

            if (product == null) continue;

            LocalDate fromDate = LocalDate.parse(line[6]);
            Optional<DiscountEntry> existing = discountEntryRepository.findByProductAndStoreAndFromDate(product, storeName, fromDate);

            if (existing.isEmpty()) {
                DiscountEntry discountEntry = new DiscountEntry(
                        storeName,
                        uploadDate,
                        fromDate,
                        LocalDate.parse(line[7]),
                        Integer.parseInt(line[8])
                );
                discountEntry.setProduct(product);
                product.addDiscountEntry(discountEntry);
                productService.addOrUpdateProduct(product);
            }
        }
    }

    private String extractStoreName(String fileName) {
        return fileName.split("_")[0];
    }

    private LocalDate extractDateFromFileName(String fileName) {
        String dateStr = fileName.replaceAll("[^0-9-]", "").replace(".csv", "");
        return LocalDate.parse(dateStr);
    }
}
