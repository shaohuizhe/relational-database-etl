# 冰冻三尺，非一日之寒

#### 关系型数据库之ETL

```
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


        DBHelp.getDefaultMappingJdbcServer(sourceUrl, sourceProp,
                //目标表与来源表字段映射
                ToMap.get(16)
                        .put("XXX", "XXX_XXX")
                        .put("YYY", "YYY_YYY")
                        .put("ZZZ", "ZZZ_ZZZ")
                        .build()
        ).copyTo(JdbcServer.of(new MyDefaultOracleJdbcExecutor(), sinkUrl, sinkProp), true, true,
                //目标表实体类
                Object.class,
                //拷贝总行数，为0全表拷贝
                0,
                map -> {
                    //获取源表XXX_XXX列
                    Object obj = map.get("XXX_XXX");
                    if (R.isBlank(obj)) return false;
                    int xxx = Help.atoi(obj.toString());
                    //过滤掉XXX_XXX不等于1的数据
                    if (xxx != 1) return false;
                    map.put("XXX_XXX", xxx);

                    obj = map.get("YYY_YYY");
                    if (R.isBlank(obj)) return false;
                    int yyy = Help.atoi(obj.toString());
                    if (yyy != 1
                            && yyy != 2
                            && yyy != 3
                            && yyy != 4) return false;
                    map.put("YYY_YYY", yyy);

                    obj = map.get("ZZZ_ZZZ");
                    if (R.isBlank(obj)) return false;
                    String zzz = obj.toString();
                    //将源库的男转为1，女转为2，其他转为0
                    switch (zzz) {
                        case "男":
                            map.put("ZZZ_ZZZ", 1);
                            break;
                        case "女":
                            map.put("ZZZ_ZZZ", 2);
                            break;
                        default:
                            map.put("ZZZ_ZZZ", 0);
                    }
                    return true;
                },
                info -> {
                    //实体类的过滤器
                    return true;
                },
                //每批次获取条数
                2000,
                //来源模式及表名
                TNP.of().tableSchem("模式").tableName("表名")
        );
```



### 多表查询一定要写xml吗？



#### 多表列表查询

```
public static class TApprovalProcessDo {
        private String id;
        private String projectId;
        private String projectName;
        private TApprovalLog log;
    }
```



```
jdbcServerWrapper.selectList(
                //查询的DO类
                TApprovalProcessDo.class,
                //select中的字符串都为DO类中的属性名，支持xxx.yyy.zzz
                MultiQueryWrapper.select("id", "projectId", "projectName", "log.id", "log.userName", "log.content")
                        //主表及别名（这里别名在条件构造器中使用）
                        .from(TApprovalProcess.class, "tap")
                        //关联表及别名（两个参数默认与主表关联，可选四个参数的指定两个关联表）
                        .innerJoin(TApprovalLog.class, "tal")
                        //关联的属性
                        .on("id", "approvalProcessId"),
                //查询条件(复杂的条件可使用Mybatis Plus的条件构造器构建)
                QueryUtil.getWrapper(dto)
        ).forEach(System.out::println);
```



#### 多表过滤查询

```
jdbcServerWrapper.filter(TApprovalProcessDo.class,
                domain -> {
                    //do类过滤
                    return true;
                },
                MultiQueryWrapper.select("id", "projectId", "projectName", "log.id", "log.userName", "log.content")
                        .from(TApprovalProcess.class, "tap")
                        .innerJoin(TApprovalLog.class, "tal")
                        .on("id", "approvalProcessId")
        ).forEach(System.out::println);
```



#### 多表特殊排序分页查询API模板

```
@Api(tags = "test")
@Validated
@RestController
@RequestMapping("audit/test")
public class TestController {
    @Autowired
    JdbcServerWrapper jdbcServerWrapper;

    @GetMapping("page")
    @ApiOperation("page_test")
    public JsonResult<SpecialPageVo<TApprovalProcessDo>> page(@Validated TApprovalLogQueryDto dto, SpecialPageParam pageParam) {
        return JsonResult.ok(jdbcServerWrapper.page(TApprovalProcessDo.class, pageParam,
                (t, u) -> {
                    //排序比较器,这里以id倒序排序
                    long cmp = Long.parseLong(t.getId()) - Long.parseLong(u.getId());
                    return cmp > 0 ? 1 : cmp < 0 ? -1 : 0;
                },
                //select中的字符串都为DO类中的属性名，支持xxx.yyy.zzz
                MultiQueryWrapper.select("id", "projectId", "projectName", "log.id", "log.userName", "log.content")
                		//主表及别名（这里别名在条件构造器中使用）
                        .from(TApprovalProcess.class, "tap")
                        //关联表及别名（两个参数默认与主表关联，可选四个参数的指定两个关联表）
                        .innerJoin(TApprovalLog.class, "tal")
                        //关联的属性，on结束之后可继续使用innerJoin方法关联其他表
                        .on("id", "approvalProcessId"),
                //此处条件构造器使用MybatisPlus的即可，无需重复造轮子
                QueryUtil.getWrapper(dto)
        ));
    }
}
```

