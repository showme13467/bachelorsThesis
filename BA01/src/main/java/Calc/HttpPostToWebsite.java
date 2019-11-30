package Calc;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static Calc.JSONconvert.writeJsonSimpleDemo;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

    public class HttpPostToWebsite {

        public static JSONObject jsonObject = new JSONObject();

        // one instance, reuse
        private final CloseableHttpClient httpClient = HttpClients.createDefault();

        public static void main(String[] args) throws Exception {

            HttpPostToWebsite obj = new HttpPostToWebsite();

         /*   writeJsonSimpleDemo("example.json");
            jsonObject = (JSONObject) readJsonSimpleDemo("example.json");
            System.out.println(jsonObject);
            String test = jsonObject.get("floor").toString();
            System.out.println(test);
            System.out.println(jsonObject.get("floor"));

*/
            try {
              /*  System.out.println("Testing 1 - Send Http GET request");
                obj.sendGet();
*/
                System.out.println("Testing 2 - Send Http POST request");
                obj.sendPost();
            } finally {
                obj.close();
            }
        }

        private void close() throws IOException {
            httpClient.close();
        }

        private void sendGet() throws Exception {

            //HttpGet request = new HttpGet("https://www.google.com/search?q=mkyong");
            HttpGet request = new HttpGet("http://irt-ap.cs.columbia.edu/api/create_room/");


            // add request headers
            request.addHeader("custom-key", "mkyong");
            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

            try (CloseableHttpResponse response = httpClient.execute(request)) {

                // Get HttpResponse Status
                System.out.println(response.getStatusLine().toString());

                HttpEntity entity = response.getEntity();
                Header headers = entity.getContentType();
                System.out.println(headers);

                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                }

            }

        }

        private void sendPost() throws Exception {

            HttpPost post = new HttpPost("http://irt-ap.cs.columbia.edu/api/create_room/");
            //HttpPost post = new HttpPost("https://httpbin.org/post");

            // add request parameter, form parameters
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("latitude", "0.000000"));
            urlParameters.add(new BasicNameValuePair("longitude", "0.000000"));
            urlParameters.add(new BasicNameValuePair("height", "0.00"));
            urlParameters.add(new BasicNameValuePair("name", "default"));
            urlParameters.add(new BasicNameValuePair("type", "default"));
            urlParameters.add(new BasicNameValuePair("floor", "7"));
            urlParameters.add(new BasicNameValuePair("room", "IRT-Lab"));

          // urlParameters.add(new BasicNameValuePair());

            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            System.out.println();

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {

                System.out.println(EntityUtils.toString(response.getEntity()));

            }

        }
/*

        public static JSONObject writeJsonSimpleDemo(String filename) throws Exception {
            JSONObject sampleObject = new JSONObject();
            sampleObject.put("name", "IRTLab");
            sampleObject.put("floor", 8);
            sampleObject.put("building", "Shapiro Center");

            JSONArray coordinates = new JSONArray();


            sampleObject.put("coordinates", coordinates);
            Files.write(Paths.get(filename), sampleObject.toJSONString().getBytes());
            return sampleObject;
        }


        public static Object readJsonSimpleDemo(String filename) throws Exception {
            FileReader reader = new FileReader(filename);
            JSONParser jsonParser = new JSONParser();
            return jsonParser.parse(reader);
        }
        */
    }





