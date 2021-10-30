package com.example.calculator ;

import javafx.application.Application ;
import javafx.event.ActionEvent ;
import javafx.event.EventHandler ;
import javafx.scene.Scene ;
import javafx.scene.control.Button ;
import javafx.scene.control.Label ;
import javafx.scene.layout.VBox ;
import javafx.scene.layout.GridPane ;
import javafx.stage.Stage ;

import java.io.IOException ;
import java.util.ArrayList;
import javax.script.ScriptEngineManager ;
import javax.script.ScriptEngine ;
import javax.script.ScriptException ;


public class Calculator extends Application
{
    String displayString  ;
    String memoryString ;
    Label display ;
    ArrayList<Button> buttons ;
    ArrayList<Character> operations ;

    ScriptEngineManager mgr ;
    ScriptEngine engine ;

    private void addSymbol(GridPane root, ArrayList<Button> buttonArray, int position, String symbol, int i, int i1)
    {
        buttonArray.add(position, new Button(symbol)) ;
        buttonArray.get(position).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                displayString = displayString + symbol ;
                display.setText(displayString) ;
            }
        }) ;

        root.add(buttons.get(position), i, i1) ;
    }

    private void evaluateExpression(ScriptEngine engine) throws ScriptException
    {
        try
        {
            memoryString = String.format("%f", (Double) engine.eval(displayString)) ;
        }
        catch(ClassCastException c)
        {
            memoryString = String.format("%d", (Integer) engine.eval(displayString)) ;
        }
    }

    private void addEquals(GridPane root, ArrayList<Button> buttonArray, int position, ScriptEngine engine, int i, int i1)
    {
        buttonArray.add(position, new Button("=")) ;
        buttonArray.get(position).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent){
                try {
                    evaluateExpression(engine) ;
                } catch (ScriptException e) {
                    memoryString = "Error" ;
                }

                if(memoryString.equals("Error"))
                {
                    displayString = "" ;
                    display.setText(memoryString);
                }
                else
                {
                    displayString = memoryString ;
                    display.setText(displayString) ;
                }
            }
        }) ;

        root.add(buttons.get(position), i, i1) ;
    }

    private void addAns(GridPane root, ArrayList<Button> buttonArray, int position, int i, int i1)
    {
        buttonArray.add(position, new Button("Ans")) ;
        buttonArray.get(position).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent){
                if(!memoryString.equals("Error") && memoryString.length() > 0)
                {
                    if(operations.contains(displayString.charAt(displayString.length() -1)))
                    {
                        displayString = displayString + "(" + memoryString + ")" ;
                    }
                    else
                    {
                        displayString = displayString + "*" + "(" + memoryString + ")" ;
                    }
                    display.setText(displayString) ;
                }
                else
                {
                    memoryString = "" ;
                }
            }
        }) ;

        root.add(buttons.get(position), i, i1) ;
    }

    private void addDel(GridPane root, ArrayList<Button> buttonArray, int position, int i, int i1)
    {
        buttonArray.add(new Button("Del")) ;
        buttonArray.get(position).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if(displayString.length()>0)
                {
                    displayString = displayString.substring(0, displayString.length() -1) ;
                    display.setText(displayString) ;
                }
            }
        });

        root.add(buttons.get(position), i, i1) ;
    }

    private void addAC(GridPane root, ArrayList<Button> buttonArray, int position, int i, int i1)
    {
        buttonArray.add(new Button("AC")) ;
        buttonArray.get(position).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                displayString = "" ;
                display.setText(displayString) ;
            }
        });

        root.add(buttons.get(position), i, i1) ;
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        displayString = "" ;
        memoryString = "" ;
        buttons = new ArrayList<Button>() ;
        operations = new ArrayList<Character>() ;
        operations.add('+') ;
        operations.add('-') ;
        operations.add('*') ;
        operations.add('/') ;
        stage.setTitle("Calculator") ;
        GridPane root = new GridPane() ;
        Scene scene = new Scene(root,1024,768);
        stage.setScene(scene);

        mgr = new ScriptEngineManager() ;
        engine = mgr.getEngineByName("JavaScript") ;

        display = new Label(displayString) ;
        root.add(display, 0, 0) ;

        addSymbol(root, buttons, 0, "0", 1, 4) ;
        addSymbol(root, buttons, 1, "1", 0, 3) ;
        addSymbol(root, buttons, 2, "2", 1, 3) ;
        addSymbol(root, buttons, 3, "3", 2, 3) ;
        addSymbol(root, buttons, 4, "4", 0, 2) ;
        addSymbol(root, buttons, 5, "5", 1, 2) ;
        addSymbol(root, buttons, 6, "6", 2, 2) ;
        addSymbol(root, buttons, 7, "7", 0, 1) ;
        addSymbol(root, buttons, 8, "8", 1, 1) ;
        addSymbol(root, buttons, 1, "9", 2, 1) ;
        addSymbol(root, buttons, 10, "+", 3, 2) ;
        addSymbol(root, buttons, 11, "-", 3, 3) ;
        addSymbol(root, buttons, 12, "*", 4, 2) ;
        addSymbol(root, buttons, 13, "/", 4, 3) ;
        addSymbol(root, buttons, 14, "(", 4, 4) ;
        addSymbol(root, buttons, 15, ")", 5, 4) ;
        addSymbol(root, buttons, 16, ".", 0, 4) ;

        addEquals(root, buttons, 17, engine, 2, 4) ;
        addAns(root, buttons, 18, 3, 4) ;
        addDel(root, buttons, 19, 3, 1) ;
        addAC(root, buttons, 20, 4, 1) ;

        root.setHgap(40) ;
        root.setVgap(40) ;

        stage.show() ;
    }

    public static void main(String[] args)
    {
        launch(args) ;
    }
}