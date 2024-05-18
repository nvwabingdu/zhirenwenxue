package cn.dreamfruits.baselib.network.imageloader

/**
 *description: 圆角类型.
 *@date 2018/10/12 10:36.
 *@author: YangYang.
 */
enum class CornerType {
  ALL,
  TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
  TOP, BOTTOM, LEFT, RIGHT,
  OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
  DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
}