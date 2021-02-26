package com.example.application.model;

import com.example.application.model.dto.NodeDto;
import com.example.application.model.entity.NodeEntity;
import com.example.application.model.entity.TagEntity;
import com.example.application.model.generated.Node;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Converter {
    public static NodeEntity convertNodeToNodeEntity(Node node) {
        List<TagEntity> tags = node.getTag()
                .stream()
                .map(tag -> convertTagToTagEntity(tag, node.getId()))
                .collect(Collectors.toList());

        return new NodeEntity(
                node.getId().longValue(),
                node.getUser(),
                node.getLon(),
                node.getLat(),
                tags
        );
    }

    private static TagEntity convertTagToTagEntity(Node.Tag tag, long nodeId) {
        return new TagEntity(
                nodeId,
                tag.getK(),
                tag.getV()
        );
    }
    private static TagEntity convertTagToTagEntity(String key, String value, long nodeId) {
        return new TagEntity(
                nodeId,
                key,
                value
        );
    }

    public static NodeEntity convertNodeDtoToNodeEntity(NodeDto nodeDto){
        List<TagEntity> tags = nodeDto.getTags().entrySet()
                .stream()
                .map(tag -> convertTagToTagEntity(
                        tag.getKey(),
                        tag.getValue(),
                        nodeDto.getId()))
                .collect(Collectors.toList());
        return new NodeEntity(
                nodeDto.getId(),
                nodeDto.getName(),
                nodeDto.getLongitude(),
                nodeDto.getLatitude(),
                tags
        );
    }

    public static NodeDto convertNodeEntityToNodeDto(NodeEntity nodeEntity){
        Map<String, String> tags = nodeEntity.getTags()
                .stream()
                .collect(Collectors
                        .toMap(TagEntity::getKey, TagEntity::getValue));
        return new NodeDto(
                nodeEntity.getId(),
                nodeEntity.getUser(),
                nodeEntity.getLongitude(),
                nodeEntity.getLatitude(),
                tags
        );
    }
}


