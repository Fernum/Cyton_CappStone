package robotinterface.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * ReadConfig Class.
 * <p>
 * @author Brian Bailey
 */
public class ReadConfig {

    private StringBuffer accumulator = new StringBuffer();        
    private HardwareConfigBaseVector HardwareConfigBaseVector;
    private GripperIndexVector GripperIndexVector;
    private RobotisHardwareConfig HardwareConfig;
    private RoboticPluginData RobotisPluginData;
    

    /**
     * Parse XML Configuration File.
     */
    public static void main(String[] args) {
        ReadConfig rConfig = new ReadConfig();
        File file = new File("cytonConfig.xml");
        rConfig.readFile(file);
    }

    
    /**
     * Read and Parse XML Configuration File, for Robot.
     * <p>
     * @param file      Configuration File
     */
    public void readFile(File file) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes)
                        throws SAXException {
                    accumulator.setLength(0);

                    if (qName.equals("RobotisPluginData")) {
                        RobotisPluginData = new RoboticPluginData();
                    }
                    if (qName.equals("RobotisHardwareConfig")) {
                        HardwareConfig = new RobotisHardwareConfig();
                    }
                    if (qName.equals("HardwareConfigBaseVector")) {
                        HardwareConfigBaseVector = new HardwareConfigBaseVector(attributes.getValue("size"));
                    }
                    if (qName.equals("gripperIndexVector")) {
                        GripperIndexVector = new GripperIndexVector(attributes.getValue("size"));
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName)
                        throws SAXException {

                    if (qName.equals("RobotisPluginData")) {
                        System.out.print(RobotisPluginData);
                    }
                    if (qName.equals("HardwareConfigBaseVector")) {
                        RobotisPluginData.HardwareConfigBaseVector = HardwareConfigBaseVector;
                    }
                    if (qName.equals("RobotisHardwareConfig")) {
                        HardwareConfigBaseVector.addHardware(HardwareConfig);
                    }
                    if (qName.equals("gripperIndexVector")) {
                        RobotisPluginData.GripperIndexVector = GripperIndexVector;
                    }
                    if (qName.equals("ccwAngleLimit")) {
                        HardwareConfig.ccwAngleLimit = accumulator.toString().trim();
                    }
                    if (qName.equals("ccwComplianceMargin")) {
                        HardwareConfig.ccwComplianceMargin = accumulator.toString().trim();
                    }
                    if (qName.equals("ccwComplianceSlope")) {
                        HardwareConfig.ccwComplianceSlope = accumulator.toString().trim();
                    }
                    if (qName.equals("cwAngleLimit")) {
                        HardwareConfig.cwAngleLimit = accumulator.toString().trim();
                    }
                    if (qName.equals("cwComplianceMargin")) {
                        HardwareConfig.cwComplianceMargin = accumulator.toString().trim();
                    }
                    if (qName.equals("cwComplianceSlope")) {
                        HardwareConfig.cwComplianceSlope = accumulator.toString().trim();
                    }
                    if (qName.equals("hardwareToActinFactor")) {
                        HardwareConfig.hardwareToActinFactor = accumulator.toString().trim();
                    }
                    if (qName.equals("hardwareVelocityToActinFactor")) {
                        HardwareConfig.hardwareVelocityToActinFactor = accumulator.toString().trim();
                    }
                    if (qName.equals("invert")) {
                        HardwareConfig.invert = accumulator.toString().trim();
                    }
                    if (qName.equals("jointMapIndex")) {
                        HardwareConfig.jointMapIndex = accumulator.toString().trim();
                    }
                    if (qName.equals("maxAccelerationInHWUnits")) {
                        HardwareConfig.maxAccelerationInHWUnits = accumulator.toString().trim();
                    }
                    if (qName.equals("maxVelocityInHWUnits")) {
                        HardwareConfig.maxVelocityInHWUnits = accumulator.toString().trim();
                    }
                    if (qName.equals("modelNumber")) {
                        HardwareConfig.modelNumber = accumulator.toString().trim();
                    }
                    if (qName.equals("offsetValueInHWUnits")) {
                        HardwareConfig.offsetValueInHWUnits = accumulator.toString().trim();
                    }
                    if (qName.equals("punch")) {
                        HardwareConfig.punch = accumulator.toString().trim();
                    }
                    if (qName.equals("servoID")) {
                        HardwareConfig.servoID = accumulator.toString().trim();
                    }
                    if (qName.equals("group")) {
                        GripperIndexVector.group = accumulator.toString().trim();
                    }
                    if (qName.equals("initAndShutdownMode")) {
                        RobotisPluginData.initAndShutdownMode = accumulator.toString().trim();
                    }
                    if (qName.equals("manipIndex")) {
                        RobotisPluginData.manipIndex = accumulator.toString().trim();
                    }
                    if (qName.equals("maxSetupVelocityInHWUnits")) {
                        RobotisPluginData.maxSetupVelocityInHWUnits = accumulator.toString().trim();
                    }
                    if (qName.equals("port")) {
                        RobotisPluginData.port = accumulator.toString().trim();
                    }
                    if (qName.equals("statusUpdateInMs")) {
                        RobotisPluginData.statusUpdateInMs = accumulator.toString().trim();
                    }
                    if (qName.equals("timeoutInMS")) {
                        RobotisPluginData.timeoutInMS = accumulator.toString().trim();
                    }
                }

                @Override
                public void characters(char buffer[], int start, int length)
                        throws SAXException {
                    accumulator.append(buffer, start, length);
                }
            };
            saxParser.parse(file, handler);
        } 
        catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println(e.getStackTrace());
        }
    }
}

