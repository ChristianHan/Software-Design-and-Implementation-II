package assignment3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public class Word {
    private String name;
    private Word parent;


    public Word(String name, Word parent){
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Word getParent() {
        return parent;
    }
}
