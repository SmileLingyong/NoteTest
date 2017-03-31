package com.example.smile.notetest.activity;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.smile.notetest.NotesDB;
import com.example.smile.notetest.R;

import java.util.Date;

/**
 * Created by lly54 on 2017/3/29.
 */

public class NoteEditActivity extends AppCompatActivity {

    private final String DEFAULT_EDITTEXT = "  写点什么吧";
    private Button savebtn, cancalbtn;
    private TextView editText, toolbarTitle;
    private Toolbar toolbar;

    private NotesDB notesDB;            //创建一个数据库
    private SQLiteDatabase dbWriter;    //创建一个添加权限


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        initToolbar();
        editText = (TextView) findViewById(R.id.edit_content);

        notesDB = new NotesDB(this);                //实例化数据库
        dbWriter = notesDB.getWritableDatabase();    //获取到当前可添加的权
    }

    //初始化Toolbar
    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_note_edit);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(R.drawable.ic_action_note_create_cancel);//设置返回按钮

        //添加返回按钮事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent upIntent = NavUtils.getParentActivityIntent(NoteEditActivity.this);
                if (NavUtils.shouldUpRecreateTask(NoteEditActivity.this, upIntent)) {
                    TaskStackBuilder.create(NoteEditActivity.this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    NavUtils.navigateUpTo(NoteEditActivity.this, upIntent);
                }
            }
        });

        toolbar.setTitle("");
        toolbarTitle.setText("新建日记");
        setSupportActionBar(toolbar);

    }

    //隐藏键盘
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // 加载保存按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_note_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 保存按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_note_create_save:
                if (editText.getText().length() == 0) {
                    finish();
                } else {
                    addDB();
                    Log.d("NoteEditActivity.class", "addDB");
                    finish();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //将EditText中的数据保存至数据库
    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, editText.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    //获取系统当前时间
    public String getTime() {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("HH:mm  yyyy.MM.dd");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }

}
