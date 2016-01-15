# DouyuCrawler
一个 Java 程序，用于抓取斗鱼弹幕等数据。

# 思路介绍

具体过程介绍见我的博客：[抓取斗鱼直播弹幕 - Brucezz's Blog](http://brucezz.github.io/articles/2016/01/11/douyu-crawler/)

# 用法

程序入口为 `me.brucezz.crawler.Main`

需要自己配置 `conf.properties` 文件：

- `debug` ： 决定是否显示详细的调试信息
- `db.X` ： 数据库（MySQL）相关设置
- `room.url.X` ： 抓取房间列表， 可同时抓取多个房间


# Todo

- 持久化Log信息到本地
- 数据库操作转化为异步，消除潜在的效率影响
- 寻求更佳的程序退出逻辑（目前为死循环不断抓取，没有结束条件，只能通过键盘中断或者终止进程）
- 寻求多种模式抓取（比如抓取人气最高的几个房间，或对类别相同的房间进行抓取）
- 解析多种数据（比如鱼丸，火箭等礼物的赠送数据）



# 欢迎提各种 issue & pull request

- Email: [im.brucezz@gmail.com](mailto:im.brucezz@gmail.com)
- 知乎: [Brucezz](https://www.zhihu.com/people/zeroZh)

