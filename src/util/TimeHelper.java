package util;

/**
 * Created by zero on 2016/01/07.
 * Douyu
 */
public class TimeHelper {
    private int timeout = 0;
    private long last = 0;

    public TimeHelper(int timeout) {
        this.timeout = timeout;
        last = System.currentTimeMillis();
    }

    public boolean checkTimeout() {
        long now = System.currentTimeMillis();

        boolean r = (now - last) > timeout;
        last = now;
        return r;
    }
}
