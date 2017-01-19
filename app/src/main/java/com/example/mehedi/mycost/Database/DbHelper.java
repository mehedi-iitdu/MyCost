package com.example.mehedi.mycost.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mehedi.mycost.adapters.ExpandableListAdapter;
import com.example.mehedi.mycost.models.CostData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehedi on 1/17/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    private static final String DATABASE_NAME = "myCostDB";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_COST = "cost";

    private static final String C_ID = "c_id";
    private static final String CATEGORY_NAME = "category_name";

    private static final String ID = "id";
    private static final String COST = "cost";
    private static final String DATE = "date";

    private static DbHelper mDbHelper;


    public static synchronized DbHelper getInstance(Context context) {

        if (mDbHelper == null) {
            mDbHelper = new DbHelper(context.getApplicationContext());
        }
        return mDbHelper;
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_category_table = "CREATE TABLE "+TABLE_CATEGORY+" ( "+C_ID+" INTEGER PRIMARY KEY, "+ CATEGORY_NAME +" TEXT"+" ) ";
        String create_cost_table = "CREATE TABLE "+TABLE_COST+" ( "+ID+" INTEGER PRIMARY KEY, "+COST+" TEXT ,"+DATE+ " TEXT, "+CATEGORY_NAME+" TEXT"+" )";
        db.execSQL(create_category_table);
        db.execSQL(create_cost_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COST);

            onCreate(db);
        }
    }


    public boolean insertCategory(String category){

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put(CATEGORY_NAME,category);

            db.insertOrThrow(TABLE_CATEGORY,null,contentValues);
            db.setTransactionSuccessful();

            return true;

        } catch (SQLException e){
            e.printStackTrace();
            Log.d(TAG, "Error inserting category to database");
        }

        finally {
            db.endTransaction();
        }

        return false;
    }

    public List<String> getCategoryList(){

        List<String> categoryList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    String category = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));

                    categoryList.add(category);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

            Log.d(TAG, "Error in retrieving data from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return categoryList;
    }

    public boolean saveCost(CostData costData) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put(COST, costData.cost);
            contentValues.put(DATE, costData.date);
            contentValues.put(CATEGORY_NAME, costData.category);

            db.insertOrThrow(TABLE_COST,null,contentValues);
            db.setTransactionSuccessful();

            return true;

        } catch (SQLException e){
            e.printStackTrace();
            Log.d(TAG, "Error inserting category to database");
        }

        finally {
            db.endTransaction();
        }

        return false;
    }


    public List<ExpandableListAdapter.Item> getDateCost() {

        /*List<CostData> costdetail = new ArrayList<>();*/

        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        String DATE_SELECT_QUERY = "SELECT DISTINCT " +DATE+ " FROM " + TABLE_COST;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(DATE_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    /*CostData costData = new CostData();*/

                    String date = cursor.getString(cursor.getColumnIndex(DATE));
                    String cost = returnCost(cursor.getString(cursor.getColumnIndex(DATE)));

                    /*costdetail.add(costData);*/

                    ExpandableListAdapter.Item item = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, date,cost);
                    item.invisibleChildren = new ArrayList<>();

                    String COST_QUERY = "SELECT * FROM " + TABLE_COST + " WHERE " + DATE + "='" + date + "'";

                    SQLiteDatabase db_cost = getReadableDatabase();
                    Cursor cursor_cost = db_cost.rawQuery(COST_QUERY, null);

                    try{
                        if (cursor_cost.moveToFirst()){
                            do {

                                String category = cursor_cost.getString(cursor_cost.getColumnIndex(CATEGORY_NAME));
                                String in_cost = cursor_cost.getString(cursor_cost.getColumnIndex(COST));

                                item.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,category + "       "+in_cost,""));

                            } while (cursor_cost.moveToNext());
                        }
                    } catch (Exception e){
                        Log.d(TAG, "Error while retriving cost");
                    }

                    finally {
                        if (cursor_cost !=null && cursor_cost.isClosed()){
                            cursor_cost.close();
                        }
                    }

                    data.add(item);


                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get costs from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return data;

    }

    public String returnCost(String date){

        String COST_QUERY = "SELECT " + COST + "  FROM " + TABLE_COST + " WHERE " + DATE + "='" + date + "'";

        SQLiteDatabase db_cost = getReadableDatabase();
        Cursor cursor_cost = db_cost.rawQuery(COST_QUERY, null);

        double total = 0;

        try{
            if (cursor_cost.moveToFirst()){
                do {

                    Log.d(TAG, "Showing Cost for " + date + " : " + cursor_cost.getString(cursor_cost.getColumnIndex(COST)));

                    total = total + Double.parseDouble(cursor_cost.getString(cursor_cost.getColumnIndex(COST)));

                } while (cursor_cost.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "Error while retriving cost");
        }

        finally {
            if (cursor_cost !=null && cursor_cost.isClosed()){
                cursor_cost.close();
            }
        }

        return Double.toString(total);
    }

}
