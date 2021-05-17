package com.coreui.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by ahmed.saad on 11/17/16.
 */

public class BitmapUtils {


//    public static Bitmap generateImageFromPdf(Context context,Uri pdfUri) {
//        int pageNumber = 0;
//        PdfiumCore pdfiumCore = new PdfiumCore(context);
//        try {
//            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
//            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(pdfUri, "r");
//            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
//            pdfiumCore.openPage(pdfDocument, pageNumber);
//            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
//            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
//            pdfiumCore.closeDocument(pdfDocument); // important!
//            return bmp;
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";
    private void saveImage(Bitmap bmp) {
        FileOutputStream out = null;
        try {
            File folder = new File(FOLDER);
            if(!folder.exists())
                folder.mkdirs();
            File file = new File(folder, "PDF.png");
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // test utils
    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

//    public static Bitmap scaleBitmap(Context context, Bitmap bitmap, int width, int height, boolean inPixels){
//        if(!inPixels){
//            width = DimensionUtils.convertPXToDP(context,width);
//            height = DimensionUtils.convertPXToDP(context,height);
//        }
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
//        return scaledBitmap;
//    }
    public static Bitmap scaleBitmapToWidth(Bitmap bitmap, int width){
        float factor = width / (float) bitmap.getWidth();
        return Bitmap.createScaledBitmap(bitmap,width,(int)(bitmap.getHeight()*factor),true);
    }
    public static Bitmap scaleBitmapToHeight(Bitmap bitmap, int height){
        float factor = height/(float) bitmap.getHeight();
        return  Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*factor),bitmap.getHeight(),true);
    }
    public static void setSvg(Context context, int sourceId, ImageView imageView){
//        SVG svg = SVGParser.getSVGFromResource(context.getResources(), sourceId);
//        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
//        imageView.setImageDrawable(svg.createPictureDrawable());

    }
    public static String convertToBase64(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
//        encodedImage=encodedImage.replaceAll("[^A-Za-z0-9/+]", "");
        return ""+encodedImage;
    }
    public static Bitmap decodeFile(File f){
        Bitmap b = null;
        final int IMAGE_MAX_SIZE = 400;
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2.0, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (Exception e) {
            Log.v("Exception",e.toString()+"");
        }
        return b;
    }
    public static String convertToBase64FromFile(File file){
        if (file ==null)return "";
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
        return ""+encodedImage;
    }
    private static File persistImage(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("ERROR", "Error writing bitmap", e);
        }finally {
            return imageFile;
        }
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static Bitmap getDrawableFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
    public  static Bitmap compressImage(File file) {
        String filePath = file.getPath();
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
//            ConstantDataBikerApp.logErrorInfo(MyTrolleyApplicationData.getAppContext(),
//                    AnalyticsEvent.EVENT_ERROR,
//                    ConstantDataBikerApp.getStackTraceString(exception));
            exception.printStackTrace();
        }
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
//            ConstantDataBikerApp.logErrorInfo(MyTrolleyApplicationData.getAppContext(),
//                    AnalyticsEvent.EVENT_ERROR,
//                    ConstantDataBikerApp.getStackTraceString(exception));
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
//            ConstantDataBikerApp.logErrorInfo(MyTrolleyApplicationData.getAppContext(),
//                    AnalyticsEvent.EVENT_ERROR,
//                    ConstantDataBikerApp.getStackTraceString(exception));
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));
        if (bmp != null) {
            bmp.recycle();
        }
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
//            ConstantDataBikerApp.logErrorInfo(MyTrolleyApplicationData.getAppContext(),
//                    AnalyticsEvent.EVENT_ERROR,
//                    ConstantDataBikerApp.getStackTraceString(e));
            e.printStackTrace();
        }
        return scaledBitmap;
    }
    // /--------------- compress image if size > 500 KB and return byte64 string
    // of compress image
    // /----------------if out of memory occur return byte64 string of compress
    // image original image
    // /----------------this processing must be done in asynctask
    /*public static String getCompressBase64(String imagePath, Context context) {
        String base64 = "";
        if (imagePath.startsWith("content://")) {
            String contentpath = getPath(context, Uri.parse(imagePath));
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(contentpath));
                if (fileInputStream != null
                        && fileInputStream.available() > 500) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = compressImage(contentpath);
                    } catch (OutOfMemoryError e) {
                    }
                    if (bitmap != null) {
                        ByteArrayOutputStream baosCompressImage = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                baosCompressImage);
                        bitmap.recycle();
                        byte[] compressImageByte = baosCompressImage
                                .toByteArray();
                        return Base64.encodeToString(compressImageByte,
                                Base64.DEFAULT);
                    } else {
                        Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        bm.recycle();
                        return Base64.encodeToString(b, Base64.DEFAULT);
                    }
                }
            } catch (Exception e) {
            }
        } else {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(imagePath));

                if (fileInputStream != null
                        && fileInputStream.available() > 500) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = compressImage(imagePath);
                    } catch (OutOfMemoryError e) {
                    }
                    if (bitmap != null) {
                        ByteArrayOutputStream baosCompressImage = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                baosCompressImage);
                        bitmap.recycle();
                        byte[] compressImageByte = baosCompressImage
                                .toByteArray();
                        return Base64.encodeToString(compressImageByte,
                                Base64.DEFAULT);
                    } else {
                        Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        bm.recycle();
                        byte[] b = baos.toByteArray();
                        return Base64.encodeToString(b, Base64.DEFAULT);
                    }
                }
            } catch (Exception e) {
            }
        }
        return base64;
    }*/
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.MediaColumns.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
