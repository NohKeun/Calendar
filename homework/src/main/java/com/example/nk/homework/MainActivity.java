package com.example.nk.homework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class MainActivity extends Activity {

    DatePicker dp;
    TextView viewDate;          // 날짜를 나타내는 텍스트뷰
    Button saveButton;          // 저장 하는 버튼
    EditText diaryContents;    // 일기내용을 보여주는 에디트텍스트
    String filename;            // 파일명을 저장하는 문자형 변수
    View view;

    String str="";
    String sdPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(//R.layout.activity_main);
        saveButton = (Button)findViewById(R.id.btnWrite);
        diaryContents = (EditText)findViewById(R.id.contents);
        viewDate = (TextView)findViewById(R.id.edtDiary);
        //외부저장경로 설정
        sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        File dir = new File(sdPath+"/mydiary");
        diaryContents.setText(sdPath);
        dir.mkdirs();//디렉토리 생성,이미 존재하면 저절로 통과

        //contents.setText(dir.getPath() + " " + Environment.getExternalStorageState());

        //오늘 날짜를 구함
        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);
        //초기 날짜 세팅
        viewDate.setText(cYear+"년_"+(cMonth+1)+"월_"+cDay+"일");
        //날짜 변경하는 부분

        /*날짜를 표시한 TextView를 터치하면 DatePicker에 잇는 dialog가 나타나고 변경가능.*/
        viewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = (View) View.inflate(MainActivity.this, R.layout.dialog1, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("날짜 선택");
                dlg.setView(view);
                dp = (DatePicker)view.findViewById(R.id.datePicker);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        str = dp.getYear() + "년_"+(dp.getMonth()+1)+"월_"+dp.getDayOfMonth()+"일";
                        viewDate.setText(str);
                        diaryContents.setText(readDiary(str));
                    }
                });
                dlg.show();
            }
        });
        /*2번 요구사항*/

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = viewDate.getText().toString().trim()+".txt";
                File file = new File(sdPath+title);
                try {
                    FileOutputStream outFs = new FileOutputStream(file);
                    String str = diaryContents.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), title + "이 저장됨", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), title + "이 저장됨", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
    String readDiary(String fName){ // 파일 이름받아 있으면 받아와서 수정하기 / 없으면 일기없으니 새로저장
        String diaryStr = null;
        FileInputStream inFs;
        try{
            inFs = new FileInputStream(sdPath + fName+".txt");//정해진 디렉토리에 읽기
            byte[] txt = new byte[inFs.available()];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
        }catch (IOException e){
            diaryStr = "읽기실패";
        }
        return diaryStr;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.largeSize:
                contents.setTextSize(30);
                break;
            case R.id.regularSize:
                contents.setTextSize(20);
                break;
            case R.id.smallSize:
                contents.setTextSize(10);
                break;
            case R.id.reOpen:
                String temp = readDiary(date.getText().toString());
                contents.setText(temp);
                break;
            case  R.id.rmDiary:
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("삭제 여부 묻기");
                dlg.setMessage(date.getText() + " 일기를 삭제하시겠습니까?");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(sdPath + date.getText() + ".txt");
                        file.delete();
                        contents.setText("");
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "삭제 안함", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
        }
        return false;
    }
}