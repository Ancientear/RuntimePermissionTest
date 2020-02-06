package com.example.runtimepermissiontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button makeCall = (Button) findViewById(R.id.make_call);
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*第一步要先判断用户是不是已经给授权
                * checkSelfPermission()方法接收两个参数
                * 第一个参数是context
                * 第二个参数是具体的权限值，本函数使用的是打电话的权限值
                *
                * 使用方法的返回值和PackageManager.PERMISSION_GRANTED做比较
                * 相等就说明用户已经授权，不等就是没有授权
                * */
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    /*申请授权
                    * 第一个参数是Activity的实例
                    * 第二个参数是一个String[]数组，把要申请的权限名放在数组中捷克
                    * 第三个参数是请求码，只要是唯一的值就可以了，这里传入1
                    * 调用完之后，系统会弹出一个权限申请的对话框
                    * 然后用户可以选择同意或者拒绝权限申请
                    *
                    * 授权的结果则会封装在grantResults参数当中
                    * 只需要判断最后的授权结果
                    * 如果同意调用call，不同意弹出失败提示
                    * */
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
            }

        });
    }
        private void call()
        {
            try{
                /*构建了一个隐式Intent
                * Intent.ACTION_CALL,这是一个系统内置的打电话的动作
                * 然后在data部分指定了协议是tel，号码是10086
                * 由于Intent.ACTION_CALL是可以直接拨打电话
                * 因此必须声明权限
                * 为了防止程序崩溃，将所有的操作都放在异常捕获代码块当中
                *
                * */
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] ==getPackageManager().PERMISSION_GRANTED){
                    call();
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }
}
