package com.vishal.user_service.controller;

import com.vishal.user_service.CurrentUser;
import com.vishal.user_service.exception.AlreadyFollowingException;
import com.vishal.user_service.model.Users;
import com.vishal.user_service.payload.request.UpdateProfileRequest;
import com.vishal.user_service.payload.request.UpdateRolesRequest;
import com.vishal.user_service.payload.response.UserResponse;
import com.vishal.user_service.security.JwtUtils;
import com.vishal.user_service.service.IUserFeatureService;
import com.vishal.user_service.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserFeaturesController {

    private final IUserFeatureService userService;
    private final JwtUtils jwtUtils;
    private final IUserService userServiceMain;

    @GetMapping("/me")
    @Operation(summary = "Get current user's profile")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getCurrentUser(@CurrentUser UUID userId) {
        Users user = userServiceMain.findUserById(userId);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PatchMapping("/profile")
    @Operation(summary = "Update user profile")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> updateProfile(
            @CurrentUser UUID userId,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @PostMapping("/{username}/follow")
    @Operation(summary = "Follow another user")
    @ApiResponse(responseCode = "204", description = "Successfully followed user")
    @ApiResponse(responseCode = "400", description = "Already following this user")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> followUser(
            @CurrentUser UUID followerId,
            @PathVariable String username
    ) throws AlreadyFollowingException {
        userService.followUser(followerId, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}/follow")
    @Operation(summary = "Unfollow another user")
    @ApiResponse(responseCode = "204", description = "Successfully unfollowed user")
    @ApiResponse(responseCode = "400", description = "Not following this user")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> unfollowUser(
            @CurrentUser UUID followerId,
            @PathVariable String username
    ) {
        userService.unfollowUser(followerId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify-token")
    @Operation(summary = "Verify JWT token validity")
    @ApiResponse(responseCode = "204", description = "Token is valid")
    @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    public ResponseEntity<Void> verifyToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        userService.verifyToken(token);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/{userId}/roles")
    @Operation(summary = "Update user roles (Admin only)")
    @ApiResponse(responseCode = "200", description = "Roles updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid roles provided")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> updateUserRoles(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateRolesRequest request
    ) {
        return ResponseEntity.ok(userService.updateRoles(userId, request.roles()));
    }

    @DeleteMapping("/admin/{userId}")
    @Operation(summary = "Deactivate user (Admin only)")
    @ApiResponse(responseCode = "204", description = "User deactivated successfully")
    @ApiResponse(responseCode = "400", description = "User already deactivated")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get public user profile")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getPublicProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }
}
