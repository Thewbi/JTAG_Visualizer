package de.template.state_machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class State {

    public String name;

    public boolean current;

    public Rectangle rectangle = new Rectangle();

    public StackPane stackPane = new StackPane();

    public Map<Integer, State> nextStateMap = new HashMap<>();

    private List<StateEventListener> stateEventListeners = new ArrayList<>();

    public void enter() {
        current = true;
        rectangle.setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 5;");
        for (StateEventListener eventListere : stateEventListeners) {
            eventListere.onEnter();
        }
    }

    public void exit() {
        current = false;
        rectangle.setStyle("-fx-fill: none; -fx-stroke: black; -fx-stroke-width: 2;");
        for (StateEventListener eventListere : stateEventListeners) {
            eventListere.onExit();
        }
    }

    public Node getRectangle() {
        Text text = new Text(name);
        rectangle.setLayoutX(10);
        rectangle.setLayoutY(10);
        rectangle.setWidth(200);
        rectangle.setHeight(40);
        if (current) {
            rectangle.setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 5;");
        } else {
            rectangle.setStyle("-fx-fill: none; -fx-stroke: black; -fx-stroke-width: 2;");
        }
        stackPane.getChildren().addAll(rectangle, text);
        return stackPane;
    }

    public void setPosition(int x, int y) {
        stackPane.setLayoutX(x);
        stackPane.setLayoutY(y);
    }

    public State getNextStateForInput(int data) {
        return nextStateMap.get(data);
    }

    public void addEventListener(StateEventListener stateEventListener) {
        stateEventListeners.add(stateEventListener);
    }

}
