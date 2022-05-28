package View_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author isabelle Matthews
 */

public class ModifyPartController implements Initializable {

    Inventory inv;
    Part part;
    private Part changedPart;
    int index;
    private Stage stage;

    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourcedRadio;

    @FXML
    private Label partSwappingLabel;

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField stock;
    @FXML
    private TextField min;
    @FXML
    private TextField max;
    @FXML
    private TextField switchingID;

    @FXML
    private Button savePart;
    @FXML
    private Button cancelPart;

    public ToggleGroup switchingIDbtn;


    /**
     * initialize class
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        switchingIDbtn = new ToggleGroup();
        this.inHouseRadio.setToggleGroup(switchingIDbtn);
        this.outSourcedRadio.setToggleGroup(switchingIDbtn);
        this.outSourcedRadio.setSelected(true);
        id.setEditable(false);
    }

    /**
     * send the part information
     */
    public void sendPart(Part part) {
        System.out.println("Modify part screen opened.");
        this.part = part;
        id.setText(Integer.toString(part.getId()));
        name.setText(part.getName());
        stock.setText(Integer.toString(part.getStock()));
        price.setText(Double.toString(part.getPrice()));
        max.setText(Integer.toString(part.getMax()));
        min.setText(Integer.toString(part.getMin()));
        if (part instanceof InHouse) {
            InHouse inHouse = (InHouse) part;
            switchingID.setText(Integer.toString(inHouse.getMachineID()));
            partSwappingLabel.setText("Machine ID");
            inHouseRadio.selectedProperty().set(true);
        } else {
            OutSourced outSource = (OutSourced) part;
            switchingID.setText(outSource.getCompanyName());
            partSwappingLabel.setText("Company Name");
            outSourcedRadio.selectedProperty().set(true);
        }
    }

    /**
     * clear the textfield
     */
    @FXML
    private void clearTextField(MouseEvent event
    ) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }

    /**
     * cancel button
     */
    @FXML
    private void cancelPart(ActionEvent event) throws IOException {
        boolean cancel = cancel();
        if (cancel) {
            mainScreen(event);
        } else {
            return;
        }
    }

    /**
     * save part and update new information
     */
    @FXML
    private void savePart(ActionEvent event) throws IOException {
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
            if (inHouseRadio.isSelected()) {
                int machineID = Integer.parseInt(switchingID.getText());
                InHouse iPart = new InHouse(idVar, nameVar, priceVar, stockVar, minVar, maxVar, machineID);
                Inventory.updatePart(Integer.parseInt(id.getText()), iPart);
            } else {
                String companyName = switchingID.getText();
                OutSourced oPart = new OutSourced(idVar, nameVar, priceVar, stockVar, minVar, maxVar, companyName);
                Inventory.updatePart(Integer.parseInt(id.getText()), oPart);
            }
            System.out.println("Part have been updated.");

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Check your text fields.");
            alert.setContentText("all text field must be answered.");
            alert.showAndWait();
            return;
        }

        mainScreen(event);
    }


    // radio button

    /**
     * when in house is select, name change
     */
    @FXML
    private void selectInHouse(ActionEvent event) {
        outSourcedRadio.setSelected(false);
        partSwappingLabel.setText("Machine ID");
    }

    /**
     * when outsourced is selected, name change
     */
    @FXML
    private void selectOutSourced(ActionEvent event) {
        inHouseRadio.setSelected(false);
        partSwappingLabel.setText("Company Name");
    }


    /**
     * border color
     */
    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        stock.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");
        switchingID.setStyle("-fx-border-color: lightgray");

    }

    /**
     * load main screen
     */
    public void mainScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/main_screen.fxml"));
        MainScreenController controller = new MainScreenController(inv);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // ALERT MESSAGE

    /**
     * cancel alert
     */
    private boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}