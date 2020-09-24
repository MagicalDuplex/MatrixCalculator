package code;

import javafx.fxml.FXML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

public class Controller {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtAreaMatrixA;

    @FXML
    private TextArea txtAreaMatrixB;

    @FXML
    private TextArea txtAreaMatrixC;

    @FXML
    private TextField txtFieldNumberA;

    @FXML
    private TextField txtFieldNumberB;

    @FXML
    private TextField txtFieldDeterminantA;

    @FXML
    private TextField txtFieldDeterminantB;

    /**
     * Обработчик кнопки сложения матриц
     */
    @FXML
    void btnAddMatricesPressed(MouseEvent event) {
        var firstMatrix = parseTxtAreaMatrix(txtAreaMatrixA);
        var secondMatrix = parseTxtAreaMatrix(txtAreaMatrixB);

        if (checkMatrix(firstMatrix) && checkMatrix(secondMatrix)) {
            var newMatrix = CalculationMethods.add(firstMatrix, secondMatrix);

            if (checkMatrix(newMatrix)) {
                printMatrix(txtAreaMatrixC, newMatrix);
            }
        }
    }

    /**
     * Обработчик кнопки вычитания матриц
     */
    @FXML
    void btnSubtractMatricesPressed(MouseEvent event) {
        var firstMatrix = parseTxtAreaMatrix(txtAreaMatrixA);
        var secondMatrix = parseTxtAreaMatrix(txtAreaMatrixB);

        if (checkMatrix(firstMatrix) && checkMatrix(secondMatrix)) {
            var newMatrix = CalculationMethods.subtract(firstMatrix, secondMatrix);

            if (checkMatrix(newMatrix)) {
                printMatrix(txtAreaMatrixC, newMatrix);
            }
        }
    }

    /**
     * Обработчик кнопки умножения матриц
     */
    @FXML
    void btnMultiplyMatricesPressed(MouseEvent event) {
        var firstMatrix = parseTxtAreaMatrix(txtAreaMatrixA);
        var secondMatrix = parseTxtAreaMatrix(txtAreaMatrixB);

        if (checkMatrix(firstMatrix) && checkMatrix(secondMatrix)) {
            var newMatrix = CalculationMethods.multiply(firstMatrix, secondMatrix);

            if (checkMatrix(newMatrix)) {
                printMatrix(txtAreaMatrixC, newMatrix);
            }
        }
    }

    /**
     * Обработчик кнопки транспонирования матрицы A
     */
    @FXML
    void btnTransposeMatrixAPressed(MouseEvent event) {
        var matrix = parseTxtAreaMatrix(txtAreaMatrixA);

        if (checkMatrix(matrix)) {
            var newMatrix = CalculationMethods.transpose(matrix);
            printMatrix(txtAreaMatrixA, newMatrix);
        }
    }

    /**
     * Обработчик кнопки транспонирования матрицы B
     */
    @FXML
    void btnTransposeMatrixBPressed(MouseEvent event) {
        var matrix = parseTxtAreaMatrix(txtAreaMatrixB);

        if (checkMatrix(matrix)) {
            var newMatrix = CalculationMethods.transpose(matrix);
            printMatrix(txtAreaMatrixB, newMatrix);
        }
    }

    /**
     * Обработчик кнопки умножения матрицы A на число
     */
    @FXML
    void btnMultiplyMatrixAByNumberPressed(MouseEvent event) {
        multiplyMatrixByNumber(txtAreaMatrixA, txtFieldNumberA);
    }

    /**
     * Обработчик кнопки умножения матрицы B на число
     */
    @FXML
    void btnMultiplyMatrixBByNumberPressed(MouseEvent event) {
        multiplyMatrixByNumber(txtAreaMatrixB, txtFieldNumberB);
    }

