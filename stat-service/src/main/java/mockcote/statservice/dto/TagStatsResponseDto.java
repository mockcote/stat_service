package mockcote.statservice.dto;

import java.util.List;

public class TagStatsResponseDto {
    private int count;
    private List<TagStatsItemDto> items;

    // Getters and Setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TagStatsItemDto> getItems() {
        return items;
    }

    public void setItems(List<TagStatsItemDto> items) {
        this.items = items;
    }
}
