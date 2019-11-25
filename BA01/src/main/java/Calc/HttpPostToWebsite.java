package Calc;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    public class HttpPostToWebsite {

        // one instance, reuse
        private final CloseableHttpClient httpClient = HttpClients.createDefault();

        public static void main(String[] args) throws Exception {

            HttpPostToWebsite obj = new HttpPostToWebsite();

            try {
                System.out.println("Testing 1 - Send Http GET request");
                obj.sendGet();

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
            HttpGet request = new HttpGet("http://irt-ap.cs.columbia.edu");


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

            HttpPost post = new HttpPost("http://irt-ap.cs.columbia.edu");
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



            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {

                System.out.println(EntityUtils.toString(response.getEntity()));
            }

        }

    }
