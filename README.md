
# 说明

使用springboot+netty 实现了rpc框架demo，参考了dubbo、xxl-job，临时整的代码、可能有bug，代码有点乱，仅供学习使用。

# 使用

本项目使用maven，导入即可用，不需安装其他的软件如 zookeeper。自己简单实现了注册中心。

# 实现技术

相关使用例子参考nrpc-test模块本框架主要实现的技术如下：


## netty 相关

  - 异步IO
 - Reactor多线程模型
 - http粘包拆包
 - 序列化反序列化
 - 零拷贝
  。。。。。。。。。。。。。。。。


## springboot相关

 - 自定义NRpcReference注解、并实现在容器创建过程中对@NRpcReference标注的属性进行注入。

 - 自定义 NRpcService 注解、实现在容器创建过程中扫描所有的@NRpcService注解类、加入到spring容器中。

-  还实现了@EnableNRpc，@EnableNRpcConfig，@NRpcComponentScan等接口

 - 实现spring.factories的自动配置

 - 手写starter

 - java多线程ThreadPoolExecutor、锁等

 - 负载均衡算法

 - 自己实现注册中心
  。。。。。。。。。。。。。。。。。


## 还没时间处理的问题

- consumer 没有定时更新 provider列表
- 注册时，一个微服务只注册一个节点appid，然后对于service列表根据appid注册。这样对于注册中心就没有provider和consumer的区别
- 对于项目包结构，以及线程的管理 要整理一下，例如在destory时要关闭线程池或线程，清除相关缓存等操作
- 获取服务列表等相关与注册中心有关的操作要优化
。。。。。。


  
  ## 模块

  
- nrpc   #nrpc 模块
  * nrpc-common  #公共模块
  * nrpc-core    #核心模块
  * nrpc-log     #日志模块
  * nrpc-registry    #注册中心模块
  * nrpc-spring-boot-autoconfigure   #springboot 自动配置模块
  * nrpc-spring-boot-starter     #springboot starter模块
- nrpc-test             #测试nrpc模块
  * nrpc-test-api    #api接口
  * nrpc-test-consumer  #测试消费者模块
  * nrpc-test-provider    #测试生产者模块
            
## 代码
### nrpc-common
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201027160824209.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMzNzgzMDY=,size_16,color_FFFFFF,t_70#pic_center)
### nrpc-core
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201027160834994.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMzNzgzMDY=,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201027160842439.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMzNzgzMDY=,size_16,color_FFFFFF,t_70#pic_center)
### nrpc-log
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201027160851569.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMzNzgzMDY=,size_16,color_FFFFFF,t_70#pic_center)
### nrpc-registry
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201027160858414.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMzNzgzMDY=,size_16,color_FFFFFF,t_70#pic_center)
### autoconfigure
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201027160908225.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMzNzgzMDY=,size_16,color_FFFFFF,t_70#pic_center)
### nrpc-test
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201027160914893.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMzNzgzMDY=,size_16,color_FFFFFF,t_70#pic_center)
