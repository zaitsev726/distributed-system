import lombok.RequiredArgsConstructor;
import model.entity.NodeEntity;
import model.generated.Node;
import service.NodeService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class OsmController {
    private Long time = 0L;
    private final NodeService nodeService;

    public void handleOsm() throws IOException, XMLStreamException, JAXBException {

        try (XMLStreamProcessor processor =
                     new XMLStreamProcessor(Files.newInputStream(Paths.get("RU-NVS.osm")))) {
            XMLStreamReader reader = processor.getReader();

            JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            int count = 0;
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT && "node".equals(reader.getLocalName())) {
                    Node node = (Node) unmarshaller.unmarshal(reader);
                    if (count % 10000 == 0 && count != 0) {
                        System.out.println("Current input objects: " + count);
                        System.out.println("Current time in ms for 1 object: " + time.doubleValue() / count);
                        System.out.println("Current time: " + time);
                        System.out.println("-----------------------------------");
                    }
                    NodeEntity entity = NodeEntity.convert(node);

                    long cur = System.currentTimeMillis();
                    nodeService.putNodeWithPreparedStatement(entity);
                    cur = System.currentTimeMillis() - cur;
                    count++;

                    time += cur;

                    if (count == 1000000)
                        break;
                }
            }
        }
        System.out.println("TIME OF WORK :" + time);

    }

}

