package com.zero.applist;


import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView lView;
    ArrayAdapter<String> adapter;
    //ArrayAdapter<String> adapter_empty;

    SearchView searchView;
    ArrayList label = new ArrayList();
    ArrayList name = new ArrayList();
    Intent intent = new Intent(Intent.ACTION_MAIN, null);


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screen
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.search_view);
        lView = (ListView) findViewById(R.id.list1);


        final PackageManager pm = getApplicationContext().getPackageManager();//.getApplicationContext(); //getApplicationContext().getPackageManager();


        searchView.setOnQueryTextListener(this);
        loadapp();
        runnow();


        lView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                int all = 0;
                String package_name = (String) ((TextView) view).getText();
                while (!package_name.equals(String.valueOf(name.get(all)))) {
                    all++;
                }
                String pack = String.valueOf(label.get(all));
                pack = "package:"+pack;
                Intent del = new Intent(Intent.ACTION_DELETE);//for uninstalling app by long clicking
                del.setData(Uri.parse(pack));
                startActivity(del);
               runnow();
                return true;
            }
        });
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int all = 0;
                String package_name = (String) ((TextView) view).getText();
                while (!package_name.equals(String.valueOf(name.get(all)))) {
                    all++;
                }
                String pack = String.valueOf(label.get(all));

                Intent i = pm.getLaunchIntentForPackage(pack);
                startActivity(i); //for launching app

            }
        });


    }


    @Override
    public boolean onQueryTextSubmit(String s) {

        switch (s) {
            case "1":
              loadapp();
                break;
            case "0":
                adapter.clear();
                Toast.makeText(MainActivity.this, "APPS REMOVED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(MainActivity.this, "NOT FOUND", Toast.LENGTH_SHORT).show(); 

        }


        return true;
    }


    @Override
    public boolean onQueryTextChange(String s) {

        adapter.getFilter().filter(s);
        lView.setAdapter(adapter);


        return true;
    }

    @Override
    public void onBackPressed() {
       
        Intent intent = new Intent(Intent.ACTION_DIAL);
        //intent.setData(Uri.parse("tel:0123456789"));
        startActivity(intent);
    }


    public void loadapp() {
        name.clear();
        label.clear();
        final PackageManager pm = getApplicationContext().getPackageManager();//.getApplicationContext(); //getApplicationContext().getPackageManager();
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
       final List<ResolveInfo> list;
        list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        Collections.sort(list, new ResolveInfo.DisplayNameComparator(pm)); //for sorting
        for (ResolveInfo rInfo : list) {
            label.add(rInfo.activityInfo.packageName);
            name.add(rInfo.activityInfo.applicationInfo.loadLabel(pm).toString());
        }
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, name);
        lView.setAdapter(adapter);
    }
    public void runnow(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        loadapp();
            }
        },5000);

    }

}
