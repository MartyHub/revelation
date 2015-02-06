package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Mandatory;
import org.joda.time.LocalDate;

public class DateCommand {

    public static interface HasDateCommand {

        DateCommand getDateCommand();
    }

    @Mandatory
    @Description("Context date")
    private LocalDate date;

    public DateCommand() {
    }

    public DateCommand(LocalDate defaultValue) {
        this.date = defaultValue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
