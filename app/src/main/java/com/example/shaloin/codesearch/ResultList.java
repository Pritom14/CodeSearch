package com.example.shaloin.codesearch;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultList extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String urlmain = "https://searchcode.com/api/codesearch_I/?q=";
    private static final String addition="&per_page=100";

    // JSON Node names
    private static final String ARR_RESULTS="results";
    private static final String OBJ_NAME="name";
    private static final String OBJ_LANG="language";
    private static final String OBJ_URL="url";

    // Seach term
    private static final String SEARCH_KEYWORD = "seachterm";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> resultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        resultsList = new ArrayList<HashMap<String, String>>();

        ListView listView=getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem

                String name=resultsList.get(position).get("name");
                String lang=resultsList.get(position).get("language");
                String urlcode=resultsList.get(position).get("url");

                Intent intent=new Intent(getApplicationContext(),
                        ViewResults.class);
                intent.putExtra(OBJ_NAME, name);
                intent.putExtra(OBJ_LANG,lang);
                intent.putExtra(OBJ_URL, urlcode);
                startActivity(intent);

            }
        });

        Intent intent1=getIntent();

        String search_item=intent1.getStringExtra(SEARCH_KEYWORD);

        // Calling async task to get json
        new GetContacts().execute(search_item);

    }


    /**
     * Async task class to get json by making HTTP call
     * */
    class GetContacts extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            /*pDialog = new ProgressDialog(ResultList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(String... search_term) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlmain+
                    URLEncoder.encode(search_term[0])+addition, ServiceHandler.GET);

            Log.d("search term: ", "> " + search_term[0]);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(ARR_RESULTS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String name = c.getString(OBJ_NAME);
                        String lang = c.getString(OBJ_LANG);
                        String url = c.getString(OBJ_URL);

                        Log.d("URL : ",url);
                        // tmp hashmap for single contact
                        HashMap<String, String> single_result = new HashMap<String, String>();

                        single_result.put(OBJ_NAME, name);
                        single_result.put(OBJ_LANG, lang);
                        single_result.put(OBJ_URL, url);

                        // adding contact to contact list
                        resultsList.add(single_result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss();*/
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(ResultList.this,
                    resultsList, R.layout.row_item, new String[] {OBJ_NAME,
                    OBJ_LANG }, new int[] { R.id.nameofrepo, R.id.langofrepo});

            setListAdapter(adapter);
        }

    }
}
