package sample;

import com.sun.org.apache.bcel.internal.generic.LCONST;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.orangepalantir.leastsquares.Fitter;
import org.orangepalantir.leastsquares.Function;
import org.orangepalantir.leastsquares.fitters.LinearFitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    public TextArea TADataXY;
    public TextField TFOrder;
    public Button BCalculate;
    public Label LHelpText;
    public Pane PBack;
    public AnchorPane APBack;
    public TextArea TACoef;
    public Label LCoef;

    public void onBCalculateAction(ActionEvent actionEvent) {
        APBack.getChildren().clear();
        TACoef.setText("                                        Нет коэфициентов");
        TACoef.setLayoutX(501.0);
        TACoef.setLayoutY(40.0);
        TACoef.setPrefHeight(393.0);
        TACoef.setPrefWidth(452.0);
        LCoef.setText("Коэффициенты:");
        LCoef.setLayoutX(667.0);
        LCoef.setLayoutY(-2.0);
        LCoef.setPrefHeight(31.0);
        LCoef.setPrefWidth(207.0);
        Font font = new Font(22.0);
        LCoef.setFont(font);
        APBack.getChildren().addAll(TACoef, LCoef);
        LHelpText.setText("");
        List<Double> dataXList = new ArrayList<Double>();
        List<Double> dataYList = new ArrayList<Double>();

        String data = TADataXY.getText();
        String cleaningData[] = data.split(";");

        double currentX = 0;
        double currentY = 0;
        boolean add;
        String coordStr[];

        for (int i = 0; i < cleaningData.length; i++){
            cleaningData[i] = cleaningData[i].trim();
            coordStr = cleaningData[i].split(",");
            if (coordStr.length > 2 || coordStr.length < 2) continue;
            for (int j = 0; j < 2; j++){
                coordStr[j] = coordStr[j].trim();
            }

            add = true;
            try {
                currentX = Double.parseDouble(coordStr[0]);
                currentY = Double.parseDouble(coordStr[1]);
            }
            catch (Exception e) {
                add = false;
            }

            if (add){
                dataXList.add(currentX);
                dataYList.add(currentY);
            }
        }

        if (dataXList.size() == 0 || dataYList.size() == 0) {
            LHelpText.setText("Введите действительные координаты!");
        }
        else {

            int order;
            try {
                order = Integer.parseInt(TFOrder.getText());
            }
            catch (Exception e){
                order = dataXList.size();
            }

            double[][] xs = new double[dataXList.size()][1];
            double[] zs = new double[dataYList.size()];

            for (int i = 0; i < dataXList.size(); i++){
                xs[i][0] = dataXList.get(i);
                System.err.println(xs[i][0]);
                zs[i] = dataYList.get(i);
                System.err.println(zs[i]);
            }

            final int finalOrder = order;

            System.err.println(dataXList.size());
            System.err.println(dataYList.size());
            Function fun = new Function(){
                @Override
                public double evaluate(double[] values, double[] parameters) {
                    double[] params = new double[finalOrder + 1];
                    for (int i = 0; i < finalOrder + 1; i++){
                        params[i] = parameters[i];
                    }
                    double x = values[0];
                    double result = 0;
                    double currentRes;

                    for (int i = 0; i < finalOrder + 1; i++){
                        currentRes = params[i];
                        for (int j = finalOrder; j >= i+1; j--){
                            currentRes *= x;
                        }
                        result += currentRes;
                    }

                    return result;
                }
                @Override
                public int getNParameters() {
                    return finalOrder + 1;
                }

                @Override
                public int getNInputs() {
                    return 1;
                }
            };

            Fitter fit = new LinearFitter(fun);
            fit.setData(xs, zs);
            double[] params = new double[finalOrder + 1];
            fit.setParameters(params);

            fit.fitData();

            System.out.println(Arrays.toString(fit.getParameters()));

            NumberAxis x = new NumberAxis();
            NumberAxis y = new NumberAxis();

            LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x,y);
            numberLineChart.setTitle("Graph");
            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Function");
            ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
            for (int i = 0; i < dataXList.size(); i++){
                datas.add(new XYChart.Data(dataXList.get(i), dataYList.get(i)));
            }

            series1.setData(datas);
            numberLineChart.getData().add(series1);

            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Optimised function");
            double[] parameters = fit.getParameters();
            double currentRes, result;
            ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
            for (int i = 0; i < dataXList.size(); i++){
                result = 0;
                for (int j = 0; j < parameters.length; j++){
                    currentRes = parameters[j];
                    for (int k = parameters.length - 1; k >= j+1; k--){
                        currentRes *= dataXList.get(i);
                    }
                    result += currentRes;
                }
                System.err.println(dataXList.get(i));
                System.err.println(result);
                datas2.add(new XYChart.Data(dataXList.get(i), result));
            }

            series2.setData(datas2);
            numberLineChart.getData().add(series2);

            APBack.getChildren().addAll(numberLineChart);

            String paramString = "";
            for (int i = 0; i < parameters.length; i++){
                paramString += "[" + (i+1) + "]  " + parameters[i] + "\n";
            }

            TACoef.setText(paramString);
        }
    }
}
