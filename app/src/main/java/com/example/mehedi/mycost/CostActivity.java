package com.example.mehedi.mycost;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mehedi.mycost.Database.DbHelper;
import com.example.mehedi.mycost.MainActivity;
import com.example.mehedi.mycost.R;
import com.example.mehedi.mycost.models.CostData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CostActivity extends AppCompatActivity {

    final Context context = this;
    private Button add_category;
    private Button save_cost;
    Spinner select_category;
    EditText editText_cost;
    EditText editText_date;
    private DatePickerDialog DatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private DbHelper dbHelper = new DbHelper(context);

    private void initialize(){

        add_category = (Button) findViewById(R.id.add_category);
        save_cost = (Button) findViewById(R.id.save_cost);
        dateFormatter =  new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        editText_date = (EditText) findViewById(R.id.date);
        editText_date.setInputType(InputType.TYPE_NULL);

        select_category = (Spinner) findViewById(R.id.select_category);
        editText_cost = (EditText) findViewById(R.id.amount);

        setDateTimeField();

    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText_date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        initialize();
        addItemsOnCategory();

        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });

        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.show();
            }
        });

        save_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CostData costData = new CostData();

                costData.category = select_category.getSelectedItem().toString();
                costData.cost = editText_cost.getText().toString();
                costData.date = editText_date.getText().toString();

                if (dbHelper.saveCost(costData)) {
                    Toast.makeText(context,"Cost added successfully",Toast.LENGTH_SHORT).show();
                    editText_cost.setText("");
                    editText_date.setText("");
                }

                else {
                    Toast.makeText(context,"Sorry",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void showDialog(){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.category_entry_dialog);


        final EditText editText = (EditText) dialog.findViewById(R.id.category_name);

        Button save_category = (Button) dialog.findViewById(R.id.save_category);

        Button cancel_category = (Button) dialog.findViewById(R.id.cancel_category);

        save_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category = editText.getText().toString();

                if (dbHelper.insertCategory(category)){
                    addItemsOnCategory();
                    Toast.makeText(context,category+" added into category list",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(context,"Sorry. Try again",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        cancel_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void addItemsOnCategory(){

        select_category = (Spinner) findViewById(R.id.select_category);
        List<String> list = dbHelper.getCategoryList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_category.setAdapter(dataAdapter);

    }
}
