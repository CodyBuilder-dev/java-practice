package math;

public class MatrixPivot {
  public static void main(String[] args) {
    int[][] matrix = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };
    printMatrix(matrix);

    int[][] pivotedMatrix = pivotMatrix(matrix);
    printMatrix(pivotedMatrix);
  }

  public static int[][] pivotMatrix(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return matrix;
    }

    int rowCount = matrix.length;
    int colCount = matrix[0].length;
    int[][] pivoted = new int[colCount][rowCount];

    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < colCount; j++) {
        pivoted[j][i] = matrix[i][j];
      }
    }

    return pivoted;
  }

  public static void printMatrix(int[][] matrix) {
    for (int[] row : matrix) {
      for (int val : row) {
        System.out.print(val + " ");
      }
      System.out.println();
    }
  }
}