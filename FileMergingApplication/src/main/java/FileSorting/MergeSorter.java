package FileSorting;

import lombok.Getter;
import lombok.Setter;
import model.DataType;
import model.SortMode;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MergeSorter {

    // Список всех объектов, дающих доступ к файлам
    private static final ArrayList<FileReader> fileReaders = new ArrayList<>();

    // Список всех объектов, считывающих файл
    private static final ArrayList<Scanner> scanners = new ArrayList<>();

    @Getter @Setter
    private DataType dataType;

    @Getter @Setter
    private SortMode sortMode;

    private boolean compareValues(String prevStringValue, String currentStringValue) {
        int compareResult = currentStringValue.compareTo(prevStringValue);
        if (sortMode == SortMode.Ascending) {
            return compareResult<0;
        } else {
            return compareResult>0;
        }
    }

    private boolean compareValues(int prevIntValue, int currentIntValue) {
        if (sortMode == SortMode.Ascending) {
            return currentIntValue < prevIntValue;
        } else {
            return currentIntValue > prevIntValue;
        }
    }

    private boolean canContinueReading() {
        for (Scanner scan : scanners) {
            if (scan.hasNextLine()) {
                return true;
            }
        }
        return false;
    }

    public boolean mergeFiles(String[] filesNames) {
        try {
            int filesCount = filesNames.length;
            for (String filesName : filesNames) {
                FileReader fileReader = new FileReader(filesName);
                fileReaders.add(fileReader);
                Scanner scan = new Scanner(fileReader);
                scanners.add(scan);
            }

            int[] valuesArray = new int[filesCount];

            while (canContinueReading()) {

            }

            for (FileReader fileReader : fileReaders) {
                fileReader.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

}
