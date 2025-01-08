package cn.qfys521;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class XiaoMingBotTerminalMain {
    private static final Logger log = LoggerFactory.getLogger("XiaoMingBot");

    public static void main(String[] args) {
        SimpleXiaoMingBotLauncherImpl xiaoMingBotLauncher = new SimpleXiaoMingBotLauncherImpl();
        if (xiaoMingBotLauncher.launch()) {
            startBot(xiaoMingBotLauncher);
        }
    }

    private static void startBot(SimpleXiaoMingBotLauncherImpl xiaoMingBotLauncher) {
        try {
            xiaoMingBotLauncher.start();
            xiaoMingBotLauncher.getXiaoMingBot().getAccountManager()
                    .createAccount(xiaoMingBotLauncher.getBotAccount().getAdminId())
                    .setAdministrator(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(-1);
        }
    }
}