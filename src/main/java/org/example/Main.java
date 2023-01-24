package org.example;
import org.example.gui.App;
import javafx.application.Application;
public class Main {
    public static void main(String[] args) {
        System.out.println("Elastic deformation FEM by WikSat");
        try{
            Application.launch(App.class, args);
        }catch (Exception e){
            System.out.println("błąd");
        }

    }
}