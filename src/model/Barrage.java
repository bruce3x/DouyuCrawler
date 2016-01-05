package model;

/**
 * Created by zero on 2016/01/03.
 * Douyu
 */
public class Barrage {

    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Barrage(String name, String content) {

        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Barrage{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
