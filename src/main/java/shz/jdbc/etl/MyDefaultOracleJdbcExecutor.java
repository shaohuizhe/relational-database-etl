package shz.jdbc.etl;

import shz.Help;
import shz.jdbc.MetaObject;
import shz.jdbc.db.DefaultOracleJdbcExecutor;

import java.time.Instant;

public class MyDefaultOracleJdbcExecutor extends DefaultOracleJdbcExecutor {
    @Override
    protected void insertFill(MetaObject metaObject) {
        Object id = metaObject.getFieldValByName("id");
        if (id == null) metaObject.setFieldValByName("id", Help.uuid());
        Object createTime = metaObject.getFieldValByName("createTime");
        if (createTime == null) metaObject.setFieldValByName("createTime", Instant.now());
        Object isDelete = metaObject.getFieldValByName("isDelete");
        if (isDelete == null) metaObject.setFieldValByName("isDelete", 1);
    }

    @Override
    protected void updateFill(MetaObject metaObject) {
        Object updateTime = metaObject.getFieldValByName("updateTime");
        if (updateTime == null) metaObject.setFieldValByName("updateTime", Instant.now());
    }
}
