package com.main;

import com.parser.TSVParser;
import com.parser.model.Names;
import com.parser.model.Title;
import com.parser.model.TitleNamesMappings;
import com.processor.EsRecordIngester;
import com.univocity.parsers.common.processor.BeanListProcessor;

import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String args[]) {
        //LOAD PROPERTIES
        Properties properties = PropertyLoader.loadProperties();
        String parseSubject = properties.getProperty("parse.subject").toLowerCase();

        switch (parseSubject) {
            case "title" :
                //EXTRACT PARSE AND INGEST TITLES (4 mins)
                ingestTitles(properties);
                break;
            case "cast" :
                //EXTRACT PARSE AND INGEST CASTS (45 mins)
                ingestCasts(properties);
                break;
            case "mapping" :
                //EXTRACT PARSE AND INGEST MAPPINGS (7 mins)
                ingestMappings(properties);
                break;
            default:
                System.out.println("parse.subject specified is invalid, please check the value in resources/config.properties");
        }
    }

    private static void ingestMappings(Properties properties) {
        TSVParser<TitleNamesMappings> mappingsTSVParser = new TSVParser<>();
        BeanListProcessor<TitleNamesMappings> mappingsProcessor = new BeanListProcessor<TitleNamesMappings>(TitleNamesMappings.class);

        Timer.startTimer();
        List<TitleNamesMappings> mappingsList = mappingsTSVParser.parseFromTsv(
                properties.getProperty("mapping.input.file"),
                mappingsProcessor,
                "tconst", "principalCast");
        Timer.stopTimer();

        System.out.println("Total Records Available: "+ mappingsList.size());

        EsRecordIngester<TitleNamesMappings> mappingsEsRecordIngester = new EsRecordIngester<TitleNamesMappings>("mappings");
        mappingsEsRecordIngester.recordIngester(mappingsList);
    }

    private static void ingestCasts(Properties properties) {
        TSVParser<Names> namesTSVParser = new TSVParser<>();
        BeanListProcessor<Names> namesProcessor = new BeanListProcessor<Names>(Names.class);

        Timer.startTimer();
        List<Names> namesLists = namesTSVParser.parseFromTsv(
                properties.getProperty("cast.input.file"),
                namesProcessor,
                "nconst", "primaryName");
        Timer.stopTimer();

        System.out.println("Total Records Available: "+ namesLists.size());

        EsRecordIngester<Names> esRecordIngesterForNames = new EsRecordIngester<Names>("names");
        esRecordIngesterForNames.recordIngester(namesLists);
    }

    private static void ingestTitles(Properties properties) {
        TSVParser<Title> tsvParser = new TSVParser<Title>();
        BeanListProcessor<Title> rowProcessor = new BeanListProcessor<Title>(Title.class);

        Timer.startTimer();
        List<Title> titleList = tsvParser.parseFromTsv(
                properties.getProperty("title.input.file"),
                rowProcessor,
                title -> "movie".equalsIgnoreCase(title.getTitleType()),
                "tconst", "titleType", "originalTitle", "startYear");
        Timer.stopTimer();

        System.out.println("Total Records Available: "+ titleList.size());

        EsRecordIngester<Title> esRecordIngester = new EsRecordIngester<Title>("titles");
        esRecordIngester.recordIngester(titleList);
    }
}
