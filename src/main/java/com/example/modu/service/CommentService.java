package com.example.modu.service;

import com.example.modu.dto.comment.CommentRequestDto;
import com.example.modu.dto.user.StatusResponseDto;
import com.example.modu.entity.Comment;
import com.example.modu.entity.TestElement.Tester;
import com.example.modu.entity.User;
import com.example.modu.repository.CommentRepository;
import com.example.modu.repository.TesterRepository;
import com.example.modu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "Comment Service")
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final TesterRepository testerRepository;

    // 댓글 작성
    public ResponseEntity<StatusResponseDto> createComment(Long testerId, CommentRequestDto requestDto) {
        User currentUser = getCurrentUser();
        Tester tester = findTesterById(testerId);

        if(currentUser==null){
            throw new IllegalStateException("로그인한 사용자만 댓글을 작성할 수 있습니다.");
        }

        Comment comment = new Comment(requestDto);
        comment.setUser(currentUser);
        comment.setTester(tester);

        commentRepository.save(comment);

        return ResponseEntity.ok(new StatusResponseDto("댓글 작성 완료.", 200));
    }

    // 댓글 수정
    public ResponseEntity<StatusResponseDto> updateComment(Long testerId, Long commentId, CommentRequestDto requestDto) {
        User currentUser = getCurrentUser();
        Tester tester = findTesterById(testerId);
        Comment comment = findCommentById(commentId);

        validateUserAuthority(comment, currentUser);

        comment.update(requestDto);

        commentRepository.save(comment);

        return ResponseEntity.ok(new StatusResponseDto("댓글 수정 완료", 200));
    }

    // 댓글 삭제
    public ResponseEntity<StatusResponseDto> deleteComment(Long testerId, Long commentId) {
        User currentUser = getCurrentUser();
        Comment comment = findCommentById(commentId);

        validateUserAuthority(comment, currentUser);

        commentRepository.delete(comment);

        return ResponseEntity.ok(new StatusResponseDto("댓글 삭제 완료", 200));
    }

    // 현재 로그인한 회원 정보 가져오기
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) { //---------------
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            return userRepository.findByUsername(currentUsername).orElseThrow(
                    () -> new IllegalArgumentException("인증된 사용자를 찾을 수 없습니다.")
            );
        } else {
            throw new IllegalStateException("올바른 인증 정보가 아닙니다.");
        }
    }

    // testId로 해당 test 가져오기
    private Tester findTesterById(Long testId) {
        return testerRepository.findById(testId).orElseThrow(
                () -> new IllegalArgumentException("해당 테스트를 찾을 수 없습니다.")
        );
    }

    // commentId로 해당 comment 가져오기
    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.")
        );
    }

    // 본인이 작성한 댓글인지 확인
    private void validateUserAuthority(Comment comment, User currentUser) {
        if (!comment.getUser().equals(currentUser)) {
            throw new IllegalArgumentException("본인의 댓글만 수정/삭제 할 수 있습니다.");
        }
    }


}
