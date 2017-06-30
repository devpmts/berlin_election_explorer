package com.devpmts.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;

import org.springframework.data.repository.CrudRepository;

import au.com.bytecode.opencsv.CSVReader;

public interface DevpmtsSpringUtil {

    static <E> void readAndPersistFromCsv(String filename, Function<String[], E> converter, CrudRepository<E, ?> repo) {
        try (CSVReader reader = new CSVReader(new FileReader(filename), ',', '"', 1)) {
            reader.readAll().stream()//
                    .map(row -> converter.apply(row))//
                    .filter(applied -> applied != null)//
                    .forEach(value -> repo.save(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String[] readFirstLineFromCsv(String filename) {
        try (CSVReader reader = new CSVReader(new FileReader(filename), ',', '"', 0)) {
            return reader.readNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

}
