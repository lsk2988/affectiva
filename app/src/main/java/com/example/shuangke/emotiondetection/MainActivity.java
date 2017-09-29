package com.example.shuangke.emotiondetection;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.format;

public class MainActivity extends AppCompatActivity implements Detector.ImageListener {
    private Toolbar toolbar;
    CameraDetector detector;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private Button start,stop,play;
    SurfaceView cameraPreview;
    DataHelper myDb;
    //score for each emotion but just in String type
    private String time, start_time;
    private int count;
    String smile,smile1;
    String joy,joy1;
    String sadness,sadness1;
    String surprise,surprise1;
    String anger,anger1;
    String contempt,contempt1;
    String disgust,disgust1;
    String engagement,engagement1;
    String fear,fear1;
    String valence,valence1;
   //emoji scores
    double disappointed;
    double flushed;
    double kissing;
    double laughing;
    double rage;
    double relaxed, scream, smiley, smirk,stuckOutTongue,sotwinkingEye,wink;
    ImageView display;
    private int emojiIndex = 5; //default emoji is "relaxed"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.my_toolBar);
        setSupportActionBar(toolbar);
        display = (ImageView) findViewById(R.id.display);
         start = (Button)findViewById(R.id.start);
         stop = (Button)findViewById(R.id.stop);
         play = (Button)findViewById(R.id.play);

          myDb = new DataHelper(this);
         /*start.setOnClickListener(new View.OnClickListener() {

            int count = 0;
            @Override
            public void onClick(View view) {
                if(count == 0){
                    detector.start();
                    count++;
                    Button start = (Button)findViewById(R.id.start);
                    start.setText("STOP");
                } else {
                    detector.stop();
                    Button start = (Button)findViewById(R.id.start);
                    start.setText("START");
                    count = 0;
                }

            }
        });*/


