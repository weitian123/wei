package com.feicui.news.service;

import java.io.File;

import com.feicui.news.common.LogUtil;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class DownloadCompleteReceiver extends BroadcastReceiver {  

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

			Toast.makeText(context, "������ɣ�", Toast.LENGTH_LONG).show();

			String fileName = "";
			DownloadManager downloadManager = (DownloadManager) context 
					.getSystemService(Context.DOWNLOAD_SERVICE);//�����ط����ȡ���ع�����
			//��ȡ���ض���
			DownloadManager.Query query = new DownloadManager.Query();
			//���ù���״̬���ɹ�
			query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
			// ��ѯ��ǰ���ع��ġ��ɹ��ļ���
			Cursor c = downloadManager.query(query);
			// �ƶ����������ص��ļ�
			if (c.moveToFirst()) {
				fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
			}
			LogUtil.d(LogUtil.TAG , "======�ļ�����=====" + fileName);
			File f = new File(fileName.replace("file://", ""));// ����·��
			installApk(f , context);// ��ʼ��װapk
			context.unregisterReceiver(this); //ȡ��ע��㲥
		}
	}  


	private void installApk(File file,Context context){
		if(!file.exists()){
			Log.i("DownLoadReceive", "�ļ�������");
			return ;
		}
		// ͨ��Intent��װapk�ļ����Զ��򿪰�װ����
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//��������BroadcastReceive������activity������������ʽ��������ΪFLAG_ACTIVITY_NEW_TASK
		context.startActivity(intent);	
	}
}
