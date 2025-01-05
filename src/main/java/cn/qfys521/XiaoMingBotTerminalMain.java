package cn.qfys521;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class XiaoMingBotTerminalMain {
    final static Logger log = LoggerFactory.getLogger("XiaoMingBot");

    public static void main(String[] args) {
        final SimpleXiaoMingBotLauncherImpl xiaoMingBotLauncher = new SimpleXiaoMingBotLauncherImpl();
        if (xiaoMingBotLauncher.launch()) {
            try {
                xiaoMingBotLauncher.start();
                xiaoMingBotLauncher.getXiaoMingBot().getAccountManager().createAccount(xiaoMingBotLauncher.getBotAccount().getAdminId()).setAdministrator(true);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                System.exit(-1);
            }
        }
    }
}
