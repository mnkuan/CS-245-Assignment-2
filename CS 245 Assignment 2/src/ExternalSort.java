import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Scanner;

@SuppressWarnings("javadoc")
public class ExternalSort {
  /**
   * Sorts an a file in an array given a certain amount of memory
   * 
   * @param inputFile  the file that will be read
   * @param outputFile the name of the file that will be outputted
   * @param n          the size of the array in the inputFile
   * @param k          the amount of memory available to the algorithm
   * @throws IOException
   */
  public void externalSort(String inputFile, String outputFile, int n, int k) throws IOException {
    Path input = Paths.get(inputFile);

    int amtFiles = (int) Math.ceil((double) n / k); // calculates the amount of arrays in the function
    int fileNUM = 1; // counts the amount of tempFiles
    int tempSize = k;

    double[] mainArray = new double[k];
    double[] tempBigArray = new double[n];
    double[] temp = new double[k];

    boolean isOdd = amtFiles % 2 == 1;

    try (Scanner read = new Scanner(input).useDelimiter("(\\[ )|(, )|(\\])");) {
      int index = 0;
      while (read.hasNext()) {
        double value = Double.parseDouble(read.next().strip());
        tempBigArray[index++] = value;
      } // end of while
    } // close the Scanner

    // Write to temp files
    for (int i = 0; i < n; i++) {
      // Check temp size
      if (i % k == 0) {
        if (n - i < k) {
          tempSize = n % k;
        } else {
          tempSize = k;
        }
      }

      temp[i % k] = tempBigArray[i];
      System.out.println(Arrays.toString(temp));

      if (i != 0 && ((i + 1) % k == 0) || (i == (n - 1))) {
        // Sort the array
        HybridSort.hybridsort(temp, 0, tempSize - 1);

        // get names for outputfileTemp
        Path outputTemp = Paths.get(outputFile + fileNUM++ + ".txt");

        // Write to the temp file
        try (BufferedWriter writer = Files.newBufferedWriter(outputTemp, StandardCharsets.UTF_8);) {
          for (int j = 0; j < tempSize; j++) {
            writer.write(temp[j] + " ");
          } // end of for
        } // close the BufferedWriter
      } // end of if
    } // end of for

    // Add all values in the first file to the main array
    try (Scanner scan = new Scanner(Paths.get(outputFile + 1 + ".txt"));) {
      int index = 0;

      while (scan.hasNextDouble()) {
        double value = scan.nextDouble();
        mainArray[index++] = value;
      } // end of while
    } // close first file

    // Merge all the files
    mainMergeSort(amtFiles, outputFile, isOdd);
  }

  private void mainMergeSort(int amtFiles, String outputFile, boolean isOdd) throws IOException {
    mergeSort(amtFiles, outputFile);

    // Replace the file to the final name
    Files.move(Paths.get(outputFile + 1 + ".txt"), Paths.get(outputFile + ".txt"), StandardCopyOption.REPLACE_EXISTING);
  }

  /**
   * merges all the .txt files if they are even number of .txt files
   * 
   * The mainMergeSort will deal with the odd number of .txt files
   * 
   * @param amtFiles   the amount of files needed to sort
   * @param outputFile
   * @throws IOException
   */
  private void mergeSort(int amtFiles, String outputFile) throws IOException {
    // Base case
    if (amtFiles > 1) {
      for (int i = 1; i <= amtFiles; i += 2) {

        if (i <= amtFiles - 1) {
          try (Scanner file1 = new Scanner(Paths.get(outputFile + i + ".txt"));
              Scanner file2 = new Scanner(Paths.get(outputFile + (i + 1) + ".txt"));
              BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile + ".txt"),
                  StandardCharsets.UTF_8);) {
            merge(file1, file2, writer);

          } // close the Scanner and BufferedWriter
          // Replace a file
          Files.move(Paths.get(outputFile + ".txt"), Paths.get(outputFile + ((i + 1) / 2) + ".txt"),
              StandardCopyOption.REPLACE_EXISTING);
        } else {
          Files.move(Paths.get(outputFile + i + ".txt"), Paths.get(outputFile + ((i + 1) / 2) + ".txt"),
              StandardCopyOption.REPLACE_EXISTING);
        } // end of if
      } // end of for

      amtFiles = (amtFiles / 2) + (amtFiles % 2);
      mergeSort(amtFiles, outputFile);
    } // end of if

  }

  /**
   * merges the two values into a .txt file
   * 
   * @param file1  first file to compare
   * @param file2  second file to compare
   * @param writer the file that writes to a new temp
   * @throws IOException
   */
  private void merge(Scanner file1, Scanner file2, BufferedWriter writer) throws IOException {
    double value1 = file1.nextDouble();
    double value2 = file2.nextDouble();

    while (value1 != Double.POSITIVE_INFINITY || value2 != Double.POSITIVE_INFINITY) {
      if (value1 <= value2) {
        writer.write(value1 + " ");
        value1 = file1.hasNextDouble() ? file1.nextDouble() : Double.POSITIVE_INFINITY;
      } else {
        writer.write(value2 + " ");
        value2 = file2.hasNextDouble() ? file2.nextDouble() : Double.POSITIVE_INFINITY;
      }
    }
  }

  public static void main(String[] args) throws IOException {
    ExternalSort exSort = new ExternalSort();

    exSort.externalSort("arrays.txt", "temp", 9, 3);
  }
}
