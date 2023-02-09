package fileSorting;

import lombok.Getter;
import lombok.Setter;
import model.DataType;
import model.SortMode;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

@Getter @Setter
public class MergeSorter {

    // Выходной файл
    private static FileWriter fileWriter;

    // Список всех объектов, дающих доступ к файлам
    private static final ArrayList<FileReader> fileReaders = new ArrayList<>();

    // Список всех объектов, считывающих файл
    private static final ArrayList<Scanner> scanners = new ArrayList<>();

    // Список текущих числовых значений
    private static final ArrayList<Integer> numericValuesArray = new ArrayList<>();

    // Список текущих текстовых значений
    private static final ArrayList<String> textValuesArray = new ArrayList<>();

    // Количество файлов -> длина списков textValuesArray, numericValuesArray, scanners, fileReaders
    private static int filesCount;

    private DataType dataType;

    private SortMode sortMode;

    private boolean compareValues(String prevStringValue, String currentStringValue) {
        if (prevStringValue.equals("")) {
            return true;
        }
        if (currentStringValue.equals("")) {
            return false;
        }

        int compareResult = currentStringValue.compareTo(prevStringValue);
        if (sortMode == SortMode.Ascending) {
            return compareResult < 0;
        } else {
            return compareResult > 0;
        }
    }

    private boolean compareValues(int prevIntValue, int currentIntValue) {
        if (prevIntValue == Integer.MIN_VALUE) {
            return true;
        }
        if (currentIntValue == Integer.MIN_VALUE) {
            return false;
        }

        if (sortMode == SortMode.Ascending) {
            return currentIntValue < prevIntValue;
        } else {
            return currentIntValue > prevIntValue;
        }
    }

    // Проверяем, а можем ли вообще продвигаться дальше
    private boolean canContinueReading() {
        for (var scan : scanners) {
            if (scan.hasNextLine()) {
                return true;
            }
        }
        return false;
    }

    // Выбираем и записываем лучшее число из всех файлов
    private void compareAllNumericValuesAndAddBest() {
        int bestNumericValue = numericValuesArray.get(0);
        int bestValueArrayIndex = 0;
        for (var i = 1; i < filesCount; i++) {
            if (compareValues(bestNumericValue, numericValuesArray.get(i))) {
                bestNumericValue = numericValuesArray.get(i);
                bestValueArrayIndex = i;
            }
        }

        // Добавляем в выходной файл лучший результат
        try {
            fileWriter.write(Integer.toString(bestNumericValue));
            fileWriter.append('\n');
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Если в "лучшем" файле ещё остались значения, меняем текущее, иначе добавляем пустышки, которые никогда не пройдут проверку
        extractNewNumericLineFromBestFile(bestValueArrayIndex);
    }

    // Выбираем и записываем лучшую строку из всех файлов
    private void compareAllTextValuesAndAddBest() {
        String bestTextValue = textValuesArray.get(0);
        int bestValueArrayIndex = 0;
        for (var i = 1; i < filesCount; i++) {
            if (compareValues(bestTextValue, textValuesArray.get(i))) {
                bestTextValue = textValuesArray.get(i);
                bestValueArrayIndex = i;
            }
        }

        try {
            fileWriter.write(bestTextValue);
            fileWriter.append('\n');
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Если в "лучшем" файле ещё остались значения, меняем текущее, иначе добавляем пустышки, которые никогда не пройдут проверку
        extractNewTextLineFromBestFile(bestValueArrayIndex);
    }

    // Делаем сравнение всех текущих строк и добавляем лучший вариант в выходной файл
    private void compareAllValues() {
        if (getDataType() == DataType.Numeric) {
            compareAllNumericValuesAndAddBest();
        } else {
            compareAllTextValuesAndAddBest();
        }
    }

    // Получаем новую строку "лучшего" файла
    private void extractNewTextLineFromBestFile(int bestValueArrayIndex) {
        if (scanners.get(bestValueArrayIndex).hasNextLine()) {
            var currentLine = scanners.get(bestValueArrayIndex).nextLine();
            textValuesArray.set(bestValueArrayIndex, currentLine);
        } else {
            textValuesArray.set(bestValueArrayIndex, "");
        }
    }

    // Получаем новую строку "лучшего" файла
    private void extractNewNumericLineFromBestFile(int bestValueArrayIndex) {
        if (scanners.get(bestValueArrayIndex).hasNextLine()) {
            var currentLine = scanners.get(bestValueArrayIndex).nextLine();
            try {
                numericValuesArray.set(bestValueArrayIndex, Integer.parseInt(currentLine));
            }
            catch (NumberFormatException ex) {
                // По идее, сюда попасть мы не могли, тк все некорректные файлы отсекли
            }
        } else {
            numericValuesArray.set(bestValueArrayIndex, Integer.MIN_VALUE);
        }
    }

    // Начальное присвоение значений массивов
    private boolean initArrays() {
        // Массив текущих значений каждого файла
        for (var i = 0; i < filesCount; i++){
            var currentLine = scanners.get(i).nextLine();
            if (getDataType() == DataType.Numeric) {
                try {
                    numericValuesArray.add(Integer.parseInt(currentLine));
                }
                catch (NumberFormatException ex) {
                    // По идее, сюда попасть мы не могли, тк все некорректные файлы отсекли
                    return false;
                }
            } else {
                textValuesArray.add(currentLine);
            }
        }
        return true;
    }

    // Добавляет самое большое значение во всех файлах
    private void addLastValue() throws IOException {
        for (var i = 0; i < filesCount; i++){
            if (getDataType() == DataType.Numeric) {
                if (numericValuesArray.get(i) != Integer.MIN_VALUE) {
                    fileWriter.write(numericValuesArray.get(i));
                }
            } else {
                String Line = textValuesArray.get(i);
                if (!Line.equals("")) {
                    fileWriter.write(textValuesArray.get(i));
                }
            }
        }
    }

    // Процедура сливания входящих файлов
    public boolean mergeFiles(String[] inputFilesNames, String outputFileName) {
        try {
            fileWriter = new FileWriter(outputFileName);
            filesCount = inputFilesNames.length;
            for (var filesName : inputFilesNames) {
                FileReader fileReader = new FileReader(filesName);
                fileReaders.add(fileReader);
                Scanner scan = new Scanner(fileReader);
                scanners.add(scan);
            }

            if (!initArrays()) {
                System.out.println("Что-то пошла не так на этапе загрузки файлов");
                return false;
            }

            // Пока есть возможность хоть кого-то считывать - продолжаем сливать файлы
            while (canContinueReading()) {
                compareAllValues();
            }

            // Осталось одно самое большое значение
            addLastValue();

            for (var fileReader : fileReaders) {
                fileReader.close();
            }
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }
}
