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
}
