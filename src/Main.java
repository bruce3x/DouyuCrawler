
import config.Config;
import thread.CrawlerThread;

/**
 * Created by zero on 2016/01/04.
 * Douyu
 */
public class Main {
    public static void main(String[] args) {

        if (!Config.loadSuccess) return;

        for (String roomUrl : Config.ROOM_LIST) {
            new Thread(new CrawlerThread(roomUrl)).start();
        }
    }
}
