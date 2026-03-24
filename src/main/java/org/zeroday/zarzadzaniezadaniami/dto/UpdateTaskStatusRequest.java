package org.zeroday.zarzadzaniezadaniami.dto;

import jakarta.validation.constraints.NotNull;
import org.zeroday.zarzadzaniezadaniami.model.TaskStatus;

public record UpdateTaskStatusRequest(
        @NotNull(message = "Status nie może być pusty")
        TaskStatus taskStatus
) {
}
