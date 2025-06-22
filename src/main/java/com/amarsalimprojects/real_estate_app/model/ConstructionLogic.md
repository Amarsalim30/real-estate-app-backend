Your current `Project` entity is strong, and you're headed in the right direction. Let's refine your structure with construction tracking features so that:

### ✅ You Can:

* Track **progress per unit** (e.g., unit A is 90% done, unit B is 80%)
* Automatically **calculate project-wide progress** as the average of all units
* Lock the final 10% of progress for **Admin-only approval**
* Eventually **mark a project as completed** when Admin signs off

---

## ✅ Step-by-Step Plan

### 1. **Update the `Unit` entity** to include construction progress.

```java
// Inside Unit.java
private float constructionProgress; // 0 - 100%

@Enumerated(EnumType.STRING)
private ConstructionStage constructionStage; // FOUNDATION, STRUCTURE, ROOFING, etc.
```

#### Recommended Enum: `ConstructionStage`

```java
public enum ConstructionStage {
    NOT_STARTED,
    FOUNDATION,
    STRUCTURE,
    ROOFING,
    PLASTERING,
    FITTINGS,
    FINAL_INSPECTION,
    COMPLETED
}
```

---

### 2. **Auto-calculate `Project.constructionProgress`** from unit progress

You'll do this in the **service layer**, not in the entity itself:

```java
public float calculateProjectProgress(Project project) {
    List<Unit> units = project.getUnits();
    if (units == null || units.isEmpty()) return 0;

    float totalProgress = 0f;
    for (Unit unit : units) {
        totalProgress += unit.getConstructionProgress();
    }

    float average = totalProgress / units.size();

    // Cap at 90%, Admin must approve the final 10%
    return Math.min(average, 90);
}
```

Then in your `ProjectService`, call this after updating units:

```java
project.setConstructionProgress(calculateProjectProgress(project));
projectRepository.save(project);
```

---

### 3. **Admin Completes Final 10% Manually**

Add this field to `Project.java`:

```java
private boolean adminSignedOff;
```

Then update your getter:

```java
public float getConstructionProgress() {
    if (adminSignedOff) {
        return 100f;
    }
    return Math.min(constructionProgress, 90f);
}
```

In your service:

```java
public void signOffProjectCompletion(Long projectId) {
    Project project = projectRepo.findById(projectId).orElseThrow();
    project.setAdminSignedOff(true);
    project.setStatus(ProjectStatus.COMPLETED);
    project.setCompletionDate(LocalDateTime.now());
    project.setConstructionProgress(100f);
    projectRepo.save(project);
}
```

---

## ✅ Final Suggested Additions to `Project.java`

```java
private boolean adminSignedOff; // True when admin approves last 10%
```

---

## ✅ Final Suggested Additions to `Unit.java`

```java
private float constructionProgress; // 0-100%

@Enumerated(EnumType.STRING)
private ConstructionStage constructionStage;
```

---

## 🧠 Business Logic Control

| Role           | Can Update Construction Progress | Can Sign Off Final 10% |
| -------------- | -------------------------------- | ---------------------- |
| Engineer/Clerk | ✅ (per unit)                     | ❌                      |
| Admin          | ✅                                | ✅                      |

---

## 🏁 Summary

Your final setup supports:

| Feature                                 | ✅ Implemented By                            |
| --------------------------------------- | ------------------------------------------- |
| Per-unit construction tracking          | `Unit.constructionProgress`                 |
| Per-unit stage label (Foundation, etc.) | `Unit.constructionStage`                    |
| Auto-calculated project average         | `ProjectService.calculateProjectProgress()` |
| 90% max cap logic                       | `Math.min(..., 90)`                         |
| Admin approval of final 10%             | `Project.adminSignedOff == true`            |
| Auto-marking as complete                | `Project.status = COMPLETED` on sign-off    |

---

Would you like:

* ✅ The updated full `Project.java` and `Unit.java` with all fields?
* ✅ Admin API endpoint to trigger sign-off?
* ✅ Dashboard-ready progress report logic?

Let me know and I’ll code out your next piece!
