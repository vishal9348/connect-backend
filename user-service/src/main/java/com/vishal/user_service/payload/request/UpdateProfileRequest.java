package com.vishal.user_service.payload.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating user profile information
 */
public record UpdateProfileRequest(
        @Size(max = 100, message = "Full name must be less than 100 characters")
        String fullName,

        @Size(max = 500, message = "Bio must be less than 100 characters")
        String bio,

        @Pattern(
                regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",
                message = "Invalid URL format for profile image"
        )
        String profileImageUrl,

        @Size(max = 100, message = "Location must be less than 100 characters")
        String location,

        @Pattern(
                regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",
                message = "Invalid URL format for website"
        )
        String website
) {
    /**
     * Builder pattern for partial updates
     */
    public static class Builder {
        private String fullName;
        private String bio;
        private String profileImageUrl;
        private String headerImageUrl;
        private String location;
        private String website;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        // ... other builder methods ...

        public UpdateProfileRequest build() {
            return new UpdateProfileRequest(
                    fullName,
                    bio,
                    profileImageUrl,
                    location,
                    website
            );
        }
    }

    /**
     * Creates empty request for partial updates
     */
    public static UpdateProfileRequest empty() {
        return new UpdateProfileRequest(null, null, null, null, null);
    }

    /**
     * Checks if all fields are null (no updates)
     */
    public boolean isEmpty() {
        return fullName == null &&
                bio == null &&
                profileImageUrl == null &&
                location == null &&
                website == null;
    }
}
