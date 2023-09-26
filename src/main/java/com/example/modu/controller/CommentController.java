package com.example.modu.controller;

import com.example.modu.dto.comment.CommentRequestDto;
import com.example.modu.dto.user.StatusResponseDto;
import com.example.modu.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    
    //@AuthenticationPrincipal User user 으로 유저 정보 접근 하는걸로 바꾸기

    // 댓글 작성
    @PostMapping("{testerId}/comment")
    public ResponseEntity<StatusResponseDto> createComment(@PathVariable Long testerId,
                                                           @RequestBody CommentRequestDto requestDto){
        return commentService.createComment(testerId, requestDto);
    }

    // 댓글 수정
    @PutMapping("{testerId}/comment/{commentId}")
    public ResponseEntity<StatusResponseDto> updateComment(@PathVariable Long testerId,
                                                           @PathVariable Long commentId,
                                                           @RequestBody CommentRequestDto requestDto){
        return commentService.updateComment(testerId, commentId, requestDto);
    }

    // 댓글 삭제
    @DeleteMapping("{testerId}/comment/{commentId}")
    public ResponseEntity<StatusResponseDto> deleteComment(@PathVariable Long testerId,
                                                           @PathVariable Long commentId){
        return commentService.deleteComment(testerId, commentId);
    }
}

