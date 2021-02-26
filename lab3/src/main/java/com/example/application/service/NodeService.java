package com.example.application.service;

import com.example.application.model.entity.NodeEntity;
import com.example.application.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class NodeService {
    private final NodeRepository nodeRepository;

    public NodeEntity saveNodeEntity(NodeEntity node) {
        return nodeRepository.save(node);
    }

    public NodeEntity getNodeEntityById(Long id) {
        return nodeRepository
                .findById(id)
                .orElseThrow(NullPointerException::new);
    }

    public NodeEntity updateNodeEntity(Long id, NodeEntity node) {
        NodeEntity nodeFromDb = nodeRepository
                .findById(id)
                .orElseThrow(NullPointerException::new);

        node.setId(nodeFromDb.getId());
        return nodeRepository.save(node);
    }

    public void deleteNodeEntity(long id) {
        NodeEntity node = nodeRepository
                .findById(id)
                .orElseThrow(NullPointerException::new);
        nodeRepository.delete(node);
    }

    public List<NodeEntity> searchNearNodes(Double latitude, Double longitude, Double radius) {
        return nodeRepository.getNearNodes(latitude, longitude, radius);
    }

}
