# watent-framework
```
IOC:Inversion of Control 控制反转 或 依赖倒置(反转)
反转：依赖对象的获得被反转了 反转为从IOC容器中获取(自动注入)
 
IOC创建管理Bean(本质是一个工厂) -> 具备提供获取Bean实例行为(参数返回值) 
-> 实例注册(如何创建) -> 注册行文 注册获取Bean -> Bean定义 告诉工厂如何创建

DI 构造器参数依赖
    getConstructorArgumentValues
   属性依赖
    getPropertyValues

Feature TODO
1.Bean别名支持 任意别名 别名唯一
2.BeanFactory 增加按Class来获取Bean对象的功能
3.加入配置参数加载 注入Bean的功能
4.加入Bean配置的条件依赖生效支持
        依赖的bean存在则配置才有效

```


