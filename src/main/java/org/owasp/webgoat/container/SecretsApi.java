package org.owasp.webgoat.container;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Base64; 

public class SecretsApi {
    private static final String apiUrl = "http://etzel.esy.es/webgoat/secrets.php"; 

    public static String getSecret(String targetCredentialName) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String cred = jsonObject.getString("cred");

                    if (cred.equals(targetCredentialName)) {
                        String encodedValue = jsonObject.getString("value");
                        return decodeBase64(encodedValue);
                    }
                }

                // If the credential with the given name is not found
                return null;
            } else {
                System.out.println("API request failed with response code: " + responseCode);
                return null; // Handle the error case accordingly
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception accordingly
        }
    }
    
    private static String decodeBase64(String encodedValue) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedValue);
        return new String(decodedBytes);
    }
}