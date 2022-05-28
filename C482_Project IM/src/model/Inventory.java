package model;

/**
 * @author Isabelle Matthews
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/** Inventory class that holds all products and parts information */
public class Inventory {

    public static ObservableList<Part> allParts = FXCollections.observableArrayList();
    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();

//ADD

    /** add product when its updated to new product into the system */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /** add new part to part list */
    public static void addPart(Part part) {
        if (part != null) {
            allParts.add(part);
        }
    }


//REMOVE

    /**
     * delete product from its list on main screen
     */
    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }


    /**
     * delete part off the list
     */
    public static void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }


//LOOK UP

    /** look up for parts using name */
    public static ObservableList<Part> lookUpPart(String part) {
        if (!allParts.isEmpty()) {
            ObservableList searchPart = FXCollections.observableArrayList();
            for (Part p : allParts) {
                if (p.getName().contains(part)) {
                    searchPart.add(p);
                }
            }
            return searchPart;
        }
        return null;
    }

    /**
     * look up for any part id in search
     */
    public static Part lookUpPart(int part) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == part) {
                    return allParts.get(i);
                }
            }
        }
        return null;
    }

    /**
     * look up products' name from search box
     */
    public static ObservableList<Product> lookUpProduct(String product) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        for (Product pr : allProducts) {
            if (pr.getName().toLowerCase().contains(product.toLowerCase())) {
                products.add(pr);
            }
        }
        return products;
    }

    /**
     * look up all product ID from search box
     */
    public static Product lookUpProduct(int product) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == product) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }



//UPDATE for modify screens

    /**
     * update part when modify part is used to update any new information
     */
    public static void updatePart(int index, Part selectedPart) {
        Part originalPart = Inventory.lookUpPart(index);
        Inventory.deletePart(originalPart);
        Inventory.addPart(selectedPart);
    }


    /**
     * update product when modify product method is used
     */
    public static void updateProduct(Product product) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == product.getId()) {
                allProducts.set(i, product);
                break;
            }
        }
    }

//SEARCH

    /**
     * search products to find the product information
     */
    public static ObservableList searchProducts(String searchProductName) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();

        if (searchProductName.length() == 0) {
            foundProducts = allProducts;//return all if blank as passed in as search term
        } else {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getName().toLowerCase().contains(searchProductName.toLowerCase())) {
                    foundProducts.add(allProducts.get(i));
                }
            }
        }

        return foundProducts;
    }


//get all parts or product

    /**
     * pull all product information to show
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * pull all part information to show
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }


 //SIZE

    /**
     * size
     */
    public static int partSize() {
        return allParts.size();
    }

    /**
     * size
     */
    public int productSize() {
        return allProducts.size();
    }

}