package com.luisreyes.apps.queue;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MessageActivity extends ActionBarActivity {

    static final int PICK_CONTACT_REQUEST = 0;

    // Widget GUI
    MaterialEditText dateEntry;
    MaterialEditText timeEntry;
    MaterialEditText phoneEntry;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;


    PhoneNumberFormattingTextWatcher phoneWatcher = new PhoneNumberFormattingTextWatcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        phoneEntry = (MaterialEditText) findViewById(R.id.addEditPhone);
        phoneEntry.addTextChangedListener(phoneWatcher);

        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scrollview_message);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_message);
        fab.attachToScrollView(scrollView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(200);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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

    public void onFabClickDone(View view){
        Intent returnIntent = new Intent();
        String[] result = new String[1];
        MaterialEditText mTitle = (MaterialEditText) findViewById(R.id.edittext_title);
        result[0] = mTitle.getText().toString();
        returnIntent.putExtra("result",result);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    public void onPickDate(View view){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dateEntry = (MaterialEditText) findViewById(R.id.addEditDate);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // Format Selected Date To Epoch
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        long epoch = 0;

                        try {
                            epoch = sdf.parse((monthOfYear+1)+"/"+dayOfMonth+"/"+year+" 00:00:00").getTime() / 1000;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Format Epoch to Human Readable Date
                        sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                        Date date = new Date(epoch*1000);

                        // Set Human Readable Date to UI Field
                        dateEntry.setText(sdf.format(date));

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void onPickTime(View view){
        timeEntry = (MaterialEditText) findViewById(R.id.addEditTime);
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Format Selected Time To Epoch

                        String formatedHour = String.valueOf(hourOfDay);
                        String formatedMinute = String.valueOf(minute);
                        String amPm = "AM";
                        if(hourOfDay == 0){ hourOfDay += 12; } else
                        if(hourOfDay > 12){ hourOfDay -= 12; amPm = "PM"; }
                        if(hourOfDay < 10){ formatedHour = "0" + hourOfDay; }else{
                            formatedHour = String.valueOf(hourOfDay);
                        }

                        if(minute < 10){ formatedMinute = "0" + String.valueOf(minute); }
                        System.out.format(formatedHour);
                        System.out.format(formatedMinute);


                        // Set Human Readable Time to UI Field
                        timeEntry.setText(hourOfDay + ":" + formatedMinute +" "+ amPm);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_CONTACT_REQUEST) {

            if(resultCode == RESULT_OK){
                String[] result = data.getStringArrayExtra("result");
                MaterialEditText et = (MaterialEditText) findViewById(R.id.addEditPhone);
                et.setText(result[0]);
            }

            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void onPickContacts(View view){

        Intent intent = new Intent(this, ContactsActivity.class);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }
}