        detector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT,(SurfaceView)findViewById(R.id.surfaceView));
        detector.setImageListener(this);
        detector.setDetectSmile(true);
        detector.setDetectAllEmotions(true);
        detector.setDetectAllEmojis(true);
        detector.setMaxProcessRate(10);

        //deal with audio recorder
        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/myrecording.3gp";;

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

    }


    public void onImageResults(List<Face> faces, Frame frame, float v) {
            TextView smile_score = (TextView)findViewById(R.id.smile);
            TextView joy_score = (TextView)findViewById(R.id.joy);
            TextView sadness_score = (TextView)findViewById(R.id.sadness);
            TextView surprise_score = (TextView)findViewById(R.id.surprise);
            TextView anger_score = (TextView)findViewById(R.id.anger);
            TextView contempt_score = (TextView)findViewById(R.id.contempt);
            TextView disgust_score = (TextView)findViewById(R.id.disgust);
            TextView engagement_score = (TextView)findViewById(R.id.engagement);
            TextView fear_score = (TextView)findViewById(R.id.fear);
            TextView valence_score = (TextView)findViewById(R.id.valence);
        //for emoji part
            TextView disappointed_txt = (TextView)findViewById(R.id.disappointed_emoji);
            TextView flushed_txt = (TextView)findViewById(R.id.flushed_emoji);
            TextView kissing_txt = (TextView)findViewById(R.id.kissing_emoji);
            TextView laughing_txt = (TextView)findViewById(R.id.laughing_emoji);
            TextView rage_txt = (TextView)findViewById(R.id.rage_emoji);
            TextView relaxed_txt = (TextView)findViewById(R.id.relaxed_emoji);
            TextView scream_txt = (TextView)findViewById(R.id.scream_emoji);
            TextView smiley_txt = (TextView)findViewById(R.id.smiley_emoji);
            TextView smirk_txt = (TextView)findViewById(R.id.smirk_emoji);
            TextView stuckOutTongue_txt = (TextView)findViewById(R.id.stuckOutTongue_emoji);
            TextView sotwinkingEye_txt = (TextView)findViewById(R.id.stuckOutTongueWinkingEye_emoji);
            TextView wink_txt = (TextView)findViewById(R.id.wink_emoji);


        if(faces.size() == 0){
            //handle emoji
            disappointed_txt.setText("NO Face");
            flushed_txt.setText("NO Face");
            kissing_txt.setText("NO Face");
            laughing_txt.setText("NO Face");
            rage_txt.setText("NO Face");
            relaxed_txt.setText("NO Face");
            scream_txt.setText("NO Face");
            smiley_txt.setText("NO Face");
            smirk_txt.setText("NO Face");
            stuckOutTongue_txt.setText("NO Face");
            sotwinkingEye_txt.setText("NO Face");
            wink_txt.setText("NO Face");


            //handle for emotion part
            smile_score.setText("NO Face");
            anger_score.setText("NO Face");
            surprise_score.setText("NO Face");
            sadness_score.setText("NO Face");
            contempt_score.setText("NO Face");
            disgust_score.setText("NO Face");
            engagement_score.setText("NO Face");
            fear_score.setText("NO Face");
            valence_score.setText("NO Face");

        } else {
                Face face = faces.get(0);
                //get the time value
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                if(count == 0){
                    start_time = dateFormat.format(date);
                    Log.d("start_time..........",start_time.toString());
                    count++;
                    time ="00: 00: 00";
                } else {
                    time = dateFormat.format(date);
                    try {
                        time = timeDifference(start_time,time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //handle emotions
                smile1 = String.format("%.2f",face.expressions.getSmile());
                Log.d("haaaaaaaaaaaaaaaaaaaaaa",smile1);
                joy1 = String.format("%.2f",face.emotions.getJoy());
                Log.d("haaaaaaaaaaaaaaaaaaaaaa",joy1);
                sadness1 = String.format("%.2f", face.emotions.getSadness());
                Log.d("haaaaaaaaaaaaaaaaaaaaaa",sadness1);
                surprise1 = String.format("%.2f",face.emotions.getSurprise());
            Log.d("haaaaaaaaaaaaaaaaaaaaaa",surprise1);
                anger1 = String.format("%.2f",face.emotions.getAnger());
            Log.d("haaaaaaaaaaaaaaaaaaaaaa",anger1);
                contempt1 = String.format("%.2f",face.emotions.getContempt());
            Log.d("haaaaaaaaaaaaaaaaaaaaaa",contempt1);
                disgust1 = String.format("%.2f",face.emotions.getDisgust());
            Log.d("haaaaaaaaaaaaaaaaaaaaaa",disgust1);
                engagement1 = String.format("%.2f",face.emotions.getEngagement());
            Log.d("haaaaaaaaaaaaaaaaaaaaaa",engagement1);
                fear1 = String.format("%.2f",face.emotions.getFear());
            Log.d("haaaaaaaaaaaaaaaaaaaaaa",fear1);
                valence1 = String.format("%.2f",face.emotions.getValence());
            Log.d("haaaaaaaaaaaaaaaaaaaaaa",valence1);
                smile_score.setText(String.format("%.2f",face.expressions.getSmile()));
                joy_score.setText(String.format("%.2f",face.emotions.getJoy()));
                sadness_score.setText(String.format("%.2f", face.emotions.getSadness()));
                surprise_score.setText(String.format("%.2f",face.emotions.getSurprise()));
                anger_score.setText(String.format("%.2f",face.emotions.getAnger()));
                contempt_score.setText(String.format("%.2f",face.emotions.getContempt()));
                disgust_score.setText(String.format("%.2f",face.emotions.getDisgust()));
                engagement_score.setText(String.format("%.2f",face.emotions.getEngagement()));
                fear_score.setText(String.format("%.2f",face.emotions.getFear()));
                valence_score.setText(String.format("%.2f",face.emotions.getValence()));

                //if score changes then insert new data and corresponding time to database
                if(smile1 != smile || joy1 != joy || sadness1 != sadness || surprise1 !=
                        surprise || anger1 != anger || contempt1 != contempt || disgust1 !=
                        disgust || engagement1 != engagement || fear1 != fear || valence1 != valence){
                    //Log.d("JJJJJJJJJJJJJJJJJJJ",Boolean.toString(myDb.insertData(time,smile1,joy1,sadness1,surprise1,anger1,contempt1,disgust1,engagement1,fear1,valence1)));
                    boolean isInserted = myDb.insertData(time,smile1,joy1,sadness1,surprise1,anger1,contempt1,disgust1,engagement1,fear1,valence1);
                    if(isInserted){
                        Toast.makeText(MainActivity.this,"Data inserted",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Data not inserted",Toast.LENGTH_LONG).show();
                    }
                }
                smile = smile1;
                joy = joy1;
                sadness = sadness1;
                surprise = surprise1;
                anger = anger1;
                contempt = contempt1;
                disgust = disgust1;
                engagement = engagement1;
                fear = fear1;
                valence = valence1;

            //set emojis scores to double type variable
            disappointed = round(face.emojis.getDisappointed());//index 0
            flushed = round(face.emojis.getFlushed());//index 1
            kissing = round(face.emojis.getKissing());//2
            laughing = round(face.emojis.getLaughing());//3
            rage = round(face.emojis.getRage());//4
            relaxed = round(face.emojis.getRelaxed());//5
            scream = round(face.emojis.getScream());//6
            smiley = round(face.emojis.getSmiley());//7
            smirk = round(face.emojis.getSmirk());//8
            stuckOutTongue = round(face.emojis.getStuckOutTongue());//9
            sotwinkingEye = round(face.emojis.getStuckOutTongueWinkingEye());//10
            wink = round(face.emojis.getWink());//11
            double[] emojiScore = new double[12];
            emojiScore[0] = disappointed;
            emojiScore[1] = flushed;
            emojiScore[2] = kissing;
            emojiScore[3] = laughing;
            emojiScore[4] = rage;
            emojiScore[5] = relaxed;
            emojiScore[6] = scream;
            emojiScore[7] = smiley;
            emojiScore[8] = smirk;
            emojiScore[9] = stuckOutTongue;
            emojiScore[10] = sotwinkingEye;
            emojiScore[11] = wink;
            double max = 0;
            //display corresponding emoji on camera preview
            for(int i = 0; i < emojiScore.length; i++) { // find max score
                if(emojiScore[i] > max) {
                    max = emojiScore[i];
                    emojiIndex = i;
                }
            }

            switch(emojiIndex) {
                case 0:
                    display.setImageResource(R.drawable.disappointed);
                    break;
                case 1:
                    display.setImageResource(R.drawable.flushed);
                    break;
                case 2:
                    display.setImageResource(R.drawable.kissing);
                    break;
                case 3:
                    display.setImageResource(R.drawable.laughing);
                    break;
                case 4:
                    display.setImageResource(R.drawable.rage);
                    break;
                case 5:
                    display.setImageResource(R.drawable.relaxed);
                    break;
                case 6:
                    display.setImageResource(R.drawable.screaming);
                    break;
                case 7:
                    display.setImageResource(R.drawable.smiley);
                    break;
                case 8:
                    display.setImageResource(R.drawable.smirk);
                    break;
                case 9:
                    display.setImageResource(R.drawable.facewithstuckouttongue);
                    break;
                case 10:
                    display.setImageResource(R.drawable.facewithstuckouttongueandwinkingeye);
                    break;
                case 11:
                    display.setImageResource(R.drawable.winking);
                    break;
                default:
                    display.setImageResource(R.drawable.relaxed);
            }
            //show emoji score in UI
            disappointed_txt.setText("" + disappointed);
            flushed_txt.setText("" + flushed);
            kissing_txt.setText("" + kissing);
            laughing_txt.setText("" + laughing);
            rage_txt.setText("" + rage);
            relaxed_txt.setText("" + relaxed);
            scream_txt.setText("" + scream);
            smiley_txt.setText("" + smiley);
            smirk_txt.setText("" + smirk);
            stuckOutTongue_txt.setText("" + stuckOutTongue);
            sotwinkingEye_txt.setText("" + sotwinkingEye);
            wink_txt.setText("" + wink);
        }
    }

    public static double round(double val) {
        DecimalFormat df2 = new DecimalFormat("###.######");
        return Double.valueOf(df2.format(val));
    }

    public String timeDifference(String time1,String time2) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        Date date3 = dateFormat.parse(time1);
        Log.d("time1..............",date3.toString());
        Date date4 = dateFormat.parse(time2);
        Log.d("time2..............",date4.toString());
        long diff1 = date4.getTime() - date3.getTime();
        long diffSeconds = diff1 / 1000 % 60;
        String diffSeconds1 ="";
        if(diffSeconds < 10){
            diffSeconds1 = "0" + diffSeconds;
        }
        else{
            diffSeconds1 = diffSeconds + "";
        }
        String diffMinutes1 = "";
        long diffMinutes = diff1 / (60 * 1000) % 60;
        if(diffMinutes < 10){
            diffMinutes1 = "0" + diffMinutes;
        }
        else{
            diffMinutes1 = diffMinutes + "";
        }
        String diffHours1 = "";
        long diffHours = diff1 / (60 * 60 * 1000) % 24;
        if(diffHours < 10){
            diffHours1 = "0" + diffHours;
        }
        else{
            diffHours1 = diffHours + "";
        }
        String out =diffHours1 + ": " +diffMinutes1 + ": " + diffSeconds1 ;
        return out;
    }


    public void start(View view){
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
            detector.start();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        start.setEnabled(false);
        start.setBackgroundColor(Color.GRAY);
        play.setEnabled(false);
        play.setBackgroundColor(Color.GRAY);
        stop.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

    }

    public void stop(View view){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        detector.stop();
        myAudioRecorder  = null;
        start.setEnabled(true);
        start.setBackgroundResource(R.color.unable);
        stop.setEnabled(false);
        stop.setBackgroundColor(Color.GRAY);
        play.setEnabled(true);
        play.setBackgroundResource(R.color.colorPrimaryDark);
        Toast.makeText(getApplicationContext(), "Audio recorded successfully",
                Toast.LENGTH_LONG).show();
    }
    public void play(View view) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException{

        MediaPlayer m = new MediaPlayer();
        m.setDataSource(outputFile);
        m.prepare();
        m.start();
        start.setEnabled(false);
        start.setBackgroundColor(Color.GRAY);
        stop.setEnabled(false);
        stop.setBackgroundColor(Color.GRAY);
        play.setEnabled(false);
        play.setBackgroundColor(Color.GRAY);
        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
    }


}
