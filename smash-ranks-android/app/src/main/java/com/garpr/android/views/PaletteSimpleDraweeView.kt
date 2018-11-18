package com.garpr.android.views

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import androidx.palette.graphics.Palette
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.ThreadUtils
import java.lang.ref.WeakReference
import javax.inject.Inject

class PaletteSimpleDraweeView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimpleDraweeView(context, attrs) {

    var colorListener: ColorListener? = null
    private val selfReference: WeakReference<View> by lazy { WeakReference<View>(this) }

    @Inject
    protected lateinit var deviceUtils: DeviceUtils

    @Inject
    protected lateinit var threadUtils: ThreadUtils


    private val bitmapSubscriber = object : BaseBitmapDataSubscriber() {
        override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
            threadUtils.runOnUi(Runnable {
                notifyListener(null)
            })
        }

        override fun onNewResultImpl(bitmap: Bitmap?) {
            val palette: Palette? = if (bitmap == null || bitmap.isRecycled) {
                null
            } else {
                Palette.from(bitmap).generate()
            }

            threadUtils.runOnUi(Runnable {
                notifyListener(palette)
            })
        }
    }

    private fun notifyListener(palette: Palette?) {
        colorListener?.onPaletteBuilt(palette) ?: (activity as? ColorListener)?.onPaletteBuilt(palette)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    override fun setImageURI(uriString: String?) {
        val uri = if (uriString?.isNotBlank() == true && !deviceUtils.hasLowRam &&
                activity is ColorListener) Uri.parse(uriString) else null

        if (uri == null) {
            super.setImageURI(uriString)
            notifyListener(null)
            return
        }

        val imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .build()

        val draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(controller)
                .build()

        Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, selfReference)
                .subscribe(bitmapSubscriber, threadUtils.executorService)

        controller = draweeController
    }

}
