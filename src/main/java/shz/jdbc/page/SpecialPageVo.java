package shz.jdbc.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shz.ToList;
import shz.jdbc.PageInfo;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@ToString
public class SpecialPageVo<T> {
    private int current;
    private String currentFirstId;
    private String currentLastId;
    private int total;
    private int pages;
    private List<T> data;

    @SuppressWarnings("unchecked")
    public static <T, VO> SpecialPageVo<VO> of(PageInfo<T> page, Function<T, VO> mapping) {
        SpecialPageVo<VO> vo = new SpecialPageVo<>();
        vo.current = page.getCurrent();
        vo.currentFirstId = page.getCurrentFirstId();
        vo.currentLastId = page.getCurrentLastId();
        vo.total = page.getTotal();
        vo.pages = page.getPages();
        List<T> data = page.getData();
        vo.data = data.isEmpty()
                ? Collections.emptyList()
                : mapping == null ? (List<VO>) data : ToList.collect(data.stream().map(mapping));
        return vo;
    }
}
