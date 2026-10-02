package friday;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    protected LocalDate by;

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] "
                + description + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Stores {@code by} in LocalDate's default ISO format (yyyy-MM-dd)
     * rather than the display format above, so it can always be parsed
     * back unambiguously with {@link LocalDate#parse(CharSequence)}.
     */
    @Override
    public String toFileFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}