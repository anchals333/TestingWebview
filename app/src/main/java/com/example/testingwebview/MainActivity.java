package com.example.testingwebview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    Button b1;
    String link = "http://www.google.com", bookmarkUrl = "";
    WebView webView;
    EditText editText;
    boolean doubleBackToExitPressedOnce = false;

    private DatabaseHelper db;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_back:
                    webView.goBack();
                    return true;
                case R.id.navigation_forward:
                    webView.goForward();
                    return true;
                case R.id.save_bookmark:
                    if (!bookmarkUrl.equals("")) {
                        Toast.makeText(MainActivity.this, "Successfully bookmarked " + bookmarkUrl, Toast.LENGTH_SHORT).show();
                        db.insertBookmark(bookmarkUrl);
                    } else
                        Toast.makeText(MainActivity.this, "open any url", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    public static boolean checkURL(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern UrlPattern = Patterns.WEB_URL;
        boolean isURL = UrlPattern.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }

    //checking network availability
    public static boolean isNetworkAvailable(Context context) {
        try {
            if (context != null) {
                final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.b1);
        webView = (WebView) findViewById(R.id.webView1);
        editText = (EditText) findViewById(R.id.editText);
        db = new DatabaseHelper(MainActivity.this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        home(link);
    }

    public void click(View view) {
        String searchQuery = editText.getText().toString();
        if (checkURL(searchQuery)) {
            Toast.makeText(this, "URL", Toast.LENGTH_SHORT).show();
            if (searchQuery.contains("http://"))
                link = searchQuery;
            else
                link = "http://" + searchQuery;
        } else {
            Toast.makeText(this, "Search text", Toast.LENGTH_SHORT).show();
            link = "https://www.google.co.in/search?query=" + searchQuery;
        }

        home(link);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookmark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_bookmark) {

            Intent i = new Intent(this, BookmarkActivity.class);
            startActivityForResult(i, 1);

        }
        return super.onOptionsItemSelected(item);
    }

    public void home(String load) {
        if (isNetworkAvailable(MainActivity.this)) {
            WebSettings webSetting = webView.getSettings();
            webSetting.setBuiltInZoomControls(true);
            webSetting.setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(load);
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("url");
                home(result);
            }
        }
    }//onActivityResult

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            bookmarkUrl = url;
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
        }

    }


}
