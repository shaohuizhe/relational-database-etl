# relational-database-etl

##### 一怒之下自己写了个关系型数据库之间的ETL工具

##### 附加一个清晰简洁的demo（假设我们只关注这一个功能)



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

