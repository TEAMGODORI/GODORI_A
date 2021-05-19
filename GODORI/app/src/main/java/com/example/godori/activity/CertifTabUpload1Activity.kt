package com.example.godori.activity

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.godori.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_certif_tab_upload1.*
import kotlinx.android.synthetic.main.activity_certif_tab_upload2.*
import kotlinx.android.synthetic.main.activity_certif_tab_upload4.*
import kotlinx.android.synthetic.main.activity_certif_tab_upload4.view.*
import okhttp3.MultipartBody
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*


class CertifTabUpload1Activity : AppCompatActivity() {
    // 데이터 목록
    lateinit var images:File
    var imagesURI: Uri? = null
    lateinit var imagesByte: ByteArray

    @SuppressLint("ClickableViewAccessibility")
    private val TAG: String = "태그명"

    // 경로 변수와 요청변수 생성
    lateinit var mCurrentPhotoPath: String //문자열 형태의 사진 경로값 (초기값을 null로 시작하고 싶을 때 - lateinti var)

    private var imgUri: Uri? = null
    private var photoURI: Uri? = null
    private var albumURI: Uri? = null

    lateinit var part: MultipartBody.Part

    private val FROM_CAMERA = 1
    private val FROM_ALBUM = 2
    private val IMAGE_CROP = 3

    //현재 시간 가져오기
    val now: Long = System.currentTimeMillis()
    //Date 형식으로 고치기
    val mDate = Date(now)
    //가져오고 싶은 형태로 가져오기 "2018-07-06 01:42:00"
    val simpleDate = SimpleDateFormat("yyyy/MM/dd\n hh:mm")
    val getTime: String = simpleDate.format(mDate)

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("SimpleDateFormat", "WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certif_tab_upload1)

        //백버튼 눌렀을 때
        backBtn1.setOnClickListener {
            onBackPressed()
        }

        //다음 화면으로 넘어가기
        next1Btn.setOnClickListener {
            // 이미지 파일이 저장
            var bitmap = viewToBitmap(save_img1)

            val bytes = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                "TimeStamp",
                null
            )
            val timestampURI = Uri.parse(path)
            Log.v("timestampURI", timestampURI.toString())

            // 데이터 전달
            val intent = Intent(this, CertifTabUpload2Activity::class.java)
            intent.putExtra("imageURI", timestampURI.toString())
            Log.v("certi1", photoURI.toString())

            // 액티비티 시작
            startActivity(intent)
        }

        //버튼 누르면 촬영하는 부분을 dispatchTakePictureIntent를 불러오게 수정
        Img_Upload1.setOnClickListener { v ->
            when (v.id) {
                R.id.Img_Upload1 -> {
                    if (checkPersmission()) {
                        makeDialog()
                    } else {
                        requestPermission()
                    }
                }
            }
        }

        //5개 버튼
        time_Btn1.setText(getTime)
        time_Btn2.setText(getTime)
        time_Btn3.setText(getTime)
        time_Btn4.setText(getTime)
        time_Btn5.setText(getTime)

