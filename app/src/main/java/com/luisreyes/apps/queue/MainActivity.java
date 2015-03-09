package com.luisreyes.apps.queue;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;


public class MainActivity extends ActionBarActivity {

    static final int CREATE_MESSAGE_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scrollview_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.attachToScrollView(scrollView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CREATE_MESSAGE_REQUEST) {

            if(resultCode == RESULT_OK){
                String[] result = data.getStringArrayExtra("result");
                TextView tv = (TextView) findViewById(R.id.textview_main);
                tv.setText(result[0]);
            }

            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
                TextView tv = (TextView) findViewById(R.id.textview_main);
                tv.setText("No Results!");
            }
        }
    }

    public void onFabClickCreate(View view){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivityForResult(intent, CREATE_MESSAGE_REQUEST);
    }
}
