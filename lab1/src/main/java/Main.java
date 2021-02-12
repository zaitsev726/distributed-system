import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException, XMLStreamException {
        logger.info("---START---");
        logger.info("---END---");
        OsmController controller = new OsmController();
        controller.handleOsm();
    }
}
