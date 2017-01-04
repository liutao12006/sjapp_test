package com.example.toolbar;

import android.service.carrier.CarrierService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //toolbar 绑定 menu  但这里不知道为什么   就是不行

//        toolbar.inflateMenu(R.menu.menu_main);//把menu和toolbar 进行绑定
//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                switch (id){
//                    case R.id.action_edit:
//                        Toast.makeText(MainActivity.this,"one",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.action_share:
//                        Toast.makeText(MainActivity.this,"two",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//
//                return false;
//            }
//        });
    }
}
