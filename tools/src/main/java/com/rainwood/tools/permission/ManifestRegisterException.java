package com.rainwood.tools.permission;

/**
 * @Author: shearson
 * @time: 2019/11/29 15:12
 * @des: 动态申请的权限没有在清单文件中注册会抛出的异常
 */
public class ManifestRegisterException extends RuntimeException{

    ManifestRegisterException(String permission) {
        super(permission == null ?
                "No permissions are registered in the manifest file" :
                (permission + ": Permissions are not registered in the manifest file"));
    }
}
