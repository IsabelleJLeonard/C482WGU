package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * FXML class
* author @Isabelle Matthews
 */


public class Main extends Application {

    /**
     *
     * @param args
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param stage
     * @throws Exception
     */

    // stage that controls the main screen to load.
    @Override
    public void start(Stage stage) throws Exception{
        Inventory inv = new Inventory();
        addData(inv);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/main_screen.fxml"));
        View_Controller.MainScreenController controller = new View_Controller.MainScreenController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    // Sample data to be saved in the file, but can be changed while in use only.
    void addData(Inventory inv) {
        //InHouse data
        InHouse headlight = new InHouse(1, "headlight", 5.00, 10, 10, 20, 123);
        inv.addPart(headlight);
        inv.addPart(new InHouse(7, "tail light", 20.00, 5, 1, 5, 4));
        inv.addPart(new InHouse(8, "brake", 3.00, 3, 1, 6, 2 ));
        //OutSourced Data
        OutSourced bulb = new OutSourced(3, "bulb", 4.00, 2, 1, 10, "Nathan");
        OutSourced alternator = new OutSourced(4, "alternator", 100.00, 8, 3, 10, "Isabelle");
        inv.addPart(bulb);
        inv.addPart(alternator);
        inv.addPart(new OutSourced(10, "tire", 30.00, 30, 20, 50, "Axel"));
        //products data
        Product ford = new Product(5, "ford", 100.00, 4, 3, 40);
        Product chevrolet = new Product(6, "chevrolet", 300.00, 6, 6, 30);
        ford.addAssociatedPartTable(headlight);
        inv.addProduct(ford);
        chevrolet.addAssociatedPartTable(bulb);
        chevrolet.addAssociatedPartTable(alternator);
        inv.addProduct(chevrolet);
    }
}