package cn.dbyl.carclient.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import cn.dbyl.carclient.R
import org.opencv.android.*
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.util.*


class OpenCVActivity : CameraActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private val TAG = "OCVSample::Activity"

    companion object {
        val VIEW_MODE_RGBA = 0
        val VIEW_MODE_HIST = 1
        val VIEW_MODE_CANNY = 2
        val VIEW_MODE_SEPIA = 3
        val VIEW_MODE_SOBEL = 4
        val VIEW_MODE_ZOOM = 5
        val VIEW_MODE_PIXELIZE = 6
        val VIEW_MODE_POSTERIZE = 7
    }

    private var mItemPreviewRGBA: MenuItem? = null
    private var mItemPreviewHist: MenuItem? = null
    private var mItemPreviewCanny: MenuItem? = null
    private var mItemPreviewSepia: MenuItem? = null
    private var mItemPreviewSobel: MenuItem? = null
    private var mItemPreviewZoom: MenuItem? = null
    private var mItemPreviewPixelize: MenuItem? = null
    private var mItemPreviewPosterize: MenuItem? = null
    private var mOpenCvCameraView: CameraBridgeViewBase? = null

    private var mSize0: Size? = null

    private var mIntermediateMat: Mat? = null
    private var mMat0: Mat? = null
    private lateinit var mChannels: Array<MatOfInt>
    private var mHistSize: MatOfInt? = null
    private val mHistSizeNum = 25
    private var mRanges: MatOfFloat? = null
    private lateinit var mColorsRGB: Array<Scalar>
    private lateinit var mColorsHue: Array<Scalar>
    private var mWhilte: Scalar? = null
    private lateinit var mP1: Point
    private lateinit var mP2: Point
    private lateinit var mBuff: FloatArray
    private var mSepiaKernel: Mat? = null

    var viewMode = VIEW_MODE_RGBA

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")
                    mOpenCvCameraView!!.enableView()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    init {
        Log.i(TAG, "Instantiated new " + this.javaClass)
    }

    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "called onCreate")
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_open_cv)
        mOpenCvCameraView =
            findViewById<View>(R.id.image_manipulations_activity_surface_view) as CameraBridgeViewBase
        mOpenCvCameraView!!.visibility = CameraBridgeViewBase.VISIBLE
        mOpenCvCameraView!!.setCvCameraViewListener(this)
    }

    override fun onPause() {
        super.onPause()
        if (mOpenCvCameraView != null) mOpenCvCameraView!!.disableView()
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun getCameraViewList(): List<CameraBridgeViewBase?>? {
        return Collections.singletonList(mOpenCvCameraView)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mOpenCvCameraView != null) mOpenCvCameraView!!.disableView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.i(TAG, "called onCreateOptionsMenu")
        mItemPreviewRGBA = menu.add("Preview RGBA")
        mItemPreviewHist = menu.add("Histograms")
        mItemPreviewCanny = menu.add("Canny")
        mItemPreviewSepia = menu.add("Sepia")
        mItemPreviewSobel = menu.add("Sobel")
        mItemPreviewZoom = menu.add("Zoom")
        mItemPreviewPixelize = menu.add("Pixelize")
        mItemPreviewPosterize = menu.add("Posterize")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG, "called onOptionsItemSelected; selected item: $item")
        if (item === mItemPreviewRGBA) viewMode = VIEW_MODE_RGBA
        if (item === mItemPreviewHist) viewMode =
            VIEW_MODE_HIST else if (item === mItemPreviewCanny) viewMode =
            VIEW_MODE_CANNY else if (item === mItemPreviewSepia) viewMode =
            VIEW_MODE_SEPIA else if (item === mItemPreviewSobel) viewMode =
            VIEW_MODE_SOBEL else if (item === mItemPreviewZoom) viewMode =
            VIEW_MODE_ZOOM else if (item === mItemPreviewPixelize) viewMode =
            VIEW_MODE_PIXELIZE else if (item === mItemPreviewPosterize) viewMode =
            VIEW_MODE_POSTERIZE
        return true
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mIntermediateMat = Mat()
        mSize0 = Size()
        mChannels = arrayOf(MatOfInt(0), MatOfInt(1), MatOfInt(2))
        mBuff = FloatArray(mHistSizeNum)
        mHistSize = MatOfInt(mHistSizeNum)
        mRanges = MatOfFloat(0f, 256f)
        mMat0 = Mat()
        mColorsRGB =
            arrayOf(
                Scalar(200.0, 0.0, 0.0, 255.0),
                Scalar(0.0, 200.0, 0.0, 255.0),
                Scalar(0.0, 0.0, 200.0, 255.0)
            )
        mColorsHue = arrayOf(
            Scalar(255.0, 0.0, 0.0, 255.0),
            Scalar(255.0, 60.0, 0.0, 255.0),
            Scalar(255.0, 120.0, 0.0, 255.0),
            Scalar(255.0, 180.0, 0.0, 255.0),
            Scalar(255.0, 240.0, 0.0, 255.0),
            Scalar(215.0, 213.0, 0.0, 255.0),
            Scalar(150.0, 255.0, 0.0, 255.0),
            Scalar(85.0, 255.0, 0.0, 255.0),
            Scalar(20.0, 255.0, 0.0, 255.0),
            Scalar(0.0, 255.0, 30.0, 255.0),
            Scalar(0.0, 255.0, 85.0, 255.0),
            Scalar(0.0, 255.0, 150.0, 255.0),
            Scalar(0.0, 255.0, 215.0, 255.0),
            Scalar(0.0, 234.0, 255.0, 255.0),
            Scalar(0.0, 170.0, 255.0, 255.0),
            Scalar(0.0, 120.0, 255.0, 255.0),
            Scalar(0.0, 60.0, 255.0, 255.0),
            Scalar(0.0, 0.0, 255.0, 255.0),
            Scalar(64.0, 0.0, 255.0, 255.0),
            Scalar(120.0, 0.0, 255.0, 255.0),
            Scalar(180.0, 0.0, 255.0, 255.0),
            Scalar(255.0, 0.0, 255.0, 255.0),
            Scalar(255.0, 0.0, 215.0, 255.0),
            Scalar(255.0, 0.0, 85.0, 255.0),
            Scalar(255.0, 0.0, 0.0, 255.0)
        )
        mWhilte = Scalar.all(255.0)
        mP1 = Point()
        mP2 = Point()
        // Fill sepia kernel
        mSepiaKernel = Mat(4, 4, CvType.CV_32F)
        mSepiaKernel!!.put(0, 0, 0.189, 0.769, 0.393, 0.0)
        mSepiaKernel!!.put(1, 0, 0.168, 0.686, 0.349, 0.0)
        mSepiaKernel!!.put(2, 0, 0.131, 0.534, 0.272, 0.0)
        mSepiaKernel!!.put(3, 0, 0.000, 0.000, 0.000, 1.0)
    }

    override fun onCameraViewStopped() { // Explicitly deallocate Mats
        if (mIntermediateMat != null) mIntermediateMat!!.release()
        mIntermediateMat = null
    }

    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat? {
        val rgba = inputFrame.rgba()
        val sizeRgba: Size = rgba.size()
        val rgbaInnerWindow: Mat
        val rows = sizeRgba.height.toInt()
        val cols = sizeRgba.width.toInt()
        val left = cols / 8
        val top: Int = rows / 8
        val width = cols * 3 / 4
        val height = rows * 3 / 4
        when (viewMode) {
            VIEW_MODE_RGBA -> {
            }
            VIEW_MODE_HIST -> {
                val hist = Mat()
                var thikness = (sizeRgba.width / (mHistSizeNum + 10) / 5).toInt()
                if (thikness > 5) thikness = 5
                val offset =
                    ((sizeRgba.width - (5 * mHistSizeNum + 4 * 10) * thikness) / 2)
                // RGB
                var c = 0
                while (c < 3) {
                    Imgproc.calcHist(
                        listOf(rgba),
                        mChannels[c],
                        mMat0,
                        hist,
                        mHistSize,
                        mRanges
                    )
                    Core.normalize(hist, hist, sizeRgba.height / 2, 0.0, Core.NORM_INF)
                    hist[0, 0, mBuff]
                    var h = 0
                    while (h < mHistSizeNum) {
                        mP2.x = (offset + (c * (mHistSizeNum + 10) + h) * thikness)
                        mP1.x = mP2.x
                        mP1.y = sizeRgba.height - 1
                        mP2.y = mP1.y - 2 - mBuff[h].toInt()
                        Imgproc.line(rgba, mP1, mP2, mColorsRGB[c], thikness)
                        h++
                    }
                    c++
                }
                // Value and Hue
                Imgproc.cvtColor(rgba, mIntermediateMat, Imgproc.COLOR_RGB2HSV_FULL)
                // Value
                Imgproc.calcHist(
                    listOf(mIntermediateMat),
                    mChannels[2],
                    mMat0,
                    hist,
                    mHistSize,
                    mRanges
                )
                Core.normalize(hist, hist, sizeRgba.height / 2, 0.0, Core.NORM_INF)
                hist[0, 0, mBuff]
                run {
                    var h = 0
                    while (h < mHistSizeNum) {
                        mP2.x = (offset + (3 * (mHistSizeNum + 10) + h) * thikness).toDouble()
                        mP1.x = mP2.x
                        mP1.y = sizeRgba.height - 1
                        mP2.y = mP1.y - 2 - mBuff[h].toInt()
                        Imgproc.line(rgba, mP1, mP2, mWhilte, thikness)
                        h++
                    }
                }
                // Hue
                Imgproc.calcHist(
                    listOf(mIntermediateMat),
                    mChannels[0],
                    mMat0,
                    hist,
                    mHistSize,
                    mRanges
                )
                Core.normalize(hist, hist, sizeRgba.height / 2, 0.0, Core.NORM_INF)
                hist[0, 0, mBuff]
                var h = 0
                while (h < mHistSizeNum) {
                    mP2.x = (offset + (4 * (mHistSizeNum + 10) + h) * thikness).toDouble()
                    mP1.x = mP2.x
                    mP1.y = sizeRgba.height - 1
                    mP2.y = mP1.y - 2 - mBuff[h].toInt()
                    Imgproc.line(rgba, mP1, mP2, mColorsHue[h], thikness)
                    h++
                }
            }
            VIEW_MODE_CANNY -> {
                rgbaInnerWindow = rgba.submat(top, top + height, left, left + width)
                Imgproc.Canny(rgbaInnerWindow, mIntermediateMat, 80.0, 90.0)
                Imgproc.cvtColor(mIntermediateMat, rgbaInnerWindow, Imgproc.COLOR_GRAY2BGRA, 4)
                rgbaInnerWindow.release()
            }
            VIEW_MODE_SOBEL -> {
                val gray = inputFrame.gray()
                val grayInnerWindow = gray.submat(top, top + height, left, left + width)
                rgbaInnerWindow = rgba.submat(top, top + height, left, left + width)
                Imgproc.Sobel(grayInnerWindow, mIntermediateMat, CvType.CV_8U, 1, 1)
                Core.convertScaleAbs(mIntermediateMat, mIntermediateMat, 10.0, 0.0)
                Imgproc.cvtColor(mIntermediateMat, rgbaInnerWindow, Imgproc.COLOR_GRAY2BGRA, 4)
                grayInnerWindow.release()
                rgbaInnerWindow.release()
            }
            VIEW_MODE_SEPIA -> {
                rgbaInnerWindow = rgba.submat(top, top + height, left, left + width)
                Core.transform(rgbaInnerWindow, rgbaInnerWindow, mSepiaKernel)
                rgbaInnerWindow.release()
            }
            VIEW_MODE_ZOOM -> {
                val zoomCorner = rgba.submat(0, rows / 2 - rows / 10, 0, cols / 2 - cols / 10)
                val mZoomWindow = rgba.submat(
                    rows / 2 - 9 * rows / 100,
                    rows / 2 + 9 * rows / 100,
                    cols / 2 - 9 * cols / 100,
                    cols / 2 + 9 * cols / 100
                )
                Imgproc.resize(
                    mZoomWindow,
                    zoomCorner,
                    zoomCorner.size(),
                    0.0,
                    0.0,
                    Imgproc.INTER_LINEAR_EXACT
                )
                val wsize: Size = mZoomWindow.size()
                Imgproc.rectangle(
                    mZoomWindow,
                    Point(1.0, 1.0),
                    Point(wsize.width - 2, wsize.height - 2),
                    Scalar(255.0, 0.0, 0.0, 255.0),
                    2
                )
                zoomCorner.release()
                mZoomWindow.release()
            }
            VIEW_MODE_PIXELIZE -> {
                rgbaInnerWindow = rgba.submat(top, top + height, left, left + width)
                Imgproc.resize(
                    rgbaInnerWindow,
                    mIntermediateMat,
                    mSize0,
                    0.1,
                    0.1,
                    Imgproc.INTER_NEAREST
                )
                Imgproc.resize(
                    mIntermediateMat,
                    rgbaInnerWindow,
                    rgbaInnerWindow.size(),
                    0.0,
                    0.0,
                    Imgproc.INTER_NEAREST
                )
                rgbaInnerWindow.release()
            }
            VIEW_MODE_POSTERIZE -> {
                /*
            Imgproc.cvtColor(rgbaInnerWindow, mIntermediateMat, Imgproc.COLOR_RGBA2RGB);
            Imgproc.pyrMeanShiftFiltering(mIntermediateMat, mIntermediateMat, 5, 50);
            Imgproc.cvtColor(mIntermediateMat, rgbaInnerWindow, Imgproc.COLOR_RGB2RGBA);
            */rgbaInnerWindow = rgba.submat(top, top + height, left, left + width)
                Imgproc.Canny(rgbaInnerWindow, mIntermediateMat, 80.0, 90.0)
                rgbaInnerWindow.setTo(Scalar(0.0, 0.0, 0.0, 255.0), mIntermediateMat)
                Core.convertScaleAbs(rgbaInnerWindow, mIntermediateMat, 1.0 / 16, 0.0)
                Core.convertScaleAbs(mIntermediateMat, rgbaInnerWindow, 16.0, 0.0)
                rgbaInnerWindow.release()
            }
        }
        return rgba
    }
}
