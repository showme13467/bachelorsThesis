package Calc;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class qwert {
    public static void main(String[] args) throws Exception {
        String payload = "{" +
                "\"coordinates\": [[" +
                "[73.8, 40.3]" +
                "]]"+ ","+
                "\"name\": \"IRT-Lab\", " +
                "\"floor\": \"7th Floor\""+
                "}";
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://irt-ap.cs.columbia.edu/api/create_user/");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}