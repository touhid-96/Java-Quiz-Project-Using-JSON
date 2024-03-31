import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    private static final JSONParser jsonParser = new JSONParser();
    protected static JSONArray readJsonArray(String fileLocation) throws IOException, ParseException {
        return (JSONArray) jsonParser.parse(new FileReader(fileLocation));
    }

    protected static void fileWrite(JSONArray jsonArray, String fileLocation) throws IOException {
        FileWriter fileWriter = new FileWriter(fileLocation);

        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();
        fileWriter.close();
    }
}
