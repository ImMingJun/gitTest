package com.xinhuamm.xinhuasdk.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileUtils {

    public static final String TAG = FileUtils.class.getSimpleName();

    public static final String DefaultCharset = "UTF-8";
    private static final int BUFFER_SIZE = 1024;

    // 得到应用位于sd卡中的根目录
    public static String getAppSDRoot(Context context, boolean create) {

        if (!isSDCardInserted() || context == null)
            return null;
        String sdRootFolder = Environment.getExternalStorageDirectory().getPath();
        String path = sdRootFolder + "/" + context.getPackageName();
        if (!create)
            return path;
        else
            return getPath(path);
    }

    /**
     * 是否有外存卡
     * @return
     */
    public static boolean isExistExternalStore() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // 如果当前路径没被创建，创建之
    public static String getPath(String dir) {
        File folder = new File(dir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return dir;
    }

    // 该路径是否 文件夹
    public static boolean isDirectory(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

    // 获取完整路径中的文件名
    public static String getFileName(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }

        File f = new File(path);
        if (!f.isFile())
            return "";

        return f.getName();
    }

    // 获取完整路径中的父目录
    public static String getParentDirectory(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }

        File f = new File(path);
        return f.getParent();
    }

    // 文件是否存在
    public static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        return f.exists();
    }

    public static boolean isFileExists(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    // 创建文件夹
    public static boolean createDirectory(String filePath) {
        if (filePath.endsWith("/")) {
            return createDirectory(new File(filePath));
        } else {
            return createParentDirectory(new File(filePath));
        }
    }

    public static boolean createDirectory(File dir) {
        if (!dir.exists()) {
             return dir.mkdirs();
        }
        return false;
    }

    public static boolean createParentDirectory(File path) {
        File dir = path.getParentFile();
        if (dir != null && !dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    // 获取文件大小
    public static long getFileSize(String path) {
        if (!isFileExists(path)) {
            return 0;
        }
        File f = new File(path);
        return f.length();
    }

    public static long getFileSize(File file) {
        if (file == null) {
            return 0;
        }
        return file.length();
    }

    // 删除文件夹
    public static void deleteDirectory(String path) {
        deleteDirectory(path, true);
    }

    public static void deleteDirectory(String path, boolean deleteChild) {
        if (!isFileExists(path))
            return;

        File f = new File(path);

        if (deleteChild) {
            File[] children = f.listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    if (child.isDirectory()) {
                        deleteDirectory(child.getAbsolutePath(), true);

                    } else {
                        child.delete();
                    }
                }
            }
        }

        f.delete();
    }

    // 删除文件
    public static void deleteFile(String path) {
        if (!isFileExists(path)) {
            return;
        }

        File f = new File(path);
        f.delete();
    }

    public static void deleteFile(File file) {
        if (!isFileExists(file)) {
            return;
        }

        file.delete();
    }

    // 重命名
    public static boolean renameFile(String path, String newPath) {
        if (!isFileExists(path)) {
            return false;
        }
        File f = new File(path);
        return f.renameTo(new File(newPath));
    }

    public static boolean renameFile(File src, File dst) {
        if (!isFileExists(src)) {
            return false;
        }
        return src.renameTo(dst);
    }

    // 复制文件
    public static void copyFile(String fromPath, String toPath, boolean rewrite) {
        if (TextUtils.isEmpty(fromPath) || TextUtils.isEmpty(toPath))
            return;

        if (fromPath.equals(toPath))
            return;

        copyFile(new File(fromPath), new File(toPath), rewrite);
    }

    // 复制文件
    public static void copyFile(File fromFile, File toFile, boolean rewrite) {
        if (fromFile.getAbsolutePath().equals(toFile.getAbsolutePath())) {
            return;
        }

        if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
            return;
        }

        createDirectory(toFile);

        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        FileInputStream fosfrom = null;
        FileOutputStream fosto = null;
        try {
            fosfrom = new FileInputStream(fromFile);
            fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[BUFFER_SIZE];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            closeStream(fosfrom);
            closeStream(fosto);
        }
    }

    public static void copyFile(File source, File target) {

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(source);
            fo = new FileOutputStream(target);

            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(fi);
            closeChannel(in);
            closeStream(fo);
            closeChannel(out);
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String readText(String filepath) throws IOException {
        return readText(filepath, DefaultCharset);
    }

    public static String readText(String filepath, String charsetName) throws IOException {
        FileInputStream inputStream = null;
        InputStreamReader reader = null;
        try {
            inputStream = new FileInputStream(filepath);
            reader = new InputStreamReader(inputStream, charsetName);

            char[] chars = new char[BUFFER_SIZE];
            StringBuilder sb = new StringBuilder();
            int size;

            while ((size = reader.read(chars)) > 0) {
                sb.append(chars, 0, size);
            }

            return sb.toString();

        } finally {
            closeStream(reader);
            closeStream(inputStream);
        }
    }

    public static void writeText(String filepath, String content) throws IOException {
        writeText(filepath, content, DefaultCharset);
    }

    public static void writeText(String filepath, String content, String charsetName) throws IOException {
        createDirectory(filepath);
        deleteFile(filepath);

        FileOutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            outputStream = new FileOutputStream(filepath);
            writer = new OutputStreamWriter(outputStream, charsetName);
            writer.write(content);
            writer.flush();

        } finally {
            closeStream(writer);
            closeStream(outputStream);
        }

    }

    public static void writeToFile(InputStream dataIns, File target) throws IOException {

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(target));
        int count;
        byte data[] = new byte[BUFFER_SIZE];
        while ((count = dataIns.read(data, 0, BUFFER_SIZE)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }

    public static void writeToFile(byte[] data, File target) throws IOException {
        FileOutputStream fo = null;
        ReadableByteChannel src = null;
        FileChannel out = null;
        try {
            src = Channels.newChannel(new ByteArrayInputStream(data));
            fo = new FileOutputStream(target);
            out = fo.getChannel();
            out.transferFrom(src, 0, data.length);
        } finally {
            if (fo != null) {
                fo.close();
            }
            if (src != null) {
                src.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }


    public static void closeStream(InputStream is) {
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
        }
    }

    public static void closeStream(OutputStream os) {
        try {
            if (os != null)
                os.close();
        } catch (IOException e) {
        }
    }

    public static void closeStream(Reader is) {
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
        }
    }

    public static void closeStream(Writer os) {
        try {
            if (os != null)
                os.close();
        } catch (IOException e) {
        }
    }

    public static void closeChannel(FileChannel channel) {
        try {
            if (channel != null)
                channel.close();
        } catch (IOException e) {
        }
    }

    // 获取内置SD卡路径
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    // 获取外置SD卡路径, 应该就一条记录或空
    public static List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }

    public static long getSDCardAvailable() {
        if (!isSDCardInserted())
            return 0;
        File sdcardDir = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(sdcardDir.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize;
    }

    @SuppressLint("NewApi")
    public static boolean isSDCardInserted() {
        boolean ret = false;
        String state = Environment.getExternalStorageState();
        ret = Environment.MEDIA_MOUNTED.equals(state);
        if (ret) {
            // test whether the path can read and write.
            File sdCardDir = Environment.getExternalStorageDirectory();
            ret = sdCardDir != null && sdCardDir.canRead()
                    && sdCardDir.canWrite();
            return ret;
        }
        // Android 9
        if (Build.VERSION.SDK_INT < 9) {
            return false;
        } else {
            try {
                ret = Environment.isExternalStorageRemovable();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            ret = !ret;
        }
        return ret;
    }

    @SuppressWarnings("rawtypes")
    protected static class CompratorByLastModified implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == o2) {
                return 0;
            }

            File file1 = (File) o1;
            File file2 = (File) o2;
            long diff = file1.lastModified() - file2.lastModified();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    // reduce the file size to minSize if total size >= maxSize.
    // 如果文件大于最大size，则删除文件到最小size
    public static boolean reduceDirSize(File dir, long minSize, long maxSize) {
        boolean ret = false;
        File[] fileList = dir.listFiles();
        if (null == dir || maxSize <= 0 || !dir.isDirectory()
                || fileList == null) {
            return ret;
        }

        try {
            Arrays.sort(fileList, new CompratorByLastModified());
        } catch (Exception e) {
            return ret;
        }

        long totalSize = 0;
        for (File f : fileList) {
            totalSize += f.length();
            // delete the file when file length = 0
            if (0 == f.length()) {
                f.delete();
            }
        }

        if (totalSize < maxSize * 1024) {
            return true;
        }
        int index = 0;

        while ((totalSize >= (minSize * 1024)) && (index < fileList.length)) {
            totalSize -= fileList[index].length();
            // move file before delete
            fileList[index].delete();
            index++;
        }
        ret = true;
        return ret;
    }


    public static byte[] file2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

/**
     * 检查外置sd卡是否存在
     */
    public static boolean checkExtSdCard(Context context) {
//        List<String> extSDCardPath = FileUtils.getExtSDCardPath();//获取外置SD卡路径
        String ExterPath = FileUtils.getStoragePath(context,true);
        Log.d("SettingAct", "checkExtSdCard: " + ExterPath);
        if (TextUtils.isEmpty(ExterPath)) {
            return false;
        }
        return true;
    }

    /**
     * 通过反射的方式使用在sdk中被 隐藏 的类 StroageVolume 中的方法getVolumeList()，获取所有的存储空间（Stroage Volume），
     * 然后通过参数is_removable控制，来获取内部存储和外部存储（内外sd卡）的路径，
     * 参数 is_removable为false时得到的是内置sd卡路径，为true则为外置sd卡路径。
     *
     *
     * @param is_removale
     * @return
     */
    public static String getStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把字节数组保存为一个文件
     */
    public static boolean SaveFile(File file, byte[] b)
    {
        return SaveFile(file,b,0,b.length);
    }
    /**
     * 把字节数组保存为一个文件
     */
    public static boolean SaveFile(File file, byte[] b, int offset, int length)
    {
        boolean ret = false;
        BufferedOutputStream stream = null;
        try {
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b, offset, length);
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return ret;
    }

    public static boolean isImageFile(String filename)
    {
        String s = filename.toLowerCase();
        return (s.endsWith(".jpeg") ||
                s.endsWith(".jpg")  ||
                s.endsWith(".gif")  ||
                s.endsWith(".bmp")  ||
                s.endsWith(".png")
        );
    }
    public static String Format_Size(String strSize) {
        String s;
        int L = strSize.length();
        if(L<4)
            s = "0."+strSize.substring(0, 1)+"k";
        else if(L>6)
            s = strSize.substring(0, L-6)+"."+strSize.substring(6, 7) + "M";
        else if(L==4)
            s = strSize.substring(0, 1)+"."+strSize.substring(1, 2)+"k";
        else
            s = strSize.substring(0, L-3) + "k";
        return (s);
    }

    public static Uri getUriFromFile(Context context, File file){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            String filePath = file.getAbsolutePath();
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Images.Media._ID },
                    MediaStore.Images.Media.DATA + "=? ",
                    new String[] { filePath }, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                if (file.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    return null;
                }
            }
        }else {
            return Uri.fromFile(file);
        }

    }
}
