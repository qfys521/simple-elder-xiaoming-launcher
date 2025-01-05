package config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotAccountInfo {
    long adminId;
    String type;
    boolean positive;
    boolean reversed;
    Integer port;
    String addr;
    String token;


}
