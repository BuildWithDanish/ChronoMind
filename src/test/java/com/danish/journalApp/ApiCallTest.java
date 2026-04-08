package com.danish.journalApp;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class ApiCallTest {

    public static void main(String[] args) {


        // The client gets the API key from the environment variable `GEMINI_API_KEY`.
        Client client = new Client.Builder().apiKey("AIzaSyBT62htdW-djNejE3Q2JQEUudxCpNSuo4k").build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3-flash-preview",
                        "hii how are you doing?",
                        null);

        System.out.println(response.text());
    }
}
