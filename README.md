快速多渠道打包方案 ，根据美团开源的解决方案。

打包原理 [美团Android自动化之旅—生成渠道包](http://tech.meituan.com/mt-apk-packaging.html)

1. 需要安装 Python 环境
2. 将 ChannelUitl.java 放入 Android 项目代码中，并在需要获取渠道号的地方调用
3. 在 info/channel.txt 文件中 写入渠道名称 ，以换行区分不同渠道
4. 将原始包 apk文件 放在Android.py 同级目录下
5. 用 python Android.py 执行命令，将会在同级目录下生成 outputDir文件夹，文件夹中就是各个多渠道包