/**
 * RobotisHardwareConfig Class
 */
class RobotisHardwareConfig {

    String baseVector;
    String gripperIndexVector;
    String ccwAngleLimit;
    String ccwComplianceMargin;
    String ccwComplianceSlope;
    String cwAngleLimit;
    String cwComplianceMargin;
    String cwComplianceSlope;
    String hardwareToActinFactor;
    String hardwareVelocityToActinFactor;
    String invert;
    String jointMapIndex;
    String maxAccelerationInHWUnits;
    String maxVelocityInHWUnits;
    String modelNumber;
    String offsetValueInHWUnits;
    String punch;
    String servoID;

    @Override
    public String toString() {
        return "\n  [RobotisHardwareConfig]"
                + "\n    ccwAngleLimit: " + ccwAngleLimit
                + "\n    ccwComplianceMargin: " + ccwComplianceMargin
                + "\n    ccwComplianceSlope: " + ccwComplianceSlope
                + "\n    cwAngleLimit: " + cwAngleLimit
                + "\n    cwComplianceMargin: " + cwComplianceMargin
                + "\n    cwComplianceSlope: " + cwComplianceSlope
                + "\n    hardwareToActinFactor: " + hardwareToActinFactor
                + "\n    hardwareVelocityToActinFactor: " + hardwareVelocityToActinFactor
                + "\n    invert: " + invert
                + "\n    jointMapIndex: " + jointMapIndex
                + "\n    maxAccelerationInHWUnits: " + maxAccelerationInHWUnits
                + "\n    maxVelocityInHWUnits: " + maxVelocityInHWUnits
                + "\n    modelNumber: " + modelNumber
                + "\n    offsetValueInHWUnits: " + offsetValueInHWUnits
                + "\n    punch: " + punch
                + "\n    servoID: " + servoID;
    }
}


/**
 * GripperIndexVector Class.
 */
class GripperIndexVector {

    String group;
    String size;

    public GripperIndexVector(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "  [GripperIndexVector]"
                + "\n    Size: " + size
                + "\n    Group: " + group;
    }
}


/**
 * HardwareConfigBaseVector Class.
 */
class HardwareConfigBaseVector {

    ArrayList<RobotisHardwareConfig> list = new ArrayList<>();
    int size;

    public HardwareConfigBaseVector(String size) {
        this.size = Integer.parseInt(size);
    }

    public void addHardware(RobotisHardwareConfig RobotisHardwareConfig) {
        list.add(RobotisHardwareConfig);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("  [HardwareConfigBaseVector]")
                .append("\n  Size: ").append(size);
        for (RobotisHardwareConfig r : list) {
            buf.append(r.toString());
        }
        buf.append("\n  [HardwareConfigBaseVector]");
        return buf.toString();
    }
}

/**
 * RoboticPluginData Class.
 */
class RoboticPluginData {

    HardwareConfigBaseVector HardwareConfigBaseVector;
    GripperIndexVector GripperIndexVector;
    String initAndShutdownMode;
    String manipIndex;
    String maxSetupVelocityInHWUnits;
    String port;
    String statusUpdateInMs;
    String timeoutInMS;

    @Override
    public String toString() {
        System.out.println("[RobotisPluginData]");
        System.out.println(HardwareConfigBaseVector);
        System.out.print(GripperIndexVector);
        return "\n  initAndShutdownMode: " + initAndShutdownMode
                + "\n  manipIndex: " + manipIndex
                + "\n  maxSetupVelocityInHWUnits: " + maxSetupVelocityInHWUnits
                + "\n  port: " + port
                + "\n  statusUpdateInMs: " + statusUpdateInMs
                + "\n  timeoutInMS: " + timeoutInMS
                + "\n[RobotisPluginData]\n";
    }
}