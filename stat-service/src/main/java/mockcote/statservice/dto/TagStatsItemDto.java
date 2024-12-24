package mockcote.statservice.dto;

import lombok.Data;

@Data
public class TagStatsItemDto {
    private TagDto tag;
    private int total;
    private int solved;
    private int partial;
    private int tried;
}
