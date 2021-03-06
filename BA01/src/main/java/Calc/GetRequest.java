package Calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


// HTTP-GET-REQUEST
public class GetRequest {

    private static final String USER_AGENT = "Mozilla/5.0";


    public static String sendGET(String httplink, String urlParameters, String PostType) throws IOException {
        URL obj = new URL(httplink);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(PostType);
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            System.out.println("GET request not worked");
        }

        return "";
    }
}