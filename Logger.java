/**
* Unified logging system that allows all logs to be easily turned off or on.
*/
public class Logger{
    private static boolean DEBUG = false; // Set this to false when submitting the release!
    /**
    * If debugging, log output to the console. Otherwise, do nothing.
    */
    public static void log(String text){
        if(DEBUG){
            System.out.println(text);
        }
    }

    /**
    * Overloaded logging method with exception input
    */
    public static void log(Exception e){
        if(DEBUG){
            e.printStackTrace();
        }
    }

    /**
    * Overloaded logging method with empty input
    */
    public static void log(){
        log("");
    }
}
