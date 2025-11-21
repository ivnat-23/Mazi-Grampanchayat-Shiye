package com.example.mazigrampanchayat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for displaying notice image in full-screen with pinch-to-zoom functionality
 */
public class Activity_notice_detail extends AppCompatActivity {

    private ImageView noticeDetailImage;
    private TextView noticeTitle;
    private ImageButton backButton;
    
    // Variables for pinch-to-zoom
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notice_detail);

        initializeViews();
        loadNoticeData();
        setupBackButton();
        setupPinchToZoom();
    }

    /**
     * Initialize views
     */
    private void initializeViews() {
        noticeDetailImage = findViewById(R.id.noticeDetailImage);
        noticeTitle = findViewById(R.id.noticeTitle);
        backButton = findViewById(R.id.backButton);
    }

    /**
     * Load notice data from intent
     */
    private void loadNoticeData() {
        String title = getIntent().getStringExtra("notice_title");
        String imageBase64 = getIntent().getStringExtra("notice_image");

        if (title != null) {
            noticeTitle.setText(title);
        }

        if (imageBase64 != null && !imageBase64.isEmpty()) {
            Bitmap bitmap = decodeBase64Image(imageBase64);
            if (bitmap != null) {
                noticeDetailImage.setImageBitmap(bitmap);
                // Set initial matrix
                matrix.setTranslate(0, 0);
                noticeDetailImage.setImageMatrix(matrix);
            }
        }
    }

    /**
     * Setup back button
     */
    private void setupBackButton() {
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Setup pinch-to-zoom functionality
     */
    private void setupPinchToZoom() {
        noticeDetailImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                        break;
                }
                
                view.setImageMatrix(matrix);
                return true;
            }
        });
    }

    /**
     * Calculate distance between two points
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate mid point of two points
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Decode Base64 string to Bitmap
     */
    private Bitmap decodeBase64Image(String base64String) {
        try {
            // Remove data URL prefix if present
            if (base64String.contains(",")) {
                base64String = base64String.split(",")[1];
            }
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

