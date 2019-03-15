import java.util.Arrays;
import java.util.Random;

public class HybridSort {
  /**
   * Sorts an array using insertion sort
   * 
   * @param arr   the array that will be sorted
   * @param left  the beginning side of the array
   * @param right the end side of the array
   */
  public static void quadraticsort(double[] arr, int left, int right) {
    for (int i = left; i < right + 1; i++) {
      double value = arr[i];
      int j = i - 1;

      while (j >= 0 && arr[j] > value) {
        arr[j + 1] = arr[j];
        j--;
      }

      arr[j + 1] = value;
    }
  }

  /**
   * Applies quicksort
   * 
   * @param arr   the array that will be sorted
   * @param left  the beginning side of the array
   * @param right the end side of the array
   * @return the pivot or -1 if not sorted
   */
  public static int quicksort(double[] arr, int left, int right) {
    if (left < right) {
      int pivot = randomPartition(arr, left, right);

      return pivot;
    }

    return -1;
  }

  /**
   * Takes the random partition and sets it to the first element
   * 
   * @param arr   the array that will be sorted
   * @param left  the beginning side of the array
   * @param right the end side of the array
   * @return the new pivot
   */
  private static int randomPartition(double[] arr, int left, int right) {
    Random random = new Random();
    int randIndex = random.nextInt(right - left + 1) + left;

    swap(arr, randIndex, left);
    return partition(arr, left, right);
  }

  /**
   * The partition method of quick sort
   * 
   * @param arr   the array that will be sorted
   * @param left  the beginning side of the array
   * @param right the end side of the array
   * @return the new pivot
   */
  private static int partition(double[] a, int left, int right) {
    if (left < right) {
      int pivot = left;
      int i = left + 1;
      int j = right;

      while (i <= j) {
        while (i <= j && a[i] <= a[pivot]) {
          i++;
        }

        while (i <= j && a[j] >= a[pivot]) {
          j--;
        }

        if (i < j) {
          swap(a, i, j);
        }
      }

      swap(a, pivot, j);
      return j;
    }

    return left;
  }

  public static void hybridsort(double[] arr, int left, int right) {
    if ((right - left) < 50) {
      quadraticsort(arr, left, right);
    } else {
      int pivot = quicksort(arr, left, right);

      hybridsort(arr, left, pivot - 1);
      hybridsort(arr, pivot + 1, right);
    }
  }

  /**
   * Swaps the two indexes of the array
   * 
   * @param arr the array that will have its indexes swapped
   * @param i   one of the indexes that will swap (the bigger number of the pivot)
   * @param j   one of the indexes that will swap (the smaller number of the
   *            pivot)
   */
  private static void swap(double[] arr, int i, int j) {
    double temp = arr[j];
    arr[j] = arr[i];
    arr[i] = temp;
  }

  public static void printArray(double[] array) {
    for (int i = 0; i < array.length; i++) {
      System.out.print(array[i] + " ");
    }
  }

  public static void main(String[] args) {
    double[] array = { 13, 19, 9, 5, 12, 8, 7, 4, 21, 2, 6, 11 };

    HybridSort.hybridsort(array, 0, array.length - 1);
    System.out.println(Arrays.toString(array));
    // return 2 4 5 6 7 8 9 11 12 13 19 21

    // System.out.println("\n\n" + index);
  }

}
