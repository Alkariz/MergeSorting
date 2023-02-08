import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class SortedFileCreatingApplication {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                // Создаём (если надо) и заполянем файл
                String fileName = "input" + i + ".txt";
                File file = new File(fileName);
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(fileName);

                // Количество строк в файле
                Random random = new Random();
                int linesCount = random.nextInt(1000000);

                // Специалньый массив, куда заполняются цифры, потом будет сортироваться
                int[] numbersArray = new int[linesCount];
                for (int j = 0; j < linesCount; j++) {
                    numbersArray[j] = random.nextInt(Integer.MAX_VALUE);
                }
                Arrays.parallelSort(numbersArray);
                for (int j = 0; j < linesCount; j++) {
                    fileWriter.write(Integer.toString(numbersArray[j]));
                    fileWriter.append('\n');
                }
                fileWriter.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
//                FileWriter fileWriter = new FileWriter("input" + Integer.toString(i));
        }
    }
}
