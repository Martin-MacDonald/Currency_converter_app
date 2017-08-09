package com.example.martin.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private final String CONVERTER_URL = "http://api.fixer.io/latest";

    private String mCurrencyOrigin;
    private String mCurrencyDest;
    Button mConvButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConvButton = (Button) findViewById(R.id.convert_button);

        //Create new spinner object
        Spinner spinnerOrig = (Spinner) findViewById(R.id.currency_origin);
        Spinner spinnerDest = (Spinner) findViewById(R.id.currency_dest);

        //Attach the string array to the spinner and set default layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency_types, android.R.layout.simple_spinner_item);

        //Set default drop down layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attche the spinner to the layout spinners
        spinnerOrig.setAdapter(adapter);
        spinnerDest.setAdapter(adapter);

        spinnerOrig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               mCurrencyOrigin = adapterView.getItemAtPosition(i).toString();
                Log.d("Currency", "" + mCurrencyOrigin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrencyDest = adapterView.getItemAtPosition(i).toString();
                Log.d("Currency", "" + mCurrencyDest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mConvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();

                params.put("base", mCurrencyOrigin);
                params.put("symbols", mCurrencyDest);

                letsCallTheAPI(params);
            }
        });
    }

    private void letsCallTheAPI(RequestParams params){

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(CONVERTER_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    Double convRate = response.getJSONObject("rates").getDouble(mCurrencyDest);
                    Log.d("Currency", convRate.toString());
                    updateUI(convRate);
                } catch (JSONException e){
                    e.printStackTrace();
                    return;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorresponse){

            }
        });
    }

    private void updateUI(Double rate){
        String currencyText = rate.toString();
        TextView currencyView = (TextView) findViewById(R.id.currency_view);

        currencyView.setText(currencyText);
    }

}
