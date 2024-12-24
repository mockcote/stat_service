package mockcote.statservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagStatsResponseDto {
    private int count;
    private List<TagStatsItemDto> items;
}
