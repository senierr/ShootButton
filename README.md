# ShootButton
[![](https://jitpack.io/v/senierr/ShootButton.svg)](https://jitpack.io/#senierr/ShootButton)

#### 拍摄按钮

> 在做一些有关录像、拍照的功能时，经常需要用到拍摄按钮。一般情况下使用UI切图。  
> 现在，我把它封装为一个普通的View，使用时，直接引用。  
> 既减少了APK体积，又可以自由定制颜色样式。同时，还提供了倒计时进度条。

### 演示

<div>
<img src="/images/Screenshot_1.png" width="22%" height="50%" />
<img src="/images/Screenshot_2.png" width="22%" height="50%" /> 
<img src="/images/Screenshot_3.png" width="22%" height="50%" /> 
<img src="/images/Screenshot_4.png" width="22%" height="50%" /> 
</div>

## 基本用法

### 1. 导入仓库

```java
maven { url 'https://jitpack.io' }
```

### 2. 添加依赖

```java
compile 'com.github.senierr:ShootButton:1.0.5'
```

### 3. XML

```java
<com.senierr.shootbutton.ShootButton
    app:circleColor="@color/colorAccent"
    app:circleWidth="8dp"
    app:centerColor="@color/colorAccent"
    app:centerMode="roundRect"
    app:centerRectPadding="8dp"
    app:centerCirclePadding="8dp"
    app:centerRectRadius="8dp"
    app:progressBgColor="@color/colorPrimary"
    app:progressColor="@color/colorPrimaryDark"
    app:maxProgress="100"
    app:progress="0"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

## 参数说明

| 参数名             | 说明             | 值               |
| ----------------- | ---------------- | ---------------- |
| circleWidth       | 圆环宽度          | dimension        |
| circleColor       | 圆环颜色          | color            |
| progressBgColor   | 进度条底色        | color            |
| progressColor     | 进度条颜色        | color            |
| centerMode        | 中心图标类型      | circle/roundRect |
| centerColor       | 中心图标颜色      | color            |
| centerCirclePadding| 中心图标-圆与圆环间距 | dimension        |
| centerRectPadding | 中心图标-方块与圆环间距 | dimension        |
| centerRectRadius  | 矩形图标圆角半径   | dimension        |
| maxProgress       | 进度最大值        | integer          |
| progress          | 进度值            | integer          |
