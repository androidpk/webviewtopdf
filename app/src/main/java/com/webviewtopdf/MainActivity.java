package com.webviewtopdf;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.webtopdf.PdfView;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    WebView webView;
    Button button_convert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        button_convert = findViewById(R.id.button);

        webView.loadUrl("https://github.com/androidpk/webviewtopdf");


        button_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/Test.pdf");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                PdfView.printToPdf(MainActivity.this, webView, path.getAbsolutePath(), new PdfView.Callback() {

                    @Override
                    public void success(String path) {
                        progressDialog.dismiss();
                        PdfView.openPdfFile(MainActivity.this, getString(R.string.app_name), "Do you want to open the pdf file?" + path, path);
                    }

                    @Override
                    public void failure() {
                        progressDialog.dismiss();

                    }
                });

            }
        });

    }

}
