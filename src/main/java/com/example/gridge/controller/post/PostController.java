package com.example.gridge.controller.post;

import com.example.gridge.controller.post.dto.*;
import com.example.gridge.controller.post.dto.CommentResponseDto;
import com.example.gridge.repository.entity.user.User;
import com.example.gridge.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name ="Post API", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;


    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @PostMapping("")
    public ResponseEntity<PostSimpleResponseDto> createPost(
            @AuthenticationPrincipal User user, // 로그인된 유저 객체를 직접 가져옵니다.
            @Valid @RequestBody PostCreateRequestDto request
    ){
        // 서비스로 유저 객체를 함께 넘겨줍니다.
        PostSimpleResponseDto post = postService.createPost(user, request);
        return ResponseEntity.status(201).body(post);
    }


    @Operation(summary = "게시글 조회", description = "모든 게시글을 조회합니다. 유저 ID로 필터링 가능하며, 페이지네이션을 지원합니다.")
    @GetMapping("")
    public ResponseEntity<Page<PostDetailResponseDto>> getPosts(
            @RequestParam(required = false) Integer userId,
            @RequestParam Integer page,
            @RequestParam Integer size){

        // userId가 null인 경우, 모든 게시글을 조회하도록 서비스에 전달
        // 서비스에서 userId의 존재 여부를 판단하도록 위임
        Page<PostDetailResponseDto> posts = postService.getPosts(userId, page, size);

        return ResponseEntity.ok(posts);
    }

    @Operation(summary="내 게시글 조회", description = "로그인된 사용자의 작성한 게시글을 조회합니다. 페이지네이션을 지원합니다.")
    @GetMapping("/me")
    public ResponseEntity<Page<PostDetailResponseDto>> getMyPosts(@AuthenticationPrincipal User user,
            @RequestParam Integer page, @RequestParam Integer size){
        Page<PostDetailResponseDto> posts = postService.getMyPosts(user, page, size);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary="게시글 수정", description = "특정 게시글을 수정합니다. 작성자만 수정할 수 있습니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity<PostSimpleResponseDto> updatePost(
            @AuthenticationPrincipal User user,
            @PathVariable Integer postId,
            @Valid @RequestBody PostUpdateRequestDto request){
        PostSimpleResponseDto updatedPost = postService.updatePost(user, postId, request);
        return ResponseEntity.ok(updatedPost);
    }


    @Operation(summary="게시글 삭제", description = "특정 게시글을 삭제합니다. 작성자만 삭제할 수 있습니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal User user,@PathVariable Integer postId){
        postService.deletePost(user, postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //전제 조건: 로그인, 유저 정보 갖고 오기
    @Operation(summary="게시글 좋아요", description = "특정 게시글에 좋아요를 추가합니다.")
    @PostMapping("/{postId}/likes")
    public ResponseEntity<LikeResponseDto> likePost(@AuthenticationPrincipal User user, @PathVariable Integer postId){
        LikeResponseDto response = postService.likePost(user, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //전제 조건:  로그인, 유저 정보 갖고 오기
    @Operation(summary="게시글 좋아요 취소", description = "특정 게시글의 좋아요를 취소합니다.")
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<Void> unlikePost(@AuthenticationPrincipal User user, @PathVariable Integer postId){
        postService.unlikePost(user, postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //전제 조건: 로그인, 유저 정보 갖고 오기
    @Operation(summary="댓글 작성", description = "특정 게시글에 댓글을 작성합니다.")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> addComment( @AuthenticationPrincipal User user,
            @PathVariable Integer postId, @Valid @RequestBody CommentRequestDto request){
        CommentResponseDto response = postService.addComment(user, postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //전제 조건: 로그인, 유저 정보 갖고 오기
    @Operation(summary="댓글 삭제", description = "특정 게시글의 댓글을 삭제합니다. 작성자만 삭제할 수 있습니다.")
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal User user,
            @PathVariable Integer postId, @PathVariable Integer commentId){
        postService.deleteComment(user, postId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/{postId}/comments")
    @Operation(summary="특정 게시물의 모든 댓글 조회", description = "특정 게시글의 모든 댓글을 조회합니다. 페이지네이션을 지원합니다.")
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @PathVariable Integer postId, @RequestParam Integer page, @RequestParam Integer size){
        Page<CommentResponseDto> comments = postService.getComments(postId, page, size);
        return ResponseEntity.ok(comments);
    }

    //전제 조건:로그인, 유저 정보 갖고 오기
    @Operation(summary="게시글 신고", description = "특정 게시글을 신고합니다.")
    @PostMapping("/{postId}/reports")
    public ResponseEntity<ReportResponseDto> reportPost(
            @AuthenticationPrincipal User user,
            @PathVariable Integer postId, @Valid @RequestBody ReportRequestDto request){
        ReportResponseDto response = postService.reportPost(user, postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
