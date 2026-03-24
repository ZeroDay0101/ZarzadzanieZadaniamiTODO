package org.zeroday.zarzadzaniezadaniami.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(

        @NotBlank(message = "Tytuł nie może być pusty")
        @Size(max = 255, message = "Tytuł nie może przekraczać 255 znaków")
        String title,

        @Size(max = 1000, message = "Opis nie może przekraczać 1000 znaków")
        String description
) {
}
