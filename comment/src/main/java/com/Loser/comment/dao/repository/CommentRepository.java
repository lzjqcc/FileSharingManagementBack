package com.Loser.comment.dao.repository;

import com.Loser.comment.dao.domain.Comment;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    PageImpl<Comment> findByAppId(Integer appId, Pageable pageable);
}
