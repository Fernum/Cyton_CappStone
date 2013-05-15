
package robotinterface.parser;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * ReadPath Class.
 * <p>
 * @author Brian Bailey
 */
public class ReadPath {

    private StringBuffer accumulator = new StringBuffer();
    private ArrayList<Element> elements = new ArrayList<>();
    
    private Element element;    
    private boolean jointPosition;
    private int stateVectorSize, transitionPeriod;

    /**
     * Parse XML Saved Path File.
     */
    public static void parseConfig( )
    {
        ReadPath rPath = new ReadPath();
        File file = new File( "savedPath.xml" );
        rPath.readFile( file );
    }

    /**
     * Read and Parse XML Configuration File, for Robot.
     * <p>
     * @param file      Configuration File
     */
    public void readFile( File file )
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                @Override
                public void startElement( String uri, String localName, String qName, Attributes attributes )
                        throws SAXException
                {
                    accumulator.setLength( 0 );

                    if ( qName.equals( "stateVector" ) )
                    {
                        String size = attributes.getValue( "size" );
                        stateVectorSize = Integer.parseInt( size );
                    }
                    if ( qName.equals( "element" ) )
                    {
                        element = new Element();
                    }
                    if ( qName.equals( "mn:positionStates" ) )
                    {
                        jointPosition = true;
                        String size = attributes.getValue( "size" );
                    }
                    if ( qName.equals( "cr:orientation" ) )
                    {
                        String q0 = attributes.getValue( "q0" );
                        String q1 = attributes.getValue( "q1" );
                        String q2 = attributes.getValue( "q2" );
                        String q3 = attributes.getValue( "q3" );

                        element.orientation.put( "q0", Integer.parseInt( q0) );
                        element.orientation.put( "q1", Integer.parseInt( q1) );
                        element.orientation.put( "q2", Integer.parseInt( q2) );
                        element.orientation.put( "q3", Integer.parseInt( q3) );

                        // TODO: Create Orientation Object
                        //Orientation orientation = new Oriantation(q0, q1, q2, q3);
                    }
                    if ( qName.equals( "cr:translation" ) )
                    {
                        String x = attributes.getValue( "x" );
                        String y = attributes.getValue( "y" );
                        String z = attributes.getValue( "z" );

                        element.translation.put( "x", Integer.parseInt( x) );
                        element.translation.put( "y", Integer.parseInt( y) );
                        element.translation.put( "z", Integer.parseInt( z) );

                        // TODO: Create Translation Object
                        //Translation translation = new Translation(x, y, z);
                    }

                    if ( qName.equals( "mn:jointPositions" ) )
                    {
                        String size = attributes.getValue( "size" );
                        element.jointPositionsSize = Integer.parseInt( size );
                    }
                    if ( qName.equals( "mn:velocityStates" ) )
                    {
                        jointPosition = false;
                        String size = attributes.getValue( "size" );
                        element.velocityStatessize = Integer.parseInt( size );
                    }
                    if ( qName.equals( "mn:jointVelocities" ) )
                    {
                        String size = attributes.getValue( "size" );
                        element.jointVelocitiesSize  = Integer.parseInt( size );
                    }
                    if ( qName.equals( "cr:angular" ) )
                    {
                        String x = attributes.getValue( "x" );
                        String y = attributes.getValue( "y" );
                        String z = attributes.getValue( "z" );

                        element.angular.put( "x", Integer.parseInt( x) );
                        element.angular.put( "y", Integer.parseInt( y) );
                        element.angular.put( "z", Integer.parseInt( z) );

                        // TODO: Create Angular Object
                        // Angular angular = new Angular(x , y, z);
                    }
                    if ( qName.equals( "cr:linear" ) )
                    {
                        String x = attributes.getValue( "x" );
                        String y = attributes.getValue( "y" );
                        String z = attributes.getValue( "z" );

                        element.linear.put( "x", Integer.parseInt( x) );
                        element.linear.put( "y", Integer.parseInt( y) );
                        element.linear.put( "z", Integer.parseInt( z) );

                        // TODO: Create Linear Object
                        // Linear linear = new Linear(x , y, z);
                    }
                }

