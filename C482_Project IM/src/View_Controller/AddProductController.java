package View_Controller;

import View_Controller.MainScreenController;
import View_Controller.AlertMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import javafx.stage.Stage;


import javafx.scene.input.MouseEvent;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.net.URL;
import java.io.IOException;

/**
 * FXML View_Controller class
 * @author isabelle Matthews
 */

public class AddProductController implements Initializable {
    Product newProd = new Product(0, "", 0.0, 0, 0, 0);

    Inventory inv;
    Product product;
    boolean error = false;
    int errorNum;

    @FXML private TextField id;
    @FXML private TextField nameText;
    @FXML private TextField stockText;
    @FXML private TextField priceText;
    @FXML private TextField maxText;
    @FXML private TextField minText;
    @FXML private TextField searchProduct;


    @FXML private TableView associatedPartsTable;

    @FXML private TableView availablePartsTable;

    @FXML private Button addPart;
    @FXML private Button saveProduct;
    @FXML private Button cancelProduct;
    @FXML private Button deletePart;

    ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    ObservableList<Part> productInventorySearch = FXCollections.observableArrayList();
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** send all product parts into the table. */
    public void sendAllProduct (Product product) {

        nameText.setText(product.getName());
        stockText.setText(String.valueOf(product.getStock()));
        priceText.setText(String.valueOf(product.getPrice()));
        minText.setText(String.valueOf(product.getMin()));
        maxText.setText(String.valueOf(product.getMax()));
    }

    /** Initialize the controller class */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateSearchTable();
        associatedPartsTable.setItems(associatedParts);
        generateProductID();
        id.setEditable(false);
    }


// ID
    /** generate its own ID */
    private void generateProductID() {
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
                generateProductID();
            } } }

     /** check if ID isnt taken already.*/
    private boolean VerifyIfTaken(Integer num) {
        Product match = Inventory.lookUpProduct(num);
        return match != null;
    }

    /** get all parts into the available table. */
    private void populateSearchTable() {
        TableColumn<Part, Double> costCol = formatPrice();
        availablePartsTable.getColumns().addAll(costCol);
        availablePartsTable.setItems(Inventory.getAllParts());
    }

//SEARCH

    /** search parts in its textbox. */
    @FXML public void searchProduct (ActionEvent actionEvent) {
        String search = searchProduct.getText();
        if(search.isEmpty()) {
        }
        ObservableList<Part> returnedParts = Inventory.lookUpPart(search);
        if (returnedParts.size() == 0) {
            try {
                int partID = Integer.parseInt(search);
                Part p = Inventory.lookUpPart(partID);
                if (p != null) {
                    returnedParts.add(p);
                }
            } catch (NumberFormatException e) {
            }
        }
        availablePartsTable.setItems(returnedParts);
        searchProduct.setText("");
        if(returnedParts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Part cannot be found");
            alert.setContentText("Part cannot be found, try again.");
            alert.showAndWait();
            availablePartsTable.setItems(Inventory.getAllParts());
        }
    }

    /** clear the textfield when clicked. */
    @FXML void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (field == searchProduct) {
            availablePartsTable.setItems(partsInventory);
        }
    }

//ADD

    /** add parts to next tableview when clicked. */
    @FXML void addButton (ActionEvent event) {
        Part part = (Part) availablePartsTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            errorWindow(1);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("confirmation to add");
        alert.setContentText("Are you sure you want to add the part?");
        alert.showAndWait();

        associatedParts.add(part);
        System.out.println("part added.");
    }

//CANCEL

    /** alert before cancel button.*/
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

    /** cancel button when clicked.*/
    public void cancelButton (ActionEvent actionEvent) throws IOException {
        boolean cancel = cancel();
        if (cancel) {
            mainScreen(actionEvent);
        } else {
            return;
        }
    }

//DELETE

    /** delete part from associated part table when clicked. */
    @FXML private void deleteButton (ActionEvent event) {
        Part removePart = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();
        if (removePart == null) {
            errorWindow(2);
            return;
        }
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = confirmationWindow(removePart.getName());
            if (remove) {
                associatedParts.remove(removePart);
                associatedPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            AlertMessage.infoWindow(1, removePart.getName());
        } else {
            AlertMessage.infoWindow(2, "");
        }

    }

//SAVE

    @FXML public void saveProduct () {
        try {
            if (nameText.getText().trim().isEmpty() || nameText.getText().trim().toLowerCase().equals("part name")) {
                AlertMessage.error(4, nameText);
                return;
            }
            if (Integer.parseInt(minText.getText().trim()) > Integer.parseInt(maxText.getText().trim())) {
                AlertMessage.error(8, minText);
                return;
            }
            if (Integer.parseInt(stockText.getText().trim()) < Integer.parseInt(minText.getText().trim())) {
                AlertMessage.error(6, stockText);
                return;
            }
            if (Integer.parseInt(stockText.getText().trim()) > Integer.parseInt(maxText.getText().trim())) {
                AlertMessage.error(7, stockText);
                return;
            }
        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Check your text fields.");
            alert.setContentText("all text field must be answered.");
            alert.showAndWait();
            return;
        }
        newProd.setId(Integer.parseInt(id.getText()));
        newProd.setName(nameText.getText());
        newProd.setPrice(Double.parseDouble(priceText.getText()));
        newProd.setStock(Integer.parseInt(stockText.getText()));
        newProd.setMin(Integer.parseInt(minText.getText()));
        newProd.setMax(Integer.parseInt(maxText.getText()));
        newProd.getAssociatedParts().addAll(associatedParts);
        Inventory.addProduct(newProd);
    }

    /** save new information into the tableview when clicked save.*/
    @FXML private void saveButton (ActionEvent event) throws IOException {
        saveProduct();

        mainScreen(event);
    }

//ERROR

    /** check error.*/
    private boolean check(TextField field) {
        if (field == priceText & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertMessage.error(3, field);
            return true;
        }
        if (field != priceText & !field.getText().trim().matches("[0-9]*")) {
            AlertMessage.error(3, field);
            return true;
        }
        return false;
    }

    /** confirm before delete. */
    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Click ok");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        } }

    /** error alert when something came up .*/
    private void errorWindow(int code) {
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("INVALID");
            alert.setContentText("nothing was selected, please choose a part to add.");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("INVALID");
            alert.setContentText("nothing was selected, please choose a part to delete.");
            alert.showAndWait();
        }
        if (code == 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("INVALID");
            alert.setContentText("no parts was selected.");
            alert.showAndWait();
        }

    }

    /** load into main screen. */
    private void mainScreen(ActionEvent event) throws IOException {
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

    /** price column. */
    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price/Cost per unit");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

        costCol.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (!empty) {
                        setText("$" + String.format("%10.2f", item));
                    }
                    else{
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }
}
