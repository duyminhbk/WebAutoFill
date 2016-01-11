package com.app.webautofill;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etLink;
    private Button btnGo;
    private WebView web;
    private Spinner spin;
    private boolean hasLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        etLink = (EditText)findViewById(R.id.tx_link);
        btnGo = (Button)findViewById(R.id.btn_go);
        web = (WebView)findViewById(R.id.web);
        spin = (Spinner)findViewById(R.id.spin_link);
        initSpinner();
        initWeb();
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etLink.getText())){
                    Toast.makeText(MainActivity.this,"enter web url",Toast.LENGTH_SHORT).show();
                    return;
                }
                hasLoaded=false;
                web.loadUrl(spin.getSelectedItem().toString()+etLink.getText()+"");

            }
        });

    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this
                , R.array.ext_link, android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence item = (CharSequence) parent.getSelectedItem();
                Toast.makeText(MainActivity.this,item,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initWeb() {

        WebViewClient client = new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String username = "123";
                String password = "1111";
                web.loadUrl("javascript:(" +

                        "function() { var fillName = function(username,password,loaded){\n" +
                        "    if(loaded == \"true\"){\n" +
                        "        alert(\"\");\n" +
                        "        return;\n" +
                        "    }\n" +
                        "    var findAccountField = function(){\n" +
                        "        var arr = [\"username\", \"email\", \"session[username_or_email]\",\"session_key\"];\n" +
                        "        var i;\n" +
                        "        for (i = 0; i < arr.length; i++) { \n" +
                        "            var object = document.getElementsByName(arr[i])[0];\n" +
                        "            if(object &&object.tagName&& object.tagName.toLowerCase() == \"input\"){\n" +
                        "                return object;\n" +
                        "            }\n" +
                        "        }\n" +
                        "    };\n" +
                        "    var findPassField = function(){\n" +
                        "        var arr = [\"passwd\", \"pass\", \"session[password]\",\"session_password\"];\n" +
                        "        var i;\n" +
                        "        for (i = 0; i < arr.length; i++) { \n" +
                        "            var object = document.getElementsByName(arr[i])[0];\n" +
                        "            if(object &&object.tagName&& object.tagName.toLowerCase() == \"input\"){\n" +
                        "                return object;\n" +
                        "            }\n" +
                        "        }\n" +
                        "    };\n" +
                        "    findAccountField().value = username;\n" +
                        "    findPassField().value = password;\n" +
                        "};\n" +
                        "fillName("+username+","+password+","+hasLoaded+");}" +

                        ")()");
                hasLoaded=true;
            }
        };
        WebChromeClient chrome = new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        };

        web.setWebChromeClient(chrome);
        web.setWebViewClient(client);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.loadUrl(spin.getSelectedItem().toString()+etLink.getText()+"");
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
}
