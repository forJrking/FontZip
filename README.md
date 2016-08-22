# 字体压缩神器
用于提取仅需要的美化文字，来压缩assets下的字体文件大小。

[***GUI.rar下载***](https://github.com/forJrking/FontZip/raw/master/GUI.rar)

[***百度云下载***](http://pan.baidu.com/s/1nuEDOSh)
## GIF

![GUI](1.gif)

### GUITool使用说明:
运行```run.bat```，选字体文件填入提取文字，点击OK，在字体同级目录下会生成 ```fontmin``` 文件，关闭GUI、命令窗口，稍等待5-10s后即可。

经过测试，已经把项目***5MB***的艺术字体，按需求提取后，占用只有***20KB***，并且可正常使用。大大压缩了apk包的大小，还可以减小资源加载的占用。有此需要的同学赶紧用起来吧。
## 下载相关
[***~~sfnttool.jar~~下载***](https://github.com/forJrking/FontZip/raw/master/FontTool/sfnttool.jar)

[***sfnttool.jar源码***](https://github.com/googlei18n/sfntly)

### ~~sfnttool.jar使用说明~~：
 * 先提取需要特殊字体美化的文字，
 * 把字体和jar包放在同目录下，按下Shift再点击鼠标右键，此处打开命令窗口，输入下面命令 

	    java -jar sfnttool.jar -s '此处放入需要提取的文字' font.ttf fontc.ttf 
    	font.ttf ------ 原字体文件名
    	fontc.ttf------ 提取后的字体文件名

## 联系 Jrking
邮箱：forjrking@sina.com
