package com.example.shuangke.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editName,editSurname,editMarks;
    Button btnAddData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        editName = (EditText)findViewById(R.id.editText_name);
        editSurname = (EditText)findViewById(R.id.editText_surname);
        editMarks = (EditText)findViewById(R.id.editText_maeks);
        btnAddData = (Button)findViewById(R.id.button_add);
        AddData();
    }
    public void AddData() {
        btnAddData.setOnClickListener(
              new View.OnClickListener(){
                  @Override
                  public void onClick(View view){
                      boolean isInserted = myDb.insertData(editName.getText().toString(),editSurname.getText().toString(),editMarks.getText().toString());
                      if(isInserted){
                          Toast.makeText(MainActivity.this,"Data inserted",Toast.LENGTH_LONG).show();
                      } else {
                          Toast.makeText(MainActivity.this,"Data not inserted",Toast.LENGTH_LONG).show();
                      }
                  }
              }
        );
    }
}
