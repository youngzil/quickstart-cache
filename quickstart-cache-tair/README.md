淘宝分布式NOSQL框架：Tair




https://github.com/alibaba/tair









https://www.cnblogs.com/chenny7/p/4875396.html


https://www.oschina.net/p/tair

Tair是由淘宝网自主开发的Key/Value结构数据存储系统，在淘宝网有着大规模的应用。您在登录淘宝、查看商品详情页面或者在淘江湖和好友“捣浆糊”的时候，都在直接或间接地和Tair交互。

Tair的功能
Tair是一个Key/Value结构数据的解决方案，它默认支持基于内存和文件的两种存储方式，分别和我们通常所说的缓存和持久化存储对应。

Tair除了普通Key/Value系统提供的功能，比如get、put、delete以及批量接口外，还有一些附加的实用功能，使得其有更广的适用场景，包括：
Version支持>
原子计数器
Item支持







