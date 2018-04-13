//
//  SFImagePicker.m
//  SFShare
//
//  Created by SmartFun on 2018/4/11.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import "SFImagePicker.h"
#import "HXPhotoPicker.h"
#import <React/RCTConvert.h>

@interface SFImagePicker()
<
HXAlbumListViewControllerDelegate,
UIImagePickerControllerDelegate
>
@property (nonatomic, assign) NSInteger type;
@property (nonatomic, assign) NSInteger number;
@property (nonatomic, assign) BOOL isSingle;
@property (nonatomic, assign) BOOL isCrop;

@property (strong, nonatomic) HXPhotoManager *manager;
@property (strong, nonatomic) HXDatePhotoToolManager *toolManager;
@property (strong, nonatomic) UIColor *bottomViewBgColor;

@end

@implementation SFImagePicker

RCT_EXPORT_MODULE();

- (void)setCommonManage{
  if (!_manager)
    return;
  HXPhotoConfiguration *config = self.manager.configuration;
  config.clarityScale = 1.7;
  config.deleteTemporaryPhoto = YES;
  config.saveSystemAblum = YES;
  config.themeColor = [UIColor blackColor];
}

- (void)setOnlyPhotoManage{
  if (self.isSingle){
    self.manager = [[HXPhotoManager alloc] init];
    self.manager.configuration.singleSelected = YES;
    if (!self.isCrop)
      self.manager.configuration.singleJumpEdit = NO;
    self.manager.configuration.movableCropBox = YES;
    self.manager.configuration.movableCropBoxEditSize = YES;
    return;
  }
  self.manager = [[HXPhotoManager alloc] initWithType:HXPhotoManagerSelectedTypePhoto];
  self.manager.configuration.photoMaxNum = self.number;
  self.manager.configuration.videoMaxNum = 0;
  self.manager.configuration.lookLivePhoto = YES;
  self.manager.configuration.lookGifPhoto = YES;
}

- (void)setOnlyVideoManage{
  self.manager = [[HXPhotoManager alloc] initWithType:HXPhotoManagerSelectedTypeVideo];
  self.manager.configuration.videoMaxNum = self.number;
}

- (void)setPhotoOrVideoManage{
  self.manager = [[HXPhotoManager alloc] initWithType:HXPhotoManagerSelectedTypePhotoAndVideo];
  self.manager.configuration.openCamera = YES;
  self.manager.configuration.saveSystemAblum = NO;
  self.manager.configuration.maxNum = self.number;
  self.manager.configuration.photoMaxNum = self.number;
  self.manager.configuration.videoMaxNum = self.number;
}

RCT_EXPORT_METHOD(select:(NSInteger)type
                  number:(NSInteger)number
                isSingle:(BOOL)isSingle
                  isCrop:(BOOL)isCrop
                    call:(RCTResponseSenderBlock)callback)
{
  self.type = type;
  self.isSingle = isSingle;
  self.number = number;
  self.isCrop = isCrop;
  
  UIViewController *root = RCTPresentedViewController();
  
  if (type == 0) {
    [self setOnlyPhotoManage];
    [self setCommonManage];
  }
  else if (type == 1) {
    [self setOnlyVideoManage];
    [self setCommonManage];
  }
  else{
    [self setPhotoOrVideoManage];
    [self setCommonManage];
  }
  
  [root hx_presentAlbumListViewControllerWithManager:self.manager done:^(NSArray<HXPhotoModel *> *allList, NSArray<HXPhotoModel *> *photoList, NSArray<HXPhotoModel *> *videoList, BOOL original, HXAlbumListViewController *viewController)
  {
    NSMutableArray *imgArr  = [NSMutableArray array];
    NSMutableArray *allArr = [NSMutableArray array];
    NSMutableArray *videoArr = [NSMutableArray array];
    for (int i = 0; i < allList.count; i++) {
      [allArr addObject:[allList[i].fileURL absoluteString]];
    }
    for (int i = 0; i < photoList.count; i++) {
      [imgArr addObject:[photoList[i].fileURL absoluteString]];
    }
    for (int i = 0; i < videoList.count; i++) {
      [videoArr addObject:[videoList[i].fileURL absoluteString]];
    }
    NSSLog(@"all - %@",allList[0].fileURL);
    NSSLog(@"photo - %@",photoList);
    NSSLog(@"video - %@",videoList);
    callback(@[ @{@"allList": allArr,@"photoList":imgArr,@"videoList":videoArr} ]);
  } cancel:^(HXAlbumListViewController *viewController) {
    NSSLog(@"取消了");
  }];
  
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
  [picker dismissViewControllerAnimated:YES completion:nil];
}
@end