                @Override
                public void endElement( String uri, String localName, String qName )
                        throws SAXException
                {
                    if ( qName.equals( "mn:group" ) )
                    {
                        double[] group = Element.tokenizeString( accumulator.toString().trim() );
                        element.processGroup( jointPosition, group );
                    }
                    if ( qName.equals( "element" ) )
                    {
                        elements.add( element );
                    }
                    if ( qName.equals( "mn:positionStates" ) )
                    {
                        // TODO: Process mn:positionStates
                        //jointPosition = false;
                    }

                    if ( qName.equals( "mn:velocityStates" ) )
                    {
                        // TODO: Process mn:velocityStates
                        //jointPosition = true;
                    }
                    if ( qName.equals( "mn:element" ) )
                    {
                        // TODO: Process mn:element
                        //( mn:positionStates | mn:velocityStates )
                    }
                    if ( qName.equals( "n:time" ) )
                    {
                        String time = accumulator.toString().trim();
                        element.time = Integer.parseInt( time );
                    }
                    if ( qName.equals( "transitionPeriod" ) )
                    {
                        // TODO: Process transitionPeriod ;
                        String transition = accumulator.toString().trim();
                        transitionPeriod = Integer.parseInt( transition );
                    }
                    if ( qName.equals( "stateVector" ) )
                    {
                        // TODO: Vecctor Parsing Completed
                        // Add ArrayList and TransitionPeriod to StatePath Object
                    }
                    if ( qName.equals( "statePath" ) )
                    {
                        for(Element e : elements) { System.out.println(e); }
                    }
                }

                @Override
                public void characters( char buffer[], int start, int length )
                        throws SAXException
                {
                    accumulator.append( buffer, start, length );
                }
            };
            saxParser.parse( file, handler );
        }
        catch ( ParserConfigurationException | SAXException | IOException e ) {
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Element of XML File to PARSE.
     */
    private static class Element {

        int time;

        int positionStatesSize;
        HashMap<String,Integer> orientation = new HashMap<>(); //{ q0,1 q1,0 q2,0 q3,0 };
        HashMap<String,Integer> translation = new HashMap<>(); //{ x,0 y,0 z,0};

        int jointPositionsSize;
        double pGroup[] =
        {
            -1.5460338118980568, -0.5553132996753245, 0.0082015521738249154, 
            1.166425944665574, -1.0022368143111198, -0.061183890605378238, 
            -0.75202699689412533, 0.0047466666666666664, 0.0047466666666666664
        };

        int velocityStatessize;
        HashMap<String,Integer> angular = new HashMap<>(); //{ x,0 y,0 z,0 };
        HashMap<String,Integer> linear = new HashMap<>(); //{ x,0 y,0 z,0};
        int jointVelocitiesSize;
        double vGroup[] = {
            -2.2914324386003503e-005, -2.2918950300606066e-005, 7.5815506130804942e-006, 
            -3.6647705855406043e-005, 2.5723078511098777e-005, -5.2078377547634349e-005, 
            -4.7003797971624932e-005, 0, 0
        };

        public Element() { }


        /**
         * Process Group in XML File.
         * <p>
         * @param group     Is pGroup or vGroup Type.
         * @param gArray    Double Array of Group Values.
         */
        private void processGroup( boolean group, double[] gArray ) {
            for (int x = 0; x < gArray.length; x++) {
                if (group) { pGroup[x] = gArray[x]; }
                else { vGroup[x] = gArray[x]; }
            }
        }

        /**
         * Tokenize String in Double Array.
         * <p>
         * @param aString       String to Tokenize.
         * @return              Array of Doubles.
         */
        static private double[] tokenizeString( String aString ) {
            String[] tString = aString.split("\\s+");
            double dArray[] = new double[ tString.length ];

            for ( int x = 0; x < tString.length; x++ ) {
                dArray[x] = Double.parseDouble( tString[x] );
            }
            return dArray;
        }
        
        
        /**
         * Print Map.
         * <p>
         * @param map       HashMap
         * @return          String Representation of HashMap.
         */
        private String printMap(HashMap<String, Integer> map) {
           StringBuilder buf = new StringBuilder();
           buf.append( " { ");
           for (Entry<String, Integer> entry : map.entrySet()) {
               buf.append(" { ")
                  .append(entry.getKey()).append(" , ")
                  .append(entry.getValue()).append(" }");
           }
           return buf.append( "  } \n" ).toString();
        }
        
        
        /**
         * Print Array.
         * <p>
         * @param dArray        Double Array.
         * @return              String Representation of Double Array.
         */
        private String printArray( double[] dArray ) {
            StringBuilder buf = new StringBuilder();
            for ( double d : dArray ) {
                buf.append( " [ ").append( d ).append( " ] " );
            }
            return buf.append( "\n" ).toString();
        }
        
        @Override
        public String toString()
        {
            StringBuilder buf = new StringBuilder();
            buf.append( "Time: " ).append( time).append( "\n" );
            buf.append( "pGroup: " ).append( printArray( pGroup ) );
            buf.append( "vGroup: " ).append( printArray( vGroup) );
            buf.append( "orientation: " ).append( printMap(orientation) );
            buf.append( "translation: " ).append( printMap(translation) );
            buf.append( "angular: " ).append( printMap(angular) );
            buf.append( "linear: " ).append( printMap(linear) );

            return buf.toString();
        }
    }

}
