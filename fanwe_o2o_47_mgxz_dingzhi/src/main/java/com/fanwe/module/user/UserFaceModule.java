package com.fanwe.module.user;

import java.io.File;
import java.util.Arrays;

import com.fanwe.UploadUserHeadActivity;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.library.handler.PhotoHandler.PhotoHandlerListener;
import com.fanwe.library.utils.SDToast;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class UserFaceModule {
	
	private PhotoHandler mPhotoHandler;
	private FragmentActivity mFragAct;
//	private Fragment mFrag;
//	private int index=-1;
	
	public UserFaceModule(FragmentActivity fragAct) {
		this.mFragAct=fragAct;
		
	}
	
	public UserFaceModule(Fragment frag2) {
		
	}

	public void initPhotoHandler() {
		
		mPhotoHandler = new PhotoHandler(mFragAct);
		mPhotoHandler.setmListener(new PhotoHandlerListener() {

			@Override
			public void onResultFromCamera(File file) {
				if (file != null && file.exists()) {
					Intent intent = new Intent(mFragAct, UploadUserHeadActivity.class);
					intent.putExtra(UploadUserHeadActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
					mFragAct.startActivity(intent);
				}
			}

			@Override
			public void onResultFromAlbum(File file) {
				if (file != null && file.exists()) {
					Intent intent = new Intent(mFragAct, UploadUserHeadActivity.class);
					intent.putExtra(UploadUserHeadActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
					mFragAct.startActivity(intent);
				}
			}

			@Override
			public void onFailure(String msg) {
				SDToast.showToast(msg);
			}
		});
	}
	
	
	public void setupUserFace(Activity activity){
		SDDialogMenu dialog = new SDDialogMenu(activity);

		String[] arrItem = new String[] { "拍照", "从手机相册选择" };
		SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(Arrays.asList(arrItem), activity);
		dialog.setAdapter(adapter);
		dialog.setmListener(new SDDialogMenuListener() {

			@Override
			public void onItemClick(View v, int index, SDDialogMenu dialog) {
				switch (index) {
				case 0:
					mPhotoHandler.getPhotoFromCamera();
					break;
				case 1:
					mPhotoHandler.getPhotoFromAlbum();
					break;

				default:
					break;
				}

			}

			@Override
			public void onDismiss(SDDialogMenu dialog) {
			}

			@Override
			public void onCancelClick(View v, SDDialogMenu dialog) {
			}
		});
		dialog.showBottom();
	}

	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		mPhotoHandler.onActivityResult(requestCode, resultCode, data);
	}
	
	public void onDestory(){
		mPhotoHandler.deleteTakePhotoFiles();
	}
}
