package com.amarsalimprojects.real_estate_app.requests;

public // // Statistics DTO class
        class UserStatistics {

    private Long totalUsers;
    private Long adminUsers;
    private Long regularUsers;
    private Long usersCreatedToday;
    private Long usersCreatedThisWeek;
    private Long usersCreatedThisMonth;
    private Long usersUpdatedToday;
    private Long usersUpdatedThisWeek;
    private Long usersUpdatedThisMonth;

    // // Constructors
    public UserStatistics() {
    }

    public UserStatistics(Long totalUsers, Long adminUsers, Long regularUsers,
            Long usersCreatedToday, Long usersCreatedThisWeek, Long usersCreatedThisMonth,
            Long usersUpdatedToday, Long usersUpdatedThisWeek, Long usersUpdatedThisMonth) {
        this.totalUsers = totalUsers;
        this.adminUsers = adminUsers;
        this.regularUsers = regularUsers;
        this.usersCreatedToday = usersCreatedToday;
        this.usersCreatedThisWeek = usersCreatedThisWeek;
        this.usersCreatedThisMonth = usersCreatedThisMonth;
        this.usersUpdatedToday = usersUpdatedToday;
        this.usersUpdatedThisWeek = usersUpdatedThisWeek;
        this.usersUpdatedThisMonth = usersUpdatedThisMonth;
    }

    // // Builder pattern
    public static UserStatisticsBuilder builder() {
        return new UserStatisticsBuilder();
    }

    public static class UserStatisticsBuilder {

        private Long totalUsers;
        private Long adminUsers;
        private Long regularUsers;
        private Long usersCreatedToday;
        private Long usersCreatedThisWeek;
        private Long usersCreatedThisMonth;
        private Long usersUpdatedToday;
        private Long usersUpdatedThisWeek;
        private Long usersUpdatedThisMonth;

        public UserStatisticsBuilder totalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
            return this;
        }

        public UserStatisticsBuilder adminUsers(Long adminUsers) {
            this.adminUsers = adminUsers;
            return this;
        }

        public UserStatisticsBuilder regularUsers(Long regularUsers) {
            this.regularUsers = regularUsers;
            return this;
        }

        public UserStatisticsBuilder usersCreatedToday(Long usersCreatedToday) {
            this.usersCreatedToday = usersCreatedToday;
            return this;
        }

        public UserStatisticsBuilder usersCreatedThisWeek(Long usersCreatedThisWeek) {
            this.usersCreatedThisWeek = usersCreatedThisWeek;
            return this;
        }

        public UserStatisticsBuilder usersCreatedThisMonth(Long usersCreatedThisMonth) {
            this.usersCreatedThisMonth = usersCreatedThisMonth;
            return this;
        }

        public UserStatisticsBuilder usersUpdatedToday(Long usersUpdatedToday) {
            this.usersUpdatedToday = usersUpdatedToday;
            return this;
        }

        public UserStatisticsBuilder usersUpdatedThisWeek(Long usersUpdatedThisWeek) {
            this.usersUpdatedThisWeek = usersUpdatedThisWeek;
            return this;
        }

        public UserStatisticsBuilder usersUpdatedThisMonth(Long usersUpdatedThisMonth) {
            this.usersUpdatedThisMonth = usersUpdatedThisMonth;
            return this;
        }

        public UserStatistics build() {
            return new UserStatistics(totalUsers, adminUsers, regularUsers,
                    usersCreatedToday, usersCreatedThisWeek, usersCreatedThisMonth,
                    usersUpdatedToday, usersUpdatedThisWeek, usersUpdatedThisMonth);
        }
    }

    // // Getters and Setters
    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(Long adminUsers) {
        this.adminUsers = adminUsers;
    }

    public Long getRegularUsers() {
        return regularUsers;
    }

    public void setRegularUsers(Long regularUsers) {
        this.regularUsers = regularUsers;
    }

    public Long getUsersCreatedToday() {
        return usersCreatedToday;
    }

    public void setUsersCreatedToday(Long usersCreatedToday) {
        this.usersCreatedToday = usersCreatedToday;
    }

    public Long getUsersCreatedThisWeek() {
        return usersCreatedThisWeek;
    }

    public void setUsersCreatedThisWeek(Long usersCreatedThisWeek) {
        this.usersCreatedThisWeek = usersCreatedThisWeek;
    }

    public Long getUsersCreatedThisMonth() {
        return usersCreatedThisMonth;
    }

    public void setUsersCreatedThisMonth(Long usersCreatedThisMonth) {
        this.usersCreatedThisMonth = usersCreatedThisMonth;
    }

    public Long getUsersUpdatedToday() {
        return usersUpdatedToday;
    }

    public void setUsersUpdatedToday(Long usersUpdatedToday) {
        this.usersUpdatedToday = usersUpdatedToday;
    }

    public Long getUsersUpdatedThisWeek() {
        return usersUpdatedThisWeek;
    }

    public void setUsersUpdatedThisWeek(Long usersUpdatedThisWeek) {
        this.usersUpdatedThisWeek = usersUpdatedThisWeek;
    }

    public Long getUsersUpdatedThisMonth() {
        return usersUpdatedThisMonth;
    }

    public void setUsersUpdatedThisMonth(Long usersUpdatedThisMonth) {
        this.usersUpdatedThisMonth = usersUpdatedThisMonth;
    }
}
