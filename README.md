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
import React, {Component} from 'react';
import {
    StyleSheet,
    Text,
    View,
} from 'react-native';

import SFImagePicker from 'react-native-sf-photos';


type Props = {};
export default class App extends Component<Props> {
    render() {
        return (
            <View
                style={styles.container}>
                <Text
                    style={styles.welcome}
                    onPress={() =>{
                        
                        /** 
                         *  @param: type   0: 照片  1: 视频  2: 照片和视频
                         *  @param: number 最多可选照片或视频数
                         *  @param: isSingle 是否为选择单张模式
                         *  @param: isCrop 是否需要剪裁
                         **/
                        var type = 0;
                        var number = 5;
                        var isSingle = false;
                        var isCrop = false;
                        SFImagePicker.select(type,number,isSingle,isCrop, () =>{

                        })
                        
                    }}>
                    Welcome to React
                    Native!
                </Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});

```