        time_RBtn1.setOnCheckedChangeListener(listener1)
        time_RBtn2.setOnCheckedChangeListener(listener2)

    }

    //라디오 버튼 멀티라인
    private var listener1: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                time_RBtn2.setOnCheckedChangeListener(null)
                time_RBtn2.clearCheck()
                time_RBtn2.setOnCheckedChangeListener(listener2)
            }
        }

    //라디오 버튼 멀티라인
    private val listener2: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                time_RBtn1.setOnCheckedChangeListener(null)
                time_RBtn1.clearCheck()
                time_RBtn1.setOnCheckedChangeListener(listener1)
            }
        }

    // 카메라 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            FROM_CAMERA
        )
    }

    // 카메라 권한 체크
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    // 권한 요청
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult")
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
        }
    }

    //다이얼로그 카메라, 앨범선택
    private fun makeDialog() {
        val alt_bld: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(
                this@CertifTabUpload1Activity,
                R.style.MyAlertDialogStyle
            )
        alt_bld.setTitle("사진 업로드")
            .setPositiveButton("사진촬영",
                DialogInterface.OnClickListener { dialog, id ->
                    Log.v("알림", "다이얼로그 > 사진촬영 선택")
                    // 사진 촬영 클릭
                    takePhoto()
                }).setNeutralButton("앨범선택",
                DialogInterface.OnClickListener { dialogInterface, id ->
                    Log.v("알림", "다이얼로그 > 앨범선택 선택")
                    //앨범에서 선택
                    selectAlbum()
                }).setNegativeButton("취소   ",
                DialogInterface.OnClickListener { dialog, id ->
                    Log.v("알림", "다이얼로그 > 취소 선택")
                    // 취소 클릭. dialog 닫기.
                    dialog.cancel()
                })
        val alert: android.app.AlertDialog? = alt_bld.create()
        if (alert != null) {
            alert.show()
        }
    }

    //사진 찍기 클릭, 카메라 인텐트 실행하는 부분
    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto() {
        // 촬영 후 이미지 가져옴
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        photoFile = createImageFile()

        val providerURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.godori.fileprovider", photoFile
        )
        imgUri = providerURI
        intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
        startActivityForResult(intent, FROM_CAMERA)
    }

    // 카메라로 촬영한 이미지를 파일로 저장해준다
    // 사진 촬영 후 썸네일만 띄워주므로 이미지를 파일로 저장해야 함. 촬영한 사진을 이미지 파일로 저장하는 함수 createImageFile
    // 사진을 찍기 전, 사진이 저장되는 임시 파일을 생성
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES) // 이미지가 저장될 폴더 이름

        if (storageDir != null) {
            if (!storageDir.exists()) {
                Log.v("알림", "storageDir 존재 x $storageDir")
                storageDir.mkdirs()
            }
        }
        Log.v("알림", "storageDir 존재함 $storageDir")
        val image = File.createTempFile(
            "JPEG_${timeStamp}_",  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }


