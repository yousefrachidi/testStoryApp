package com.example.applicationpfe.module;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//private static final String API_KEY = "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";
import okhttp3.*;

public class ChatGPTAPI {
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/completions";
  //  private static final String API_URL = "https://api.openai.com/v1/engines/text-davinci-003/completions";

    public void getResponse(String question, final ResponseCallback callback) {
        String requestBody = "{\n" +
                "  \"model\": \"text-davinci-003\",\n" +
                "  \"prompt\": \"" + question + "\",\n" +
                "  \"temperature\": 0,\n" +
                "  \"max_tokens\": 100,\n" +
                "  \"top_p\": 1.0,\n" +
                "  \"frequency_penalty\": 0.2,\n" +
                "  \"presence_penalty\": 0.0,\n" +
                "  \"stop\": [\"\\n\"]\n" +
                "}";



        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(RequestBody.create(requestBody, JSON))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onError("API failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (responseBody != null) {
                    System.out.println("data: " + responseBody);
                } else {
                    System.out.println("data: empty");
                }

                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray jsonArray = jsonObject.getJSONArray("choices");
                    String textResult = jsonArray.getJSONObject(0).getString("text");
                    callback.onSuccess(textResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError("JSON parsing error");
                }
            }
        });
    }

    public interface ResponseCallback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }
}
