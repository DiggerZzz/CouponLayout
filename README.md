# CouponLayout

类似优惠券带有锯齿的layout，继承于FrameLayout，可根据实际情况修改继承

## Effect

![](Screenshot/effect.jpg)

## Use

### 1.layout attribute
| attribute      | description    | type           | default        |
| -------------- | -------------- | -------------- | -------------- |
| coupon_background | layout 背景 | reference 或者 color | null |
| saw_radius        | 锯齿半径     | dimension           | 3dp |
| saw_gap           | 锯齿间隔     | dimension           | 0dp |
| draw_gravity      | 锯齿绘制位置  | lef, top, right, bottom   | top |

### 2.method
| method         | description    | params         |
| -------------- | -------------- | -------------- |
|setBackground           | 设置背景         | Drawable d     |
|setBackgroundResource   | 设置背景         | int drawableRes|
|setBackgroundColor      | 设置背景         | int color      |
