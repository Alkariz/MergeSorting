package textFileValidation;

import lombok.Getter;
import lombok.Setter;
import model.DataType;
import model.SortMode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@Getter @Setter
public class TextFileValidator {

    private DataType dataType;

    private SortMode sortMode;

    // Функция сравнения предыдущего и текущего строкового значения
    private boolean compareValues(String prevStringValue, String currentStringValue) {
        int compareResult = currentStringValue.compareTo(prevStringValue);
        if (sortMode == SortMode.Ascending) {
            return compareResult < 0;
        } else {
            return compareResult > 0;
        }
    }

    // Функция сравнения предыдущего и текущего числового значения
    private boolean compareValues(int prevIntValue, int currentIntValue) {
        if (sortMode == SortMode.Ascending) {
            return currentIntValue < prevIntValue;
        } else {
            return currentIntValue > prevIntValue;
        }
    }

    // Функция проверки файла на соответствие заданным параметрам
    public boolean validateFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            Scanner scan = new Scanner(fileReader);

            // Предыдущие прочитанные значения
            String prevStringValue = "";
            int prevIntValue = Integer.MIN_VALUE;
            while (scan.hasNextLine()) {
                String currentStringValue = scan.nextLine();
                if (getDataType() == DataType.Numeric) {
                    try {
                        int currentIntValue = Integer.parseInt(currentStringValue);
                        // Сравниваем числа (если получилось добыть число из строки) в зависимости от режима сортирвоки
                        if (compareValues(prevIntValue, currentIntValue)) {
                            System.out.println("Файл " + filePath + " содержит несортированые строки");
                            return false;
                        } else {
                            prevIntValue = currentIntValue;
                        }
                    } catch (NumberFormatException ex) {
                        // Если не удалось собрать число, файл бракованый
                        System.out.println("Файл " + filePath + " содержит нечисловые символы");
                        return false;
                    }
                } else {
                    // Проверка на правильность текущей строки
                    if (currentStringValue.contains(" ")) {
                        System.out.println("Файл " + filePath + " содержит пробельные символы");
                        return false;
                    }
                    // Сравниваем строки в зависимости от режима сортировки
                    if (compareValues(prevStringValue, currentStringValue)) {
                        System.out.println("Файл " + filePath + " содержит несортированые строки");
                        return false;
                    } else {
                        prevStringValue = currentStringValue;
                    }
                }
            }
            fileReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Отсутствует файл " + filePath);
            return false;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
}
