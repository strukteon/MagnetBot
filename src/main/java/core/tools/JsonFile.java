package core.tools;

/*
    Created by nils on 02.01.2018 at 21:32.
    
    (c) nils 2018
*/


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class JsonFile {
    String path;
    JSONObject main;

    public JsonFile(String path) {
        path = path;
        this.path = path;
        try {
            File file = new File(path);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                save(new JSONObject());
            }
            main = (JSONObject) new JSONParser().parse(new FileReader(path));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void set(String key, Object value){
        try {
            main.put(key, value);
            save(main);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Object get(String key, Object filler){
        if (main.containsKey(key))
            return main.get(key);
        return filler;
    }

    public Object get(String key){
        return get(key, null);
    }

    private void save(JSONObject object){
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(){
        File file = new File(path);
        file.delete();
    }

}
