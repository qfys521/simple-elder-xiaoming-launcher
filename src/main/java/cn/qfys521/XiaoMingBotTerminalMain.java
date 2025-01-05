import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.PropertyConfigurator;

@Slf4j
@Getter
@Setter
public class XiaoMingBotTerminalMain {
    public static void main(String[] args) {
        final Charset defaultCharset = Charset.defaultCharset();
        try(InputStream loggerPropertyStream = XiaoMingBotTerminalMain.class.getClassLoader().getResourceAsStream("logger/" + defaultCharset.name().toLowerCase() + ".properties")) {

            if (Objects.isNull(loggerPropertyStream)) {
                log.error("当前系统编码为 {}，未找到该编码的日志配置。", defaultCharset.name());
                log.error("解决方案见 https://chuanwise.cn:10074/#/question");
                log.error("current default charset is {}, can not find the matchable slf4j configuration file.", defaultCharset.name());
                return;
            } else {
                PropertyConfigurator.configure(loggerPropertyStream);
                log.info("当前系统编码为 {}，已加载此编码配置文件", defaultCharset.name());
                log.info("current default charset is {}, slf4j logger configured.", defaultCharset.name());
            }

            final File directory = new File("launcher");
            if (!directory.isDirectory() && !directory.mkdirs()) {
                log.error("无法创建启动器配置文件夹：{}", directory.getAbsolutePath());
                return;
            }
        } catch (IOException e) {
            log.error("无法启动小明");
            log.error(e.getMessage(), e);
            System.exit(-1);
        }

        final SimpleXiaoMingBotLauncherImpl xiaoMingBotLauncher = new SimpleXiaoMingBotLauncherImpl();
        if (xiaoMingBotLauncher.launch()){
            try {
                xiaoMingBotLauncher.start();
                xiaoMingBotLauncher.getXiaoMingBot().getAccountManager().createAccount(xiaoMingBotLauncher.getBotAccount().getAdminId()).setAdministrator(true);
            }catch (Exception e){
                log.error(e.getMessage(), e);
                System.exit(-1);
            }
        }
    }
}
