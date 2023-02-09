import fileSorting.MergeSorter;
import model.DataType;
import model.SortMode;
import textFileValidation.TextFileValidator;

import java.util.ArrayList;

public class FileMergingApplication {
    public static void main(String[] args) {

        // 0 аргумент - имя приложения
        // 1 аргумент - тип входных значений
        // 2 аргумент - необязательный - режим сортировки
        // 3 аргумент - выходной файл
        int inputFileNameIndex = 3;
        
        // Первый аргумент должен быть либо -i, либо -s
        DataType dataType;
        if (args[1].equals("-i")) {
            dataType = DataType.Numeric;
        } else  if (args[1].equals("-s")) {
            dataType = DataType.Text;
        } else {
            System.out.println("Поступил некорректный аргумент типа входных значений");
            return;
        }

        SortMode sortMode = SortMode.Ascending;
        // Второй аргумент может быть -a или -d
        if (args[2].equals("-d")) {
            sortMode = SortMode.Descending;
            inputFileNameIndex++;
        } else  if (args[2].equals("-a")) {
            inputFileNameIndex++;
        }

        // Класс, проверяющий соответствие входных файлов заданным требованиям
        TextFileValidator textFileValidator = new TextFileValidator();
        textFileValidator.setDataType(dataType);
        textFileValidator.setSortMode(sortMode);

        ArrayList<String> inputFileNames  = new ArrayList<>();
        // Производим проверку всех файлов
        for (var i=inputFileNameIndex; i< args.length; i++) {
            String inputFileName = args[i];
            // Если файл проходит проверку, добавляем его в список для сортировки
            if (textFileValidator.validateFile(inputFileName)) {
                inputFileNames.add(inputFileName);
            } else {
                String fileNotAllowed = inputFileName + " не является подходящим под данный тип сортировки";
                System.out.println(fileNotAllowed);
            }
        }

        MergeSorter mergeSorter = new MergeSorter();
        mergeSorter.setDataType(dataType);
        mergeSorter.setSortMode(sortMode);
        String[] tempArray = new String[inputFileNames.size()];
        inputFileNames.toArray(tempArray);
        if (mergeSorter.mergeFiles(tempArray, args[inputFileNameIndex-1])) {
            System.out.println("Сортировка проведена успешно");
        } else {
            System.out.println("Сортировка проведена успешно");
        }

        System.out.println("Hello world!");
    }
}