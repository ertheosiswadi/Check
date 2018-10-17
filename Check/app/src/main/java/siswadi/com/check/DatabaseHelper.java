package siswadi.com.check;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "master.db";
    private static final String TABLE_ACCOUNT_CREATE = "CREATE TABLE account(" +
            "accId integer PRIMARY KEY, " +
            "username text not null, " +
            "password text not null, " +
            "email text not null);";

    private static final String TABLE_FOOD_CREATE = "CREATE TABLE food(" +
            "foodId integer PRIMARY KEY, " +
            "accId integer not null, " +
            "foodName text not null, " +
            "recipeLink text not null);";

    private static final String TABLE_RELAY_CREATE = "CREATE TABLE relay(" +
            "ingredientId integer DEFAULT '0' not null, " +
            "foodId integer DEFAULT '0' not null, " +
            "PRIMARY KEY (foodId, ingredientId));";

    private static final String TABLE_INGREDIENT_CREATE = "CREATE TABLE ingredient(" +
            "ingredientId integer PRIMARY KEY, " +
            "accId integer not null, " +
            "name text not null, " +
            "amount text not null, " +
            "expiryDate text not null, " +
            "location text not null, " +
            "inStock integer not null);";
           /* "photo link text not null, " +
            "email text not null);";*/

    SQLiteDatabase db;

    String TABLE_ACCOUNT_NAME = "account";
    String TABLE_FOOD_NAME = "food";
    String TABLE_INGREDIENT_NAME ="ingredient";
    String TABLE_RELAY_NAME = "relay";


    String COL_ACCOUNT_USERNAME = "username";
    String COL_ACCOUNT_PASSWORD = "password";
    String COL_ACCOUNT_EMAIL = "email";
    String COL_ACCOUNT_ID = "accId";


    String COL_INGREDIENT_NAME = "name";
    String COL_INGREDIENT_AMOUNT = "amount";
    String COL_INGREDIENT_EXPIRYDATE = "expiryDate";
    String COL_INGREDIENT_LOCATION = "location";
    String COL_INGREDIENT_ID = "ingredientId";
    String COL_INGREDIENT_ACCOUNT_ID = "accId";
    String COL_INGREDIENT_STOCKSTATUS = "inStock";

    String COL_FOOD_ID = "foodId";
    String COL_FOOD_ACCOUNT_ID = "accId";
    String COL_FOOD_NAME = "foodName";
    String COL_FOOD_RECIPE_LINK = "recipeLink";

    String COL_RELAY_NAME  = "relay";
    String COL_RELAY_FOOD_ID = "foodId";
    String COL_RELAY_INGREDIENT_ID = "ingredientId";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ACCOUNT_CREATE);
        db.execSQL(TABLE_FOOD_CREATE);
        db.execSQL(TABLE_INGREDIENT_CREATE);
        db.execSQL(TABLE_RELAY_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String q1 = "DROP TABLE IF EXISTS " + TABLE_ACCOUNT_CREATE;
        String q2 = "DROP TABLE IF EXISTS " + TABLE_FOOD_CREATE;
        String q3 = "DROP TABLE IF EXISTS " + TABLE_INGREDIENT_CREATE;
        String q4 = "DROP TABLE IF EXISTS " + TABLE_RELAY_CREATE;
        db.execSQL(q1);
        db.execSQL(q2);
        db.execSQL(q3);
        db.execSQL(q4);
        this.onCreate(db);
    }

    public void addAccount(Account a)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.putNull(COL_ACCOUNT_ID);
        values.put(COL_ACCOUNT_EMAIL, a.getEmail());
        values.put(COL_ACCOUNT_USERNAME, a.getUsername());
        values.put(COL_ACCOUNT_PASSWORD, a.getPassword());

        db.insert(TABLE_ACCOUNT_NAME, null, values);
        db.close();
    }

    public void addIngredient(Ingredient i)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.putNull(COL_INGREDIENT_ID);
        values.put(COL_INGREDIENT_NAME, i.getName());
        values.put(COL_INGREDIENT_AMOUNT, i.getAmount());
        values.put(COL_INGREDIENT_EXPIRYDATE, i.getExpiryDate());
        values.put(COL_INGREDIENT_LOCATION, i.getLocation());
        values.put(COL_INGREDIENT_ACCOUNT_ID, i.getAccountId());
        values.put(COL_INGREDIENT_STOCKSTATUS, i.getInStock());

        db.insert(TABLE_INGREDIENT_NAME, null, values);
        db.close();
    }

    public void addIngredientOfFood(Ingredient i, Food f)
    {
        addIngredient(i);
        addRelay(this.getLatestIngredientEntry().getId(), f.getId());
    }

    public Ingredient getLatestIngredientEntry()
    {
        db = getReadableDatabase();
        String q = "select * FROM " + TABLE_INGREDIENT_NAME + " ORDER BY " + COL_INGREDIENT_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(q, null);
        Ingredient a = new Ingredient();
        if(cursor.moveToFirst()) {
            a.setId(cursor.getInt(0));
            a.setAccountId(cursor.getInt(1));
            a.setName(cursor.getString(2));
            a.setAmount(cursor.getString(3));
            a.setExpiryDate(cursor.getString(4));
            a.setLocation(cursor.getString(5));
            a.setInStock(cursor.getInt(6));
        }
        return a;
    }

    public void linkIngredientToFood(Ingredient i, Food f)
    {
        addRelay(i.getId(), f.getId());
    }

    public void addFood(Food f)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.putNull(COL_FOOD_ID);
        values.put(COL_FOOD_ACCOUNT_ID, f.getAccountId());
        values.put(COL_FOOD_NAME, f.getName());
        values.put(COL_FOOD_RECIPE_LINK, f.getRecipeLink());

        db.insert(TABLE_FOOD_NAME, null, values);
        db.close();
    }

    public void addRelay(int ingredientId, int foodId)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        System.out.println("Add to Relay -> (" + ingredientId+ "," + foodId+ ")");

        //values.putNull(COL_FOOD_ID);
        values.put(COL_RELAY_FOOD_ID, foodId);
        values.put(COL_RELAY_INGREDIENT_ID, ingredientId);

        db.insert(TABLE_RELAY_NAME, null, values);
        db.close();
    }

    public Account getAccount(String username)
    {
        db = this.getReadableDatabase();
        String q = "select * from " + TABLE_ACCOUNT_NAME + " where username = '" + username + "';";
        Cursor cursor = db.rawQuery(q, null);
        Account a = new Account();
        if(cursor.moveToFirst()) {
            a.setId(cursor.getInt(0));
            a.setUsername(cursor.getString(1));
            a.setPassword(cursor.getString(2));
            a.setEmail(cursor.getString(3));
        }
        return a;
    }

    public Account getAccountFromId(int id)
    {
        db = this.getReadableDatabase();
        String q = "select * from " + TABLE_ACCOUNT_NAME + " where " + COL_ACCOUNT_ID + " = " + id + ";";
        Cursor cursor = db.rawQuery(q, null);
        Account a = new Account();
        if(cursor.moveToFirst()) {
            a.setId(cursor.getInt(0));
            a.setUsername(cursor.getString(1));
            a.setPassword(cursor.getString(2));
            a.setEmail(cursor.getString(3));
        }
        return a;
    }

    public Food getFoodFromId(int id)
    {
        db = this.getReadableDatabase();
        String q = "select * from " + TABLE_FOOD_NAME + " where " + COL_FOOD_ID + " = " + id + ";";
        Cursor cursor = db.rawQuery(q, null);
        Food f = new Food();
        if(cursor.moveToFirst()) {
            f.setId(cursor.getInt(0));
            f.setAccountId(cursor.getInt(1));
            f.setName(cursor.getString(2));
            f.setRecipeLink(cursor.getString(3));
        }
        return f;
    }

    public Ingredient getIngredientFromId(int id)
    {
        db = this.getReadableDatabase();
        String q = "select * from " + TABLE_INGREDIENT_NAME + " where " + COL_INGREDIENT_ID  + " = " + id + ";";
        Cursor cursor = db.rawQuery(q, null);
        Ingredient i = null;
        if(cursor.moveToFirst())
        {
            i = new Ingredient(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
        }
        return i;
    }

    public int updateIngredient(Ingredient i)
    {
        if(i != null)System.out.println(i.getId() + i.getName() + i.getAmount() + i.getExpiryDate() + i.getLocation() + i.getInStock());
        else System.out.println("ingredient is null");

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INGREDIENT_NAME, i.getName());
        values.put(COL_INGREDIENT_AMOUNT, i.getAmount());
        values.put(COL_INGREDIENT_EXPIRYDATE, i.getExpiryDate());
        values.put(COL_INGREDIENT_LOCATION, i.getLocation());
        values.put(COL_INGREDIENT_STOCKSTATUS, i.getInStock());

        String q = COL_INGREDIENT_ID + " = " + i.getId();
        return db.update(TABLE_INGREDIENT_NAME, values, q, null);
    }

    public LinkedList<Ingredient> getInventory(Account a)
    {
        db = this.getReadableDatabase();
        String q = "select * from ingredient WHERE ingredient.accId=" + a.getId() + ";";
        Cursor cursor = db.rawQuery(q, null);
        LinkedList<Ingredient> iList = new LinkedList<Ingredient>();
        if(cursor.moveToFirst()) {
            Ingredient i = new Ingredient();
            i.setId(cursor.getInt(0));
            i.setAccountId(cursor.getInt(1));
            i.setName(cursor.getString(2));
            i.setAmount(cursor.getString(3));
            i.setExpiryDate(cursor.getString(4));
            i.setLocation(cursor.getString(5));
            i.setInStock(cursor.getInt(6));
            iList.add(i);
        }
        while(cursor.moveToNext())
        {
            Ingredient i = new Ingredient();
            i.setId(cursor.getInt(0));
            i.setAccountId(cursor.getInt(1));
            i.setName(cursor.getString(2));
            i.setAmount(cursor.getString(3));
            i.setExpiryDate(cursor.getString(4));
            i.setLocation(cursor.getString(5));
            i.setInStock(cursor.getInt(6));
            iList.add(i);
        }
        return iList;
    }

    public LinkedList<Food> getMenu(Account a)
    {
        db = this.getReadableDatabase();
        String q = "select * from food WHERE food.accId=" + a.getId() + ";";
        Cursor cursor = db.rawQuery(q, null);
        LinkedList<Food> iList = new LinkedList<Food>();
        if(cursor.moveToFirst()) {
            Food i = new Food();
            i.setId(cursor.getInt(0));
            i.setAccountId(cursor.getInt(1));
            i.setName(cursor.getString(2));
            i.setRecipeLink(cursor.getString(3));
            iList.add(i);
        }
        while(cursor.moveToNext())
        {
            Food i = new Food();
            i.setId(cursor.getInt(0));
            i.setAccountId(cursor.getInt(1));
            i.setName(cursor.getString(2));
            i.setRecipeLink(cursor.getString(3));
            iList.add(i);
        }
        return iList;
    }

    /*public LinkedList<Relay> getAllRelay(Account a)
    {
        db = this.getReadableDatabase();
        String q = "select * from relay WHERE food.accId=" + a.getId() + ";";
        Cursor cursor = db.rawQuery(q, null);
        LinkedList<Food> iList = new LinkedList<Food>();
        if(cursor.moveToFirst()) {
            Food i = new Food();
            i.setId(cursor.getInt(0));
            i.setAccountId(cursor.getInt(1));
            i.setName(cursor.getString(2));
            i.setRecipeLink(cursor.getString(3));
            iList.add(i);
        }
        while(cursor.moveToNext())
        {
            Food i = new Food();
            i.setId(cursor.getInt(0));
            i.setAccountId(cursor.getInt(1));
            i.setName(cursor.getString(2));
            i.setRecipeLink(cursor.getString(3));
            iList.add(i);
        }
        return iList;
    }*/

    public LinkedList<Ingredient> getIngredientsOfFood (Food f)
    {
        /* x is a query that selects all the ingredientIds that correspond to the foodId of f
           x is then matched with table ingredient to get the information of each ingredientId in x*/

        /*Inner join is unecessary ? because there will always be a match of ingredientId in table ingredient*/
        db = this.getReadableDatabase();
        String q = "select * from ingredient INNER JOIN (SELECT ingredientId FROM relay WHERE foodId=" + f.getId() + ") x ON ingredient.ingredientId=x.ingredientId;";
        Cursor cursor = db.rawQuery(q, null);
        LinkedList<Ingredient> iList = new LinkedList<Ingredient>();
        if(cursor.moveToFirst()) {
            Ingredient i = new Ingredient();
            i.setId(cursor.getInt(0));
            i.setAccountId(cursor.getInt(1));
            i.setName(cursor.getString(2));
            i.setAmount(cursor.getString(3));
            i.setExpiryDate(cursor.getString(4));
            i.setLocation(cursor.getString(5));
            i.setInStock(cursor.getInt(6));
            iList.add(i);
        }
        while(cursor.moveToNext())
        {
            Ingredient i = new Ingredient();
            i.setId(cursor.getInt(0));
            i.setAccountId(cursor.getInt(1));
            i.setName(cursor.getString(2));
            i.setAmount(cursor.getString(3));
            i.setExpiryDate(cursor.getString(4));
            i.setLocation(cursor.getString(5));
            i.setInStock(cursor.getInt(6));
            iList.add(i);
        }
        return iList;
    }

    public int getPercentage (Account a, Food f)
    {
        LinkedList<Ingredient> list = getIngredientsOfFood(f);
        int count = 0;
        for(Ingredient i : list)
        {
            if(i.getInStock() == 1)//1 is instock
                count++;
        }
        System.out.println("count-> " + count + " #ingredientsOfFood-> " + list.size());
        return (int)((count * 1.0 / list.size())*100);
    }

    public int getNumberOfIngredientsInInventory(Account a)
    {
        int count = 0;
        db = this.getReadableDatabase();
        String q = "select * from ingredient WHERE ingredient.accId=" + a.getId() + ";";
        Cursor cursor = db.rawQuery(q, null);
        LinkedList<Ingredient> iList = new LinkedList<Ingredient>();
        if(cursor.moveToFirst()) {
            count++;
        }
        while(cursor.moveToNext())
        {
            count++;
        }
        return count;
    }

    public Boolean isIngredientOfFood(Ingredient i, Food f)
    {
        db = getReadableDatabase();
        System.out.println("is " + i.getId() + " of " + f.getId());
        String q = "SELECT " + COL_RELAY_INGREDIENT_ID + " FROM " + COL_RELAY_NAME + " WHERE " + COL_RELAY_FOOD_ID + "=" + f.getId() + " AND " + COL_RELAY_INGREDIENT_ID + "=" + i.getId() + ";";
        Cursor cursor = db.rawQuery(q, null);
        if(cursor.moveToFirst())//if ingredient is of Food
        {
            return true;
        }
        else return false;
    }

    public void toDelete()
    {
        Ingredient i = new Ingredient();
        Ingredient j = new Ingredient();
        Ingredient k = new Ingredient();
        i.setAccountId(1);
        j.setAccountId(1);
        k.setAccountId(1);
        i.setName("Beans");
        j.setName("Salt");
        k.setName("Tomato");
        i.setInStock(1);
        j.setInStock(1);
        k.setInStock(1);
        i.setLocation("Pantry");
        j.setLocation("Pantry");
        k.setLocation("Pantry");
        i.setExpiryDate("0/0/2001");
        j.setExpiryDate("0/0/2002");
        k.setExpiryDate("0/0/2003");
        i.setAmount("1 gal");
        j.setAmount("2 tons");
        k.setAmount("6 pitchers");
        i.setId(1);
        j.setId(2);
        k.setId(3);
        addIngredient(i);
        addIngredient(j);
        addIngredient(k);

        addRelay(i.getId(), 1);
        addRelay(j.getId(), 1);
        addRelay(k.getId(), 1);
    }
}
