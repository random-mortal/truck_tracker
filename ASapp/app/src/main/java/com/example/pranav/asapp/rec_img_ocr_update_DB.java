package com.example.pranav.asapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;

import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;

import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.graphics.Bitmap;

import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import android.os.StrictMode;







public class rec_img_ocr_update_DB extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_img_ocr_update__db);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }















        //get the image view
        ImageView picView = (ImageView) findViewById(R.id.picture);
        //get the text view
        TextView txtView = (TextView) findViewById(R.id.txt);


        //get the received intent
        Intent receivedIntent = getIntent();


        //get the action
        String receivedAction = receivedIntent.getAction();

        //find out what we are dealing with
        String receivedType = receivedIntent.getType();


        if (receivedAction.equals(Intent.ACTION_SEND)) {
            //content is being shared

            if (receivedType.startsWith("image/")) {
                //handle sent image


                //the next line hides the text view
                //txtView.setVisibility(View.GONE);


                //get the uri of the received image
                Uri receivedUri = (Uri) receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);

                //check we have a uri
                if (receivedUri != null) {
                    //set the picture
                    //RESAMPLE YOUR IMAGE DATA BEFORE DISPLAYING
                    //picView.setImageURI(receivedUri);//this will display the shared image in this app


                    Context context = getApplicationContext();
                    TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

                    if (!textRecognizer.isOperational()) {
                        new AlertDialog.Builder(this)
                                .setMessage("Text recognizer could not be set up on your device :(")
                                .show();
                        return;
                    }


                    //need to make a Frame as an input for textRecognizer


                    //we need a bitmap to make the frame
                    Bitmap bitmap = null;


                    try {


                        // We need to recyle unused bitmaps
                        if (bitmap != null) {
                            bitmap.recycle();
                        }


                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), receivedUri);
                        //now, the bitmap is ready, we need to make a frame using this












































                        //resample the bitmap
                        //bitmap = getBitmap(receivedIntent);



















































                        Frame inputframe = new Frame.Builder().setBitmap(bitmap).build();
                        //now, the frame is ready for the textRecognizer


                        String textResult = "OCR Results:\n\n\n\n";


                        //input the frame into the textRecognizer
                        SparseArray<TextBlock> textblock = textRecognizer.detect(inputframe);
                        TextBlock tb = null;
                        List<Text> texto = new ArrayList<>();


                        for (int i = 0; i < textblock.size(); i++) {
                            tb = textblock.get(textblock.keyAt(i));
                            //Log.e("TEXT", tb.toString() + "");
                            texto.addAll(tb.getComponents());
                        }

                        for (Text t : texto) {
                            for (Text t2 : t.getComponents()) {
                                textResult += t2.getValue() + " ";
                            }
                            textResult += "\n";
                        }

                        if (!textResult.equals("")) {//if textResult is not empty
                            bitmap.recycle();
                            txtView.setText(textResult + "");
                            Log.e("TEXT", textResult + "");
                        } else {
                            Toast toast = Toast.makeText(this, "failed", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                            txtView.setText("empty result");
                            Log.e("TEXT", "empty result" + "");
                        }


                        //now the result is in texto and textResult.


                        updatedb(textResult);

                        Log.e("TEXT", "\nDatabase successfully updated!" + "");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //


                }


            } else if (receivedType.startsWith("text/")) {
                //handle sent text

                //hide the other ui item
                picView.setVisibility(View.GONE);

                //get the received text
                String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);


                //check we have a string
                if (receivedText != null) {
                    //set the text
                    txtView.setText(receivedText);
                }

            }
        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
            //app has been launched directly, not from share list

            txtView.setText("Nothing has been shared!");

        }


    }









    public void updatedb(String textResult) {

        int start = 0;
        int end = 0;

        String ownername = "";
        String regno = "";
        String makermodel = "";
        String regdate = "";
        String fueltype = "";
        String vehclass = "";

        //String vehage = "";
        String engineno = "";
        String chassisno = "";
        String fitness = "";
        String insurance = "";
        //String fuelnorms = "";













        start = textResult.indexOf("Owner Name");
        start = textResult.indexOf("\n",  start   );
        start = start + 1;
        end = textResult.indexOf("Registration No");
        end = end - 2;
        ownername = ownername + textResult.substring(start, end);
        //ownername = ownername + textResult.charAt(start);





























        start = textResult.indexOf("Registration No");
        if(start>0)
        {
            start = start + 18;
            end = textResult.indexOf("\n",  start   );
            while(end<=start)
            {
                end = textResult.indexOf("\n",  start  +1 );
            }
            end = end - 1;
            if(end<textResult.length()-1 && end>0)
            {
                String temp = textResult.substring(start, end);
                String temp1 = "" + temp.charAt(0) + temp.charAt(1);
                for(int i=2;i<=3;i++)
                {
                    if(temp.charAt(i)=='O')
                    {
                        temp1 = temp1 + "0";
                    }
                    else
                    {
                        temp1 = temp1 + temp.charAt(i);
                    }
                }

                temp1 = temp1 + temp.substring(4);

                regno = regno + temp1;

            }

            //regno = "..." + start + "..." + end;
        }


















        start = textResult.indexOf("Maker");
        start = textResult.indexOf("\n",  start   );
        start = start + 1;
        end = textResult.indexOf("Registration Date");
        end = end - 2;
        makermodel = makermodel + textResult.substring(start, end);
        //ownername = ownername + textResult.charAt(start);







        start = textResult.indexOf("Registration Date");
        if(start>0)
        {
            start = start + 19;
            end = textResult.indexOf("\n",  start   );
            while(end<=start)
            {
                end = textResult.indexOf("\n",  start  +1 );
            }
            end = end - 1;



            if(end<textResult.length()-1 && end>0)
            {
                String temp2 = textResult.substring(start, end);
                if(temp2.indexOf("No")>0)
                {
                    int l = temp2.indexOf("No");
                    char[] charr = temp2.toCharArray();
                    charr[l+2]='v';
                    temp2 = String.valueOf(charr);
                }
                regdate = regdate + temp2;
                //makermodel = "..." + textResult.charAt(start) + "..." + textResult.charAt(end-1);
            }
        }









        start = textResult.indexOf("Fuel Type");
        if(start>0)
        {
            start = start + 12;
            end = textResult.indexOf("\n",  start   );
            while(end<=start)
            {
                end = textResult.indexOf("\n",  start  +1 );
            }
            end = end - 1;

            if(end<textResult.length()-1 && end>0)
            {
                fueltype = fueltype + textResult.substring(start, end);
                //makermodel = "..." + textResult.charAt(start) + "..." + textResult.charAt(end-1);
            }
        }




        start = textResult.indexOf("Vehicle Class");
        if(start>0)
        {
            start = start + 15;
            end = textResult.indexOf("\n",  start   );
            while(end<=start)
            {
                end = textResult.indexOf("\n",  start  +1 );
            }
            end = end - 1;

            if(end<textResult.length()-1 && end>0)
            {
                vehclass = vehclass + textResult.substring(start, end);
                //vehclass = "..." + start + "..." + end;
            }
        }



        //vehicle age is not neeeded in the table. it needs to be freshly calculated using registration date
        /*start = textResult.indexOf("Vehicle Age");
        if(start>0)
        {
            start = start + 13;
            end = textResult.indexOf("\n",  start   );
            end = end - 1;

            if(end<textResult.length()-1 && end>0)
            {
                vehage = vehage + textResult.substring(start, end);
            }
        }*/

        start = textResult.indexOf("Engine No");
        if(start>0)
        {
            start = start + 12;
            end = textResult.indexOf("\n",  start   );
            while(end<=start)
            {
                end = textResult.indexOf("\n",  start  +1 );
            }
            end = end - 1;

            if(end<textResult.length()-1 && end>0)
            {
                engineno = engineno + textResult.substring(start, end);
            }
        }







        start = textResult.indexOf("Chassis No");
        if(start<0)
        {
            start = textResult.indexOf("Chassjs No");
        }


        if(start>0)
        {
            start = start + 12;
            end = textResult.indexOf("\n",  start  );
            while(end<=start)
            {
                end = textResult.indexOf("\n",  start  +1 );
            }
            end = end - 1;


            if(end<textResult.length()-1 && end>0)
            {
                chassisno = chassisno + textResult.substring(start, end);
                //chassisno = " " + start + " " + end;
            }
        }








        start = textResult.indexOf("Fitness Upto");
        if(start>0)
        {
            start = start + 15;
            end = start + 11;

            if(end<textResult.length()-1)
            {
                String temp2 = textResult.substring(start, end);
                if(temp2.indexOf("No")>0)
                {
                    int l = temp2.indexOf("No");
                    char[] charr = temp2.toCharArray();
                    charr[l+2]='v';
                    temp2 = String.valueOf(charr);
                }
                fitness = fitness + temp2;
                //makermodel = "..." + textResult.charAt(start) + "..." + textResult.charAt(end-1);
            }
        }

        start = textResult.indexOf("Insurance Upto");
        if(start>0)
        {
            start = start + 17;
            end = start + 11;

            if(end <= textResult.length()-1)
            {
                String temp2 = textResult.substring(start, end);
                if(temp2.indexOf("No")>0)
                {
                    int l = temp2.indexOf("No");
                    char[] charr = temp2.toCharArray();
                    charr[l+2]='v';
                    temp2 = String.valueOf(charr);
                }
                insurance = insurance + temp2;
                //makermodel = "..." + textResult.charAt(start) + "..." + textResult.charAt(end-1);
            }
        }














        //update the database using the above strings




        background bg= new background(this);
        bg.execute(ownername,regno, makermodel, regdate, fueltype, vehclass, engineno, chassisno, fitness, insurance);







    }


































    private Bitmap getBitmap(Intent receivedIntent) {

        try
        {
            int inWidth = 0;
            int inHeight = 0;
            int dstWidth = 720;
            int dstHeight = 1280;

            ContentResolver mContentResolver = this.getContentResolver();
            InputStream in = null;
            Uri uri = (Uri) receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            in = mContentResolver.openInputStream(uri);


            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            //in = new FileInputStream(pathOfInputImage);
            in = mContentResolver.openInputStream(uri);




            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
            return resizedBitmap;
            /*// save image
            try
            {
                FileOutputStream out = new FileOutputStream(pathOfOutputImage);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            }
            catch (Exception e)
            {
                Log.e("Image", e.getMessage(), e);
            }*/
        }
        catch (IOException e)
        {
            Log.e("Image", e.getMessage(), e);
        }
        return null;


    }








    /*public Bitmap getBitmap(Intent receivedIntent) {

        Uri uri = (Uri) receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP








            ContentResolver mContentResolver = this.getContentResolver();
            in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();


            int scale = 1;
            while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            //Log.d(TAG, "scale = " + scale + ", orig-width: " + options.outWidth + ", orig-height: " + options.outHeight);

            Bitmap resultBitmap = null;
            in = mContentResolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                resultBitmap = BitmapFactory.decodeStream(in, null, options);

                // resize to desired dimensions
                int height = resultBitmap.getHeight();
                int width = resultBitmap.getWidth();
                //Log.d(TAG, "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(resultBitmap, (int) x,
                        (int) y, true);
                resultBitmap.recycle();
                resultBitmap = scaledBitmap;

                System.gc();
            } else {
                resultBitmap = BitmapFactory.decodeStream(in);
            }
            in.close();

            //Log.d(TAG, "bitmap size - width: " + resultBitmap.getWidth() + ", height: " + resultBitmap.getHeight());
            return resultBitmap;
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage(), e);
            return null;
        }

    }*/





}
