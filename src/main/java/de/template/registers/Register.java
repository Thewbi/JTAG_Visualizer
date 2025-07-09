package de.template.registers;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Register {

    public int instruction;

    public String name;

    public boolean current;

    public int value;

    public Rectangle rectangle = new Rectangle();

    private Text content = new Text();

    public VBox vBox = new VBox();

    private Register shiftRegister;
    
    public Node getRectangle() {

        StackPane stackPane = new StackPane();
        
        Text text = null;
        if (instruction != 0x00) {
            String instructionAsString = String.format("%8s", Integer.toBinaryString(instruction)).replace(' ', '0');
            text = new Text(name + " (" + instructionAsString + ")");
        } else {
            text = new Text(name);
        }

        rectangle.setLayoutX(10);
        rectangle.setLayoutY(10);
        rectangle.setWidth(200);
        rectangle.setHeight(40);
        if (current) {
            rectangle.setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 5;");
        } else {
            rectangle.setStyle("-fx-fill: none; -fx-stroke: black; -fx-stroke-width: 2;");
        }

        stackPane.getChildren().addAll(rectangle, content);

        vBox.getChildren().addAll(stackPane, text);

        return vBox;
    }

    public void activate() {
        current = true;
        rectangle.setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 5;");
    }

    public void deactivate() {
        current = false;
        rectangle.setStyle("-fx-fill: none; -fx-stroke: black; -fx-stroke-width: 2;");
    }

    public void setPosition(int x, int y) {
        vBox.setLayoutX(x);
        vBox.setLayoutY(y);
    }

    public void setValue(int x) {
        value = x;

        String data = String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0');
        content.setText(data);
    }

    public int getValue() {
        return value;
    }

    public void setShiftRegister(Register shiftRegister) {
        this.shiftRegister = shiftRegister;
    }

    public Register getShiftRegister() {
        return shiftRegister;
    }

}
