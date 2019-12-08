package com.Lowser.personalAsserts.comment.controller;

import com.Lowser.personalAsserts.comment.controller.param.CommentParam;
import com.Lowser.personalAsserts.comment.controller.result.CommentResult;
import com.Lowser.personalAsserts.comment.dao.domain.Comment;
import com.Lowser.personalAsserts.comment.dao.repository.CommentRepository;
import com.Lowser.common.utils.ModelUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping(name = "/comment")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @GetMapping(value = "/get/{appId}")
    public Object getComment(Integer appId, Pageable pageable) {
        PageImpl<Comment> page = commentRepository.findByAppId(appId, pageable);
        List<Comment> parentComment = filterComment(page.getContent(), comment -> !comment.getDelete() && comment.getParentId() == 0);
        List<Comment> childComments = filterComment(page.getContent(), comment -> !comment.getDelete() && comment.getParentId() != 0);
        Map<Integer, List<Comment>> parentIdAndCommentMap = childComments.stream().collect(Collectors.groupingBy(Comment::getParentId, Collectors.toList()));
        List<CommentResult> parentResult = ModelUtils.toTargets(parentComment, CommentResult.class);
        for (CommentResult commentResult : parentResult) {
            commentResult.setChilds(ModelUtils.toTargets(parentIdAndCommentMap.get(commentResult.getId()), CommentResult.class));
        }
        return parentResult;
    }
    private List<Comment> filterComment(List<Comment> comments, Predicate<Comment> commentPredicate) {
        return comments.stream().filter(commentPredicate).collect(Collectors.toList());
    }
    @PostMapping(value = "/add")
    public void addComment(CommentParam commentParam) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentParam, comment);
        comment.setDelete(false);
        comment.setInsertTime(new Date());
        commentRepository.save(comment);
    }
}
