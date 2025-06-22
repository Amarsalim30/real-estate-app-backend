package com.amarsalimprojects.real_estate_app.model.enums;

public enum ConstructionStage {
    PLANNING(0),
    FOUNDATION(10),
    STRUCTURE(20),
    ROOFING(30),
    ELECTRICAL(40),
    FLOORING(50),
    PLUMBING(60),
    PLASTERING(65),
    FITTINGS(75),
    FINAL_INSPECTION(80),
    COMPLETED(100);

    private final int milestone;

    ConstructionStage(int milestone) {
        this.milestone = milestone;
    }

    public int getMilestone() {
        return milestone;
    }
}
