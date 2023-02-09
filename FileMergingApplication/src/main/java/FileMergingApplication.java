import fileSorting.MergeSorter;
import model.DataType;
import model.SortMode;
import org.jetbrains.annotations.NotNull;
import textFileValidation.TextFileValidator;

import java.util.ArrayList;

public class FileMergingApplication {
    public static void main(String @NotNull [] args) {
        // input0.txt input1.txt input2.txt input3.txt input4.txt input5.txt input6.txt input7.txt input8.txt
        if (args.length<3) {
            System.out.println("Поступил некорректный набор аргументов");
            return;
        }

        // 0 аргумент - тип входных значений
        // 1 аргумент - необязательный - режим сортировки
        // 2 аргумент - выходной файл
        int inputFileNameIndex = 2;

        // Первый аргумент должен быть либо -i, либо -s
        DataType dataType;
        if (args[0].equals("-i")) {
            dataType = DataType.Numeric;
        } else  if (args[0].equals("-s")) {
            dataType = DataType.Text;
        } else {
            System.out.println("Поступил некорректный аргумент типа входных значений");
            return;
        }

        SortMode sortMode = SortMode.Ascending;
        // Второй аргумент может быть -a или -d
        if (args[1].equals("-d")) {
            sortMode = SortMode.Descending;
            inputFileNameIndex++;
        } else  if (args[1].equals("-a")) {
            inputFileNameIndex++;
        }

        // Класс, проверяющий соответствие входных файлов заданным требованиям
        TextFileValidator textFileValidator = new TextFileValidator();
        textFileValidator.setDataType(dataType);
        textFileValidator.setSortMode(sortMode);

        ArrayList<String> inputFileNames  = new ArrayList<>();
        String jarPath = FileMergingApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        // Производим проверку всех файлов
        for (var i=inputFileNameIndex; i< args.length; i++) {
            String inputFileName = jarPath + args[i];
            if (inputFileNames.indexOf(inputFileName)!=-1) {
                System.out.println("Дублируется файл "+args[i]);
                continue;
            }
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
        if (mergeSorter.mergeFiles(tempArray, jarPath + args[inputFileNameIndex-1])) {
            System.out.println("Сортировка проведена успешно");
        } else {
            System.out.println("Сортировка не была произведена");
        }
    }
}