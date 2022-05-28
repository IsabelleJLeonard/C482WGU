package View_Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class for the main screen
 * @author Isabelle Matthews
 */

public class MainScreenController implements Initializable {

    Inventory inv;
    Product product;
    Part part;

    // product table
    @FXML
    private TableView productsTable;

    //button for product
    @FXML
    private TextField searchProduct;
    @FXML
    private Button deleteProduct;
    @FXML
    private Button addProduct;
    @FXML
    private Button modifyProduct;

    // part table
    @FXML
    private TableView partsTable;

    @FXML
    private TextField searchPart;
    @FXML
    private Button modifyPart;
    @FXML
    private Button deletePart;
    @FXML
    private Button addPart;

    @FXML
    private Button exitProgram;
    Stage stage;

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Product> productInventorySearch = FXCollections.observableArrayList();

    /** the Constructor */
    public MainScreenController(Inventory inv) {
        this.inv = inv;
    }

    /** initialize the controller class

     *<p><b>

     -part G b:
     For this, I would add third table view and show product and parts, that will show
     which are connected. it will provide more accessible to the view
     instead check every modify button to see which product or parts
     that are connected. Chevrolet own bulb and alternator. that will show on
     third table view.

     *</b></p> */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePartsTable();
        generateProductsTable();
    }

//GENERATE IDS

    /** get all parts and columns into the part table */
    private void generatePartsTable() {
        // allParts.setAll(inv.getAllParts());
        TableColumn<Part, Double> costCol = formatPrice();
        partsTable.getColumns().addAll(costCol);
        partsTable.setItems(Inventory.getAllParts());
          partsTable.refresh();
    }

    /** get all products and columns into the product table */
    private void generateProductsTable() {
        // Inventory.getAllProducts().setAll(inv.getAllProducts());
        TableColumn<Product, Double> costCol = formatPrice();
        productsTable.getColumns().addAll(costCol);
        productsTable.setItems(Inventory.getAllProducts());
          productsTable.refresh();
    }

//EXIT PROGRAM
    /** exit the program */
    @FXML
    public void exitProgram(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
        }
    }

    @FXML
    void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (searchPart == field) {
        }
        if (searchProduct == field) {
            if (Inventory.getAllProducts().size() != 0) {
                productsTable.setItems(Inventory.getAllProducts());
            }
        }
    }


 //SEARCH

    /** search product in the box. HIGHLIGHT only what typed ONLY FULL NAME or number of ID */
    @FXML
    public void searchProduct(ActionEvent actionEvent) {
        String search = searchProduct.getText();
        if(search.isEmpty()) {
        }
        ObservableList<Product> products = Inventory.lookUpProduct(search);
        if (products.size() == 0) {
            try {
                int productID = Integer.parseInt(search);
                Product p = Inventory.lookUpProduct(productID);
                if (p != null) {
                    products.add(p);
                }
            } catch (NumberFormatException e) {
            }
        }
        productsTable.setItems(products);
        searchProduct.setText("");
        if(products.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product cannot be found");
            alert.setContentText("Product cannot be found, try again");
            alert.showAndWait();
            productsTable.setItems(Inventory.getAllProducts());
        }
    }


/** search part box, HIGHLIGHT what is typed, FULL name or number of ID only. */
    @FXML
    public void searchPart(ActionEvent actionEvent) {
        String search = searchPart.getText();
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
        partsTable.setItems(returnedParts);
        searchPart.setText("");
        if(returnedParts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Part cannot be found");
            alert.setContentText("Part cannot be found, try again.");
            alert.showAndWait();
            partsTable.setItems(Inventory.getAllParts());
        }
    }


//DELETE

    /** delete product button plus the error needed. */
    @FXML
    public void deleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            errorWindow(4);
            return;
        }
        boolean deleted = false;
        if(selectedProduct != null) {
            if (selectedProduct.getAssociatedParts().size() > 0){
                infoWindow(11, "Cannot Remove Product, product still have parts assigned. ");
                        return;
            }
            boolean remove = confirmationWindow(selectedProduct.getName());
            if (remove) {
            Inventory.getAllProducts().remove(selectedProduct);
        }
        else
        if (deleted) {
            AlertMessage.infoWindow(1, selectedProduct.getName());
        } else {
            AlertMessage.infoWindow(2, "");
        }

    }}

    /** delete any of parts with alert */
    @FXML private void deletePart (ActionEvent actionEvent){
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            errorWindow(3);
            return;
        }
        boolean deleted = false;
        if(selectedPart != null) {
            boolean remove = confirmationWindow(selectedPart.getName());
            if (remove) {
                Inventory.getAllParts().remove(selectedPart);
            }
            else
            if (deleted) {
                AlertMessage.infoWindow(1, selectedPart.getName());
            } else {
                AlertMessage.infoWindow(2, "");
            }
            } }


//ADD

    /** add product screen */
    @FXML
    public void addMainButton(ActionEvent actionEvent) throws IOException {
        Parent addProductParent = FXMLLoader.load(getClass().getResource("/View_Controller/AddProduct.fxml"));
        Scene addProductScene = new Scene(addProductParent);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(addProductScene);
        window.show();
    }

    /** add part */
    @FXML private void addPart (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddPart.fxml"));
        View_Controller.AddPartController controller = new AddPartController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


//MODIFY

        /** modify product screen */
    @FXML public void modifyProduct (ActionEvent actionEvent) throws IOException {
            Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
                if (selectedProduct == null) {
                    errorWindow(2);
                    return;
                }
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View_Controller/ModifyProduct.fxml"));
                Parent modifyPartParent = loader.load();
                Scene modifyPartScene = new Scene(modifyPartParent);

                //access the controller and call a method
                ModifyProductController controller = loader.getController();
                controller.sendProduct((Product) productsTable.getSelectionModel().getSelectedItem());

                Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                window.setScene(modifyPartScene);
                window.show();
            }

            /** Modify part screen. */
          @FXML private void modifyPart(ActionEvent actionEvent) throws IOException {
              Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
              if (selectedPart == null) {
                  errorWindow(1);
                  return;
              }
                  FXMLLoader loader = new FXMLLoader();
                  loader.setLocation(getClass().getResource("ModifyPart.fxml"));
                  Parent modifyPartParent = loader.load();
                  Scene modifyPartScene = new Scene(modifyPartParent);

                  //access the controller and call a method
                  ModifyPartController controller = loader.getController();
                  controller.sendPart((Part) partsTable.getSelectionModel().getSelectedItem());

                  Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                  window.setScene(modifyPartScene);
                  window.show();
               }


//ALERT MESSAGES

        /** confirmation alert if any delete action */
    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /** any parts that never have been selected.*/
    private void errorWindow(int code) {
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("INVALID");
            alert.setContentText("nothing was selected, please choose a part to modify.");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("INVALID");
            alert.setContentText("nothing was selected, please choose a product to modify.");
            alert.showAndWait();
        }
        if (code == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("INVALID");
            alert.setContentText("nothing was selected, please choose a part to delete.");
            alert.showAndWait();
        }
        if (code == 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("INVALID");
            alert.setContentText("nothing was selected, please choose a product to delete.");
            alert.showAndWait();
        }
    }

/** alert any of error.*/
    private void infoWindow(int code, String name) {
        Alert alert = new Alert(AlertType.INFORMATION);
        if (code != 2) {
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText(name + "");

            alert.showAndWait();
        } else {
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error!");
        }
    }   

    /** price column with $ assigned. */
    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price / Cost per Unit");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Format as currency
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