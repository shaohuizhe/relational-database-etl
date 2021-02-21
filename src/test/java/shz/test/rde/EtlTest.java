package shz.test.rde;

import com.mit.jdbc.JdbcServer;
import com.mit.jdbc.TNP;
import com.mit.jdbc.db.DefaultOracleJdbcExecutor;
import shz.p.Validator;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 关系型数据库的etl测试
 */
public class EtlTest {

    public static void main(String[] args) {
        String sourceUrl = "jdbc:oracle:thin:@//xxx:1521/xxx";
        Properties sourceProp = System.getProperties();
        sourceProp.put("driverClassName", "oracle.jdbc.driver.OracleDriver");
        sourceProp.put("user", "xxx");
        sourceProp.put("password", "xxx");
        sourceProp.put("remarksReporting", "true");

        String sinkUrl = "jdbc:oracle:thin:@//xxx:1521/xxx";
        Properties sinkProp = System.getProperties();
        sinkProp.put("driverClassName", "oracle.jdbc.driver.OracleDriver");
        sinkProp.put("user", "xxx");
        sinkProp.put("password", "xxx");
        sinkProp.put("remarksReporting", "true");

        JdbcServer.of(
                new DefaultOracleJdbcExecutor() {
                    @Override
                    protected String columnName(Field field) {
                        //字段映射
                        String s = super.columnName(field);
                        switch (s) {
                            case "ID":
                                //将目标数据库的ID映射为源数据库的XXX
                                return "XXX";
                            case "NAME":
                                //将目标数据库的NAME映射为源数据库的XXX
                                return "XXX";
                            default:
                                //若字段相同则不处理
                                return s;
                        }
                    }
                }, sourceUrl, sourceProp
        ).copyTo(
                //执行拷贝、转换及装载
                //数据目标服务
                JdbcServer.of(sinkUrl, sinkProp),
                //结束后提交并关闭连接
                true,
                //批量插入是否提交
                true,
                //目标数据库表实体类
                KeyAreaInfo.class,
                //拷贝数据量，为0则全表拷贝
                0,
                //若为null则不过滤数据原样拷贝
                t -> {
                    //TODO 进行数据的过滤及转换
                    //例
                    //返回false表示不拷贝该条数据
                    if (t.getIsDelete() == null
                            || t.getIsDelete() == 1
                            || Validator.isBlank(t.getId())
                            || Validator.isBlank(t.getName())) return false;

                    if (t.getSortNo() == null) t.setSortNo(0);

                    return true;
                },
                //每批次获取2000条数据
                2000,
                //设置源数据库模式及表名
                TNP.of().tableSchem("模式").tableName("表名")
        );
    }
}
