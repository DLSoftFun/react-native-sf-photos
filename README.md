# react-native-sf-photos
# 图片选择器，支持同时选择图片、视频, 裁剪功能
![show](./lasts.gif)
# 安装
> npm install react-native-sf-photos
> react-native link 
# 参数
|  parameter  |  type  |  required  |   description   |
|:-----|:-----|:-----|:-----|
|type|number|yes|相册显示格式|
|number|number|yes|相片选择个数|
|isSingle|boolean|yes|是否单张|
|isCrop|boolean|yes|是否剪裁|
# IOS 
需要添加相册相册等权限 

//相册   Privacy - Photo Library Usage Description

//相机   NSCameraUsageDescription

//麦克风  NSMicrophoneUsageDescription

#注
将目录下的HXWeiboPhotoPicker.bundle文件拽入主工程目录下

# 例子
```

var param = {
            number: this.maxNumber,
            isSingle: false,
            isCrop: false,
            type: 'all',
        }
      
      

SFPhoto.getPhotos(param, (err,data) => {
            // tempArr = tempArr.concat(data.allList);

            console.log(data);
            // tempArr = data.allList;

            // this.setState({
            //     photoArray: tempArr,
            // }, () => {
            //
            //     console.log(this.state.photoArray)
            //
            //     if (this.props.source)
            //         this.props.source(tempArr)
            // })
        })

```
