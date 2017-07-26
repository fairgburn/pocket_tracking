package main;

import java.util.HashMap;

public class Arguments
{

    private HashMap<String, String> argList = new HashMap<>();

    public Arguments(String[] args) {
        for (int i = 0; i < args.length; i++) {

            // argument variables start with a dash, values are listed immediately after
            if (args[i].charAt(0) == '-' && i != (args.length - 1)) {
                argList.put(args[i], args[i+1]);
            }

        }

    }

    public String get(String key) {
        return (argList.containsKey(key)) ? argList.get(key) : null;
    }

    public boolean contains(String key) {
        return argList.containsKey(key);
    }

}
