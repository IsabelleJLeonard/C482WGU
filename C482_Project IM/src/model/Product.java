package model;

/**
 * class product
 * @author isabelle matthews
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/** main Product class */
public class Product {

    /** initializes the associated parts to product */
    private int id;
    private String name;
    private double price = 0.0;
    private int stock = 0;
    private int min;
    private int max;
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    public ObservableList<Part> getAssociatedParts(){
        return associatedParts;
    }

/** constructor */
    public Product(int id, String name, double price, int stock, int min, int max) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }

  /** getter and setter for all id, name, stock, price, min, and max */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) { this.name = name; }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return this.min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }


/** when new part is add to bottom table view in product screen. */
    public void addAssociatedPartTable(Part partAdd) {
        associatedParts.add(partAdd);
    }


    /**  when part is removed from bottom table view in product screen. */
    public void deleteAssociatedPart(Part associatedPart){
        associatedParts.remove(associatedPart);
    }



/** look up part with ID  */
    public Part lookUpPart(int part){
        for (int i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == part) {
                return associatedParts.get(i);
            } }
                return null;
        }


        /** size */
    public int getPartSize() {
        return associatedParts.size();
    }
}