    /**
     * Функция умножения матрицы на число
     * @param txtAreaMatrix - Тествое поле с матрицей
     * @param txtFieldNumber - Тествое поле с числом
     */
    private void multiplyMatrixByNumber(TextArea txtAreaMatrix, TextField txtFieldNumber) {
        var matrix = parseTxtAreaMatrix(txtAreaMatrix);

        if (checkMatrix(matrix)) {
            try {
                var number = Double.parseDouble(txtFieldNumber.getText());
                var newMatrix = CalculationMethods.multiplyByNumber(matrix, number);
                printMatrix(txtAreaMatrix, newMatrix);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.NONE,
                        "Не верно введено число", ButtonType.APPLY);
                alert.show();
            }
        }
    }

    /**
     * Обработчик кнопки определения детерминанта матрицы A
     */
    @FXML
    void btnCalculateDeterminantAPressed(MouseEvent event) {
        calculateMatrixDeterminant(txtAreaMatrixA, txtFieldDeterminantA);
    }

    /**
     * Обработчик кнопки определения детерминанта матрицы B
     */
    @FXML
    void btnCalculateDeterminantBPressed(MouseEvent event) {
        calculateMatrixDeterminant(txtAreaMatrixB, txtFieldDeterminantB);
    }

    /**
     * Функция определения детерминанта матрицы
     * @param txtAreaMatrix - Тествое поле с матрицей
     * @param txtFieldDeterminant - Тествое поле для детерминанта
     */
    private void calculateMatrixDeterminant(TextArea txtAreaMatrix, TextField txtFieldDeterminant) {
        var matrix = parseTxtAreaMatrix(txtAreaMatrix);

        if (checkMatrix(matrix) && checkDimensionForDeterminant(matrix)) {
            var determinant = CalculationMethods.calculateDeterminant(matrix);
            txtFieldDeterminant.setText(determinant + "");
        }
    }

    /**
     * Обработчик кнопки нахождения обратной матрицы A
     */
    @FXML
    void btnInvertMatrixAPressed(MouseEvent event) {
        invertMatrix(txtAreaMatrixA);
    }

    /**
     * Обработчик кнопки нахождения обратной матрицы B
     */
    @FXML
    void btnInvertMatrixBPressed(MouseEvent event) {
        invertMatrix(txtAreaMatrixB);
    }

    /**
     * Функция нахождения обратной матрицы
     * @param txtAreaMatrix - Тествое поле с матрицей
     */
    private void invertMatrix (TextArea txtAreaMatrix) {
        var matrix = parseTxtAreaMatrix(txtAreaMatrix);

        if (checkMatrix(matrix) && checkDimensionForDeterminant(matrix)) {
            if(CalculationMethods.calculateDeterminant(matrix) != 0) {
                var newMatrix = CalculationMethods.invert(matrix);
                printMatrix(txtAreaMatrix, newMatrix);
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE,
                        "Для вырожденных матриц обратных матриц не существует", ButtonType.APPLY);
                alert.show();
            }
        }
    }

    /**
     * Обработчик кнопки сохранения результат вычесления в файл
     */
    @FXML
    void btnSavePressed(MouseEvent event) throws IOException {
        DirectoryChooser dc = new DirectoryChooser();
        var selectedDir = dc.showDialog(null);

        File file = new File(selectedDir + "/" + "result.txt");

        if(!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(txtAreaMatrixC.getText());
        fileWriter.flush();
        fileWriter.close();
    }

    private double[][] parseTxtAreaMatrix(TextArea txtAreaMatrix) {

        try {
            var strRows = txtAreaMatrix.getText().lines();
            var arrayRows = strRows.toArray(String[]::new);

            var row = getDoubleRow(arrayRows[0]);
            double[][] matrix;

            if (row != null) {
                var listRows = new ArrayList<double[]>();

                listRows.add(row);
                var countCol = row.length;

                for (var i = 1; i < arrayRows.length; i++) {

                    row = getDoubleRow(arrayRows[i]);

                    if (row != null) {
                        if (row.length == countCol) {
                            listRows.add(row);
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }

                matrix = new double[listRows.size()][countCol];

                for (var rowM = 0; rowM < countCol; rowM++) {
                    for (var colM = 0; colM < listRows.size(); colM++) {
                        matrix[colM][rowM] = listRows.get(colM)[rowM];
                    }
                }
            } else {
                return null;
            }

            return matrix;
        } catch (Exception e) {
            return null;
        }
    }

    private double[] getDoubleRow(String row) {
        row = deleteDoubleSpace(row);

        if (!row.equals("") && !row.equals(" ")) {
            return convertRowToDouble(row);
        } else {
            return null;
        }
    }

    private String deleteDoubleSpace(String row) {
        return row.replaceAll("[\\s]{2,}", " ");
    }

    private double[] convertRowToDouble(String row) {
        var strArr = row.split(" ");
        var numArr = new double[strArr.length];

        for (var i = 0; i < numArr.length; i++) {
            try {
                numArr[i] = Double.parseDouble(strArr[i]);
            } catch (Exception e) {
                return null;
            }
        }

        return numArr;
    }

    private boolean checkMatrix(double[][] matrix) {
        if (matrix != null) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "Не верно введена матрица", ButtonType.APPLY);
            alert.show();
            return false;
        }
    }

    private static boolean checkDimensionForDeterminant(double[][] matrix) {
        if((matrix.length == matrix[0].length)) {
            return true;
        }else {
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "Матрица должна быть квадратной", ButtonType.APPLY);
            alert.show();
            return false;
        }
    }

    private void printMatrix(TextArea txtAreaMatrix, double[][] matrix) {
        txtAreaMatrix.clear();


        for (int col = 0; col < matrix.length; col++) {
            for (int row = 0; row < matrix[0].length; row++) {
                txtAreaMatrix.appendText((double)Math.round(matrix[col][row] * 100000) / 100000 + " ");
            }

            txtAreaMatrix.appendText("\r\n");
        }
    }
}