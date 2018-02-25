package com.example.shaloin.codesearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnSearch;
    private EditText txt_search;
    private static final String SEARCH_KEYWORD = "seachterm";
    public static final String ARG_SEARCH = "search_arg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch=(Button)findViewById(R.id.searchButton);
        txt_search=(EditText)findViewById(R.id.searchText);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_string=txt_search.getText().toString().trim().replaceAll("\\s+","");

                if(search_string.length()>0){

                    Intent intent=new Intent(getApplicationContext(),ResultList.class);
                    intent.putExtra(SEARCH_KEYWORD,search_string);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Empty field!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
