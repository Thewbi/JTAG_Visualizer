package de.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.template.registers.Register;
import de.template.state_machine.State;
import de.template.state_machine.StateEventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class FXMLController {

    TODO: add a custom data register and implement capture, data shifting and updating

    private static final int IDCODE = 0x7f;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button tdi0TMS0Button;

    @FXML
    private Button tdi1TMS0Button;

    @FXML
    private Button tdi0TMS1Button;

    @FXML
    private Button tdi1TMS1Button;

    // @FXML
    // private Label label;

    private Register irShiftRegister = new Register();
    private Register irDataRegister = new Register();

    private Register idcodeShiftRegister = new Register();
    private Register idcodeDataRegister = new Register();

    private Map<Integer, Register> instructionMap = new HashMap<>();

    private State testLogicResetState = new State();

    private State runTestIdleState = new State();

    private State selectDRScanState = new State();

    private State captureDRState = new State();

    private State shiftDRState = new State();

    private State exit1DRState = new State();

    private State pauseDRState = new State();

    private State exit2DRState = new State();

    private State updateDRState = new State();

    private State selectIRScanState = new State();

    private State captureIRState = new State();

    private State shiftIRState = new State();

    private State exit1IRState = new State();

    private State pauseIRState = new State();

    private State exit2IRState = new State();

    private State updateIRState = new State();

    private List<State> stateList = new ArrayList<>();

    @FXML
    private void handleButton0Action(ActionEvent event) {

        //System.out.println("You clicked TMS = 0");
        //label.setText("You clicked TMS = 0");

        for (State state : stateList) {
            if (state.current) {
                state.exit();
                State nextState = state.getNextStateForInput(0);
                nextState.enter();
                break;
            }
        }

    }

    @FXML
    private void handleButton1Action(ActionEvent event) {

        //System.out.println("You clicked TMS = 1");
        //label.setText("You clicked TMS = 1");

        for (State state : stateList) {
            if (state.current) {
                state.exit();
                State nextState = state.getNextStateForInput(1);
                nextState.enter();
                break;
            }
        }

    }

    @FXML
    public void handleButtonTDI0TMS0Action(ActionEvent event) {

        State currentState = null;
        for (State state : stateList) {
            if (state.current) {
                currentState = state;
                break;
            }
        }

        if (currentState.name.equalsIgnoreCase("Shift IR")) {
            int value = irShiftRegister.getValue();
            value >>= 1;
            value = value | (0 << 7);
            irShiftRegister.setValue(value);
        }

        State nextState = null;
        for (State state : stateList) {
            if (state.current) {
                state.exit();
                nextState = state.getNextStateForInput(0);
                nextState.enter();
                break;
            }
        }

    }

    @FXML
    public void handleButtonTDI1TMS0Action(ActionEvent event) {

        State currentState = null;
        for (State state : stateList) {
            if (state.current) {
                currentState = state;
                break;
            }
        }

        if (currentState.name.equalsIgnoreCase("Shift IR")) {
            int value = irShiftRegister.getValue();
            value >>= 1;
            value = value | (1 << 7);
            irShiftRegister.setValue(value);
        }

        for (State state : stateList) {
            if (state.current) {
                state.exit();
                State nextState = state.getNextStateForInput(0);
                nextState.enter();
                break;
            }
        }
    }

    @FXML
    public void handleButtonTDI0TMS1Action(ActionEvent event) {
        for (State state : stateList) {
            if (state.current) {
                state.exit();
                State nextState = state.getNextStateForInput(1);
                nextState.enter();
                break;
            }
        }
    }

    @FXML
    public void handleButtonTDI1TMS1Action(ActionEvent event) {
        for (State state : stateList) {
            if (state.current) {
                state.exit();
                State nextState = state.getNextStateForInput(1);
                nextState.enter();
                break;
            }
        }
    }

    public void initialize() {

        tdi0TMS0Button.setDisable(true);
        tdi1TMS0Button.setDisable(true);
        tdi0TMS1Button.setDisable(true);
        tdi1TMS1Button.setDisable(true);

        instructionMap.put(IDCODE, idcodeDataRegister);

        int offsetX = 800;
        int offsetY = 10;

        irShiftRegister.name = "Shift IR";
        irShiftRegister.current = false;
        irShiftRegister.instruction = 0x00;
        irShiftRegister.setPosition(offsetX + 10, offsetY);
        irShiftRegister.setShiftRegister(irShiftRegister);
        anchorPane.getChildren().add(irShiftRegister.getRectangle());

        irDataRegister.name = "Data IR";
        irDataRegister.current = false;
        irDataRegister.instruction = 0x00;
        irDataRegister.setValue(IDCODE); // place IRCODE value initially
        irDataRegister.setPosition(offsetX + 10, offsetY + 60);
        irDataRegister.setShiftRegister(irShiftRegister);
        anchorPane.getChildren().add(irDataRegister.getRectangle());

        offsetX = 800;
        offsetY = 160;

        idcodeShiftRegister.name = "Shift IDCODE";
        idcodeShiftRegister.current = false;
        idcodeShiftRegister.instruction = 0x00;
        idcodeShiftRegister.setPosition(offsetX + 10, offsetY);
        idcodeShiftRegister.setShiftRegister(idcodeShiftRegister);
        anchorPane.getChildren().add(idcodeShiftRegister.getRectangle());

        idcodeDataRegister.name = "Data IDCODE";
        idcodeDataRegister.current = false;
        idcodeDataRegister.instruction = IDCODE;
        idcodeDataRegister.setValue(0xAA);
        idcodeDataRegister.setPosition(offsetX + 10, offsetY + 60);
        idcodeDataRegister.setShiftRegister(idcodeShiftRegister);
        anchorPane.getChildren().add(idcodeDataRegister.getRectangle());

        offsetX = 200;
        offsetY = 10;

        testLogicResetState.name = "Test Logic Reset";
        testLogicResetState.setPosition(offsetX + 10, offsetY + 10);
        testLogicResetState.current = true;
        anchorPane.getChildren().add(testLogicResetState.getRectangle());

        int row = 0;

        Line line = new Line(offsetX + 10, offsetY + 1 + 50 + (70 * row) - 50, offsetX + 30, offsetY + 80 + (70 * row) - 50);
        anchorPane.getChildren().add(line);
        Text text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 5);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row) - 50);
        anchorPane.getChildren().add(text);

        line = new Line(offsetX + 10 + 100, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("a TMS = 0");
        text.setLayoutX(offsetX + 10 + 105);
        text.setLayoutY(offsetY + 70);
        anchorPane.getChildren().add(text);

        runTestIdleState.name = "Run Test Idle";
        runTestIdleState.setPosition(offsetX + 10, offsetY + 80);
        anchorPane.getChildren().add(runTestIdleState.getRectangle());

        row = 1;

        line = new Line(offsetX + 10, offsetY + 1 + 50 + (70 * row) - 50, offsetX + 30, offsetY + 80 + (70 * row) - 50);
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 5);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row) - 50);
        anchorPane.getChildren().add(text);

        line = new Line(offsetX + 10 + 100, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 105);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        selectDRScanState.name = "Select DR Scan";
        selectDRScanState.setPosition(offsetX + 10, offsetY + 150);
        selectDRScanState.addEventListener(new StateEventListener() {

            @Override
            public void onEnter() {

                irShiftRegister.deactivate();

                int instruction = irDataRegister.getValue();
                Register register = instructionMap.get(instruction);
                register.getShiftRegister().activate();
            }

            @Override
            public void onExit() {
                // nothing
            }

        });
        anchorPane.getChildren().add(selectDRScanState.getRectangle());

        line = new Line(offsetX + 10 + 1 + 200, offsetY + 1 + 50 + (70 * row) + 50, offsetX + 10 + 300, offsetY + 1 + 50 + (70 * row)+ 50);
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 1 + 220);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row) + 40);
        anchorPane.getChildren().add(text);

        row = 2;

        line = new Line(offsetX + 10 + 100, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 10 + 105);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        captureDRState.name = "Capture DR";
        captureDRState.setPosition(offsetX + 10, offsetY + 220);
        anchorPane.getChildren().add(captureDRState.getRectangle());

        captureDRState.addEventListener(new StateEventListener() {

            @Override
            public void onEnter() {
                irShiftRegister.deactivate();

                int instruction = irDataRegister.getValue();
                Register register = instructionMap.get(instruction);
                int value = register.getValue();
                register.getShiftRegister().setValue(value);
            }

            @Override
            public void onExit() {
                // nothing
            }

        });

        row = 3;

        // TMS = 0
        line = new Line(offsetX + 10 + 100, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 10 + 105);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        // TMS = 1 (CaptureDR -> Exit1DR)
        line = new Line(offsetX + 10 + 1 + 200, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 230, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);
        line = new Line(offsetX + 10 + 1 + 230, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 230, offsetY + 1 + 50 + (70 * (row + 1) + 50));
        anchorPane.getChildren().add(line);
        
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 1 + 210);
        text.setLayoutY(offsetY + 70 + (70 * row) - 50);
        anchorPane.getChildren().add(text);

        shiftDRState.name = "Shift DR";
        shiftDRState.setPosition(offsetX + 10, offsetY + 290);
        anchorPane.getChildren().add(shiftDRState.getRectangle());

        shiftDRState.addEventListener(new StateEventListener() {

            @Override
            public void onEnter() {
                tdi0TMS0Button.setDisable(false);
                tdi1TMS0Button.setDisable(false);
                tdi0TMS1Button.setDisable(false);
                tdi1TMS1Button.setDisable(false);
            }

            @Override
            public void onExit() {
                tdi0TMS0Button.setDisable(true);
                tdi1TMS0Button.setDisable(true);
                tdi0TMS1Button.setDisable(true);
                tdi1TMS1Button.setDisable(true);
            }

        });

        row = 4;

        line = new Line(offsetX + 10, offsetY + 1 + 50 + (70 * row) - 50, offsetX + 30, offsetY + 80 + (70 * row) - 50);
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 5);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row) - 50);
        anchorPane.getChildren().add(text);

        line = new Line(offsetX + 10 + 1 + 200, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 250, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 105);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        line = new Line(offsetX + 10 + 100, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);

        // vertical
        line = new Line(offsetX + 10 + 250, offsetY + 1 + 50 + (70 * row) - 20, offsetX + 10 + 250, offsetY + 80 + (70 * (row + 2)) + 20);
        anchorPane.getChildren().add(line);

        

        exit1DRState.name = "Exit 1 DR";
        exit1DRState.setPosition(offsetX + 10, offsetY + 360);
        anchorPane.getChildren().add(exit1DRState.getRectangle());

        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 - 50);
        text.setLayoutY(offsetY + 70 + (70 * row) + 20);
        anchorPane.getChildren().add(text);

        row = 5;



        // TMS = 1 (CaptureDR -> Exit1DR)
        line = new Line(offsetX + 10 + 1 + 200, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 230, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        

        line = new Line(offsetX + 10 + 100, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 10 + 105);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        pauseDRState.name = "Pause DR";
        pauseDRState.setPosition(offsetX + 10, offsetY + 430);
        anchorPane.getChildren().add(pauseDRState.getRectangle());

        row = 6;

        line = new Line(offsetX + 10, offsetY + 1 + 50 + (70 * row) - 50, offsetX + 30, offsetY + 80 + (70 * row) - 50);
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 5);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row) - 50);
        anchorPane.getChildren().add(text);

        line = new Line(offsetX + 10 + 100, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 105);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        exit2DRState.name = "Exit 2 DR";
        exit2DRState.setPosition(offsetX + 10, offsetY + 500);
        anchorPane.getChildren().add(exit2DRState.getRectangle());

        row = 7;

        // TMS = 1 (Exit2DR -> ShiftDR)
        line = new Line(offsetX + 10 + 1 + 200, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 250, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 10 + 205);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row));
        anchorPane.getChildren().add(text);

        line = new Line(offsetX + 10 + 100, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 105);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        

        updateDRState.name = "Update DR";
        updateDRState.setPosition(offsetX + 10, offsetY + 570);
        anchorPane.getChildren().add(updateDRState.getRectangle());


        row = 8;

        line = new Line(offsetX + 10 + 50, offsetY + 1 + 50 + (70 * (row)), offsetX + 10 + 50, offsetY + 1 + 50 + (70 * (row) + 20));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 50);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        line = new Line(offsetX + 10 + 160, offsetY + 1 + 50 + (70 * (row)), offsetX + 10 + 160, offsetY + 1 + 50 + (70 * (row) + 40));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 10 + 160);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        //
        // IR
        //

        selectIRScanState.name = "Select IR Scan";
        selectIRScanState.setPosition(offsetX + 10 + 300, offsetY + 150);
        anchorPane.getChildren().add(selectIRScanState.getRectangle());
        selectIRScanState.addEventListener(new StateEventListener() {

            @Override
            public void onEnter() {
                irShiftRegister.activate();
            }

            @Override
            public void onExit() {
                // nothing
            }

        });

        row = 2;

        line = new Line(offsetX + 10 + 100 + 300, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100 + 300, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("i TMS = 0");
        text.setLayoutX(offsetX + 10 + 105 + 300);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        // Select IR Scan to Test Logic Reset
        line = new Line(offsetX + 10 + 1 + 500, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 530, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX + 10 + 1 + 200, offsetY + 1 + 50 + (70 * (row - 3) + 50), offsetX + 10 + 530, offsetY + 1 + 50 + (70 * (row - 3) + 50));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX + 10 + 1 + 530, offsetY + 1 + 50 + (70 * (row - 3) + 50), offsetX + 10 + 1 + 530, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 1 + 510);
        text.setLayoutY(offsetY + 70 + (70 * (row - 1)) + 50);
        anchorPane.getChildren().add(text);

        row = 3;

        captureIRState.name = "Capture IR";
        captureIRState.setPosition(offsetX + 10 + 300, offsetY + 220);
        anchorPane.getChildren().add(captureIRState.getRectangle());

        captureIRState.addEventListener(new StateEventListener() {

            @Override
            public void onEnter() {
                irShiftRegister.setValue(irDataRegister.getValue());
            }

            @Override
            public void onExit() {
            }
                
        });

        // TMS = 1 (CaptureIR -> Exit1IR)
        line = new Line(offsetX + 10 + 1 + 500, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 530, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);
        line = new Line(offsetX + 10 + 1 + 530, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 530, offsetY + 1 + 50 + (70 * (row + 1) + 50));
        anchorPane.getChildren().add(line);
        
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 1 + 510);
        text.setLayoutY(offsetY + 70 + (70 * row) - 50);
        anchorPane.getChildren().add(text);

        

        line = new Line(offsetX + 10 + 100 + 300, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100 + 300, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("j TMS = 0");
        text.setLayoutX(offsetX + 10 + 105 + 300);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        

        shiftIRState.name = "Shift IR";
        shiftIRState.setPosition(offsetX + 10 + 300, offsetY + 290);
        anchorPane.getChildren().add(shiftIRState.getRectangle());
        shiftIRState.addEventListener(new StateEventListener() {

            @Override
            public void onEnter() {
                tdi0TMS0Button.setDisable(false);
                tdi1TMS0Button.setDisable(false);
                tdi0TMS1Button.setDisable(false);
                tdi1TMS1Button.setDisable(false);
            }

            @Override
            public void onExit() {
                tdi0TMS0Button.setDisable(true);
                tdi1TMS0Button.setDisable(true);
                tdi0TMS1Button.setDisable(true);
                tdi1TMS1Button.setDisable(true);
            }

        });

        row = 4;

        line = new Line(offsetX + 300, offsetY + 1 + 50 + (70 * row) - 50, offsetX + 330, offsetY + 80 + (70 * row) - 50);
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 295);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row) - 50);
        anchorPane.getChildren().add(text);

        // vertical
        line = new Line(offsetX + 10 + 550, offsetY + 1 + 50 + (70 * row) - 20, offsetX + 10 + 550, offsetY + 80 + (70 * (row + 2)) + 20);
        anchorPane.getChildren().add(line);

        

        line = new Line(offsetX + 10 + 1 + 500, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 550, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX + 10 + 100 + 300, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100 + 300, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 105 + 300);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        exit1IRState.name = "Exit 1 IR";
        exit1IRState.setPosition(offsetX + 10 + 300, offsetY + 360);
        anchorPane.getChildren().add(exit1IRState.getRectangle());

        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 250);
        text.setLayoutY(offsetY + 70 + (70 * row) + 20);
        anchorPane.getChildren().add(text);

        row = 5;

        // Exit1DR -> UpdateDR
        line = new Line(offsetX - 20, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX - 20, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX - 20, offsetY + 1 + 50 + (70 * (row + 2) + 50));
        anchorPane.getChildren().add(line);

        // TMS = 1 (CaptureIR -> Exit1IR)
        line = new Line(offsetX + 10 + 1 + 500, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 530, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX + 10 + 1 + 270, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 1 + 270, offsetY + 1 + 50 + (70 * (row + 2) + 50));
        anchorPane.getChildren().add(line);

        // Exit1IR -> UpdateIR
        line = new Line(offsetX + 10 + 1 + 270, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 300, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX + 10 + 100 + 300, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100 + 300, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 10 + 105 + 300);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        pauseIRState.name = "Pause IR";
        pauseIRState.setPosition(offsetX + 10 + 300, offsetY + 430);
        anchorPane.getChildren().add(pauseIRState.getRectangle());

        row = 6;

        line = new Line(offsetX + 300, offsetY + 1 + 50 + (70 * row) - 50, offsetX + 330, offsetY + 80 + (70 * row) - 50);
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 295);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row) - 50);
        anchorPane.getChildren().add(text);

        line = new Line(offsetX + 10 + 100 + 300, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100 + 300, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 105 + 300);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        exit2IRState.name = "Exit 2 IR";
        exit2IRState.setPosition(offsetX + 10 + 300, offsetY + 500);
        anchorPane.getChildren().add(exit2IRState.getRectangle());

        row = 7;

        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 10 + 505);
        text.setLayoutY(offsetY + 1 + 50 + (70 * row));
        anchorPane.getChildren().add(text);

        // TMS = 1 (CaptureIR -> Exit1IR)
        line = new Line(offsetX + 10 + 1 + 500, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 550, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);


        

        line = new Line(offsetX + 10 + 100 + 300, offsetY + 1 + 50 + (70 * row), offsetX + 10 + 100 + 300, offsetY + 80 + (70 * row));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 105 + 300);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        row = 8;

        // Exit1DR -> UpdateDR
        line = new Line(offsetX - 20, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        

        // Exit1IR -> UpdateIR
        line = new Line(offsetX + 10 + 1 + 270, offsetY + 1 + 50 + (70 * (row - 1) + 50), offsetX + 10 + 300, offsetY + 1 + 50 + (70 * (row - 1) + 50));
        anchorPane.getChildren().add(line);

        updateIRState.name = "Update IR";
        updateIRState.setPosition(offsetX + 10 + 300, offsetY + 570);
        anchorPane.getChildren().add(updateIRState.getRectangle());
        updateIRState.addEventListener(new StateEventListener() {

            @Override
            public void onEnter() {
                int value = irShiftRegister.getValue();
                irDataRegister.setValue(value);
            }

            @Override
            public void onExit() {
                // nothing
            }

        });

        line = new Line(offsetX + 10 + 350, offsetY + 1 + 50 + (70 * (row)), offsetX + 10 + 350, offsetY + 1 + 50 + (70 * (row) + 20));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 1");
        text.setLayoutX(offsetX + 10 + 350);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        line = new Line(offsetX - 40, offsetY + 1 + 50 + (70 * (row) + 20), offsetX + 10 + 350, offsetY + 1 + 50 + (70 * (row) + 20));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX - 40, offsetY + 1 + 50 + (70 * (1) + 50), offsetX - 40, offsetY + 1 + 50 + (70 * (row) + 20));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX + 10 + 460, offsetY + 1 + 50 + (70 * (row)), offsetX + 10 + 460, offsetY + 1 + 50 + (70 * (row) + 40));
        anchorPane.getChildren().add(line);
        text = new Text("TMS = 0");
        text.setLayoutX(offsetX + 10 + 460);
        text.setLayoutY(offsetY + 70 + (70 * row));
        anchorPane.getChildren().add(text);

        line = new Line(offsetX - 60, offsetY + 1 + 50 + (70 * (row) + 40), offsetX + 10 + 460, offsetY + 1 + 50 + (70 * (row) + 40));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX - 60, offsetY + 1 + 50 + (70 * (0) + 50), offsetX - 60, offsetY + 1 + 50 + (70 * (row) + 40));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX - 60, offsetY + 1 + 50 + (70 * (0) + 50), offsetX + 10, offsetY + 1 + 50 + (70 * (0) + 50));
        anchorPane.getChildren().add(line);

        line = new Line(offsetX - 40, offsetY + 1 + 50 + (70 * (1) + 50), offsetX + 10, offsetY + 1 + 50 + (70 * (1) + 50));
        anchorPane.getChildren().add(line);

        //
        // Transitions
        //

        stateList.add(testLogicResetState);
        testLogicResetState.nextStateMap.put(0, runTestIdleState);
        testLogicResetState.nextStateMap.put(1, testLogicResetState);

        stateList.add(runTestIdleState);
        runTestIdleState.nextStateMap.put(0, runTestIdleState);
        runTestIdleState.nextStateMap.put(1, selectDRScanState);

        //
        // Select DR
        //

        stateList.add(selectDRScanState);
        selectDRScanState.nextStateMap.put(0, captureDRState);
        selectDRScanState.nextStateMap.put(1, selectIRScanState);

        stateList.add(captureDRState);
        captureDRState.nextStateMap.put(0, shiftDRState);
        captureDRState.nextStateMap.put(1, exit1DRState);

        stateList.add(shiftDRState);
        shiftDRState.nextStateMap.put(0, shiftDRState);
        shiftDRState.nextStateMap.put(1, exit1DRState);

        stateList.add(exit1DRState);
        exit1DRState.nextStateMap.put(0, pauseDRState);
        exit1DRState.nextStateMap.put(1, updateDRState);

        stateList.add(pauseDRState);
        pauseDRState.nextStateMap.put(0, pauseDRState);
        pauseDRState.nextStateMap.put(1, exit2DRState);

        stateList.add(exit2DRState);
        exit2DRState.nextStateMap.put(0, shiftDRState);
        exit2DRState.nextStateMap.put(1, updateDRState);

        stateList.add(updateDRState);
        updateDRState.nextStateMap.put(0, runTestIdleState);
        updateDRState.nextStateMap.put(1, selectDRScanState);

        //
        // Select IR
        //

        stateList.add(selectIRScanState);
        selectIRScanState.nextStateMap.put(0, captureIRState);
        selectIRScanState.nextStateMap.put(1, testLogicResetState);

        stateList.add(captureIRState);
        captureIRState.nextStateMap.put(0, shiftIRState);
        captureIRState.nextStateMap.put(1, exit1IRState);

        stateList.add(shiftIRState);
        shiftIRState.nextStateMap.put(0, shiftIRState);
        shiftIRState.nextStateMap.put(1, exit1IRState);

        stateList.add(exit1IRState);
        exit1IRState.nextStateMap.put(0, pauseIRState);
        exit1IRState.nextStateMap.put(1, updateIRState);

        stateList.add(pauseIRState);
        pauseIRState.nextStateMap.put(0, pauseIRState);
        pauseIRState.nextStateMap.put(1, exit2IRState);

        stateList.add(exit2IRState);
        exit2IRState.nextStateMap.put(0, shiftIRState);
        exit2IRState.nextStateMap.put(1, updateIRState);

        stateList.add(updateIRState);
        updateIRState.nextStateMap.put(0, runTestIdleState);
        updateIRState.nextStateMap.put(1, selectDRScanState);

    }
}