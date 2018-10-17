package siswadi.com.check;


public class Ingredient {

    int id;
    int accountId;
    String name;
    String expiryDate;
    String amount;
    String location;
    int inStock;

    public Ingredient(){}

    public Ingredient(int id, int accountId, String n, String a, String e, String l, int i)
    {
        this.id = id;
        this.accountId = accountId;
        this.name = n;
        this.expiryDate = e;
        this.amount = a;
        this.location = l;
        this.inStock = i;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }



    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
