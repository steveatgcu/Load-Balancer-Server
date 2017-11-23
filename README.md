# LoadBalancer

Java 课程设计 轻量级的集群负载均衡设计
这个是一个建立在应用层的负载分发软件，灵感来源于Nginx，通过各个客户端中的Client，获取各个客户端的状态，从而实现知道哪些服务器比较繁忙，把繁忙的服务器切出服务器列表，直至负载下降。

Client 服务器监视程序参考这里 : https://github.com/ousheobin/LoadBalancer-Client

# Developer Enviroment
- Eclipse 
- JDK 1.8

# Dependcies

- Commons pool2-2.4.2 
- dom4j 1.6.1
- fastjson 1.2.11  

- hamcrest-all 1.1  
- junit 4.12  

- log4j 1.2.17


# Developer Enviroment
- Eclipse 
- JDK 1.8

# Dependcies

- Commons pool2-2.4.2 
- dom4j 1.6.1
- fastjson 1.2.11  

- hamcrest-all 1.1  
- junit 4.12  

- log4j 1.2.17

# Notes
本项目也托管在 Git@OSC : https://gitee.com/ousheobun/load-balancer-server
