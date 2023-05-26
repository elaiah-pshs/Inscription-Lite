package models;

/**
 * <code>App</code> represents the running instance of this application. Currently, it only contains a static <code>Session</code> object.
 */
public class App {
    /** 
     * The session assosciated with the running instance of this application.
     */
    private static Session session = new Session();
    
    /** 
     * Returns the session assosciated with the running instance of this application.
     * 
     * @return      the value of the <code>session</code> field.
     */
    public static Session getSession() {
        return session;
    }
}
