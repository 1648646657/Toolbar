package com.lzx.test;

/**
 * @author
 * @date
 */
public class Utils {
//    /**
//     * lzx PopupMenu
//     * 显示app列表
//     **/
//    @SuppressLint("RestrictedApi")
//    private fun showOptionsPopupMenu(view: View){
//        val popupMenu = PopupMenu(this, view)
//        popupMenu.menuInflater.inflate(R.menu.filelist_menu, popupMenu.menu)
//
//        val pm = packageManager
//        var ddIcon: Drawable? = try {
//            pm.getActivityIcon(ComponentName(ddPkgName, ddClsName))
//        } catch (e: Exception) {
//            null
//        }
////          var ddIcon: Drawable? = AppItemUtils.getAppIcon(this,ddPkgName, ddClsName)
//        var tenMeetingIcon: Drawable? = try {
//            pm.getActivityIcon(ComponentName(tenMeetPkgName, tenMeetClsName))
//        } catch (e: Exception) {
//            null
//        }
//
//        popupMenu.menu.findItem(R.id.app1).icon = ddIcon
//        popupMenu.menu.findItem(R.id.app2).icon = tenMeetingIcon
//        //利用反射显示图标
//        try{
////            val clazz = Class.forName("com.android.internal.view.menu.MenuBuilder")
////            val m = clazz.getDeclaredMethod("setOptionalIconsVisible", Boolean::class.javaPrimitiveType)
////            m.isAccessible = true;
////            m.invoke(popupMenu, true);
//
//            val field = popupMenu.javaClass.getDeclaredField("mPopup")
//            field.isAccessible = true;
//            val mHelper = field[popupMenu] as MenuPopupHelper
//            mHelper.setForceShowIcon(true)
//        }catch (e: Exception){
//            e.printStackTrace()
//        }
//
//        popupMenu.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.app1 -> {
//                    updateAppItem(ddPkgName, ddClsName, view)
//                    ToastUtils.show("钉钉")
//                }
//
//                R.id.app2 -> {
//                    updateAppItem(tenMeetPkgName, tenMeetClsName, view)
//                    ToastUtils.show("腾讯会议")
//                }
//
//                R.id.app3 -> {
//                    ToastUtils.show("app3")
//                }
//
//                R.id.app4 -> {
//                    ToastUtils.show("app4")
//                }
//                else -> {}
//            }
//            true
//        }
//
//        popupMenu.gravity = Gravity.START
//        popupMenu.show()
//    }
}
