package friday;

class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String getDescription() {
        return this.description;
    }

    public void changeStatus(String option) {
        if ("mark".equals(option)) {
            this.isDone = true;
        } else if ("unmark".equals(option)) {
            this.isDone = false;
        }
    }

    /**
     * Returns this task encoded as a single line for storage on disk,
     * e.g. "T | 1 | read book". Subclasses override this to add their
     * own fields (such as a deadline date or event timing).
     */
    public String toFileFormat() {
        return "? | " + (isDone ? "1" : "0") + " | " + description;
    }
}
