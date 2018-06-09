package android.print;

import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;

public class PdfPrint {
    private static final String TAG = PdfPrint.class.getSimpleName();
    private final PrintAttributes printAttributes;
    private String mFullPath;

    public PdfPrint(PrintAttributes printAttributes) {
        this.printAttributes = printAttributes;
    }

    public void print(final PrintDocumentAdapter printAdapter, String fullPath, final CallbackPrint callback) {
        mFullPath = fullPath;

        if (mFullPath==null || mFullPath.equals("")){
            Log.e(TAG,"Please check the given path of file!");
            callback.onFailure();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            printAdapter.onLayout(null, printAttributes, null, new PrintDocumentAdapter.LayoutResultCallback() {
                @Override
                public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        printAdapter.onWrite(new PageRange[]{PageRange.ALL_PAGES}, getOutputFile(mFullPath), new CancellationSignal(), new PrintDocumentAdapter.WriteResultCallback() {
                            @Override
                            public void onWriteFinished(PageRange[] pages) {
                                super.onWriteFinished(pages);
                                if (pages.length > 0) {
                                    callback.success(mFullPath);
                                } else {
                                    callback.onFailure();
                                }
                            }
                        });
                    }
                }
            }, null);
        }
    }


    private ParcelFileDescriptor getOutputFile(String fullPath) {
        File file = new File(fullPath);
        try {
            file.createNewFile();
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e);
        }
        return null;
    }


    public interface CallbackPrint {
        void success(String path);

        void onFailure();
    }
}
