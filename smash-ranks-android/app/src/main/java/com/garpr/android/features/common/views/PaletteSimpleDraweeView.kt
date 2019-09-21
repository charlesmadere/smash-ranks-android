package com.garpr.android.features.common.views

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.garpr.android.extensions.activity
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.ThreadUtils
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.ref.WeakReference

class PaletteSimpleDraweeView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimpleDraweeView(context, attrs), Heartbeat, KoinComponent {

    var colorListener: ColorListener? = null
    protected val deviceUtils: DeviceUtils by inject()
    protected val threadUtils: ThreadUtils by inject()
    private val selfReference by lazy { WeakReference<View>(this) }

    private val bitmapSubscriber = object : BaseBitmapDataSubscriber() {
        override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
            post { notifyListener(null) }
        }

        override fun onNewResultImpl(bitmap: Bitmap?) {
            val palette: Palette? = if (bitmap == null || bitmap.isRecycled) {
                null
            } else {
                Palette.from(bitmap).generate()
            }

            post { notifyListener(palette) }
        }
    }

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    private fun notifyListener(palette: Palette?) {
        if (isAlive) {
            (colorListener ?: activity as? ColorListener?)?.onPaletteBuilt(palette)
        }
    }

    override fun setImageURI(uriString: String?) {
        val uri = if (uriString.isNullOrBlank() || deviceUtils.hasLowRam) {
            null
        } else {
            Uri.parse(uriString)
        }

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
                .subscribe(bitmapSubscriber, threadUtils.background)

        controller = draweeController
    }

}
