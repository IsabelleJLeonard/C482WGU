package model;

/**
 * @Isabelle Matthews
 */


/** outsourced  class hold information when part is out sourced. */
public class OutSourced extends Part {

    private String companyName;

 /** Constructor */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }

    /** set company name as parts company name */
    public void setCompanyName(String name) {
        this.companyName = name;
    }

    /** get company name method */
    public String getCompanyName() {
        return companyName;
    }
}
