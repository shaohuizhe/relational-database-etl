package shz.test.rde;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@ToString
public final class KeyAreaInfo {
    private String id;
    private String creator;
    private Instant createTime;
    private String updater;
    private Instant updateTime;
    private Short isDelete;
    private String gridId;
    private String name;
    private String detailedAddress;
    private BigDecimal floorArea;
    private String remarks;
    private Integer sortNo;
    private String subwayExitIds;
    private String busStationIds;
    private Byte show;
}
