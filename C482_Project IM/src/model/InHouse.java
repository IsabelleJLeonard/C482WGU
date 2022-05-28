package model;

/**
*
* @author Isabelle Matthews
 */


/** In house class hold part's information if part is in-house */
public class InHouse extends Part {

        private int machineID;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
       super(id, name, price, stock, min, max);
        setMachineID(machineID);
    }

    public int getMachineID() {
        return this.machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

}
