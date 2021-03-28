package shz.test.rde.page;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import shz.jdbc.JdbcServer;
import shz.jdbc.PageInfo;
import shz.jdbc.sql.MultiQueryWrapper;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class JdbcServerWrapper {
    private final JdbcServer jdbcServer;

    public JdbcServerWrapper(JdbcServer jdbcServer) {
        this.jdbcServer = jdbcServer;
    }

    /**
     * 单表过滤查询（用于特殊查询）
     *
     * @param cls      实体类class对象
     * @param filter   实体类过滤器
     * @param columns  查询的列名
     * @param orderBys 排序的列
     */
    public final <T> List<T> filter(Class<T> cls, Predicate<T> filter, List<String> columns, List<String> orderBys) {
        return jdbcServer.apply((ep, executor) -> executor.filter(
                ep, cls, 0, filter, 1000, columns, null, orderBys
        ));
    }

    /**
     * 单表过滤查询（查询所有列，用于特殊查询）
     */
    public final <T> List<T> filter(Class<T> cls, Predicate<T> filter) {
        return filter(cls, filter, null, null);
    }

    /**
     * 单表分页查询（在wrapper过滤之后再经过filter的特殊过滤，使用comparator进行特殊排序）
     *
     * @param cls        实体类class对象
     * @param pageParam  分页参数
     * @param filter     实体类过滤器
     * @param comparator 实体类比较器
     * @param columns    查询的列名
     * @param wrapper    查询条件
     * @param mapping    实体类与Vo类映射关系
     */
    public final <T, VO> SpecialPageVo<VO> page(Class<T> cls, SpecialPageParam pageParam, Predicate<T> filter,
                                                BiFunction<T, T, Integer> comparator, List<String> columns,
                                                QueryWrapper<?> wrapper, Function<T, VO> mapping) {
        return jdbcServer.apply((ep, executor) -> SpecialPageVo.of(executor.page(
                ep, cls, pageParam.toPageInfo(), null, filter, comparator,
                1000, columns, MPWhereWrapper.of(wrapper)
        ), mapping));
    }

    /**
     * 单表分页查询（查询所有列，使用filter特殊过滤，使用comparator进行特殊排序）
     */
    public final <T, VO> SpecialPageVo<VO> page(Class<T> cls, SpecialPageParam pageParam, Predicate<T> filter,
                                                BiFunction<T, T, Integer> comparator, Function<T, VO> mapping) {
        return page(cls, pageParam, filter, comparator, (List<String>) null, null, mapping);
    }

    public final <T> SpecialPageVo<T> page(Class<T> cls, SpecialPageParam pageParam, Predicate<T> filter,
                                           BiFunction<T, T, Integer> comparator) {
        return page(cls, pageParam, filter, comparator, (List<String>) null, null, null);
    }

    /**
     * 单表分页查询（查询所有列，使用comparator进行特殊排序）
     */
    public final <T, VO> SpecialPageVo<VO> page(Class<T> cls, SpecialPageParam pageParam, BiFunction<T, T, Integer> comparator,
                                                QueryWrapper<?> wrapper, Function<T, VO> mapping) {
        return page(cls, pageParam, null, comparator, (List<String>) null, wrapper, mapping);
    }

    public final <T> SpecialPageVo<T> page(Class<T> cls, SpecialPageParam pageParam, BiFunction<T, T, Integer> comparator,
                                           QueryWrapper<?> wrapper) {
        return page(cls, pageParam, null, comparator, (List<String>) null, wrapper, null);
    }

    /**
     * 多表列表查询
     *
     * @param select 查询的列与关联的表，通过MultiQueryWrapper的静态方法select构造
     */
    public final <DO> List<DO> selectList(Class<DO> cls, MultiQueryWrapper.From select, QueryWrapper<?> wrapper,
                                          List<String> orderBys) {
        return jdbcServer.apply((ep, executor) -> executor.selectList(
                ep, cls, false, select, MPWhereWrapper.of(wrapper), orderBys
        ));
    }

    public final <DO> List<DO> selectList(Class<DO> cls, MultiQueryWrapper.From select, QueryWrapper<?> wrapper) {
        return selectList(cls, select, wrapper, null);
    }

    /**
     * 多表分页查询
     */
    public final <DO> PageInfo<DO> page(Class<DO> cls, PageInfo<DO> pageInfo, MultiQueryWrapper.From select,
                                        QueryWrapper<?> wrapper, List<String> orderBys) {
        return jdbcServer.apply((ep, executor) -> executor.page(
                ep, cls, pageInfo, false, select, MPWhereWrapper.of(wrapper), orderBys
        ));
    }

    public final <DO> PageInfo<DO> page(Class<DO> cls, PageInfo<DO> pageInfo, MultiQueryWrapper.From select,
                                        QueryWrapper<?> wrapper) {
        return page(cls, pageInfo, select, wrapper, null);
    }

    /**
     * 多表过滤查询（用于特殊查询）
     */
    public final <DO> List<DO> filter(Class<DO> cls, Predicate<DO> filter, MultiQueryWrapper.From select,
                                      QueryWrapper<?> wrapper, List<String> orderBys) {
        return jdbcServer.apply((ep, executor) -> executor.filter(
                ep, cls, 0, null, filter, 1000, select, MPWhereWrapper.of(wrapper), orderBys
        ));
    }

    /**
     * 多表过滤查询（用于特殊查询）
     */
    public final <DO> List<DO> filter(Class<DO> cls, Predicate<DO> filter, MultiQueryWrapper.From select) {
        return filter(cls, filter, select, null, null);
    }

    /**
     * 多表分页查询（用于特殊查询及排序）
     */
    public final <DO, VO> SpecialPageVo<VO> page(Class<DO> cls, SpecialPageParam pageParam, Predicate<DO> filter,
                                                                            BiFunction<DO, DO, Integer> comparator, MultiQueryWrapper.From select,
                                                                            QueryWrapper<?> wrapper, Function<DO, VO> mapping) {
        return jdbcServer.apply((ep, executor) -> SpecialPageVo.of(executor.page(
                ep, cls, pageParam.toPageInfo(), null, filter, comparator,
                1000, select, MPWhereWrapper.of(wrapper)
        ), mapping));
    }

    /**
     * 多表分页查询（用于特殊查询及排序）
     */
    public final <DO, VO> SpecialPageVo<VO> page(Class<DO> cls, SpecialPageParam pageParam, Predicate<DO> filter,
                                                                            BiFunction<DO, DO, Integer> comparator, MultiQueryWrapper.From select,
                                                                            Function<DO, VO> mapping) {
        return page(cls, pageParam, filter, comparator, select, null, mapping);
    }

    public final <DO> SpecialPageVo<DO> page(Class<DO> cls, SpecialPageParam pageParam, Predicate<DO> filter,
                                                                        BiFunction<DO, DO, Integer> comparator, MultiQueryWrapper.From select) {
        return page(cls, pageParam, filter, comparator, select, null, null);
    }

    /**
     * 多表分页查询（用于特殊排序）
     */
    public final <DO, VO> SpecialPageVo<VO> page(Class<DO> cls, SpecialPageParam pageParam,
                                                                            BiFunction<DO, DO, Integer> comparator, MultiQueryWrapper.From select,
                                                                            QueryWrapper<?> wrapper, Function<DO, VO> mapping) {
        return page(cls, pageParam, null, comparator, select, wrapper, mapping);
    }

    public final <DO> SpecialPageVo<DO> page(Class<DO> cls, SpecialPageParam pageParam, BiFunction<DO, DO, Integer> comparator,
                                                                        MultiQueryWrapper.From select, QueryWrapper<?> wrapper) {
        return page(cls, pageParam, null, comparator, select, wrapper, null);
    }
}
