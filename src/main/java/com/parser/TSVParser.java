package com.parser;

import com.parser.model.ImdbComponents;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TSVParser<T extends ImdbComponents> {

    public List<T> parseFromTsv(String filePath, BeanListProcessor<T> rowProcessor, String... fieldNames) {
        List<T> parsedEntities = new ArrayList<>();
        try {
            extractRowsFromTsvFile(filePath, rowProcessor, fieldNames);

            parsedEntities = rowProcessor.getBeans();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return parsedEntities;
    }

    public List<T> parseFromTsv(String filePath, BeanListProcessor<T> rowProcessor, Predicate<T> predicate, String... fieldNames) {
        List<T> parsedEntities = new ArrayList<>();
        try {
            extractRowsFromTsvFile(filePath, rowProcessor, fieldNames);

            parsedEntities = rowProcessor.getBeans()
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return parsedEntities;
    }

    private void extractRowsFromTsvFile(String filePath, BeanListProcessor<T> rowProcessor, String[] fieldNames) throws FileNotFoundException {
        TsvParser parser = getDefaultParser(rowProcessor, fieldNames);
        List<String[]> allRows = parser.parseAll(new FileReader(filePath));
        System.out.println("Total Rows Extracted From File: " + allRows.size());
    }

    private TsvParser getDefaultParser(BeanListProcessor<T> rowProcessor, String... fieldNames) {
        TsvParserSettings settings = new TsvParserSettings();
        settings.setRowProcessor(rowProcessor);
        settings.selectFields(fieldNames);

        return new TsvParser(settings);
    }
}
