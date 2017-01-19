package com.example.mehedi.mycost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mehedi.mycost.adapters.ExpandableListAdapter;
import com.example.mehedi.mycost.adapters.ListAdapter;
import com.example.mehedi.mycost.adapters.Listener;
import com.example.mehedi.mycost.Database.DbHelper;
import com.example.mehedi.mycost.models.CostData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Listener {

    RecyclerView recyclerView;
    DbHelper dbHelper;
    ListAdapter adapter;
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DbHelper.getInstance(getApplicationContext());

/*        recyclerView = (RecyclerView) findViewById(R.id.rv_costlist);
        adapter = new ListAdapter(this, dbHelper.getDateCost());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/

        recyclerview = (RecyclerView) findViewById(R.id.rv_costlist);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<ExpandableListAdapter.Item> data = dbHelper.getDateCost();

        recyclerview.setAdapter(new ExpandableListAdapter(data));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_add_cost:
                add_cost_activity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void add_cost_activity() {

        Intent i = new Intent(MainActivity.this, CostActivity.class);
        startActivity(i);
    }


    @Override
    public void dateDetails(CostData costData) {

        Toast.makeText(MainActivity.this, costData.date + "  " + costData.cost, Toast.LENGTH_SHORT).show();
    }
}
