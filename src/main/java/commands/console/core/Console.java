package commands.console.core;
/*
    Created by nils on 28.12.2017 at 15:33.
    
    (c) nils 2017
*/

public class Console {

    public static void info(String text){
        System.out.println(ConsoleColor.BLUE+ text + ConsoleColor.RESET);
    }

    public static void error(String text){
        System.out.println(ConsoleColor.RED + "Error: " + text + ConsoleColor.RESET);
    }

}
