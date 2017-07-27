package main;

public class Debug
{
    private static boolean enabled = false;

    public static void log(Object o) {
        if (enabled) {
            System.err.println(o.toString());
        }
    }

    public static void enable() { enabled = true; }

    public static void disable() { enabled = false; }
}
