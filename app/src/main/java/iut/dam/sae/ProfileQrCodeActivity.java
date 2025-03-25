package iut.dam.sae;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileQrCodeActivity extends AppCompatActivity {
    private ImageView qrCodeImageView;
    private Button shareButton;
    private Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_qr_code);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        shareButton = findViewById(R.id.shareButton);

        // Générer le QR code
        String intentUri = "intent:#Intent;action=android.intent.action.VIEW;" +
                "package=" + getPackageName() + ";" +
                "component=" + getPackageName() + "/.MainActivity;" +
                "end";

        qrCodeBitmap = generateQRCode(intentUri, 500, 500);

        if (qrCodeBitmap != null) {
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        }

        // Bouton de partage
        shareButton.setOnClickListener(v -> shareQRCode(qrCodeBitmap));
    }

    private Bitmap generateQRCode(String content, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void shareQRCode(Bitmap qrCodeBitmap) {
        // Sauvegarder le QR code temporairement
        try {
            File cachePath = new File(getCacheDir(), "qr_code.png");
            FileOutputStream stream = new FileOutputStream(cachePath);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri contentUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    cachePath
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Téléchargez l'application France Asso Santé et rejoignez-nous !");

            startActivity(Intent.createChooser(shareIntent, "Partager le QR Code"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}