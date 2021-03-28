package shz.jdbc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import shz.jdbc.sql.WhereWrapper;
import shz.p.Validator;

import java.util.Map;

public class MPWhereWrapper implements WhereWrapper {
    @Override
    public String sql() {
        return sql;
    }

    @Override
    public Object[] values() {
        return values;
    }

    private final String sql;
    private final Object[] values;

    private MPWhereWrapper(String sql, Object[] values) {
        this.sql = sql;
        this.values = values;
    }

    public static MPWhereWrapper of(QueryWrapper<?> wrapper) {
        if (wrapper == null) {
            return null;
        }

        String customSqlSegment = wrapper.getCustomSqlSegment();
        if (StringUtils.isBlank(customSqlSegment)) {
            return new MPWhereWrapper("", Validator.emptyArray());
        }

        Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
        int size = paramNameValuePairs.size();
        if (size == 0) {
            return new MPWhereWrapper("", Validator.emptyArray());
        }

        String sql = " " + customSqlSegment.replaceAll("#\\{ew\\.paramNameValuePairs\\.MPGENVAL\\d+\\}", "?");
        Object[] values = new Object[size];
        for (int i = 1; i <= size; ++i) {
            values[i - 1] = paramNameValuePairs.get(String.format("MPGENVAL%d", i));
        }
        return new MPWhereWrapper(sql, values);
    }
}
