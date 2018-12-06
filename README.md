# 工作流平台（未经同意禁止做商业用途）
> 流程，表单，报表，手动配置生成实际工作流

[![JDK](https://img.shields.io/badge/JDK-1.8-yellow.svg)](#)
[![License](http://img.shields.io/:license-apache2-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

#### [Github](https://github.com/wengwh/plumdo-work) | [Gitee](https://gitee.com/wengwh/plumdo-work)

**交流群:** 717933986


## Demo 演示
[系统控制台](http://work.plumdo.com) 

[表单设计器](https://wengwh.github.io/plumdo-work)


![Aaron Swartz](https://raw.githubusercontent.com/wengwh/plumdo-work/master/docs/design.png)

## 模块介绍
>  前端工程

| 模块名称      |          备注说明           |
| :---------:   | :-------------------------: |
| work-admin    |          管理台        |
| form-modeler  | 表单模型（设计，明细） |
| flow-modeler  | 流程模型（设计，监控） |

>  后端工程

| 模块名称          |          备注说明           |
| :-------------:   |   :-----------------------: |
| common-module     | 项目公共模块  |
| identity-service  | 人员接口      |
| flow-service      | 流程接口      |
| form-service      | 表单接口      |


## 下载搭建环境
下载项目 `git clone https://github.com/wengwh/plumdo-work.git`

> 前端构建

```bash
下载安装nodejs 地址:http://nodejs.cn/download/
npm install -g bower #安装bower
npm install -g gulp #安装gulp 

cd html #进入html目录

如果环境没有翻墙情况，使用淘宝镜像做node-sass，否则会出现下载失败
set SASS_BINARY_SITE=https://npm.taobao.org/mirrors/node-sass

npm install #安装npm依赖库

#安装成功
cd work-admin #进入相对应目录
bower i   #安装bower依赖的第三方库
gulp serve  #执行gulp进行开发
```

```bash
"E:\kibana\node_modules\node-sass\build\binding.sln" (default target) (1) ->
(_src_\libsass target) ->
  MSBUILD : error MSB3428: 未能加载 Visual C++ 组件“VCBuild.exe”。要解决此问题
，1) 安装 .NET Fr
amework 2.0 SDK；2) 安装 Microsoft Visual Studio 2005；或 3) 如果将该组件安装到
了其他位置，请将其位置添加到
系统路径中。 [E:\kibana\node_modules\node-sass\build\binding.sln]

出现上面环境问题，可以执行
npm install -g node-gyp 
npm install –global –production windows-build-tools
npm install #继续安装npm依赖库
```


> 后端构建

```bash
cd java #进入java目录
mvn eclipse:eclipse #eclipse编辑器做示例
```



## 相关技术

>  前端技术
 
| 技术名称           |          备注说明           |
| :-------------:    |    :----------------------: |
| Yeoman Bower Gulp  |          构建工具           |
| AngularJS v1       |          MVVM框架           |
| Bootstrap v3       |          UI框架             |

>  后端技术

| 技术名称              |          备注说明         |
| :----------------:    |   :---------------------: |
| Java v1.8             |         编码语言          |
| Maven                 |         构建工具          |
| SpringBoot            |代码框架（后续springcloud）|
| Flowable JPA Mybatis  |         第三方组件        |
| Mysql                 |          数据库           |


## 文件介绍
```
deploy:部署文件
html:前端页面模块
java:后端服务模块

部署说明：docker部署
安装docker-compose
执行docker-compose build
执行docker-compose up -d
```

## 功能介绍
```
目前只完成表单设计器，流程接口和设计器

缺少：
表单的数据保存和使用
表单与流程的交互
报表整个模块

```