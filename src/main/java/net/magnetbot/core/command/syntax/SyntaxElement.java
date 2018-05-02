package net.magnetbot.core.command.syntax;
/*
    Created by nils on 02.04.2018 at 17:46.
    
    (c) nils 2018
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyntaxElement {

    private SyntaxElementType type;

    private String name;


    public SyntaxElement(String name, SyntaxElementType type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public SyntaxElementType getType() {
        return type;
    }

    @Override
    public String toString() {
        return getName() + " (" + getType() + ")";
    }

    public static class SubCommand extends SyntaxElement {
        List<String> possibilities;

        public SubCommand(String name, String... possibilities){
            super(name, SyntaxElementType.STRING_OF_LIST);
            this.possibilities = Arrays.asList(possibilities);
        }

        public List<String> getPossibilities() {
            return possibilities;
        }
    }

}
