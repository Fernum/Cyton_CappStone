package robotinterface.client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * RobotClient Class. <p>
 *
 * @author Brian Bailey
 */
public class RobotClient {

     private static final boolean VERBOSE = true;
    private static final String ROBOT_ADDRESS = "localhost";
    private static final int ROBOT_PORT = 2694;
    private static final int MAX_SIZE = 1024;
    private static final byte[] bytes = new byte[MAX_SIZE];
    private static ByteBuffer buffer = ByteBuffer.wrap(bytes);
    private static final byte[] rbytes = new byte[MAX_SIZE];
    private static ByteBuffer rBuffer = ByteBuffer.wrap(rbytes);
    private static boolean isRunning = false;
    private static SocketAddress rAddress;
    private static SocketChannel rChannel;
    private static RobotHandler rHandler;
    private static Thread runningTask;

    /**
     * RobotClient Default Constructor.
     */
    public RobotClient() {
    }

    /**
     * Start Client.
     */
    public static void startClient() {
        init();
        isRunning = true;
    }

    /**
     * Stop Client.
     */
    public static void stopClient() {
        close();
        isRunning = false;
    }

    /**
     * Initialize client to send data to server.
     */
    private static void init() {
        if (isRunning == true) {
            return;
        }
        if (VERBOSE) {
            System.out.println("[TCP Client Started]");
        }
        try {
            rAddress = new InetSocketAddress(ROBOT_ADDRESS, ROBOT_PORT);
            rChannel = SocketChannel.open();
            rChannel.connect(rAddress);
        } catch (UnknownHostException ex) {
            System.err.println("IP address of " + ROBOT_ADDRESS
                    + " could not be determined. Port: " + ROBOT_PORT);
        } catch (IOException ex) {
            System.err.println("Couldn't get I/O for the connection to: "
                    + ROBOT_ADDRESS + ". Port: " + ROBOT_PORT);
            System.err.println(ex.getMessage());
        }
        //EntryDAO.connect();
        startListening();
    }

    /**
     * Sends Message to Server containing Joint Array. <p>
     *
     * @param joints Array of joints angles.
     * @return Return message from server.
     */
    public static String sendMessage(double joints[]) {

        buffer.clear();
        if (rChannel == null) {
            System.err.println("TCP Connection Not Established.");
            return null;
        }

        try {
            /* This Takes double array and make it a string, stringing [ and ] */
            byte[] data = Arrays.toString(joints).replaceAll("[\\[\\]]", "").getBytes();
            if (VERBOSE) {
                System.out.println("Sending: \n  " + new String(data)
                        + "\nDestination: " + ROBOT_ADDRESS);
            }
            buffer.put(data).flip();

            if (rChannel != null && rChannel.write((ByteBuffer) buffer) != -1) {
                buffer.rewind();
                if (VERBOSE) {
                    System.out.println("Transmitted: "
                            + new String(buffer.array()).trim());
                }
                // TODO: Database Stuff
                //if(!EntryDAO.addEntry(new Entry(new String(buffer.array()).trim()))) {
                //    System.err.println("Failed to insert Entry into database");
                //}
            }

        } catch (IOException ex) {
            System.err.println("Couldn't write buffer to connection: "
                    + ROBOT_ADDRESS + ".");
        }
        return null;
    }

    /**
     * Send Message to Server. <p>
     *
     * @param aString Message to Send to server.
     * @return Return Message from Server.
     */
    public static String sendMessage(String aString) {

        buffer.clear();
        try {
            if (VERBOSE) {
                System.out.println("Sending: \n" + aString
                        + "\nDestination: " + ROBOT_ADDRESS);
            }
            String size = Integer.toString(aString.getBytes("UTF-8").length);
            if (size.length() < 4) {
                size = "0" + size;
            }
            aString = size + aString;
            buffer.put(ByteBuffer.wrap(aString.getBytes())).flip();

            if (rChannel != null && rChannel.write((ByteBuffer) buffer) != -1) {
                buffer.rewind();
                if (VERBOSE) {
                    System.out.println("Transmitted: "
                            + new String(buffer.array()).trim());
                }
                // TODO: Database Stuff
                //if(!EntryDAO.addEntry(new Entry(new String(buffer.array()).trim()))) {
                //    System.err.println("Failed to insert Entry into database");
                //}
            }
        } catch (IOException ex) {
            System.err.println("Couldn't write buffer to connection: " + ROBOT_ADDRESS + ".");
        }
        return null;
    }

    /**
     * Start Listening for Server Messages.
     */
    private static void startListening() {
        isRunning = true;
        if (VERBOSE) {
            System.err.println("RobotClient.startListening()");
        }
        runningTask = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    if (rChannel != null) {
                        readMessage();
                    }
                } while (isRunning);
            }
        });
        runningTask.start();
    }

    /**
     * Read Message from Server and fire Callback.
     */
    private static void readMessage() {
        try {
            rBuffer.clear();
            if (rChannel.read(rBuffer) > -1) {
                if (VERBOSE) {
                    System.out.println("Receiving Data from: "
                            + ROBOT_ADDRESS);
                }
                rBuffer.flip();
                rHandler.gotMessage(new String(rBuffer.array()).trim());
            }
        } catch (IOException ex) {
        }
    }

    /**
     * Closes Socket Channel
     */
    private static void close() {
        if (isRunning == false) {
            return;
        }
        if (VERBOSE) {
            System.out.println("[TCP Client Stopped]");
        }
        try {
            rChannel.close();
        } catch (IOException ex) { /* TODO: Catch Exception */ }
    }

    /**
     * Set RobotHandler for Client. <p>
     *
     * @param rHandler Client's RobotHandler.
     */
    public static void setHandler(RobotHandler rHandler) {
        RobotClient.rHandler = rHandler;
    }
}
