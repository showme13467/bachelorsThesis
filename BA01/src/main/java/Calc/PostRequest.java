package Calc;

import com.google.gson.*;

import java.io.*;
import java.net.*;


class Requests {
    public static String executePost(String targetURL, String urlParameters, String PostType) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(PostType);
            connection.setRequestProperty("Content-Type",
                    "application/JSON");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void main(String[] args){
        Requests x = new Requests();
        String Response =
                x.executePost(
                        "http://irt-ap.cs.columbia.edu/api/buildings",
                        "{\n"
                                + " \"name\" : \"test\",\n"
                                + "  \"coordinates\": [[234,3422],[23123,244]]\n"
                                + "}", "POST");


        JsonObject jsonObject = new JsonParser().parse(Response).getAsJsonObject();
        JsonArray test2 = jsonObject.getAsJsonArray("data");
        System.out.println(test2);
        System.out.println(test2.get(0).getAsJsonObject().get("name"));

    }





}