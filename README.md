
# [jjdxm_ucloudlive][project] #
### Copyright notice ###

我在网上写的文章、项目都可以转载，但请注明出处，这是我唯一的要求。当然纯我个人原创的成果被转载了，不注明出处也是没有关系的，但是由我转载或者借鉴了别人的成果的请注明他人的出处，算是对前辈们的一种尊重吧！

虽然我支持写禁止转载的作者，这是他们的成果，他们有这个权利，但我不觉得强行扭转用户习惯会有一个很好的结果。纯属个人的观点，没有特别的意思。可能我是一个版权意识很差的人吧，所以以前用了前辈们的文章、项目有很多都没有注明出处，实在是抱歉！有想起或看到的我都会逐一补回去。

从一开始，就没指望从我写的文章、项目上获得什么回报，一方面是为了自己以后能够快速的回忆起曾经做过的事情，避免重复造轮子做无意义的事，另一方面是为了锻炼下写文档、文字组织的能力和经验。如果在方便自己的同时，对你们也有很大帮助，自然是求之不得的事了。要是有人转载或使用了我的东西觉得有帮助想要打赏给我，多少都行哈，心里却很开心，被人认可总归是件令人愉悦的事情。

站在了前辈们的肩膀上，才能走得更远视野更广。前辈们写的文章、项目给我带来了很多知识和帮助，我没有理由不去努力，没有理由不让自己成长的更好。写出好的东西于人于己都是好的，但是由于本人自身视野和能力水平有限，错误或者不好的望多多指点交流。

项目中如有不同程度的参考借鉴前辈们的文章、项目会在下面注明出处的，纯属为了个人以后开发工作或者文档能力的方便。如有侵犯到您的合法权益，对您造成了困惑，请联系协商解决，望多多谅解哈！若您也有共同的兴趣交流技术上的问题加入交流群QQ： 548545202


## Introduction ##

这是一款低仿映客直播的Android直播聊天应用，本项目通过使用ucloud的直播平台提供的sdk进行推流和拉流，使用环信IM来作为用户系统的管理直播聊天室中消息收发、发送礼物、弹幕、私信等功能。本项目旨在借用第三方直播平台提供的sdk方案快速搭建一款类似映客直播的安卓APP，项目中主要内容是抽取了聊天室的一个基类、和一些自定义view（视频点赞、礼物动画、弹幕动画、消息输入编辑工具栏等），更多关于接入直播的涉及的内容可自行阅读ucloud提供的直播方案，项目重点在于界面UI的模仿与实现。项目中没有真正提供一个后台去支持，发起直播没有真正去推流的，可以根据真正的环境去接入推流地址，观看直播，不是真正的直播地址，只是添加了一个点播地址进行播放，也是可以根据实际环境接入拉流地址的。


## Features ##

1.仿映客部分UI
2.发起直播
3.观看主播直播
4.直播平台方案之一的简单实现参考
5.直播聊天室技术点实现案例参考
6.艾特聊天室成员的实现


## Screenshots ##

<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon01.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon02.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon03.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon04.png" width="300">
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon05.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon06.png" width="300">
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon07.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon08.png" width="300">
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon09.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon10.png" width="300"> 


## Download ##

[demo apk下载][downapk]

## Get Started ##

### step1 ###
准备工作

ucloud直播平台注册并开通直播服务，创建一个直播频道，获取推流地址和拉流地址，下载直播SDK或者用项目中已经接入的SDK

环信IM平台注册并创建一个APP，配置相应的参数，获取APP的key和其他信息

### step2 ###

将获取到的推流和拉流地址分别接入到项目中，也可以根据实际项目，通过后台去创建直播，获取直播推流地址来发起直播，获取拉流地址来播放直播

接入环信IM的账号信息

## More Actions ##

## ChangeLog ##

## About Author ##

#### 个人网站:[http://www.dou361.com][web] ####
#### GitHub:[jjdxmashl][github] ####
#### QQ:316988670 ####
#### 交流QQ群:548545202 ####


## License ##

    Copyright (C) dou361, The Framework Open Source Project
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
     	http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

## (Frequently Asked Questions)FAQ ##
## Bugs Report and Help ##

If you find any bug when using project, please report [here][issues]. Thanks for helping us building a better one.




[web]:http://www.dou361.com
[github]:https://github.com/jjdxmashl/
[project]:https://github.com/jjdxmashl/jjdxm_ucloudlive/
[issues]:https://github.com/jjdxmashl/jjdxm_ucloudlive/issues/new
[downapk]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/apk/app-debug.apk
[lastaar]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/release/jjdxm-ucloudlive-1.0.0.aar
[lastjar]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/release/jjdxm-ucloudlive-1.0.0.jar
[icon01]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon01.png
[icon02]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_ucloudlive/master/screenshots/icon02.png
[jaraar]:https://github.com/jjdxmashl/jjdxm_ecodingprocess/blob/master/架包的打包引用以及冲突解决.md
[minify]:https://github.com/jjdxmashl/jjdxm_ecodingprocess/blob/master/AndroidStudio代码混淆注意的问题.md
[author]:http://www.jianshu.com/users/ec59bd61433a/latest_articles
[url]:http://www.jianshu.com/p/03fdcfd3ae9c?utm_campaign=maleskine&utm_content=note&utm_medium=writer_share