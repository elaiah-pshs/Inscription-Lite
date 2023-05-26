package models.processors;

/**
 * <code>LogProcessor</code> contains functions to log events during this app's startup.
 */
public class LogProcessor {
    /** 
     * Prints the startup message for the project.
     */
    public static void start() {
        System.out.println("Inscryption Lite");
        System.out.println("A Computer Science 4 Project by Asperin, Co, & Kasilag");
        System.out.println("=".repeat(80));
    }

    /** 
     * Prints the startup message for a method.
     * 
     * @param className     the name of the class containing the method that was ran.
     * @param methodName    the name of the method that was ran.
     */
    public static void start(String className, String methodName) {
        System.out.println(className + "." + methodName + "()");
        System.out.println("=".repeat(80));
    }

    /** 
     * Logs a normal message.
     * 
     * @param message       the message to print.
     */
    public static void log(String message) {
        System.out.println("LOG: " + message);
    }

    /** 
     * Logs a success message.
     * 
     * @param message       the message to print.
     */
    public static void success(String message) {
        System.out.println("SUCCESS: " + message + "!");
    }

    /** 
     * Logs an error message.
     * 
     * @param message       the message to print.
     */
    public static void error(String message) {
        System.out.println("ERROR: " + message + "!");
    }
}
