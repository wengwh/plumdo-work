## 流程引擎接口
```
1.使用flowable6.3.0做引擎
2.自己封装rest接口，提供外部调用
3.模型类，使用act_re_model，标识做数据的归属，根据版本做累加，修改key，全部key修改，删除全部key删除
4.人员权限和业务系统集成，主流可能是用视图，考虑到后续的分库，微服务的理念，重写EntityManager，内部通过调用接口获取数据，通过集成IdmEngineAutoConfiguration，设置重写的manager

```

