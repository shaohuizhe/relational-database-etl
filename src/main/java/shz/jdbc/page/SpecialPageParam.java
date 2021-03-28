package shz.jdbc.page;

import lombok.*;
import shz.jdbc.PageInfo;

@Getter
@Setter
@ToString
public class SpecialPageParam {
    private int page = 1;
    private int size = 10;
    private int current;

    private String currentFirstId;

    private String currentLastId;

    public final <T> PageInfo<T> toPageInfo() {
        PageInfo<T> pageInfo = PageInfo.of(page, size);
        pageInfo.setCurrent(current);
        pageInfo.setCurrentFirstId(currentFirstId);
        pageInfo.setCurrentLastId(currentLastId);
        return pageInfo;
    }
}
