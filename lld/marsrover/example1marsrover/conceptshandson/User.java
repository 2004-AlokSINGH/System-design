package marsrover.example1marsrover.conceptshandson;

import java.util.Objects;

public final class User {
    // 1. Fields are private and final for Immutability
    private final String userId;     // Mandatory
    private final String email;      // Mandatory
    private final int age;           // Optional
    private final String phone;      // Optional

    // 2. Private constructor: only accessible by the Builder
    private User(Builder builder) {
        this.userId = builder.userId;
        this.email = builder.email;
        this.age = builder.age;
        this.phone = builder.phone;
    }

    // 3. Static Nested Builder Class
    public static class Builder {
        // Same fields as the outer class
        private final String userId;
        private final String email;
        private int age = 0;           // Default value for optional
        private String phone = "N/A";  // Default value for optional

        // 4. Builder Constructor for MANDATORY fields
        public Builder(String userId, String email) {
            // Validating mandatory fields early
            this.userId = Objects.requireNonNull(userId, "ID cannot be null");
            this.email = Objects.requireNonNull(email, "Email cannot be null");
        }

        // 5. Fluent Setters for OPTIONAL fields
        public Builder age(int age) {
            this.age = age;
            return this; // Returns builder for chaining
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        // 6. Build method: Final validation and object creation
        public User build() {
            User user = new User(this);
            if (user.age < 0) {
                throw new IllegalStateException("Age cannot be negative");
            }
            return user;
        }
    }

    // Standard Getters (No Setters)
    public String getUserId() { return userId; }
    public String getEmail() { return email; }

    public static void main(String[] args) {
        // Mandatory fields are in the constructor; optional are chained
        User user = new User.Builder("U123", "dev@example.com")
                            .age(25)
                            .phone("+1-555-0199") // Optional
                            .build();
        
        // If optional field 'phone' is skipped, it uses the default "N/A"
        User basicUser = new User.Builder("U456", "test@example.com")
                                 .build(); 
    }
}
