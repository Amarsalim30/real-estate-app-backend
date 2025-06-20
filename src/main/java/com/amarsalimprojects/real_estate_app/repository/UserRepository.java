package com.amarsalimprojects.real_estate_app.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amarsalimprojects.real_estate_app.model.User;
import com.amarsalimprojects.real_estate_app.model.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // // Basic finder methods
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    // // Search methods (containing)
    List<User> findByUsernameContaining(String username);

    List<User> findByEmailContaining(String email);

    List<User> findByFirstNameContaining(String firstName);

    List<User> findByLastNameContaining(String lastName);

    // Case-insensitive search methods
    List<User> findByUsernameContainingIgnoreCase(String username);

    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    List<User> findByLastNameContainingIgnoreCase(String lastName);

    // // Date-based queries
    List<User> findByCreatedAtAfter(LocalDateTime date);

    List<User> findByCreatedAtBefore(LocalDateTime date);

    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<User> findByUpdatedAtAfter(LocalDateTime date);

    List<User> findByUpdatedAtBefore(LocalDateTime date);

    List<User> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // // Combined role and date queries
    List<User> findByRoleAndCreatedAtAfter(UserRole role, LocalDateTime date);

    List<User> findByRoleAndCreatedAtBefore(UserRole role, LocalDateTime date);

    List<User> findByRoleAndCreatedAtBetween(UserRole role, LocalDateTime startDate, LocalDateTime endDate);

    List<User> findByRoleAndUpdatedAtAfter(UserRole role, LocalDateTime date);

    List<User> findByRoleAndUpdatedAtBefore(UserRole role, LocalDateTime date);

    List<User> findByRoleAndUpdatedAtBetween(UserRole role, LocalDateTime startDate, LocalDateTime endDate);

    // // Ordering methods
    List<User> findAllByOrderByUsernameAsc();

    List<User> findAllByOrderByUsernameDesc();

    List<User> findAllByOrderByEmailAsc();

    List<User> findAllByOrderByEmailDesc();

    List<User> findAllByOrderByFirstNameAsc();

    List<User> findAllByOrderByFirstNameDesc();

    List<User> findAllByOrderByLastNameAsc();

    List<User> findAllByOrderByLastNameDesc();

    List<User> findAllByOrderByCreatedAtAsc();

    List<User> findAllByOrderByCreatedAtDesc();

    List<User> findAllByOrderByUpdatedAtAsc();

    List<User> findAllByOrderByUpdatedAtDesc();

    // // Role-based ordering
    List<User> findByRoleOrderByUsernameAsc(UserRole role);

    List<User> findByRoleOrderByUsernameDesc(UserRole role);

    List<User> findByRoleOrderByEmailAsc(UserRole role);

    List<User> findByRoleOrderByEmailDesc(UserRole role);

    List<User> findByRoleOrderByFirstNameAsc(UserRole role);

    List<User> findByRoleOrderByFirstNameDesc(UserRole role);

    List<User> findByRoleOrderByLastNameAsc(UserRole role);

    List<User> findByRoleOrderByLastNameDesc(UserRole role);

    List<User> findByRoleOrderByCreatedAtAsc(UserRole role);

    List<User> findByRoleOrderByCreatedAtDesc(UserRole role);

    List<User> findByRoleOrderByUpdatedAtAsc(UserRole role);

    List<User> findByRoleOrderByUpdatedAtDesc(UserRole role);

    // // Existence checks
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    // // Count methods
    Long countByRole(UserRole role);

    Long countByCreatedAtAfter(LocalDateTime date);

    Long countByCreatedAtBefore(LocalDateTime date);

    Long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Long countByUpdatedAtAfter(LocalDateTime date);

    Long countByUpdatedAtBefore(LocalDateTime date);

    Long countByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Long countByRoleAndCreatedAtAfter(UserRole role, LocalDateTime date);

    Long countByRoleAndCreatedAtBefore(UserRole role, LocalDateTime date);

    Long countByRoleAndCreatedAtBetween(UserRole role, LocalDateTime startDate, LocalDateTime endDate);

    Long countByRoleAndUpdatedAtAfter(UserRole role, LocalDateTime date);

    Long countByRoleAndUpdatedAtBefore(UserRole role, LocalDateTime date);

    Long countByRoleAndUpdatedAtBetween(UserRole role, LocalDateTime startDate, LocalDateTime endDate);

    // // Authentication methods
    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByEmailAndPassword(String email, String password);

    // // Advanced search methods
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword% OR u.firstName LIKE %:keyword% OR u.lastName LIKE %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByKeywordIgnoreCase(@Param("keyword") String keyword);

    // // Full name search
    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE %:fullName%")
    List<User> findByFullNameContaining(@Param("fullName") String fullName);

    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<User> findByFullNameContainingIgnoreCase(@Param("fullName") String fullName);

    // // Users with complete profile
    @Query("SELECT u FROM User u WHERE u.firstName IS NOT NULL AND u.lastName IS NOT NULL AND u.email IS NOT NULL")
    List<User> findUsersWithCompleteProfile();

    // // Users with incomplete profile
    @Query("SELECT u FROM User u WHERE u.firstName IS NULL OR u.lastName IS NULL")
    List<User> findUsersWithIncompleteProfile();

    // // Recently active users
    @Query("SELECT u FROM User u WHERE u.updatedAt > :date ORDER BY u.updatedAt DESC")
    List<User> findRecentlyActiveUsers(@Param("date") LocalDateTime date);

    // // Inactive users
    @Query("SELECT u FROM User u WHERE u.updatedAt < :date ORDER BY u.updatedAt ASC")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);

    // // Users created in date range with role
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.createdAt BETWEEN :startDate AND :endDate ORDER BY u.createdAt DESC")
    List<User> findByRoleAndCreatedBetweenOrderByCreatedAtDesc(@Param("role") UserRole role, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // // Users updated in date range with role
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.updatedAt BETWEEN :startDate AND :endDate ORDER BY u.updatedAt DESC")
    List<User> findByRoleAndUpdatedBetweenOrderByUpdatedAtDesc(@Param("role") UserRole role, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // // Find users by multiple roles
    @Query("SELECT u FROM User u WHERE u.role IN :roles")
    List<User> findByRoleIn(@Param("roles") List<UserRole> roles);

    // // Find users by multiple roles ordered by username
    @Query("SELECT u FROM User u WHERE u.role IN :roles ORDER BY u.username ASC")
    List<User> findByRoleInOrderByUsernameAsc(@Param("roles") List<UserRole> roles);

    // // Find users by multiple roles ordered by created date
    @Query("SELECT u FROM User u WHERE u.role IN :roles ORDER BY u.createdAt DESC")
    List<User> findByRoleInOrderByCreatedAtDesc(@Param("roles") List<UserRole> roles);

    // // Count users by multiple roles
    @Query("SELECT COUNT(u) FROM User u WHERE u.role IN :roles")
    Long countByRoleIn(@Param("roles") List<UserRole> roles);

    // // Find users created today
    @Query("SELECT u FROM User u WHERE DATE(u.createdAt) = CURRENT_DATE")
    List<User> findUsersCreatedToday();

    // // Find users updated today
    @Query("SELECT u FROM User u WHERE DATE(u.updatedAt) = CURRENT_DATE")
    List<User> findUsersUpdatedToday();

    // // Count users created today
    @Query("SELECT COUNT(u) FROM User u WHERE DATE(u.createdAt) = CURRENT_DATE")
    Long countUsersCreatedToday();

    // // Count users updated today
    @Query("SELECT COUNT(u) FROM User u WHERE DATE(u.updatedAt) = CURRENT_DATE")
    Long countUsersUpdatedToday();

    // // Find users by email domain
    @Query("SELECT u FROM User u WHERE u.email LIKE CONCAT('%@', :domain)")
    List<User> findByEmailDomain(@Param("domain") String domain);

    // // Count users by email domain
    @Query("SELECT COUNT(u) FROM User u WHERE u.email LIKE CONCAT('%@', :domain)")
    Long countByEmailDomain(@Param("domain") String domain);

    // // Find users with specific username pattern
    @Query(value = "SELECT * FROM users u WHERE u.username LIKE pattern", nativeQuery = true)
    List<User> findByUsernamePattern(@Param("pattern") String pattern);

    // // Find duplicate emails (if any exist due to data issues)
    @Query("SELECT u.email FROM User u GROUP BY u.email HAVING COUNT(u.email) > 1")
    List<String> findDuplicateEmails();

    // // Find duplicate usernames (if any exist due to data issues)
    @Query("SELECT u.username FROM User u GROUP BY u.username HAVING COUNT(u.username) > 1")
    List<String> findDuplicateUsernames();

    // // Statistics queries
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> getUserCountByRole();

    @Query("SELECT DATE(u.createdAt), COUNT(u) FROM User u WHERE u.createdAt >= :startDate GROUP BY DATE(u.createdAt) ORDER BY DATE(u.createdAt)")
    List<Object[]> getUserRegistrationStats(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT DATE(u.updatedAt), COUNT(u) FROM User u WHERE u.updatedAt >= :startDate GROUP BY DATE(u.updatedAt) ORDER BY DATE(u.updatedAt)")
    List<Object[]> getUserActivityStats(@Param("startDate") LocalDateTime startDate);

    // // Monthly registration stats
    @Query("SELECT YEAR(u.createdAt), MONTH(u.createdAt), COUNT(u) FROM User u GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)")
    List<Object[]> getMonthlyRegistrationStats();

    // // Yearly registration stats
    @Query("SELECT YEAR(u.createdAt), COUNT(u) FROM User u GROUP BY YEAR(u.createdAt) ORDER BY YEAR(u.createdAt)")
    List<Object[]> getYearlyRegistrationStats();

    // // Find oldest users
    @Query("SELECT u FROM User u ORDER BY u.createdAt ASC")
    List<User> findOldestUsers();

    // // Find newest users
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findNewestUsers();

    // // Find most recently updated users
    @Query("SELECT u FROM User u ORDER BY u.updatedAt DESC")
    List<User> findMostRecentlyUpdatedUsers();

    // // Find least recently updated users
    @Query("SELECT u FROM User u ORDER BY u.updatedAt ASC")
    List<User> findLeastRecentlyUpdatedUsers();

    // // Custom delete methods
    void deleteByUsername(String username);

    void deleteByEmail(String email);

    void deleteByRole(UserRole role);

    void deleteByCreatedAtBefore(LocalDateTime date);

    void deleteByUpdatedAtBefore(LocalDateTime date);

    // // Batch operations
    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findByIdIn(@Param("ids") List<Long> ids);

    // // Find users for bulk operations
    @Query("SELECT u FROM User u WHERE u.role = :oldRole")
    List<User> findUsersForRoleUpdate(@Param("oldRole") UserRole oldRole);

    // // Advanced authentication queries
    @Query("SELECT u FROM User u WHERE (u.username = :identifier OR u.email = :identifier) AND u.password = :password")
    Optional<User> findByUsernameOrEmailAndPassword(@Param("identifier") String identifier, @Param("password") String password);

    // // Check if user can be deleted (no constraints)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = :id")
    boolean canUserBeDeleted(@Param("id") Long id);

    // // Find users with similar usernames
    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT(:username, '%') AND u.id != :excludeId")
    List<User> findSimilarUsernames(@Param("username") String username, @Param("excludeId") Long excludeId);

    // // Find users with similar emails
    @Query("SELECT u FROM User u WHERE u.email LIKE CONCAT(:email, '%') AND u.id != :excludeId")
    List<User> findSimilarEmails(@Param("email") String email, @Param("excludeId") Long excludeId);

    // // Performance optimized queries with specific field selection
    @Query("SELECT u.id, u.username, u.email FROM User u")
    List<Object[]> findBasicUserInfo();

    @Query("SELECT u.id, u.username, u.email, u.role FROM User u WHERE u.role = :role")
    List<Object[]> findBasicUserInfoByRole(@Param("role") UserRole role);

    @Query("SELECT u.id, u.username, u.email, u.createdAt FROM User u ORDER BY u.createdAt DESC")
    List<Object[]> findBasicUserInfoOrderByCreatedAt();

    // // Find users by partial name match
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);

    // // Find users by initials
    @Query("SELECT u FROM User u WHERE LEFT(u.firstName, 1) = :firstInitial AND LEFT(u.lastName, 1) = :lastInitial")
    List<User> findByInitials(@Param("firstInitial") String firstInitial, @Param("lastInitial") String lastInitial);

    // // Find users with null first or last name
    @Query("SELECT u FROM User u WHERE u.firstName IS NULL OR u.lastName IS NULL OR u.firstName = '' OR u.lastName = ''")
    List<User> findUsersWithMissingNames();

    // // Find users with complete names
    @Query("SELECT u FROM User u WHERE u.firstName IS NOT NULL AND u.lastName IS NOT NULL AND u.firstName != '' AND u.lastName != ''")
    List<User> findUsersWithCompleteNames();

    // // Count users with missing information
    @Query("SELECT COUNT(u) FROM User u WHERE u.firstName IS NULL OR u.lastName IS NULL OR u.firstName = '' OR u.lastName = ''")
    Long countUsersWithMissingNames();

    // // Find users by creation date range and role
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findUsersByRoleAndDateRange(@Param("role") UserRole role, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // // Find users who haven't updated their profile recently
    @Query("SELECT u FROM User u WHERE u.updatedAt < :date OR u.createdAt = u.updatedAt")
    List<User> findUsersWithStaleProfiles(@Param("date") LocalDateTime date);

    // // Count users who haven't updated their profile recently
    @Query("SELECT COUNT(u) FROM User u WHERE u.updatedAt < :date OR u.createdAt = u.updatedAt")
    Long countUsersWithStaleProfiles(@Param("date") LocalDateTime date);

    // // Find users by multiple criteria
    @Query("SELECT u FROM User u WHERE "
            + "(:role IS NULL OR u.role = :role) AND "
            + "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND "
            + "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND "
            + "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND "
            + "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))")
    List<User> findUsersByCriteria(@Param("role") UserRole role,
            @Param("username") String username,
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName);

    // // Count users by multiple criteria
    @Query("SELECT COUNT(u) FROM User u WHERE "
            + "(:role IS NULL OR u.role = :role) AND "
            + "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND "
            + "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND "
            + "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND "
            + "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))")
    Long countUsersByCriteria(@Param("role") UserRole role,
            @Param("username") String username,
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName);

    // // Find users with specific username length
    @Query("SELECT u FROM User u WHERE LENGTH(u.username) = :length")
    List<User> findByUsernameLength(@Param("length") int length);

    // // Find users with username length in range
    @Query("SELECT u FROM User u WHERE LENGTH(u.username) BETWEEN :minLength AND :maxLength")
    List<User> findByUsernameLengthBetween(@Param("minLength") int minLength, @Param("maxLength") int maxLength);

    // // Find users with short usernames
    @Query("SELECT u FROM User u WHERE LENGTH(u.username) < :maxLength")
    List<User> findUsersWithShortUsernames(@Param("maxLength") int maxLength);

    // // Find users with long usernames
    @Query("SELECT u FROM User u WHERE LENGTH(u.username) > :minLength")
    List<User> findUsersWithLongUsernames(@Param("minLength") int minLength);

    // // Find users by email provider
    @Query("SELECT u FROM User u WHERE u.email LIKE CONCAT('%@', :provider, '.%')")
    List<User> findByEmailProvider(@Param("provider") String provider);

    // // Count users by email provider
    @Query("SELECT COUNT(u) FROM User u WHERE u.email LIKE CONCAT('%@', :provider, '.%')")
    Long countByEmailProvider(@Param("provider") String provider);

    // // Get email provider statistics
    @Query("SELECT SUBSTRING(u.email, LOCATE('@', u.email) + 1), COUNT(u) FROM User u WHERE u.email IS NOT NULL GROUP BY SUBSTRING(u.email, LOCATE('@', u.email) + 1) ORDER BY COUNT(u) DESC")
    List<Object[]> getEmailProviderStats();

//     // // Find users with numeric usernames
//     @Query("SELECT u FROM User u WHERE u.username REGEXP '^[0-9]+$'")
//     List<User> findUsersWithNumericUsernames();
    // // Find users with alphabetic usernames only
//     @Query("SELECT u FROM User u WHERE u.username REGEXP '^[a-zA-Z]+$'")
//     List<User> findUsersWithAlphabeticUsernames();
//     // // Find users with alphanumeric usernames
//     @Query("SELECT u FROM User u WHERE u.username REGEXP '^[a-zA-Z0-9]+$'")
//     List<User> findUsersWithAlphanumericUsernames();
    // // Find users created on specific day of week
    @Query("SELECT u FROM User u WHERE DAYOFWEEK(u.createdAt) = :dayOfWeek")
    List<User> findUsersCreatedOnDayOfWeek(@Param("dayOfWeek") int dayOfWeek);

    // // Count users created on specific day of week
    @Query("SELECT COUNT(u) FROM User u WHERE DAYOFWEEK(u.createdAt) = :dayOfWeek")
    Long countUsersCreatedOnDayOfWeek(@Param("dayOfWeek") int dayOfWeek);

    // // Get day of week registration statistics
    @Query("SELECT DAYOFWEEK(u.createdAt), COUNT(u) FROM User u GROUP BY DAYOFWEEK(u.createdAt) ORDER BY DAYOFWEEK(u.createdAt)")
    List<Object[]> getDayOfWeekRegistrationStats();

    // // Find users created in specific hour of day
    @Query("SELECT u FROM User u WHERE HOUR(u.createdAt) = :hour")
    List<User> findUsersCreatedInHour(@Param("hour") int hour);

    // // Count users created in specific hour of day
    @Query("SELECT COUNT(u) FROM User u WHERE HOUR(u.createdAt) = :hour")
    Long countUsersCreatedInHour(@Param("hour") int hour);

    // // Get hourly registration statistics
    @Query("SELECT HOUR(u.createdAt), COUNT(u) FROM User u GROUP BY HOUR(u.createdAt) ORDER BY HOUR(u.createdAt)")
    List<Object[]> getHourlyRegistrationStats();

    // // Find users by age of account (days since creation)
    @Query("SELECT u FROM User u WHERE DATEDIFF(CURRENT_DATE, DATE(u.createdAt)) = :days")
    List<User> findUsersByAccountAge(@Param("days") int days);

    // // Find users with account older than specified days
    @Query("SELECT u FROM User u WHERE DATEDIFF(CURRENT_DATE, DATE(u.createdAt)) > :days")
    List<User> findUsersWithAccountOlderThan(@Param("days") int days);

    // // Find users with account newer than specified days
    @Query("SELECT u FROM User u WHERE DATEDIFF(CURRENT_DATE, DATE(u.createdAt)) < :days")
    List<User> findUsersWithAccountNewerThan(@Param("days") int days);

    // // Count users with account older than specified days
    @Query("SELECT COUNT(u) FROM User u WHERE DATEDIFF(CURRENT_DATE, DATE(u.createdAt)) > :days")
    Long countUsersWithAccountOlderThan(@Param("days") int days);

    // // Count users with account newer than specified days
    @Query("SELECT COUNT(u) FROM User u WHERE DATEDIFF(CURRENT_DATE, DATE(u.createdAt)) < :days")
    Long countUsersWithAccountNewerThan(@Param("days") int days);

    // // Find users who never updated their profile
    @Query("SELECT u FROM User u WHERE u.createdAt = u.updatedAt")
    List<User> findUsersWhoNeverUpdatedProfile();

    // // Count users who never updated their profile
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt = u.updatedAt")
    Long countUsersWhoNeverUpdatedProfile();

    // // Find most active users (most profile updates)
    @Query("SELECT u FROM User u WHERE u.createdAt != u.updatedAt ORDER BY u.updatedAt DESC")
    List<User> findMostActiveUsers();

    // // Advanced search with pagination support
    @Query("SELECT u FROM User u WHERE "
            + "(:searchTerm IS NULL OR "
            + "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);

    // // Count search results
    @Query("SELECT COUNT(u) FROM User u WHERE "
            + "(:searchTerm IS NULL OR "
            + "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Long countSearchResults(@Param("searchTerm") String searchTerm);

    // // Find users for export (basic info only)
    @Query("SELECT u.id, u.username, u.email, u.firstName, u.lastName, u.role, u.createdAt FROM User u ORDER BY u.createdAt DESC")
    List<Object[]> findUsersForExport();

    // // Find users for backup (all info)
    @Query("SELECT u FROM User u ORDER BY u.id ASC")
    List<User> findUsersForBackup();

    // // Validation queries for data integrity
    @Query("SELECT COUNT(u) FROM User u WHERE u.username IS NULL OR u.username = ''")
    Long countUsersWithEmptyUsername();

    @Query("SELECT COUNT(u) FROM User u WHERE u.email IS NULL OR u.email = ''")
    Long countUsersWithEmptyEmail();

    @Query("SELECT COUNT(u) FROM User u WHERE u.password IS NULL OR u.password = ''")
    Long countUsersWithEmptyPassword();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role IS NULL")
    Long countUsersWithNullRole();

    // // Find users with data quality issues
    @Query("SELECT u FROM User u WHERE u.username IS NULL OR u.username = '' OR u.email IS NULL OR u.email = '' OR u.password IS NULL OR u.password = '' OR u.role IS NULL")
    List<User> findUsersWithDataQualityIssues();

    // // Count users with data quality issues
    @Query("SELECT COUNT(u) FROM User u WHERE u.username IS NULL OR u.username = '' OR u.email IS NULL OR u.email = '' OR u.password IS NULL OR u.password = '' OR u.role IS NULL")
    Long countUsersWithDataQualityIssues();
}