//    @Throws(IOException::class)
//    fun createImageFile(): File {
//        val imgFileName = System.currentTimeMillis().toString() + ".jpg"
//        var imageFile: File? = null
//        val storageDir =
//            File(Environment.getExternalStorageDirectory().toString() + "/Pictures", "ireh")
//        if (!storageDir.exists()) {
//            Log.v("알림", "storageDir 존재 x $storageDir")
//            storageDir.mkdirs()
//        }
//        Log.v("알림", "storageDir 존재함 $storageDir")
//        imageFile = File(storageDir, imgFileName)
//        mCurrentPhotoPath = imageFile.absolutePath
//        return imageFile
//    }

    //앨범 선택 클릭, 앨범 열기
    private fun selectAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        intent.putExtra("crop", true) //기존 코드에 이 줄 추가!
        startActivityForResult(intent, FROM_ALBUM)
    }

    //사진 저장
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(mCurrentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
            Toast.makeText(this, "사진이 저장되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    //이미지 자르기
    private fun cropImage(uri: Uri?){
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            //사각형 모양으로 자른다
            .start(this)
    }

    // 카메라로 촬영한 영상을 가져오는 부분, 가져온 사진 뿌리기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        if (data != null) {
//            CropImage.activity(data.data!!).setAspectRatio(4,3).start(requireContext(), this)
        }
        when (requestCode) {
            FROM_ALBUM -> {
                //앨범에서 가져오기
                data?.data?.let { uri ->
                    cropImage(uri) //이미지를 선택하면 여기가 실행됨
                }
                if (data != null) {
                    if (data.data != null) {
                        try {
                            var albumFile: File? = null
                            albumFile = createImageFile() //이미지 파일로 저장
//                            images = albumFile // images를 다음 인텐트트로 넘김
                            photoURI = data.data
                            albumURI = Uri.fromFile(albumFile)

                            Img_Upload1.setImageURI(photoURI)
                            TimeStampBtn()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.v("알림", "앨범에서 가져오기 에러")
                        }
                    }
                }
            }
            FROM_CAMERA -> {
                //카메라 촬영
                if (resultCode === RESULT_OK) {
                    data?.data?.let { uri ->
                        cropImage(uri)
                    }//

                    val file = File(mCurrentPhotoPath)
                    val selectedUri = Uri.fromFile(file)//
                    val bitmap: Bitmap?
                    if (Build.VERSION.SDK_INT >= 29) {
                        val source = ImageDecoder.createSource(
                            contentResolver, Uri.fromFile(file)
                        )
                        try {
                            bitmap = ImageDecoder.decodeBitmap(source)
                            cropImage(selectedUri)//
//                            Img_Upload1.setImageBitmap(bitmap)
                            TimeStampBtn()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                Uri.fromFile(file)
                            )
                            if (bitmap != null) {
//                                Img_Upload1.setImageBitmap(bitmap)
                                cropImage(selectedUri)//
                                TimeStampBtn()
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@CertifTabUpload1Activity,
                        "사진찍기를 취소하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                //그후, 이곳으로 들어와 RESULT_OK 상태라면 이미지 Uri를 결과 Uri로 저장!
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let {
                        Img_Upload1.setImageBitmap(result.bitmap)
                        Img_Upload1.setImageURI(result.uri)
                        photoURI = result.uri

                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Toast.makeText(this@CertifTabUpload1Activity, error.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else ->{finish()}
        }
    }

    private fun TimeStampBtn(){
        TimeText1.setText(getTime)

        //버튼 누르면 글쓰기
        time_Btn1.setOnClickListener { v ->
            if (Img_Upload1!= null) {
                TimeText1.x = 145f
                TimeText1.y = 240f
                TimeText1.gravity= Gravity.CENTER
                TimeText1.setVisibility(View.VISIBLE)
            }
        }
        time_Btn2.setOnClickListener { v ->
            if (Img_Upload1 != null) {
                TimeText1.x = 145f
                TimeText1.y = 430f
                TimeText1.gravity= Gravity.CENTER
                TimeText1.setVisibility(View.VISIBLE)
            }
        }
        time_Btn3.setOnClickListener { v ->
            if (Img_Upload1 != null) {
                TimeText1.x = 150f
                TimeText1.y = 40f
                TimeText1.gravity= Gravity.CENTER
                TimeText1.setVisibility(View.VISIBLE)
            }
        }
        time_Btn4.setOnClickListener { v ->
            if (Img_Upload1 != null) {
                TimeText1.x = 60f
                TimeText1.y = 430f
                TimeText1.gravity= Gravity.LEFT
                TimeText1.setVisibility(View.VISIBLE)
            }
        }
        time_Btn5.setOnClickListener { v ->
            if (Img_Upload1 != null) {
                TimeText1.x = 220f
                TimeText1.y = 430f
                TimeText1.gravity= Gravity.RIGHT
                TimeText1.setVisibility(View.VISIBLE)
            }
        }
        time_Btn6.setOnClickListener { v ->
            if (Img_Upload1 != null) {
                TimeText1.setVisibility(View.GONE)
            }
        }
    }

    private fun viewToBitmap(view: View): Bitmap? { //뷰를 비트맵으로 변환
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

//    @SuppressLint("ResourceType")
//    fun onClick(v: View) {
//        // TODO Auto-generated method stub
//        when (v.getId()) {
//            R.id.btn_id_confirm -> {
//                if (time_RBtn1.getCheckedRadioButtonId() > 0) {
//                    val radioButton: View =
//                        time_RBtn1.findViewById(time_RBtn1.checkedRadioButtonId)
//                    val radioId: Int = time_RBtn1.indexOfChild(radioButton)
//                    val btn = time_RBtn1.getChildAt(radioId) as RadioButton
//                } else if (time_RBtn2.getCheckedRadioButtonId() > 0) {
//                    val radioButton: View =
//                        time_RBtn2.findViewById(time_RBtn2.getCheckedRadioButtonId())
//                    val radioId: Int = time_RBtn2.indexOfChild(radioButton)
//                    val btn = time_RBtn2.getChildAt(radioId) as RadioButton
//                }
//            }
//        }
//    }

    // 뒤로가기 함수
    override fun onBackPressed() {
        startActivity(Intent(this, TabBarActivity::class.java))
        finish()
    }
}

