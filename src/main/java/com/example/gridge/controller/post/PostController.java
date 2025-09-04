package com.example.gridge.controller.post;

import com.example.gridge.controller.post.dto.*;
import com.example.gridge.controller.post.dto.CommentResponseDto;
import com.example.gridge.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name ="Post API", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    //전제 조건: 로그인, 유저 정보 가져오기
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @PostMapping("")
    public ResponseEntity<PostSimpleResponseDto> createPost(@Valid @RequestBody PostCreateRequestDto request){
        PostSimpleResponseDto post = postService.createPost(request);
        return ResponseEntity.status(201).body(post);
    }

    //전제 조건: 로그인
    @Operation(summary = "게시글 조회", description = "모든 게시글을 조회합니다. 유저 ID로 필터링 가능하며, 페이지네이션을 지원합니다.")
    @GetMapping("")
    public ResponseEntity<Page<PostDetailResponseDto>> getPosts(
            @RequestParam(required = false) Integer userId,
            @RequestParam Integer page,
            @RequestParam Integer size){

        Page<PostDetailResponseDto> posts;
        if (userId != null) {
            // userId가 존재하면 특정 유저의 게시글 조회
            posts = postService.getPostsByUserId(userId, page, size);
        } else {
            // userId가 없으면 전체 게시글 조회
            posts = postService.getAllPosts(page, size);
        }
        return ResponseEntity.ok(posts);
    }

    //전제 조건 로그인
    @Operation(summary="내 게시글 조회", description = "로그인한 사용자가 작성한 게시글을 조회합니다. 페이지네이션을 지원합니다.")
    @GetMapping("/me")
    public ResponseEntity<Page<PostDetailResponseDto>> getMyPosts(@RequestParam Integer page, @RequestParam Integer size){
        Page<PostDetailResponseDto> posts = postService.getMyPosts(page, size);
        return ResponseEntity.ok(posts);
    }

    //전제 조건: 로그인, 유저 정보도 넘기기
    @Operation(summary="게시글 수정", description = "특정 게시글을 수정합니다. 작성자만 수정할 수 있습니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity<PostSimpleResponseDto> updatePost(
            @PathVariable Integer postId,
            @Valid @RequestBody PostUpdateRequestDto request){
        PostSimpleResponseDto updatedPost = postService.updatePost(postId, request);
        return ResponseEntity.ok(updatedPost);
    }

    //전제 조건: 로그인, 유저 정보도 넘기기
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId){
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //전제 조건: 로그인, 유저 정보 갖고 오기
    @PostMapping("/{postId}/likes")
    public ResponseEntity<LikeResponseDto> likePost(@PathVariable Integer postId){
        LikeResponseDto response = postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //전제 조건:  로그인, 유저 정보 갖고 오기
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<Void> unlikePost(@PathVariable Integer postId){
        postService.unlikePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //전제 조건: 로그인, 유저 정보 갖고 오기
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Integer postId, @Valid @RequestBody CommentRequestDto request){
        CommentResponseDto response = postService.addComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //전제 조건: 로그인, 유저 정보 갖고 오기
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer postId, @PathVariable Integer commentId){
        postService.deleteComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @PathVariable Integer postId, @RequestParam Integer page, @RequestParam Integer size){
        Page<CommentResponseDto> comments = postService.getComments(postId, page, size);
        return ResponseEntity.ok(comments);
    }

    //전제 조건:로그인, 유저 정보 갖고 오기
    @PostMapping("/{postId}/reports")
    public ResponseEntity<ReportResponseDto> reportPost(
            @PathVariable Integer postId, @Valid @RequestBody ReportRequestDto request){
        ReportResponseDto response = postService.reportPost(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
