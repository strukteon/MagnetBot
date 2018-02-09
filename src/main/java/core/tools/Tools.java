package core.tools;
/*
    Created by nils on 07.02.2018 at 02:21.
    
    (c) nils 2018
*/

public class Tools {

    public static String argsToString(String[] args, String filler){
        String out = "";
        for (String s : args){
            if (out != "")
                out += filler;
            out += s;
        }
        return out;
    }

    public static boolean isUrl(String url){
        return url.startsWith("http://") || url.startsWith("https://");
    }

}
