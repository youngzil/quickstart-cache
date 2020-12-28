maxTotal	资源池中的最大连接数	默认值8
maxIdle	资源池允许的最大空闲连接数	默认值8
minIdle	资源池确保的最少空闲连接数	默认值0


maxIdle实际上才是业务需要的最大连接数，maxTotal是为了给出余量，所以maxIdle不要设置过小，否则会有new Jedis(新连接)开销，而minIdle是为了控制空闲资源监测。


maxIdle数量过小，maxTotal比较大，业务不报错，但是产生大量的短连接



参考  
https://tech.antfin.com/docs/2/98726  
https://blog.csdn.net/lavorange/article/details/84448910  
https://jjlu521016.github.io/2018/12/09/JedisPool%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98.html  
https://blog.csdn.net/lixia0417mul2/article/details/107275108  
http://blog.didispace.com/JedisPool%E8%B5%84%E6%BA%90%E6%B1%A0%E4%BC%98%E5%8C%96/  




JedisPool的testOnBorrow、testOnReturn和testWhileIdle参数的实现原理



testOnBorrow  
如果池中存在空闲可用的连接实例，则需要连接时依据后进先出策略挑选一个连接实例，并激活返回给客户端。如果激活失败或者testOnBorrow被设置成true并且校验失败，则连接实例被废弃，继续检查下一个可用的连接实例，直到找到一个合法可用的连接实例或者连接池再没有可用的连接实例了。从中我们可以看出，像SpringBoot自动装配时默认设置这个参数为false是有道理的，testOnBorrow对性能的损耗是比较高的。


testOnReturn  
如果在业务层设置了maxIdle参数，并且池中的空闲实例数达到这个数值，返回的连接实例将被废弃。如果testOnReturn设置为true，连接实例在返回给连接池前需要做校验，如果校验失败，则实例废弃。否则，连接将被根据先进先出或者后进先出策略归还给线程池。


testWhileIdle和timeBetweenEvictionRunsMillis  
在生产环境中，这两个参数往往是配合使用的，大概意思是说每隔多长时间验证一下空闲的连接实例是否有效。试想如果让我们自己去实现，势必要围绕一个定时线程池做设计，事实上源码中也是这样实现的，源码中EvictionTimer空闲连接回收器正是通过ScheduledThreadPoolExecutor来定时做校验的




参考  
https://blog.csdn.net/u014495560/article/details/103576786

