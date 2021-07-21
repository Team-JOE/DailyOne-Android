package com.aosproject.dailyone.fragment;

import android.app.ActionBar;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.util.DiaryHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryFragment extends Fragment {

    private final String TAG = "DiaryFragment";

    DiaryHelper diaryHelper;
    EditText edtContent;
    ImageView ivJoy, ivSad, ivAngry, ivSoso;
    Button btnInsert;
    String diaryContent;
    SQLiteDatabase DB;
    int dbEmoji;
    // boolean[] emojiChoice = {false, false, false, false};
    ImageView[] ivEmoji = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_diary, container,false);

        diaryHelper = new DiaryHelper(getContext());

        addListener(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void addListener(View view){
        edtContent = view.findViewById(R.id.diary_edt_content);

        ivJoy = view.findViewById(R.id.diary_iv_emoji_joy);
        ivSad = view.findViewById(R.id.diary_iv_emoji_sad);
        ivAngry = view.findViewById(R.id.diary_iv_emoji_angry);
        ivSoso = view.findViewById(R.id.diary_iv_emoji_soso);
        ivEmoji = new ImageView[] {ivJoy, ivSad, ivAngry, ivSoso};


        btnInsert = view.findViewById(R.id.diary_btn_insert);


        edtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

        ivJoy.setOnClickListener(emojiClickListener);
        ivSad.setOnClickListener(emojiClickListener);
        ivAngry.setOnClickListener(emojiClickListener);
        ivSoso.setOnClickListener(emojiClickListener);

        btnInsert.setOnClickListener(insertClickListener);
    }

    View.OnClickListener insertClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try{
                diaryContent = edtContent.getText().toString();
                // joy = 0, sad = 1, angry = 2, soso = 3
                DB = diaryHelper.getWritableDatabase();
                String query = "INSERT INTO diarydata (content, emoji) VALUES ('" + diaryContent + "', " + dbEmoji + ");";
                DB.execSQL(query);

                diaryHelper.close();
                Toast.makeText(getContext(), "Insert OK!", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getContext(), "Insert Error!" , Toast.LENGTH_LONG).show();
            }

        }
    };

    View.OnClickListener emojiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.diary_iv_emoji_joy:
                    dbEmoji = chooseEmoji(0);
                    break;
                case R.id.diary_iv_emoji_sad:
                    dbEmoji = chooseEmoji(1);
                    break;
                case R.id.diary_iv_emoji_angry:
                    dbEmoji = chooseEmoji(2);
                    break;
                case R.id.diary_iv_emoji_soso:
                    dbEmoji = chooseEmoji(3);
                    break;
                default:
                    break;
            }
        }
    };

    // get emoji number
    private int chooseEmoji(int emojiNum){
        for(int i=0; i<ivEmoji.length; i++){
            ivEmoji[i].setBackgroundColor(Color.TRANSPARENT);
            if(i==emojiNum){
                ivEmoji[i].setBackgroundColor(Color.LTGRAY);
            }
        }
        return emojiNum;
    }


//    private String getTime() {
//        long mNow = System.currentTimeMillis();
//        Date mDate = new Date(mNow);
//        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String stringDate = mFormat.format(mDate);
//        return stringDate;
//    }
}
