package net.skhu.firechat2.Room.UploadActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import net.skhu.firechat2.FirebaseDBService.Storage.FirebaseStorageService;
import net.skhu.firechat2.R;

import java.io.File;

public class VideoUploadActivity extends AppCompatActivity {

    VideoView videoView;
    //Button btnStart, btnStop;
    Button btnSearch, btnUploadVideo;

    final int VIDEO = 1;

    private static final String TAG = "MainActivity";

    private Uri filePath;

    //String filename;
    String mediaType;

    File path;

    static final int RC_LOGIN = 2; //  로그인 액티비티 호출을 구별하기 위한 식별 번호이다.
    FirebaseUser currentUser = null; // 현재 사용자

    boolean uploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);

        uploaded = false;

        //레이아웃 위젯 findViewById
        videoView = (VideoView) findViewById(R.id.view);
        //btnStart = (Button) findViewById(R.id.btnStart);
        //btnStop = (Button) findViewById(R.id.btnStop);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnUploadVideo = (Button)findViewById(R.id.btnUploadVideo);

        //미디어컨트롤러 추가하는 부분
        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);

        //비디오뷰 포커스를 요청함
        videoView.requestFocus();


        //동영상이 재생준비가 완료되었을 때를 알 수 있는 리스너 (실제 웹에서 영상을 다운받아 출력할 때 많이 사용됨)
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //Toast.makeText(getApplicationContext(), "동영상이 준비되었습니다. \n'시작' 버튼을 누르세요", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "동영상이 준비되었습니다.", Toast.LENGTH_SHORT).show();
                videoView.seekTo(0);
            }
        });

        //동영상 재생이 완료된 걸 알 수 있는 리스너
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //동영상 재생이 완료된 후 호출되는 메소드
                //Toast.makeText(getApplicationContext(), "동영상 재생이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "동영상 재생이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");
                startActivityForResult(pickIntent, VIDEO);

            }
        });

        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (uploaded == false) {
                    uploadFile();
                }
            }
        });

        /*btnStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View arg0) {
               playVideo();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                stopVideo();
            }
        });*/
    }

    //동영상 재생 Method
    private void playVideo() {
        //비디오를 처음부터 재생할 때 0으로 시작(파라메터 sec)
        //videoView.seekTo(0);
        videoView.start();
    }

    //동영상 정지 Method
    private void stopVideo() {
        //비디오 재생 잠시 멈춤
        videoView.pause();
        //비디오 재생 완전 멈춤
//        videoView.stopPlayback();
        //videoView를 null로 반환 시 동영상의 반복 재생이 불가능
//        videoView = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == VIDEO) {
            if (resultCode == RESULT_OK) {

                filePath = data.getData();
                if (filePath.toString().contains("video")) {
                    //handle video
                    videoView.setVideoURI(filePath);
                }

                String mimeType = getContentResolver().getType(filePath);
                mediaType = mimeType.replaceAll("video/", ".");
                Toast.makeText(this, mediaType, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            uploaded = true;

            FirebaseStorageService.videoUpload(this, filePath, mediaType,
                    (uploadFilename)->returnResult(uploadFilename));

//            //업로드 진행 Dialog 보이기
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("업로드중...");
//            progressDialog.show();
//
//            //storage
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//
//            //Unique한 파일명을 만들자.
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss.SSS");
//            Date now = new Date();
//            filename = formatter.format(now) + mediaType;
//            //storage 주소와 폴더 파일명을 지정해 준다.
//            StorageReference storageRef = storage.getReferenceFromUrl(FireBaseReference.FIREBASE_STORAGE_REF).child("videos/" + filename);
//
//            //올라가거라...
//            storageRef.putFile(filePath)
//                    //성공시
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @RequiresApi(api = Build.VERSION_CODES.O)
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
//                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
//
//                            /*
//                           // Uri externalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                            String[] projection = new String[]{
//                                    MediaStore.Video.Media._ID,
//                                    MediaStore.Video.Media.DISPLAY_NAME,
//                                    MediaStore.Video.Media.MIME_TYPE
//                            };
//
//                            Cursor cursor = getContentResolver().query(filePath, projection, null, null, null);
//
//                            if (cursor == null || !cursor.moveToFirst()) {
//                                Log.e("pjw", "cursor null or cursor is empty");
//                            }
//                            else {
//                                do {
//                                    String contentUrl = filePath.toString() + "/" + cursor.getString(0);
//                                    Log.v("pjw", "contentUrl: " + contentUrl);
//
//                                    path = getFilesDir();
//
//                                    //저장하는 파일의 이름
//                                    final File file = new File(path, filename);
//
//                                    try {
//                                        file.createNewFile();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    try {
//
//                                        FileInputStream inputStream = new FileInputStream(contentUrl);
//
//                                        FileOutputStream outputStream = new FileOutputStream(file);
//
//                                        int bytesRead = 0;
//
//                                        byte[] buffer = new byte[1024];
//
//                                        while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
//                                            outputStream.write(buffer, 0, bytesRead);
//                                        }
//
//                                        outputStream.close();
//
//                                        inputStream.close();
//
//                                    } catch (FileNotFoundException e) {
//                                        // TODO Auto-generated catch block
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        // TODO Auto-generated catch block
//                                        e.printStackTrace();
//                                    }
//
//                                    Log.v("pjw", "\nfile Path " + file.toString());
//
//                                } while (cursor.moveToNext());
//                            }*/
//
//
//
//                            Intent intent = new Intent();
//                            intent.putExtra("downloadVideoFileName", filename);
//                            setResult(Activity.RESULT_OK, intent);
//
//                            finish();
//                        }
//                    })
//                    //실패시
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    //진행중
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
//                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
//                            //dialog에 진행률을 퍼센트로 출력해 준다
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
//                        }
//                    });


        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    void returnResult(String uploadFilename){
        Log.v("pjw", "returnResult"+uploadFilename);

        Intent intent = new Intent();
        intent.putExtra("downloadVideoFileName", uploadFilename);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }
}
