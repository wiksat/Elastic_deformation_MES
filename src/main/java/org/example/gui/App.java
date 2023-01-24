package org.example.gui;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Elastic_Deformation_FEM;

public class App extends Application{

    private LineChart linechart;

    @Override
    public void start(Stage primaryStage) throws IllegalArgumentException {
        Elastic_Deformation_FEM solver = new Elastic_Deformation_FEM(2);
        solver.compute(3);
        Label label = new Label("Elastic Deformation FEM");
        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(label);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setStyle("-fx-font-size: 20px");
        NumberAxis xAxis = new NumberAxis("x", 0, 2.1, 0.1);
        NumberAxis yAxis = new NumberAxis("u(x)", 0, 60, 1);

        linechart = new LineChart(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("Wykres funckji u(x)");
        for (int i = 0; i <solver.y_es.length; i++) {
            if (solver.y_es[i]>0 || solver.x_es[i]>0) {
                series.getData().add(new XYChart.Data<>(solver.x_es[i], solver.y_es[i]));
            }
        }
        linechart.setMinWidth(900);
        linechart.setMaxWidth(900);
        linechart.setMinHeight(600);
        linechart.setMaxHeight(600);
        linechart.setCreateSymbols(false);
        linechart.getData().add(series);

        TextField movesInput = new TextField();
        Button startButton = new Button("Calculate");
        startButton.setOnAction(action -> {
            int new_n = Integer.parseInt(movesInput.getText());
            if (new_n > 50 || new_n==1) {
                throw new IllegalArgumentException("too low or too many elements");
            }
            linechart.getData().clear();
            solver.compute(new_n);
            XYChart.Series seriesB = new XYChart.Series();
            seriesB.setName("Wykres funckji u(x)");
            for (int i = 0; i < solver.y_es.length; i++) {
                if(solver.x_es[i]>0 || solver.y_es[i]>0){
                    seriesB.getData().add(new XYChart.Data<>(solver.x_es[i], solver.y_es[i]));

                }
            }
            linechart.getData().add(seriesB);
        });
        Label label2 = new Label("(a larger number may cause the program to run slowly)");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(movesInput, startButton);
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-font-size: 20px");


        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBox1,linechart,label2, hBox);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox, 900, 750);
        primaryStage.setTitle("Elastic Deformation FEM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
