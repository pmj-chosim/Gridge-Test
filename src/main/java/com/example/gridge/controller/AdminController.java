package com.example.gridge.controller;


import com.example.gridge.controller.payment.dto.PaymentProcessRequestDto;
import com.example.gridge.controller.payment.dto.PaymentResponseDto;
import com.example.gridge.controller.payment.dto.SubscriptionResponseDto;
import com.example.gridge.controller.post.dto.CommentRequestDto;
import com.example.gridge.controller.post.dto.CommentResponseDto;
import com.example.gridge.controller.post.dto.PostDetailResponseDto;
import com.example.gridge.controller.user.dto.UserResponseDto;
import com.example.gridge.controller.user.dto.UserSimpleResponseDto;
import com.example.gridge.repository.entity.Post.VisibleStatus;
import com.example.gridge.repository.entity.payment.PaymentStatus;
import com.example.gridge.repository.entity.payment.SubscriptionStatus;
import com.example.gridge.repository.entity.user.ActiveLevel;
import com.example.gridge.repository.entity.user.User;
import com.example.gridge.service.PaymentService;
import com.example.gridge.service.PostService;
import com.example.gridge.service.SubscriptionService;
import com.example.gridge.service.UserCreationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;

import java.time.LocalDate;
import java.util.Optional;

