package com.example.advancepizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseHelperCustomer extends android.database.sqlite.SQLiteOpenHelper {

    private static final String dbName = "AdvancePizzaD16411";
    private static final int dbVersion = 1;

    public DataBaseHelperCustomer(Context context) {
        super(context, dbName, null, dbVersion);
    }

    private static final String CREATE_PIZZA_TABLE = "CREATE TABLE Pizza (" +
            "PizzaID INTEGER PRIMARY KEY," +
            "Name TEXT," +
            "Type TEXT," +
            "Image INTEGER)";

    private static final String CREATE_SPECIAL_OFFERS_TABLE = "CREATE TABLE SpecialOffers (" +
            "OfferID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "PizzaID INTEGER," +
            "PizzaSize TEXT," +
            "StartDate TEXT," +
            "EndDate TEXT," +
            "Discount TEXT," +
            "FOREIGN KEY(PizzaID) REFERENCES Pizza(PizzaID) ON DELETE CASCADE);";


    private static final String CREATE_FAVORITES_TABLE = "CREATE TABLE Favorites (" +
            "FavoriteID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Email TEXT," +
            "PizzaID INTEGER," +
            "FOREIGN KEY(Email) REFERENCES CUSTOMER(Email) ON DELETE CASCADE," +
            "FOREIGN KEY(PizzaID) REFERENCES Pizza(PizzaID));";

    private static final String CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS [Order] (" +
            "OrderID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Email TEXT," +
            "PizzaID INTEGER," +
            "PizzaSize TEXT," +
            "OrderDate TEXT," +
            "OrderTime TEXT," +
            "OrderPrice double," +
            "FOREIGN KEY (Email) REFERENCES CUSTOMER(Email) ON DELETE CASCADE," +
            "FOREIGN KEY (PizzaID) REFERENCES Pizza(PizzaID));";



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Pizza");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS [Order]");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CUSTOMER");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Favorites");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SpecialOffers");
        sqLiteDatabase.execSQL("CREATE TABLE CUSTOMER(Email TEXT PRIMARY KEY,FirstName TEXT,LastName TEXT,Password TEXT,PhoneNumber TEXT,Gender TEXT, Permission TEXT, ProfilePicture BLOB);");
        sqLiteDatabase.execSQL(CREATE_PIZZA_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDER_TABLE);
        sqLiteDatabase.execSQL(CREATE_SPECIAL_OFFERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAVORITES_TABLE);
        sqLiteDatabase.execSQL("INSERT INTO CUSTOMER(Email,FirstName,LastName,Password,PhoneNumber,Gender,Permission) VALUES ('samir@gmail.com','Samir','Shoukri','"+Hash.hashPassword("12345asdfg")+"','0599999999','Male','Admin')");

    }
    public boolean insertPizza(Pizza pizza){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PizzaID",pizza.getId());
        contentValues.put("Name",pizza.getName());
        contentValues.put("Type",pizza.getType());
        contentValues.put("Image", pizza.getImgPizza());

        if (sqLiteDatabase.insert("Pizza", null, contentValues) == -1)
            return false;
        else
            return true;
    }

    public boolean insertFavorite(String userEmail, int pizzaID){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email", userEmail);
        contentValues.put("PizzaID", pizzaID);
        if (sqLiteDatabase.insert("Favorites",null,contentValues) == -1)
            return false;
        else
            return true;
    }

    public void deleteFavorite(String userEmail, int pizzaID){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("Favorites","Email = '"+userEmail+"' AND PizzaID = '"+pizzaID+"'",null);
    }


    public void deleteOrder(String userEmail, int pizzaID){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("[Order]","Email = '"+userEmail+"' AND PizzaID = '"+pizzaID+"'",null);
    }

    public void deletePizzaFromAllFavorites(int pizzaID){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("Favorites","PizzaID = '"+pizzaID+"'",null);
    }

    // get favorites with car info by user email
    public Cursor getFavoritesWithPizzaInfoByEmail(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Favorites INNER JOIN Pizza ON Favorites.PizzaID = Pizza.PizzaID WHERE Email = '"+email+"'",null);
    }



    public boolean insertSpecialOffer(SpecialOffer specialOffer){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PizzaID",specialOffer.getPizzaId());
        contentValues.put("PizzaSize",specialOffer.getPizzaSize());
        contentValues.put("StartDate",specialOffer.getStartDate());
        contentValues.put("EndDate",specialOffer.getEndDate());
        contentValues.put("Discount",specialOffer.getDiscount());
        if (sqLiteDatabase.insert("SpecialOffers",null,contentValues) == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllPizzas(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Pizza",null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP DATABASE IF EXISTS AdvancePizzaPizzas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Pizza");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS [Order]");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CUSTOMER");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Favorites");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SpecialOffers");

        onCreate(sqLiteDatabase);
    }


    public void insertCustomer(User customer) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email", customer.getEmail());
        contentValues.put("FirstName", customer.getFirstName());
        contentValues.put("LastName", customer.getLastName());
        contentValues.put("Password", customer.getPassword());
        contentValues.put("PhoneNumber", customer.getPhoneNumber());
        contentValues.put("Gender", customer.getGender());
        contentValues.put("Permission", customer.getPermission());
        contentValues.put("ProfilePicture", customer.getProfilePicture());

        sqLiteDatabase.insert("CUSTOMER", null, contentValues);

    }

    public Cursor getAllCustomers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM CUSTOMER", null);
    }

    public Cursor getPizzaByID(int id){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Pizza WHERE PizzaID = '"+id+"'",null);
    }

    public Cursor getPizzaByName(String name){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Pizza WHERE Name = '"+name+"'",null);
    }


    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from CUSTOMER where EMAIL = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkEmailPassword(String email, String password){
        boolean b;
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from CUSTOMER where EMAIL = ? and PASSWORD = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            b= true;
        }else {
            b=false;
        }
        return b;
    }

    public boolean isFavorite(String userEmail, int pizzaID){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Favorites WHERE Email = '"+userEmail+"' AND pizzaID = '"+pizzaID+"'",null);
        if (cursor.getCount() == 0)
            return false;
        else
            return true;
    }



    public void clearDatabase(SQLiteDatabase db) {
        db.execSQL("DELETE FROM Pizza");

        // Repeat for other tables if necessary
    }

    public Cursor getOrderWithPizzaInfoByEmail(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM [Order] INNER JOIN Pizza ON [Order].PizzaID = Pizza.PizzaID WHERE Email = '"+email+"'",null);
    }

    /*public Cursor getAllOrdersWithPizzaInfoAndUserInfo(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Order INNER JOIN Pizza ON Order.PizzaID = Pizza.PizzaID INNER JOIN CUSTOMER ON Order.Email = CUSTOMER.Email",null);
    }*/

    public Cursor getSpecialOfferForPizza(int pizzaID){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SpecialOffers WHERE PizzaID = '"+pizzaID+"'",null);
    }

    public void updateUserProfilePicture(String email, byte[] profilePicture){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProfilePicture",profilePicture);
        sqLiteDatabase.update("CUSTOMER",contentValues,"Email = '"+email+"'",null);
    }

    public void updateUserPassword(String email, String password){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Password",password);

        sqLiteDatabase.update("CUSTOMER",contentValues,"Email = '"+email+"'",null);

    }

    public void updateUserInfo(User user){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FirstName",user.getFirstName());
        contentValues.put("LastName",user.getLastName());
        contentValues.put("PhoneNumber", user.getPhoneNumber());
        sqLiteDatabase.update("CUSTOMER",contentValues,"Email = '"+user.getEmail()+"'",null);
    }

    public Cursor getUserByEmail(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM CUSTOMER WHERE Email = '"+email+"'",null);
    }


    public void insertOrder(Order order){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email",order.getUserEmail());
        contentValues.put("PizzaID",order.getPizzaID());
        contentValues.put("PizzaSize",order.getPizzaSize());
        contentValues.put("OrderDate", order.getOrderDate());
        contentValues.put("OrderTime", order.getOrderTime());
        contentValues.put("OrderPrice", order.getPrice());
        sqLiteDatabase.insert("[Order]", null, contentValues);


    }


    public void deleteSpecialOfferByID(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("SpecialOffers","OfferID = '"+id+"'",null);
    }

    public Cursor getAllSpecialOffersWithPizzaInfo(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SpecialOffers INNER JOIN Pizza ON SpecialOffers.PizzaID = Pizza.PizzaID WHERE Pizza.PizzaID",null);
    }

    public Cursor getAllOrdersWithPizzaInfo(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM [Order] INNER JOIN Pizza ON [Order].PizzaID = Pizza.PizzaID WHERE Pizza.PizzaID",null);
    }

    public Cursor getAllOrdersWithPizzaInfoAndUserInfo(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM [Order] INNER JOIN Pizza ON [Order].PizzaID = Pizza.PizzaID INNER JOIN CUSTOMER ON [Order].Email = CUSTOMER.Email",null);
    }



}


