# 关于项目构建

project structure -> 新建或者导入

![](.readme_images/d9a81bd7.png)

选择maven，webapp骨架的第二个
![](.readme_images/5726ea3d.png)

## 总体流程如下
![](.readme_images/5d3a55a3.png)

## 2 tomcat_web

### 2.1 maven项目导入
![](.readme_images/ecaceec3.png)

### 2.2 打war包
![](.readme_images/打war包.png)

报错
```shell
Cannot access defaults field of Properties
```
在pom.xmlproject下加入
```xml
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <version>3.2.2</version>
      </plugin>
    </plugins>
  </build>
```
成功打包
![](.readme_images/成功打包.png)

### 2.3tomcat构建项目

![](.readme_images/构建项目.png)
页面
![](.readme_images/run_sccess.png)
成功跑起来
![](.readme_images/f2dcc52a.png)