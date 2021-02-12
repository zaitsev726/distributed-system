import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OsmController {
    private Map<String, Integer> tagsInNodes = new HashMap<>();
    private Map<String, Integer> userNodes = new HashMap<>();

    public void handleOsm() throws IOException, XMLStreamException {

        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Files.newInputStream(Paths.get("RU-NVS.osm")))) {
            XMLStreamReader reader = processor.getReader();
            String userName = "";
            Integer nodes = 0;
            boolean opened = false;
            while (reader.hasNext()) {
                int event = reader.next();
                if(event == XMLEvent.START_ELEMENT && "tag".equals(reader.getLocalName()) && opened){
                    String value = reader.getAttributeValue(0);
                    if (!tagsInNodes.containsKey(value)) {
                        tagsInNodes.put(value, 1);
                    } else {
                        tagsInNodes.put(value, tagsInNodes.get(value) + 1);
                    }
                }
                if (event == XMLEvent.START_ELEMENT && "node".equals(reader.getLocalName())) {
                    opened = true;
                    if (userName.equals("")) {
                        userName = reader.getAttributeValue(4);
                    } else if (!userName.equals(reader.getAttributeValue(4))) {

                        if (!userNodes.containsKey(userName)) {
                            userNodes.put(userName, nodes);
                        } else {
                            userNodes.put(userName, userNodes.get(userName) + nodes);
                        }
                        userName = reader.getAttributeValue(4);
                        nodes = 0;
                    }
                    nodes++;
                }
                if(event == XMLEvent.END_ELEMENT && "node".equals(reader.getLocalName())){
                    opened = false;
                }
            }
            Map<String, Integer> sorted = userNodes.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            for (Map.Entry<String, Integer> entry : sorted.entrySet())
                System.out.printf("#1 Username: %s count of user changes: %s%n", entry.getKey(), entry.getValue());


            Map<String, Integer> sortedTagsMap = tagsInNodes.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            for (Map.Entry<String, Integer> entry : sortedTagsMap.entrySet())
                System.out.printf("#2 Count of TAG [%s] in nodes: %s%n", entry.getKey(), entry.getValue());
        }

    }
}
