// ItemDto.java
package ru.practicum.item;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Long ownerId; // Keep this for the controller's input
    private Boolean available;
    private Long requestId; // Keep this
}