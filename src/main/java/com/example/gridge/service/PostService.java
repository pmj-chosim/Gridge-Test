package com.example.gridge.service;

import com.example.gridge.controller.post.dto.*;
import com.example.gridge.repository.*;
import com.example.gridge.repository.entity.Post.*;
import com.example.gridge.repository.entity.user.User;
import com.example.gridge.repository.entity.Post.VisibleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public PostSimpleResponseDto createPost(User user, PostCreateRequestDto request) {
        Post post = Post.create(
                user,
                request.getContent(),
                request.getVisibleStatus(),
                request.getLocation(),
                request.getMediaUrls(),
                request.getMediaTypes()
        );
        Post savedPost = postRepository.save(post);
        return PostSimpleResponseDto.from(savedPost);
    }

    @Transactional(readOnly = true)
    public Page<PostDetailResponseDto> getPosts(Integer userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts;

        if (userId != null) {
            posts = postRepository.findByUserId(userId, pageable);
        } else {
            posts = postRepository.findAll(pageable);
        }
        return posts.map(PostDetailResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostDetailResponseDto> getMyPosts(User user, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByUserId(user.getId(), pageable);
        return posts.map(PostDetailResponseDto::from);
    }

    @Transactional
    public PostSimpleResponseDto updatePost(User user, Integer postId, PostUpdateRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to update this post.");
        }

        post.update(request.getContent(), request.getVisibleStatus());
        Post updatedPost = postRepository.save(post);
        return PostSimpleResponseDto.from(updatedPost);
    }

    @Transactional
    public void deletePost(User user, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this post.");
        }

        // This assumes CascadeType.REMOVE is set on the Post entity's relationships
        // with Comment and Like to handle deletion automatically.


        postRepository.delete(post);
    }

    @Transactional
    public LikeResponseDto likePost(User user, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (likeRepository.existsByUserIdAndPostId(user.getId(), postId)) {
            throw new RuntimeException("You have already liked this post.");
        }

        Like like = Like.create(post, user);
        Like savedLike = likeRepository.save(like);

        // 좋아요 개수 증가
        post.incrementLikeCount();
        postRepository.save(post);

        return LikeResponseDto.from(savedLike);
    }

    @Transactional
    public void unlikePost(User user, Integer postId) {
        Like like = likeRepository.findByUserIdAndPostId(user.getId(), postId)
                .orElseThrow(() -> new RuntimeException("You have not liked this post."));

        likeRepository.delete(like);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // 좋아요 수 감소
        post.decrementLikeCount();
        postRepository.save(post);
    }

    @Transactional
    public CommentResponseDto addComment(User user, Integer postId, CommentRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        Comment comment = Comment.create(post, user, request.getContent());
        Comment savedComment = commentRepository.save(comment);

        // 댓글 개수 증가
        post.incrementCommentCount();
        postRepository.save(post);

        return CommentResponseDto.from(savedComment);
    }

    @Transactional
    public void deleteComment(User user, Integer postId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

        if (!comment.getPost().getId().equals(postId)) {
            throw new RuntimeException("This comment does not belong to the specified post.");
        }

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // 댓글 개수 감소
        post.decrementCommentCount();
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getComments(Integer postId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        return comments.map(CommentResponseDto::from);
    }

    @Transactional
    public ReportResponseDto reportPost(User user, Integer postId, ReportRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // DTO에서 받은 정보로 Report 객체 생성.
        // Report.create() 메서드에서 이미 상태가 PROCESSING으로 설정됨.
        Report report = Report.create(user, post, request.getReportReason(), request.getReportContent());
        Report savedReport = reportRepository.save(report);

        return ReportResponseDto.from(savedReport);
    }

    @Transactional
    public void hidePost(User user, Integer reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + reportId));

        // 신고 상태를 ACCEPTED로 변경
        report.updateStatus(ReportStatus.ACCEPTED);

        // 연결된 게시글의 상태를 PRIVATE으로 변경
        Post post = report.getPost();
        post.setStatus(VisibleStatus.PRIVATE);
        postRepository.save(post);
    }

    public PostDetailResponseDto getPostById(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        return PostDetailResponseDto.from(post);
    }
}