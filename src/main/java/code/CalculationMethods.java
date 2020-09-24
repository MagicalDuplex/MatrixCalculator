package code;

public class CalculationMethods {
    /**
     * Функция сложения двух матриц
     * @param firstMatrix - Первая матрица
     * @param secondMatrix - Вторая матрица
     * @return возвращает результат сложения матриц
     */
    public static double[][] add(double[][] firstMatrix, double[][] secondMatrix) {
        if (!checkDimensionOfMatrices(firstMatrix, secondMatrix)) return null;

        addSub(firstMatrix, secondMatrix);

        return firstMatrix;
    }

    /**
     * Функция вычитания двух матриц
     * @param firstMatrix - Первая матрица
     * @param secondMatrix - Вторая матрица
     * @return возвращает результат вычитания матриц
     */
    public static double[][] subtract(double[][] firstMatrix, double[][] secondMatrix) {
        if (!checkDimensionOfMatrices(firstMatrix, secondMatrix)) return null;

        secondMatrix = multiplyByNumber(secondMatrix, -1);
        addSub(firstMatrix, secondMatrix);

        return firstMatrix;
    }

    /**
     * Производит арифметическое действия для сложения/вычитания матриц
     * @param firstMatrix - Первая матрица
     * @param secondMatrix - Вторая матрица
     * @return возвращает результат сложения/вычитания матриц
     */
    public static double[][] addSub(double[][] firstMatrix, double[][] secondMatrix) {
        for (var col = 0; col < firstMatrix.length; col++) {
            for (var row = 0; row < firstMatrix[0].length; row++) {
                firstMatrix[col][row] = firstMatrix[col][row] + secondMatrix[col][row];
            }
        }

        return firstMatrix;
    }

    /**
     * Функция проверки размеронсти матриц, для сложения и вычитания
     * @param firstMatrix - Первая матрица
     * @param secondMatrix - Вторая матрица
     * @return если размерности матриц равны вернёт true, иначе false
     */
    private static boolean checkDimensionOfMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        if ((firstMatrix.length == secondMatrix.length) && (firstMatrix[0].length == secondMatrix[0].length))
            return true;
        return false;
    }


    /**
     * Функция умножения двух матриц
     * @param firstMatrix - Первая матрица
     * @param secondMatrix - Вторая матрица
     * @return возвращает результат умножения матриц
     */
    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        var fColumns = firstMatrix.length;
        var fRows = firstMatrix[0].length;
        
        var sColumns = secondMatrix.length;
        var sRows = secondMatrix[0].length;

        if (fRows != sColumns) {
            return null;
        }

        var result = new double[fColumns][sRows];

        for (var fCol = 0; fCol < fColumns; fCol++) {
            for (var sRow = 0; sRow < sRows; sRow++) {
                for (var fRow = 0; fRow < fRows; fRow++) {
                    result[fCol][sRow] += (firstMatrix[fCol][fRow] * secondMatrix[fRow][sRow]);
                }
            }
        }

        return result;
    }

    /**
     * Функция транспонировании матрицы
     * @param matrix - Матрица
     * @return возвращает результат транспонирования матрицы
     */
    public static double[][] transpose(double[][] matrix) {
        var tmp = new double[matrix[0].length][matrix.length];
        for (var col = 0; col < matrix[0].length; col++) {
            for (var row = 0; row < matrix.length; row++) {
                tmp[col][row] = matrix[row][col];
            }
        }

        return tmp;
    }

    /**
     * Функция умножения матрицы на число
     * @param matrix - Матрица
     * @param number - Число
     * @return возвращает результат умножения матрицы на число
     */
    public static double[][] multiplyByNumber(double[][] matrix, double number) {
        for (var col = 0; col < matrix.length; col++) {
            for (var row = 0; row < matrix[0].length; row++) {
                matrix[col][row] = matrix[col][row] * number;
            }
        }

        return matrix;
    }

    /**
     * Функция определения детерминанта матрицы
     * @param matrix - Матрица
     * @return возвращает детерминант матрицы
     */
    public static double calculateDeterminant(double[][] matrix) {
        double temporary[][];
        double result = 0;

        if (matrix.length == 1) {
            result = matrix[0][0];
            return result;
        }

        if (matrix.length == 2) {
            result = ((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]));
            return result;
        }

        for (int fRow = 0; fRow < matrix[0].length; fRow++) {
            temporary = new double[matrix.length - 1][matrix[0].length - 1];

            for (int fCol = 1; fCol < matrix.length; fCol++) {
                for (int sRow = 0; sRow < matrix[0].length; sRow++) {
                    if (sRow < fRow) {
                        temporary[fCol - 1][sRow] = matrix[fCol][sRow];
                    } else if (sRow > fRow) {
                        temporary[fCol - 1][sRow - 1] = matrix[fCol][sRow];
                    }
                }
            }

            result += matrix[0][fRow] * Math.pow(-1, (double) fRow) * calculateDeterminant(temporary);
        }

        return result;
    }

    /**
     * Функция нахождения обратной матрицы
     * @param matrix - Матрица
     * @return возвращает обратную матрицу
     */
    public static double[][] invert(double[][] matrix) {
        double[][] identityMatrix = new double[matrix.length][matrix[0].length];

        for (int col = 0; col < matrix.length; col++) {
            for (int row = 0; row < matrix[0].length; row++) {
                identityMatrix[col][row] = 0f;

                if (col == row)
                    identityMatrix[col][row] = 1f;
            }
        }

        double temp;

        for (int fCol = 0; fCol < matrix.length; fCol++) {
            temp = matrix[fCol][fCol];

            for (int fRow = 0; fRow < matrix.length; fRow++) {
                matrix[fCol][fRow] /= temp;
                identityMatrix[fCol][fRow] /= temp;
            }

            for (int sCol = fCol + 1; sCol < matrix.length; sCol++) {
                temp = matrix[sCol][fCol];

                for (int sRow = 0; sRow < matrix.length; sRow++) {
                    matrix[sCol][sRow] -= matrix[fCol][sRow] * temp;
                    identityMatrix[sCol][sRow] -= identityMatrix[fCol][sRow] * temp;
                }
            }
        }

        for (int fCol = matrix.length - 1; fCol > 0; fCol--) {
            for (int sCol = fCol - 1; sCol >= 0; sCol--) {
                temp = matrix[sCol][fCol];

                for (int fRow = 0; fRow < matrix.length; fRow++) {
                    matrix[sCol][fRow] -= matrix[fCol][fRow] * temp;
                    identityMatrix[sCol][fRow] -= identityMatrix[fCol][fRow] * temp;
                }
            }
        }

        for (int col = 0; col < matrix.length; col++)
            for (int row = 0; row < matrix.length; row++)
                matrix[col][row] = identityMatrix[col][row];


        return matrix;
    }
}