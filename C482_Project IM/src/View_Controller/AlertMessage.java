package View_Controller;

import javafx.scene.control.TextField;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;

/**
 * Alert Message class
 * @author isabelle Matthews
 */

public class AlertMessage {
    public static void error(int code, TextField field) {
        fieldError(field);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error while adding part");
        alert.setHeaderText("Unable to add Part");
        switch (code) {
            case 1: {
                alert.setContentText("empty field!");
                break;
            }
            case 2: {
                alert.setContentText("Please set In house or OutSourced category!");
                break;
            }
            case 3: {
                alert.setContentText("Wrong format!");
                break;
            }
            case 4: {
                alert.setContentText("Invalid Name format!");
                break;
            }
            case 5: {
                alert.setContentText("Number can not be zero or negative, please enter a positive number.");
                break;
            }
            case 6: {
                alert.setContentText("Inventory can not be lower than min.");
                break;
            }
            case 7: {
                alert.setContentText("Inventory can not be higher than Max.");
                break;
            }
            case 8: {
                alert.setContentText("Max must be greater than min!");
                break;
            }
            case 9: {
                alert.setContentText("Machine Id value must be numeric!");
                break;
            }
            case 11: {
                alert.setContentText("Product still has parts assigned to it.");
                break;
            }
            default: {
                alert.setContentText("UnknownError.");
                break;
            }
        }
        alert.showAndWait();
    }

    /**
     * color of border when used.
     */
    private static void fieldError(TextField field) {
        if (field != null) {
            field.setStyle("-fx-border-color: black");
        }
    }

    /**
     * confirm to delete alert.
     */
    public static boolean confirmationWindow(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Are you sure you want delete: " + name);
        alert.setContentText("Click ok to confirm.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     * confirm to cancel screen
     */
    public static boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setHeaderText("Click ok to confirm.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     * info that delete have been performed.
     */
    public static void infoWindow(int code, String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (code != 2) {
            alert.setTitle("Confirmed");
            alert.setHeaderText(null);
            alert.setContentText(name + "has been deleted.");
            alert.showAndWait();
        } else {
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred.");
        }
    }

    public static void confirmationSave() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("save");
        alert.setHeaderText("Are you sure you want save: ");
        alert.setContentText("Click ok to confirm.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);

        }
    }
}
