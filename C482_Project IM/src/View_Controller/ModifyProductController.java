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
import java.util.ResourceBundle;
import java.net.URL;
import java.io.IOException;

/**
 * FXML controller class
 *
 * @author isabelle matthews
 */

public class ModifyProductController implements Initializable {

    Inventory inv;
    private Product newProduct;
    boolean error = false;
    int errorNum;

    @FXML
    private TextField idText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField stockText;
    @FXML
    private TextField priceText;
    @FXML
    private TextField maxText;
    @FXML
    private TextField minText;
    @FXML
    private TextField searchProduct;


    @FXML
    private TableView associatedPartsTable;

    @FXML
    private TableView availablePartsTable;

    @FXML
    private Button addPart;
    @FXML
    private Button saveProduct;
    @FXML
    private Button cancelProduct;
    @FXML
    private Button deletePart;

    ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    ObservableList<Part> productInventorySearch = FXCollections.observableArrayList();
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public void sendProduct(Product product) {
        System.out.println("Modify product screen opened.");
        newProduct = product;
        idText.setText(Integer.toString(newProduct.getId()));
        nameText.setText(newProduct.getName());
        stockText.setText(String.valueOf(newProduct.getStock()));
        priceText.setText(String.valueOf(newProduct.getPrice()));
        minText.setText(String.valueOf(newProduct.getMin()));
        maxText.setText(String.valueOf(newProduct.getMax()));
        associatedPartsTable.setItems(associatedParts);
        associatedParts.addAll(newProduct.getAssociatedParts());
    }

/** initialize the class */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateSearchTable();
        associatedPartsTable.setItems(associatedParts);
        idText.setEditable(false);
    }

    /** pull information from top table view */
    private void populateSearchTable() {
        TableColumn<Part, Double> costCol = formatPrice();
        availablePartsTable.getColumns().addAll(costCol);
        availablePartsTable.setItems(Inventory.getAllParts());
    }

 //Search

    /** search product in textbox*/
    @FXML
    public void searchProduct(ActionEvent actionEvent) {
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

    /** clear the text field */
    @FXML
    void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (field == searchProduct) {
            availablePartsTable.setItems(partsInventory);
        }
    }

//ADD

/** add button to bottom table veiw */
    @FXML
    void addButton(ActionEvent event) {
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
    }

//CANCEL

    /** cancel button */
    public void cancelButton (ActionEvent actionEvent) throws IOException {
        boolean cancel = cancel();
        if (cancel) {
            mainScreen(actionEvent);
        } else {
            return;
        }}

/** alert to check if want to cancel */
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

//Delete

    @FXML
    private void deleteButton(ActionEvent event) {
        Part selectedPart = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            errorWindow(2);
            return;
        }
        boolean deleted = false;
        if (selectedPart != null) {
            boolean remove = confirmationWindow(selectedPart.getName());
            if (remove) {
                associatedParts.remove(selectedPart);
                associatedPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            AlertMessage.infoWindow(1, selectedPart.getName());
        } else {
            AlertMessage.infoWindow(2, "");
        }

    }


//SAVE

    /** save button to save/update new product information

     *<p><b>

     part G a question:

     At first, part's alert if any error with min, max, and inventory, and it have load new product
     and update new product information in its own FXML public void called saveProduct. the save button had saveProduct(); in to load its own
     information.

     the problem raised, the alert would show for any error then it would let product to be saved without giving a chance to correct the error.

     I moved whole code from saveProduct to saveButton and add try and catch exception. I added alert right after catch exception e if theres any text field is not filled. it will
     be returned to modify product screen after Ok is clicked.
     I also added the line of get associated parts, that way bottom table will save the parts that are selected to go with product.

     *</b></p>*/
    @FXML
    private void saveButton(ActionEvent event) throws IOException {
        try {
            Product product = new Product(Integer.parseInt(idText.getText().trim()), nameText.getText().trim(), Double.parseDouble(priceText.getText().trim()),

                    Integer.parseInt(stockText.getText().trim()), Integer.parseInt(minText.getText().trim()), Integer.parseInt(maxText.getText().trim()));
            if (nameText.getText().trim().isEmpty() || nameText.getText().trim().toLowerCase().equals("part name")) {
                AlertMessage.error(4, nameText);
                return;
            }
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
            product.getAssociatedParts().addAll(associatedParts);

            Inventory.updateProduct(product);


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


    /** confirmation before delete */
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

        /** load main screen */
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

    /** price column */
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

    /** alert message when error is discovered*/
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
            }}
    }