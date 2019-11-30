package Calc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

import static Calc.JSONconvert.writeJsonSimpleDemo;

public class JSONmain {

    public static void main(String[] args) throws Exception {
        writeJsonSimpleDemo("example.json");
        JSONObject jsonObject = (JSONObject) readJsonSimpleDemo("example.json");
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("floor"));
    }

    public static Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
    }

