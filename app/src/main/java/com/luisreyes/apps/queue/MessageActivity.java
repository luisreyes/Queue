package com.luisreyes.apps.queue;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MessageActivity extends ActionBarActivity {

    static final int PICK_CONTACT_REQUEST = 0;

    // Input Fields
    MaterialEditText titleField;
    MaterialEditText dateField;
    MaterialEditText timeField;
    MaterialEditText messageField;
    MaterialAutoCompleteTextView phoneField;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;


    PhoneNumberFormattingTextWatcher phoneFieldWatcher = new PhoneNumberFormattingTextWatcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        titleField = (MaterialEditText) findViewById(R.id.message_title);
        dateField = (MaterialEditText) findViewById(R.id.message_date);
        timeField = (MaterialEditText) findViewById(R.id.message_time);
        messageField = (MaterialEditText) findViewById(R.id.message_message);
        phoneField = (MaterialAutoCompleteTextView) findViewById(R.id.message_phone);

        // Format Phone Number on Text Change
        phoneField.addTextChangedListener(phoneFieldWatcher);

        // Observable ScrollView Handles FloatingActionButton visibility
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scrollview_message);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_message);
        fab.attachToScrollView(scrollView);

        // Activity's main toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);

        // Transparent toolbar styling 0-255
        toolbar.getBackground().setAlpha(200);

        // Display native ActionBar back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generateAutoCompleteContactList();
    }

    private void generateAutoCompleteContactList() {

        List<HashMap<String,String>> contacts = new ArrayList<>();

        // Contacts cursor
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            HashMap<String, String> hMap = new HashMap<>();

            hMap.put("image", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)));
            hMap.put("number", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            hMap.put("name", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

            contacts.add(hMap);

        }
        cursor.close();


        // Keys used in Hashmap
        String[] from = { "name", "number"};

        // Ids of views in listview_layout
        int[] to = { R.id.name, R.id.number};

        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), contacts, R.layout.contact_item, from, to);


        MaterialAutoCompleteTextView textView = (MaterialAutoCompleteTextView) findViewById(R.id.message_phone);
        textView.setAdapter(adapter);
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
        MaterialEditText mTitle = (MaterialEditText) findViewById(R.id.message_title);
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
        dateField = (MaterialEditText) findViewById(R.id.message_date);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // Format Selected Date To Epoch
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
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
                        dateField.setText(sdf.format(date));

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void onPickTime(View view){
        timeField = (MaterialEditText) findViewById(R.id.message_time);
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
                        timeField.setText(hourOfDay + ":" + formatedMinute + " " + amPm);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_CONTACT_REQUEST) {

            if(resultCode == RESULT_OK){
                String[] result = data.getStringArrayExtra("result");
                MaterialAutoCompleteTextView et = (MaterialAutoCompleteTextView) findViewById(R.id.message_phone);
                et.setText(result[0]);
            }

            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
