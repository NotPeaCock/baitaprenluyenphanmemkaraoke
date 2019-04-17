package com.trungse.baitaprenluyenphanmemkaraoke;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.Toast;

import com.trungse.adapter.BaiHatAdapter;
import com.trungse.model.BaiHat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    
    public static String DATABASE_NAME = "Arriang.s3db";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;
    public static String TableName = "ArriangSongList";
    ListView lvAll;
    BaiHatAdapter adapter;
    TabHost tabHost;
    ListView lvLove;
    BaiHatAdapter adapterLove;
    public static int selectedTab=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
        setUpTabHost();
        addControls();
        hienThiToanBoBaiHat();
        addEvents();
    }

    private void addEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tab1")){
                    hienThiToanBoBaiHat();
                    selectedTab=0;
                }else{
                    hienThiBaiHatYeuThich();
                    selectedTab=1;
                }
            }
        });
    }

    private void hienThiBaiHatYeuThich() {
        Cursor cursor = database.query(TableName, null, "YEUTHICH=?", new String[] {"1"},null, null, null);
        adapterLove.clear();
        while(cursor.moveToNext()){
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            String tacgia = cursor.getString(3);
            int thich = cursor.getInt(5);
            BaiHat baiHat = new BaiHat(ma, ten, tacgia,thich);
            adapterLove.add(baiHat);
        }
        cursor.close();
    }

    private void setUpTabHost() {
        tabHost = findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("All");
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2= tabHost.newTabSpec("tab2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Love");
        tabHost.addTab(tab2);
    }


    private void hienThiToanBoBaiHat() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query(TableName, null, null, null,null, null, null);
        adapter.clear();
        while(cursor.moveToNext()){
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            String tacgia = cursor.getString(3);
            int thich = cursor.getInt(5);
            BaiHat baiHat = new BaiHat(ma, ten, tacgia,thich);
            adapter.add(baiHat);
        }
        cursor.close();
    }

    private void addControls() {
        lvAll = findViewById(R.id.lv_all);
        adapter = new BaiHatAdapter(MainActivity.this, android.R.layout.simple_list_item_1);
        lvAll.setAdapter(adapter);
        lvLove = findViewById(R.id.lv_love);
        adapterLove = new BaiHatAdapter(MainActivity.this, android.R.layout.simple_list_item_1);
        lvLove.setAdapter(adapterLove);
    }

    private void processCopy() {
        try{
            File dbFile = getDatabasePath(DATABASE_NAME);
            if(!dbFile.exists()){
                copyDatabaseFromAssets();
                Toast.makeText(this, "Sao chep CSDL Sqlite thanh cong!", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ex){
            Log.e("Loi", ex.toString());
        }
    }

    private String getDatabasePath(){
        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }

    private void copyDatabaseFromAssets() {
        try{
            InputStream myInput = getAssets().open(DATABASE_NAME, MODE_PRIVATE);
            File f = new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            String outFileName = getDatabasePath();
            OutputStream myOutput =new FileOutputStream(outFileName);
            int length;
            byte []buffer =new byte[1024];
            while((length=myInput.read(buffer))>0){
                myOutput.write(buffer,0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }catch(Exception ex){
            Log.e("Loi", ex.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem mnuSearch = menu.findItem(R.id.mnu_search);
        SearchView searchView = (SearchView) mnuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                xuLyTimKiem(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void xuLyTimKiem(String newText) {
        Cursor cursor = database.query(TableName, null, "MABH=? or TENBH like ? or TACGIA like ?",
                new String[] {"%"+newText+"%","%"+newText+"%","%"+newText+"%"}, null,null, null);
        adapter.clear();
        while(cursor.moveToNext()){
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            String tacgia = cursor.getString(3);
            int thich = cursor.getInt(5);
            BaiHat baiHat = new BaiHat(ma, ten, tacgia,thich);
            adapter.add(baiHat);
        }
        cursor.close();
    }
}
