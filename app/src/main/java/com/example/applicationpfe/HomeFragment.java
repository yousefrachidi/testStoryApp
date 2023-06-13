package com.example.applicationpfe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.applicationpfe.module.ChatGPTAPI;

import java.io.IOException;


public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    private void forTest() {
        ChatGPTAPI chatGPTAPI = new ChatGPTAPI();

        ChatGPTAPI.ResponseCallback responseCallback = new ChatGPTAPI.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                // Handle successful response
                System.out.println("API response: " + response);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                System.err.println("API error: " + errorMessage);
            }
        };

        String question = "Hi";
        chatGPTAPI.getResponse(question, responseCallback);
    }
}