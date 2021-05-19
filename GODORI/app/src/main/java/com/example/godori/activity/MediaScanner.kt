package com.example.godori.activity

import android.content.Context
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import android.text.TextUtils


class MediaScanner {
    private var mContext: Context? = null

    @Volatile
    private var mMediaInstance: MediaScanner? = null
    private var mMediaScanner: MediaScannerConnection? = null
    //private                 MediaScannerConnection.MediaScannerConnectionClient mMediaScannerClient;

    //private                 MediaScannerConnection.MediaScannerConnectionClient mMediaScannerClient;
    private var mFilePath: String? = null

    fun getInstance(context: Context?): MediaScanner? {
        if (null == context) return null
        if (null == mMediaInstance) mMediaInstance = MediaScanner(context)
        return mMediaInstance
    }

    fun releaseInstance() {
        if (null != mMediaInstance) {
            mMediaInstance = null
        }
    }


    private fun MediaScanner(context: Context): MediaScanner? {
        mContext = context
        mFilePath = ""
        val mediaScanClient: MediaScannerConnectionClient
        mediaScanClient = object : MediaScannerConnectionClient {
            override fun onMediaScannerConnected() {
                mMediaScanner!!.scanFile(mFilePath, null)
                //                mFilePath = path;
            }

            override fun onScanCompleted(path: String?, uri: Uri?) {
                println("::::MediaScan Success::::")
                mMediaScanner!!.disconnect()
            }
        }
        mMediaScanner = MediaScannerConnection(mContext, mediaScanClient)
        return null //수정
    }

    fun mediaScanning(path: String?) {
        if (TextUtils.isEmpty(path)) return
        mFilePath = path
        if (!mMediaScanner!!.isConnected) mMediaScanner!!.connect()

        //mMediaScanner.scanFile( path,null );
    }

}