package cn.qfys521;

import cn.chuanwise.toolkit.preservable.loader.FileLoader;
import cn.chuanwise.toolkit.serialize.json.JacksonSerializer;
import cn.chuanwise.xiaoming.bot.XiaoMingBot;
import cn.chuanwise.xiaoming.bot.XiaoMingBotImpl;
import cn.chuanwise.xiaoming.launcher.XiaoMingLauncher;
import cn.qfys521.config.BotAccountInfo;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.mrxiaom.overflow.BotBuilder;

@Getter
@Setter
public class SimpleXiaoMingBotLauncherImpl implements XiaoMingLauncher {
    private static final Logger log = LoggerFactory.getLogger("XiaoMingBot");
    private final FileLoader fileLoader = new FileLoader(new JacksonSerializer());
    private final Scanner scanner = new Scanner(System.in);
    private XiaoMingBot xiaoMingBot;
    private BotAccountInfo botAccount = fileLoader.loadOrSupply(BotAccountInfo.class, new File("bot-config.json"), BotAccountInfo::new);

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public boolean launch() {
        try {
            return loadBotAccount();
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private void onFirstInit() {
        log.warn("这是你初次启动小明吗？小明找不到或无法读取配置文件，我们现在就开始配置吧！");
        log.info("首先呢，请输入Bot的管理员: ");
        botAccount.setAdminId(Long.parseLong(scanner.nextLine()));
        log.info("请输入你的登陆方式:\n1,正向WebSocket 2,反向WebSocket");

        int type;
        while (true) {
            String inp = scanner.nextLine();
            if (inp.matches("\\d+")) {
                type = Integer.parseInt(inp);
                botAccount.saveOrFail();
                break;
            } else {
                log.error("请重新输入");
            }
        }

        if (type == 1) {
            botAccount.setPositive(true);
            log.info("token:");
            botAccount.setToken(scanner.nextLine());
            log.info("正向ws URL：");
            botAccount.setAddr(scanner.nextLine());
            botAccount.setType("Positive");
        } else if (type == 2) {
            botAccount.setReversed(true);
            log.info("token: ");
            botAccount.setToken(scanner.nextLine());
            log.info("反向ws PORT：");
            botAccount.setPort(Integer.parseInt(scanner.nextLine()));
            botAccount.setType("Reversed");
        } else {
            throw new RuntimeException();
        }

        botAccount.saveOrFail();
        log.info("已应用配置。请重新启动机器人吧！");
    }

    private boolean loadBotAccount() {
        if (botAccount == null) {
            onFirstInit();
            return false;
        }

        if (botAccount.isPositive() || "Positive".equals(botAccount.getType())) {
            if (botAccount.getAdminId() == 0 || Objects.isNull(botAccount.getAddr())) {
                onFirstInit();
                return false;
            }
            return initializeBot(BotBuilder.positive(botAccount.getAddr()));
        } else if (botAccount.isReversed() || "Reversed".equals(botAccount.getType())) {
            if (botAccount.getAdminId() == 0 || Objects.isNull(botAccount.getToken()) || botAccount.getPort() == 0) {
                onFirstInit();
                return false;
            }
            return initializeBot(BotBuilder.reversed(botAccount.getPort()));
        } else {
            onFirstInit();
            return false;
        }
    }

    private boolean initializeBot(BotBuilder botBuilder) {
        try {
            xiaoMingBot = new XiaoMingBotImpl(botBuilder.token(botAccount.getToken()).connect());
            return true;
        } catch (Exception e) {
            getLogger().error(e.toString());
            return false;
        }
    }

    @Override
    public void stop() {
        xiaoMingBot.stop();
    }
}