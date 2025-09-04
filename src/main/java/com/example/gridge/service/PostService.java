package com.example.gridge.service;


import com.example.gridge.controller.post.dto.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Getter
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public PostSimpleResponseDto createPost(PostCreateRequestDto request) {
        User user= userRepository.findById(request.getUserId())
                .orElseThrow(()-> new RuntimeException("Invalid user ID: " + request.getUserId()));

        Post post= Post.create(
            request.getUserId(),
            request.getContent(),
            request.getVisibleStatus(),
            request.getLocation(),
            request.getMediaUrls()
        );

        Post savedPost = postRepository.save(post);
        return PostSimpleResponseDto.from(savedPost);
    }


    public Page<PostDetailResponseDto> getPostsByUserId(Integer userId, Integer page, Integer size) {
        User user= userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Invalid user ID: " + userId));
        Page<Post> posts = postRepository.findByUserId(userId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return posts.map(PostDetailResponseDto::from);
    }

    public Page<PostDetailResponseDto> getAllPosts(Integer page, Integer size) {
        Page<Post> posts =postRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return posts.map(PostDetailResponseDto::from);
    }


    public Page<PostDetailResponseDto> getMyPosts(Integer page, Integer size) {
        // 현재 로그인한 사용자의 ID를 가져오는 메서드!!!!!!!!!!!!! authen?? 이런걸로 바꾸기
        Integer currentUserId = getCurrentUserId();
        Page<Post> posts = postRepository.findByUserId(currentUserId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return posts.map(PostDetailResponseDto::from);
    }


    public PostSimpleResponseDto updatePost(Integer postId, PostUpdateRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // 현재 로그인한 사용자의 ID를 가져오는 메서드!!!!!!!!!!!!! authen?? 이런걸로 바꾸기
        Integer currentUserId = getCurrentUserId();

        if (!post.getUserId().equals(currentUserId)) {
            //익셉션 내가 정의하기
            throw new RuntimeException("You are not authorized to update this post.");
        }

        post.update(
                request.getContent(),
                request.getVisibleStatus()
        );

        Post updatedPost = postRepository.save(post);
        return PostSimpleResponseDto.from(updatedPost);
    }

    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // 현재 로그인한 사용자의 ID를 가져오는 메서드!!!!!!!!!!!!! authen?? 이런걸로 바꾸기
        Integer currentUserId = getCurrentUserId();

        if (!post.getUserId().equals(currentUserId)) {
            //익셉션 내가 정의하기
            throw new RuntimeException("You are not authorized to delete this post.");
        }

        // 게시글에 달린 댓글 삭제
        commentRepository.deleteByPostId(postId);
        // 게시글에 달린 좋아요 삭제
        likeRepository.deleteByPostId(postId);
        // 게시글 삭제
        postRepository.delete(post);
    }


    public LikeResponseDto likePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // 현재 로그인한 사용자의 ID를 가져오는 메서드!!!!!!!!!!!!! authen?? 이런걸로 바꾸기
        Integer currentUserId = getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Invalid user ID: " + currentUserId));

        // 이미 좋아요를 눌렀는지 확인
        if (likeRepository.existsByUserIdAndPostId(currentUserId, postId)) {
            //익셉션 정의하기
            throw new RuntimeException("You have already liked this post.");
        }

        Like like = Like.create(currentUserId, postId);
        Like savedLike = likeRepository.save(like);

        // 좋아요 수 증가
        post.incrementLikeCount();
        postRepository.save(post);

        return LikeResponseDto.from(savedLike);
    }


    public void unlikePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // 현재 로그인한 사용자의 ID를 가져오는 메서드!!!!!!!!!!!!! authen?? 이런걸로 바꾸기
        Integer currentUserId = getCurrentUserId();

        Like like = likeRepository.findByUserIdAndPostId(currentUserId, postId)
                .orElseThrow(() -> new RuntimeException("You have not liked this post."));

        likeRepository.delete(like);

        // 좋아요 수 감소
        post.decrementLikeCount();
        postRepository.save(post);
    }
}
