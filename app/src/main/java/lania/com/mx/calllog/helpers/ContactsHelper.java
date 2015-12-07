package lania.com.mx.calllog.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import lania.com.mx.calllog.models.Action;

/**
 * Created by clerks on 12/7/15.
 */
public final class ContactsHelper {
    private static final String TAG = ContactsHelper.class.getSimpleName();
    public static final String WITH_PHONE_NUMBER = "1";

    public static void exportContacts(Context context,
                                      String filePathDirectory, int boundary, Action callback) {

        List<String> contactsIncluded = new ArrayList<>();
        List<String> files = new ArrayList<>();
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = ?";
        String[] selectionArgs = {WITH_PHONE_NUMBER};
        String[] projection =
                new String[]{ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.Contacts.DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection
                , selection, selectionArgs,
                ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor == null) {
            return;
        }
        try {
            StringBuilder uriListBuilder = new StringBuilder();
            int index = 0;
            int fileIndex = 0;

            while (cursor.moveToNext()) {
                String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (!contactsIncluded.contains(lookupKey)) {
                    Log.d(TAG, String.format("name %s lookupKey  %s", name, lookupKey));
                    if (index > 0 && (index % boundary) == 0) {
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_MULTI_VCARD_URI,
                                Uri.encode(uriListBuilder.toString()));
                        String filePath = saveUriToFile(context, uri, filePathDirectory, fileIndex);
                        Log.d(TAG, filePath);
                        files.add(filePath);
                        fileIndex++;
                        uriListBuilder = new StringBuilder();
                    }

                    if (index != 0 || (index % boundary) != 0)
                        uriListBuilder.append(':');

                    uriListBuilder.append(lookupKey);
                    contactsIncluded.add(lookupKey);
                    index++;
                }

            }

            if (uriListBuilder.toString() != "") {
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_MULTI_VCARD_URI,
                        Uri.encode(uriListBuilder.toString()));
                String filePath = saveUriToFile(context, uri, filePathDirectory, fileIndex);
                Log.d(TAG, filePath);
                files.add(filePath);
            }
        } finally {
            cursor.close();
        }

        callback.execute(files);
    }

    public static void exportContacts(Context context,
                                      String filePath, Action callback) {
        final ContentResolver contentResolver = context.getContentResolver();

        final File file = new File(filePath);
        file.delete();

        Uri uri = getAllContactsVcardUri(context);

        AssetFileDescriptor assetFileDescriptor;
        try {
            assetFileDescriptor = contentResolver.openAssetFileDescriptor(uri, "r");
            if (assetFileDescriptor == null)
                return;

            FileInputStream fileInputStream = assetFileDescriptor.createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(filePath, true);
            copyLarge(fileInputStream, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        callback.execute();
    }

    public static void exportContacts(Context context,
                                      String filePath, List<String> contacts, Action callback) {
        final ContentResolver contentResolver = context.getContentResolver();

        //contacts apply filter
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst())
            return;

        final File file = new File(filePath);
        file.delete();

        do {
            String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);

            AssetFileDescriptor assetFileDescriptor;
            try {
                assetFileDescriptor = contentResolver.openAssetFileDescriptor(uri, "r");
                if (assetFileDescriptor == null)
                    return;

                FileInputStream fileInputStream = assetFileDescriptor.createInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath, true);
                copyLarge(fileInputStream, fileOutputStream);
                fileOutputStream.close();
                cursor.moveToNext();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } while (cursor.moveToNext());

        if (cursor != null)
            cursor.close();

        callback.execute();
    }

    private static String saveUriToFile(Context context, Uri uri, String filePathDirectory, int fileIndex) {

        File directory = new File(filePathDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = filePathDirectory.concat("contacts" + fileIndex + ".vcf");

        AssetFileDescriptor assetFileDescriptor;
        try {
            assetFileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            if (assetFileDescriptor == null)
                return "";

            FileInputStream fileInputStream = assetFileDescriptor.createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(filePath, false);
            copyLarge(fileInputStream, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e1) {
            Log.e(TAG, e1.getMessage(), e1);
        }
        return filePath;
    }

    private static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private static Uri getAllContactsVcardUri(Context context) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            StringBuilder uriListBuilder = new StringBuilder();
            int index = 0;
            while (cursor.moveToNext()) {
                if (index != 0) uriListBuilder.append(':');
                uriListBuilder.append(cursor.getString(0));
                index++;
            }
            return Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_MULTI_VCARD_URI,
                    Uri.encode(uriListBuilder.toString()));
        } finally {
            cursor.close();
        }
    }
}
