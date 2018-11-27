package com.example.evgeniy.mypizzamegaapp.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class GetterJSON extends AsyncTask<String, Void, String> {

    public interface onCompleteEventHandler {
        void onComplete(String Json);

        void onFail(String error);
    }

    private onCompleteEventHandler callBack;
    private ProgressBar pgLoading;
    private Button btn;
    private String LOG_TAG = "GetterJSON";
    private String requestMethod = "POST";
    private ErrorType errorType = ErrorType.NoError;

    private enum ErrorType {InternetError, TimeOutError, RequestError, NoError}

    public GetterJSON(ProgressBar pgLoading, Button btn, onCompleteEventHandler callBack, String requestMethod) {
        this.pgLoading = pgLoading;
        this.callBack = callBack;
        this.btn = btn;
        this.requestMethod = requestMethod.toUpperCase();
    }

    public GetterJSON(ProgressBar pgLoading, Button btn, onCompleteEventHandler callBack) {
        this.callBack = callBack;
        this.btn = btn;
        this.pgLoading = pgLoading;
    }

    public GetterJSON(onCompleteEventHandler callBack) {
        this.callBack = callBack;
        this.btn = null;
        this.pgLoading = null;

    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod.toUpperCase();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (pgLoading != null)
            pgLoading.setVisibility(View.VISIBLE);  //To show ProgressBar
        if (btn != null)
            btn.setEnabled(false);
    }

    @Override
    protected String doInBackground(String... strings) {
        String Url = strings[0];
        StringBuilder JSon = new StringBuilder();
        try {
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (requestMethod.equals("POST")) {
                String Params = strings[1];
                conn.setRequestProperty("Content-Length", Integer.toString(Params.getBytes().length));
                OutputStream os = conn.getOutputStream();
                byte[] data = Params.getBytes("UTF-8");
                os.write(data);
            }
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String s;
                while ((s = reader.readLine()) != null) {
                    JSon.append(s);
                }
            } else {
                Log.e(LOG_TAG, "responseCode = " + responseCode);
                errorType = ErrorType.RequestError;
                return null;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Ошибка " + Arrays.toString(e.getStackTrace()));
            Log.e(LOG_TAG, "doInBackground: " + e.getMessage());
            errorType = ErrorType.TimeOutError;
            return null;
        }
        errorType = ErrorType.NoError;
        return JSon.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (pgLoading != null)
            pgLoading.setVisibility(View.GONE);     // To Hide ProgressBar
        if (btn != null)
            btn.setEnabled(true);
        if (errorType == ErrorType.NoError)
            callBack.onComplete(s);
        else {
            callBack.onFail("Произошла ошибка типа " + errorType);
        }
    }
}
