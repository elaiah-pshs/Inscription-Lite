package models.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <code>StyleProcessor</code> is in charge of processing the styles of the application before it runs. All of the methods below should be called in the main class' runner function before the launch command.
 */
public class StyleProcessor {
    /** 
     * The general font size of the application.
     */
    private static int font_size = 16;

    
    /** 
     * Returns a the general font size of the application.
     * 
     * @return              the value of <code>font_size</code>.
     */
    public static int getFontSize() {
        return font_size;
    }

    /** 
     * Compiles all Sass styles into CSS.
     */
    public static void compile() {
        LogProcessor.start("StyleProcessor", "compile");

        LogProcessor.log("Initializing batch script");
        ProcessBuilder processBuilder = new ProcessBuilder("./src/scripts/compile.bat");
        	
        try {
            LogProcessor.log("Running batch script");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                LogProcessor.success(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
