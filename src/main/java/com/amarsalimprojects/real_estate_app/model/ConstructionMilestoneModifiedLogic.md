Ah, thank you for the clarification!

You're saying:

> Instead of each stage having a fixed **weight** toward total progress (like FOUNDATION = 10%), you want each stage to be **mapped to an absolute progress milestone** (like:
> FOUNDATION = 0%, ROOFING = 50%, FINAL\_INSPECTION = 80%, COMPLETED = 100%).

This is an even simpler and more **business-intuitive** approach — and it fits your model perfectly.

---

## ✅ Final Model (Stage ↔ Milestone %)

### ✅ 1. Define `ConstructionStage` with **progress milestones**

```java
public enum ConstructionStage {
    NOT_STARTED(0),
    FOUNDATION(0),
    STRUCTURE(30),
    ROOFING(50),
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
```

---

### ✅ 2. Store only `currentStage` per unit

You've already got this in your `Unit.java`:

```java
@Enumerated(EnumType.STRING)
private ConstructionStage currentStage;
```

No need for `Set<ConstructionStage>` unless you want to track history.

---

### ✅ 3. Calculate Unit Progress (Single Line)

In a utility or service method:

```java
public int calculateUnitProgress(Unit unit) {
    return unit.getCurrentStage().getMilestone();
}
```

---

### ✅ 4. Project Progress = Average of All Unit Progresses, Capped at 90%

```java
public float calculateProjectProgress(Project project) {
    List<Unit> units = project.getUnits();
    if (units == null || units.isEmpty()) return 0;

    int total = 0;
    for (Unit unit : units) {
        total += calculateUnitProgress(unit);
    }

    float avg = (float) total / units.size();
    return Math.min(avg, 90); // Final 10% unlocked via admin sign-off
}
```

---

## ✅ Project Completion by Admin

In your `ProjectService`:

```java
public void approveFinalCompletion(Project project) {
    project.setAdminSignedOff(true);
    project.setConstructionProgress(100f);
    project.setCompletionDate(LocalDateTime.now());
    project.setStatus(ProjectStatus.COMPLETED);
    projectRepository.save(project);
}
```

---

## ✅ Final Summary

| Feature                       | Implementation                     |
| ----------------------------- | ---------------------------------- |
| Unit progress by stage        | `ConstructionStage.getMilestone()` |
| Project progress              | Average of unit milestones         |
| Cap at 90%                    | `Math.min(..., 90)` logic          |
| Admin completes last 10%      | Via `adminSignedOff = true`        |
| Frontend displayable progress | `project.getDisplayableProgress()` |

---

### 🧠 Bonus Suggestion

In your frontend or dashboard, show a badge like:

```text
ROOFING → 50% Complete
FINAL_INSPECTION → 80% Complete
```

based directly on `unit.getCurrentStage().getMilestone()`.

---

Would you like:

* Full updated `ConstructionStage.java`?
* Service class with all calculations ready?
* Example REST endpoint to update unit progress?

Let me know where you'd like to go next!
