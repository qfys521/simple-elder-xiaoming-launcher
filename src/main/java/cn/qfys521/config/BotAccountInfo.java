package cn.qfys521.config;

import cn.chuanwise.toolkit.preservable.AbstractPreservable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotAccountInfo extends AbstractPreservable {
    long adminId;
    String type;
    boolean positive;
    boolean reversed;
    Integer port;
    String addr;
    String token;


}
