package View_Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import model.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;

/**
 * FXML View_Controller class
 * @author Isabelle Matthews
 */

public class AddPartController implements Initializable {

    Inventory inv;

    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outSourcedRadio;
    @FXML private Label partSwappingLabel;
    @FXML private ToggleGroup switchingIDbtn;

    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField stock;
    @FXML private TextField price;
    @FXML private TextField max;
    @FXML private TextField switchingID;
    @FXML private TextField min;

    @FXML private Button savePart;
    @FXML private Button cancelPart;

   public AddPartController(Inventory inv) {
        this.inv = inv;
    }

    ObservableList<Part> allParts = FXCollections.observableArrayList();

   /** Initialize the controller class */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePartID();
        resetFields();
        id.setEditable(false);
    }

    /** create own part ID */
    private void generatePartID() {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);
        if (Inventory.partSize() == 0) {
            id.setText(num.toString());
        }
        if (Inventory.partSize() == 1000) {
            AlertMessage.error(3, null);
        } else {
            match = VerifyIfTaken(num);
            if (match == false) {
                id.setText(num.toString());
            } else {
                generatePartID();
            } } }

  /** make sure ID isnt the same */
    private boolean VerifyIfTaken(Integer num) {
        Part match = inv.lookUpPart(num);
        return match != null;
    }

/** texts on the textfields */
    private void resetFields() {
        name.setPromptText("Part Name");
        stock.setPromptText("Inv Count");
        price.setPromptText("Part Price");
        min.setPromptText("Min");
        max.setPromptText("Max");
        switchingID.setPromptText("Machine ID");
        partSwappingLabel.setText("Machine ID");
        inHouseRadio.setSelected(true);
    }

/** clear the text field */
    @FXML private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }

 //SAVE

    /** save part information and pass to table view */
    @FXML private void savePart (ActionEvent actionEvent) throws IOException {
        try {
            int idVar = Integer.parseInt(id.getText());
            String nameVar = name.getText();
            double priceVar = Double.parseDouble(price.getText());
            int stockVar = Integer.parseInt(stock.getText());
            int minVar = Integer.parseInt(min.getText());
            int maxVar = Integer.parseInt(max.getText());



            if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
                AlertMessage.error(4, name);
                return;
            }

            if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertMessage.error(8, min);
                return;
            }
            if (Integer.parseInt(stock.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                AlertMessage.error(6, stock);
                return;
            }
            if (Integer.parseInt(stock.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertMessage.error(7, stock);
                return;

        } else if (outSourcedRadio.getText().trim().isEmpty() || switchingID.getText().trim().toLowerCase().equals("company name")) {
            AlertMessage.error(3, switchingID);
            return;

        } else if (inHouseRadio.isSelected() && !switchingID.getText().trim().matches("[0-9]*")) {
                AlertMessage.error(9, switchingID);
                return;
            }
            if(inHouseRadio.isSelected())
            {
                int machineID = Integer.parseInt(switchingID.getText());
                InHouse iPart = new InHouse(idVar, nameVar, priceVar, stockVar, minVar, maxVar, machineID);
                Inventory.updatePart(Integer.parseInt(id.getText()), iPart);
            }
            else {
                String companyName = switchingID.getText();
                OutSourced oPart = new OutSourced(idVar, nameVar, priceVar, stockVar, minVar, maxVar, companyName);
                Inventory.updatePart(Integer.parseInt(id.getText()), oPart);
            }
            System.out.println("Part have been added.");

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Check your text fields.");
            alert.setContentText("all text field must be answered.");
            alert.showAndWait();
            return;
        }

        mainScreen(actionEvent);
    }

 //CANCEL

    /** cancel button to cancel */
    public void cancelPart (ActionEvent actionEvent) throws IOException {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen (actionEvent);
        }
    }

//when radio button is selected

    /** when in house radio button is selected, machine id come up */
    @FXML private void selectedInHouse(ActionEvent actionEvent) {
        outSourcedRadio.setSelected(false);
        partSwappingLabel.setText("Machine ID");
        switchingID.setPromptText("Machine ID");
    }

    /** when outsourced radio button is selected, name come up */
    @FXML private void selectedOutSourced(ActionEvent actionEvent) {
        inHouseRadio.setSelected(false);
        partSwappingLabel.setText("Company Name");
        switchingID.setPromptText("Company Name");
    }

//add new information

    /** add new in house part into table */
    private void addInHouse() {
        inv.addPart(
                new InHouse(
                        Integer.parseInt(id.getText().trim()),
                        name.getText().trim(),
                        Double.parseDouble(price.getText()),
                        Integer.parseInt(stock.getText()),
                        Integer.parseInt(min.getText()),
                        Integer.parseInt(max.getText()),
                        Integer.parseInt(switchingID.getText())));
    }

    /** add new sourced part into table */
    private void addOutSourced() {
        inv.addPart(
                new OutSourced(
                        Integer.parseInt(id.getText().trim()),
                        name.getText(),
                        Double.valueOf(price.getText()),
                        Integer.valueOf(stock.getText()),
                        Integer.valueOf(min.getText()),
                        Integer.valueOf(max.getText()),
                        switchingID.getText()));
    }

    /** load main screen */
    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/main_screen.fxml"));
            MainScreenController controller = new MainScreenController(inv);
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setResizable(false);
            window.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        } }
}
