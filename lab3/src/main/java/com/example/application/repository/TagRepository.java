package com.example.application.repository;

import com.example.application.model.entity.TagEntity;
import com.example.application.model.entity.TagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, TagId> {
}
