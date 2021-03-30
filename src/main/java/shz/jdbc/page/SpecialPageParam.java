package shz.jdbc.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shz.jdbc.PageInfo;

@Getter
@Setter
@ToString
public class SpecialPageParam {
    private int page = 1;
    private int size = 10;
    /**
     * 下面三个属性取SpecialPageVo对应的值，当page = 1 时可不传值
     */
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
