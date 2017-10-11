package org.ftc7244.datalogger;

import org.ftc7244.robotcontroller.Debug;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by BeaverDuck on 10/8/17.
 */

public class Logger implements Runnable{
    /**
     * Logger sends sets of data from the used android device to a computer on the receiving port
     * hosting the logger program.
     */
    private static final Logger instance = new Logger();

    private static final String INET_ADDRESS = "";

    private static final int PORT = 0, FIGURES_AFTER_DECIMAL = 4;

    private static final long SEND_INTERVAL_MS = 100;

    private HashMap<String, ArrayList<Double>> data;

    private Thread thread;

    private Socket logger;

    private PrintStream out;

    private boolean running;

    public static Logger getInstance() {
        return instance;
    }

    private Logger() {
        if(Debug.STATUS) {
            running = true;

            data = new HashMap<>();

            try {
                logger = new Socket(InetAddress.getByName(INET_ADDRESS), PORT);
                out = new PrintStream(logger.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     *
     * @param tag identifier for the data
     * @param data data point being added
     * @throws InvalidCharacterException if tag contains ":", which is used for parsing on the
     * receiving end
     */
    public void addData(String tag, double data) {
        if(this.data.containsKey(tag)) {
            this.data.get(tag).add(data);
        }
        else {
            if(tag.contains(":")){
                throw new InvalidCharacterException("Tag cannot contain \":\"");
            }
            this.data.put(tag, new ArrayList<>(Collections.singletonList(data)));
        }
    }

    @Override
    public void run() {
        while (running){
            try {
                Thread.sleep(SEND_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(String key : data.keySet()){
                out.print(generateOutput(key));
            }
        }
    }

    /**
     *
     * @param key data identifier to reference when generating string
     * @return string containing identifier key and corresponding data points
     */
    private String generateOutput(String key){
        String out = key + ":";
        for(Double num : data.get(key)){
            out += truncate(num + "") + ":";
        }
        return out;
    }

    /**
     * shortens length of sent data string to avoid long messages due to floating point inaccuracy
     * @param raw the raw, unshortened data
     * @return shortened data string
     */

    private String truncate(String raw){
        return raw.substring(0, raw.indexOf('.')+FIGURES_AFTER_DECIMAL);
    }
}