package ru.jktu88.alfa;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import ru.jktu88.alfa.model.Group;
import ru.jktu88.alfa.model.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class AlfaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlfaApplication.class, args);
    }

    public <T> List<T> loadObjectList(Class<T> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = new File(fileName);
            MappingIterator<T> readValues =
                    mapper.reader(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }

    @Bean
    Map<String, Group> Groups() {
        return loadObjectList(Group.class, "groups.csv").stream()
                .collect(Collectors.toMap(Group::getId, Function.identity()));
    }

    @Bean
    Map<String, Item> Items() {
        return loadObjectList(Item.class, "items.csv").stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));
    }
}
