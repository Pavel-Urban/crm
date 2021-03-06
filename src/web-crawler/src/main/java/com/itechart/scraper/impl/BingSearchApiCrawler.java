package com.itechart.scraper.impl;

import com.itechart.scraper.Crawler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

public class BingSearchApiCrawler implements Crawler {
    private static final String API_KEY = "5hqj2uxuSfIKqheVZCK17dXoPaW4ctgNja5a8pSc9YY";
    private String fileName = "./bing_profiles.txt";

    public void crawl(String fileName) throws IOException {
        if (!Objects.isNull(fileName)) this.fileName = fileName;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.fileName))) {
            for (int i = 0; i < 499; i ++) {
                fetchProfiles(i * 50, 50, writer);
            }
        }
    }

    private void fetchProfiles(int skip, int top, BufferedWriter writer) throws IOException {
        String bingUrlPattern = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%%27%s%%27&$format=JSON&$skip=" + skip + "&$top=" + top;

        String query = URLEncoder.encode("site:www.linkedin.com See who you know in common “location * London”", Charset.defaultCharset().name());
        String bingUrl = String.format(bingUrlPattern, query);

        String accountKeyEnc = Base64.getEncoder().encodeToString((API_KEY + ":" + API_KEY).getBytes());

        URL url = new URL(bingUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            JSONObject json = new JSONObject(response.toString());
            JSONObject d = json.getJSONObject("d");
            JSONArray results = d.getJSONArray("results");
            int resultsLength = results.length();
            for (int i = 0; i < resultsLength; i++) {
                JSONObject aResult = results.getJSONObject(i);
                System.out.println(aResult.get("Url"));
                writer.write(aResult.get("Url") + "\n");
            }

        }
    }
}
