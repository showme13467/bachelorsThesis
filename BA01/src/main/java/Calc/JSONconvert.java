package Calc;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONconvert {

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
}
