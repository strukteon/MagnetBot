package core.files;
/*
    Created by nils on 03.01.2018 at 16:48.
    
    (c) nils 2018
*/

import core.tools.JsonFile;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class UserFile {
    private static final String dir = "users/";

    private final JsonFile file;

    public UserFile(User user){
        file = new JsonFile(dir + user.getId() + ".json");

        addFields();
    }

    private void addFields(){
        addField("registered", true);
        addField("msgs", 0);
        addField("xp", 0);
        addField("reputation", 100);
    }

    private void addField(String key, Object o){
        if (file.get(key) == null)
            file.set(key, o);
    }



    public UserFile addMessages(long n){
        file.set("msgs", (long) file.get("msgs", 0) + n);
        return this;
    }

    public UserFile setMessages(long n){
        file.set("msgs", n);
        return this;
    }

    public long getMessages(){
        return (long) file.get("msgs");
    }


    public UserFile addXp(long n){
        file.set("xp", (long) file.get("xp") + n);
        return this;
    }

    public UserFile setXp(long n){
        file.set("xp", n);
        return this;
    }

    public long getXp(){
        return (long) file.get("xp");
    }

    public long getLevel(){
        long xp = getXp();
        int level = 0;
        for (int i = 1; i*10 <= xp; i*= 2){
            level++;
        }
        return level;
    }

}
