package com.vishal.user_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_tbl")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username; // @handle

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;
    private String phone;
    private String bio;
    private String profileImageUrl;
    private boolean verified = false;
    private boolean active = true;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> role = Set.of("ROLE_USER");

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private long totalFollowers = 0;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private long totalFollowing = 0;

    @ElementCollection
    @CollectionTable(name = "user_following", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "following_id")
    private Set<UUID> followingIds = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_follower", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "follower_id")
    private Set<UUID> followerIds = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper methods
    public void addFollowing(UUID userId) {
        this.followingIds.add(userId);
        this.totalFollowing = this.followingIds.size();
    }

    public void removeFollowing(UUID userId) {
        this.followingIds.remove(userId);
        this.totalFollowing = this.followingIds.size();
    }
}
