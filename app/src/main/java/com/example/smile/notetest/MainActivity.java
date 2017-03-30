package com.example.smile.notetest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private FloatingActionButton addButton;
    private ListView listView;      //ListView项
    private MyAdapter adapter;

    private NotesDB notesDB;        //创建一个数据库对象
    private SQLiteDatabase dbReader;    //创建一个读取权限
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    public void initView() {
        listView = (ListView) findViewById(R.id.list);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("生活点滴");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        notesDB = new NotesDB(this);    //实例化数据库
        dbReader = notesDB.getReadableDatabase();   //获取读取的权限

        //获取点击ListView的事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, LookDiaryActivity.class);
                i.putExtra(NotesDB.ID, cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);
                startActivity(intent);
        }
    }

    public void selectDB() {
        cursor = dbReader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
        adapter = new MyAdapter(this,cursor);   //实例化adapter，通过adapter进行适配
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
