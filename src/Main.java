
import config.Config;
import thread.CrawlerThread;

import java.util.Set;

/**
 * Created by zero on 2016/01/04.
 * Douyu
 */
public class Main {
    public static void main(String[] args) {

        if (!Config.loadSuccess) return;

        Set<String> nameSet = Config.ROOM_MAP.keySet();

        for (String name : nameSet) {
            new Thread(new CrawlerThread(name, Config.ROOM_MAP.get(name))).start();
        }
    }
}
