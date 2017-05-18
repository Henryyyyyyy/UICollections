package me.henry.uicollections;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;
import me.henry.uicollections.circlemenu.CircleMenuActivity;
import me.henry.uicollections.quickindex.QuickIndexActivity;
import me.henry.uicollections.seekbar.HenryCustomSeekBar;
import me.henry.uicollections.seekbar.SeekBarActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btnQuickIndex)
    FButton btnQuickIndex;
    @BindView(R.id.btnCircleMenu)
    FButton btnCircleMenu;
    @BindView(R.id.btnSeekBar)
    FButton btnSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnQuickIndex.setOnClickListener(this);
        btnCircleMenu.setOnClickListener(this);
        btnSeekBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnQuickIndex:
                Intent intent1=new Intent(MainActivity.this,QuickIndexActivity.class);
                startActivity(intent1);
                break;
            case R.id.btnCircleMenu:
                Intent intent2=new Intent(MainActivity.this,CircleMenuActivity.class);
                startActivity(intent2);
                break;
            case R.id.btnSeekBar:
                Intent intent3=new Intent(MainActivity.this,SeekBarActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
