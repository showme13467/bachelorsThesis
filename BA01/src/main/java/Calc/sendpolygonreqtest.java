package Calc;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class sendpolygonreqtest {
    static JsonParser jsonParser = new JsonParser();

    public static void main(String[] args) throws IOException{

        String PolygonID = "";

        JsonObject SendPolygonJsonObj = new JsonObject();

        String coordinatesofpolygon = "";

        SendPolygonJsonObj.addProperty("coordinates", coordinatesofpolygon);
        SendPolygonJsonObj.addProperty("name", "testooooo");
        SendPolygonJsonObj.addProperty("floor", "5de68ab0756b75438fbec5f8");

        StringEntity SendPolygonentityJson = new StringEntity(SendPolygonJsonObj.toString(),
                ContentType.APPLICATION_JSON);

        HttpClient httpClientSendPolygon = HttpClientBuilder.create().build();
        HttpPost requestSendPolygon = new HttpPost("http://irt-ap.cs.columbia.edu/api/room/");
        requestSendPolygon.setEntity(SendPolygonentityJson);

        HttpResponse responseSendPolygon = null;
        try {
            responseSendPolygon = httpClientSendPolygon.execute(requestSendPolygon);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String SendPolygonhttpanswer = null;
        try {
            SendPolygonhttpanswer = EntityUtils.toString(responseSendPolygon.getEntity());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(SendPolygonhttpanswer);

        JsonObject JsonObjectSendPolygonArray = jsonParser.parse(SendPolygonhttpanswer).getAsJsonObject();

        System.out.println(JsonObjectSendPolygonArray.get("id").toString());

    }
}