@Tag(name="Admin", description="어드민 권한자만 접근 가능한 정보 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserCreationService userCreationService;
    private final PostService postService;
    private final PaymentService paymentService;
    private final SubscriptionService subscriptionService;


    //유저 관련 API
    @Secured("ROLE_ADMIN")
    @Operation(summary="특정 유저 정보 조회", description="특정 유저의 상세 정보를 조회합니다. (어드민 권한 필요)")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        UserResponseDto user = userCreationService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary="모든 유저 정보 조회(페이징)",
            description="모든 유저의 간단한 정보를 페이징 처리하여 조회합니다. (어드민 권한 필요)," +
                    "필터링 옵션: username, status(active, nonactive, banned), date(YYYY-MM-DD)")
    @GetMapping("/users")
    public ResponseEntity<Page<UserSimpleResponseDto>> getAllUsers(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<ActiveLevel> status,
            @RequestParam(required = false) Optional<LocalDate> startDate,
            @RequestParam(required = false) Optional<LocalDate> endDate){

        Page<UserSimpleResponseDto> users = userCreationService.getAllUsers(
                page, size, username, status, startDate, endDate);
        return ResponseEntity.ok(users);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary="특정 유저 상태 변경", description="특정 유저의 상태를 변경합니다.-어드민 권한 필요")
    @PatchMapping("/users/{id}/status")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @PathVariable Integer id,
            @RequestParam ActiveLevel status) {
        UserResponseDto updatedUser = userCreationService.updateUserStatus(id, status);
        return ResponseEntity.ok(updatedUser);
    }

    //콘텐츠 관련 API- 게시글, 댓글
    //게시글
    @Secured("ROLE_ADMIN")
    @Operation(summary="특정 게시글 조회", description="특정 게시글의 상세 정보를 조회합니다. (어드민 권한 필요)")
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailResponseDto> getPostById(@PathVariable Integer id) {
        PostDetailResponseDto post = postService.getPostByIdAdmin(id);
        return ResponseEntity.ok(post);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary="모든 게시글 조회(페이징)", description="모든 게시글의 간단한 정보를 페이징 처리하여 조회합니다. (어드민 권한 필요)")
    @GetMapping("/posts")
    public ResponseEntity<Page<PostDetailResponseDto>> getAllPosts(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<VisibleStatus> visibleStatus,
            @RequestParam(required = false) Optional<LocalDate> findStartDate,
            @RequestParam(required = false) Optional<LocalDate> findEndDate,
            @RequestParam(required = false) Optional<Boolean> hasLikes,
            @RequestParam(required = false) Optional<Boolean> hasComments
    ) {
        Page<PostDetailResponseDto> posts = postService.getAllPosts(page, size, visibleStatus, findStartDate, findEndDate, hasLikes, hasComments);
        return ResponseEntity.ok(posts);
    }

    @Secured("ROLE_ADMIN")
    @Operation (summary="특정 게시글 상태 변경", description="특정 게시글의 상태를 변경합니다. (어드민 권한 필요)")
    @PatchMapping("/posts/{id}/status")
    public ResponseEntity<PostDetailResponseDto> updatePostStatus(
            @PathVariable Integer id,
            @RequestParam VisibleStatus status) {
        PostDetailResponseDto post = postService.updatePostStatus(id, status);
        return ResponseEntity.ok(post);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary="특정 게시글 삭제", description="특정 게시글을 삭제합니다. (어드민 권한 필요)")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Integer id) {
        postService.deletePostAdmin(id);
        return ResponseEntity.ok("Post deleted with ID: " + id);
    }

    //댓글
    @Secured("ROLE_ADMIN")
    @Operation(summary="모든 댓글 정보 조회(페이징)", description="모든 댓글의 간단한 정보를 페이징 처리하여 조회합니다. (어드민 권한 필요)")
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentResponseDto>> getAllComments(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<Integer> userId,
            @RequestParam(required = false) Optional<Integer> postId,
            @RequestParam(required = false) Optional<LocalDate> startDate,
            @RequestParam(required = false) Optional<LocalDate> endDate
    ) {
        Page<CommentResponseDto> comments = postService.getAllComments(page, size, userId, postId, startDate, endDate);
        return ResponseEntity.ok(comments);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary="댓글 생성", description="댓글을 생성합니다. (어드민 권한 필요)")
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestParam Integer postId,
            @RequestParam Integer userId,
            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto comment = postService.createCommentAdmin(postId, userId, commentRequestDto);
        return ResponseEntity.ok(comment);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary="특정 댓글 수정", description="특정 댓글을 수정합니다. (어드민 권한 필요)")
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Integer commentId,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto comment = postService.updateCommentAdmin(commentId, commentRequestDto);
        return ResponseEntity.ok(comment);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary="특정 댓글 삭제", description="특정 댓글을 삭제합니다. (어드민 권한 필요)")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer commentId) {
        postService.deleteCommentAdmin(commentId);
        return ResponseEntity.ok("Comment deleted with ID: " + commentId);
    }

    //구독
    @Secured("ROLE_ADMIN")
    @Operation(summary="모든 구독 정보 조회(페이징)", description="모든 구독 정보를 페이징 처리하여 조회합니다. (어드민 권한 필요)")
    @GetMapping("/subscriptions")
    public ResponseEntity<Page<SubscriptionResponseDto>> getAllSubscriptions(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam (required = false) Optional<LocalDate> startFindDate,
            @RequestParam (required = false) Optional<LocalDate> endFindDate,
            @RequestParam (required = false) Optional<SubscriptionStatus> status
    ) {
        Page<SubscriptionResponseDto> subscriptions = subscriptionService.getAllSubscriptions(page, size, startFindDate, endFindDate,status);
        return ResponseEntity.ok(subscriptions);
    }


    @Secured("ROLE_ADMIN")
    @Operation(summary="새 구독 정보 생성", description="새 구독 정보를 생성합니다. (어드민 권한 필요)")
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionResponseDto> createSubscription(@RequestParam Integer userId,
                                                                      @Valid @RequestBody PaymentProcessRequestDto paymentDto) {
        SubscriptionResponseDto responseDto = subscriptionService.createSubscriptionAdmin(userId, paymentDto);
        return ResponseEntity.ok(responseDto);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "구독 정보 업데이트", description = "구독 정보를 새로 업데이트 합니다(어드민 권한 필요)")
    @PatchMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<SubscriptionResponseDto> updateSubscription(
            @PathVariable Integer subscriptionId,
            @Valid @RequestBody PaymentProcessRequestDto paymentDto) {
        SubscriptionResponseDto updatedSubscription = subscriptionService.updateSubscription(subscriptionId, paymentDto);
        return ResponseEntity.ok(updatedSubscription);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "구독 정보 수정" , description = "구독 정보를 수정합니다(어드민 권한 필요)")
    @PatchMapping("/subscriptions/{subscriptionId}/status")
    public ResponseEntity<SubscriptionResponseDto> updateSubscriptionStatus(
            @PathVariable Integer subscriptionId,
            @RequestParam SubscriptionStatus status) {
        SubscriptionResponseDto updatedSubscription = subscriptionService.updateSubscriptionStatusAdmin(subscriptionId, status);
        return ResponseEntity.ok(updatedSubscription);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "특정 구독 정보 삭제", description = "특정 구독 정보를 삭제합니다(어드민 권한 필요)")
    @DeleteMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<String> deleteSubscription(@PathVariable Integer subscriptionId) {
        subscriptionService.deleteSubscriptionAdmin(subscriptionId);
        return ResponseEntity.ok("Subscription deleted with ID: " + subscriptionId);
    }

    //결제
    @Secured("ROLE_ADMIN")
    @Operation(summary="모든 결제 정보 조회(페이징)", description="모든 결제 정보를 페이징 처리하여 조회합니다. (어드민 권한 필요)")
    @GetMapping("/payments")
    public ResponseEntity<Page<PaymentResponseDto>> getAllPayments(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<LocalDate> startFindDate,
            @RequestParam(required = false) Optional<LocalDate> endFindDate,
            @RequestParam(required = false) Optional<PaymentStatus> status,
            @RequestParam(required = false) Optional<Integer> userId
    ) {
        Page<PaymentResponseDto> payments = paymentService.getAllPayments(page, size, startFindDate, endFindDate, status, userId);
        return ResponseEntity.ok(payments);
    }

}
