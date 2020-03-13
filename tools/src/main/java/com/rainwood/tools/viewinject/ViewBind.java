package com.rainwood.tools.viewinject;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @Author: shearson
 * @time: 2019/11/27 15:21
 * @des: java类作用描述  暂时只能支持在activity中使用
 */
public class ViewBind {

    private static final String TAG = ViewBind.class.getSimpleName();

    /**
     * 在 activity中使用
     */
    public static void inject(Activity activity){
        inject(new ViewFinder(activity));
    }

    /**
     * 实际处理者
     */
    private static void inject(ViewFinder finder){
        // ViewBind
        injectField(finder);
        // OnClick
        injectEvent(finder);
    }

    /**
     * 注入域
     */
    private static void injectField(ViewFinder finder){
        // 获取类里面的所有的属性
        Class<?> clazz = finder.finderClass();
        Field[] fields = clazz.getDeclaredFields();

        // 依次遍历并并获取域上的ViewById注解
        for (Field field : fields) {
            // 获取ViewById 注解
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null){
                // 获取对应的id，对应R.id.xxx
                int id = viewById.value();
                if (id != 0){
                    View view = finder.finderViewById(id);
                    if (view != null){
                        // 能够注入所有修饰符
                        field.setAccessible(true);
                        try {
                            // 动态的注入找到View
                            field.set(finder.finderObject(), view);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 注入 事件
     */
    private static void injectEvent(ViewFinder finder){
        // 获取所有的方法
        Class<?> clazz = finder.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        // 依次遍历并获取方法的ViewById 注解
        for (Method method : methods) {
            // 获取onClick注解
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null){
                // 获取id值， 对应 R.id.xxx
                int[] ids = onClick.value();
                for (int id : ids) {
                    View view = finder.finderViewById(id);
                    if (view != null){
                        view.setOnClickListener(new DeclaredOnClickListener(method, finder.finderObject()));
                    }
                }
            }
        }
    }


    /**
     * 设置 OnClickListener 的监听器
     */
    private static class DeclaredOnClickListener implements View.OnClickListener{

        /**
         * 反射时调用的对象
         */
        private Object mObject;

        /**
         * 反射的方法
         */
        private Method mMethod;

        public DeclaredOnClickListener(Method mMethod, Object mObject) {
            this.mObject = mObject;
            this.mMethod = mMethod;
        }

        /**
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            // 反射该方法
            try {
                mMethod.setAccessible(true);
                mMethod.invoke(mObject, v);
            } catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, new Object[]{});
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
