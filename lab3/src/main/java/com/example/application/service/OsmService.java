package com.example.application.service;

import com.example.application.model.Converter;
import com.example.application.model.entity.NodeEntity;
import com.example.application.model.generated.Node;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class OsmService {
    private final NodeService nodeService;

    public OsmService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

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
                    if (count % 1000 == 0 && count != 0) {
                        System.out.println("Current input objects: " + count);
                        System.out.println("-----------------------------------");
                    }
                    NodeEntity entity = Converter.convertNodeToNodeEntity(node);
                    nodeService.saveNodeEntity(entity);
                    count++;
                    if (count == 50000)
                        break;
                }
            }
        }

    }

}

