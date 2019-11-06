package com.android.work.viewannotation;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.work.viewbus.annotation.ContentView;
import com.android.work.viewbus.annotation.InjectView;
import com.android.work.viewbus.annotation.OnClick;
import com.android.work.viewbus.manager.ViewInjectManager;

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {

    @InjectView(R.id.tv)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewInjectManager.inject(this);

        /*textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"我被点击了！！！！！！",0).show();
            }
        });*/

        textView.setText("我不知道哪里出了问题");
    }

    @OnClick(R.id.tv)
    public void click(View view){
        Toast.makeText(MainActivity.this,"我被点击了！！！！！！",0).show();
    }

    @OnClick(R.id.tv)
    public void abc(){

    }
}
