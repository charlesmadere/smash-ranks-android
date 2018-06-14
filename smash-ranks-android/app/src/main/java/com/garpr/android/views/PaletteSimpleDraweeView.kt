package com.garpr.android.views

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.graphics.Palette
import android.util.AttributeSet
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
import javax.inject.Inject

class PaletteSimpleDraweeView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimpleDraweeView(context, attrs) {

    @Inject
    protected lateinit var deviceUtils: DeviceUtils

    @Inject
    protected lateinit var threadUtils: ThreadUtils


    private val bitmapSubscriber = object : BaseBitmapDataSubscriber() {
        override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
            threadUtils.runOnUi(Runnable {
                (context.activity as? ColorListener)?.onPaletteBuilt(null)
            })
        }

        override fun onNewResultImpl(bitmap: Bitmap?) {
            val palette: Palette? = if (bitmap == null || bitmap.isRecycled) {
                null
            } else {
                Palette.from(bitmap).generate()
            }

            threadUtils.runOnUi(Runnable {
                (context.activity as? ColorListener)?.onPaletteBuilt(palette)
            })
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    override fun setImageURI(uriString: String?) {
        val uri = if (uriString.isNullOrBlank() && !deviceUtils.hasLowRam &&
                context.activity is ColorListener) null else Uri.parse(uriString)

        if (uri == null) {
            super.setImageURI(uriString)
            return
        }

        val imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .build()

        val draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(controller)
                .build()

        Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, context)
                .subscribe(bitmapSubscriber, threadUtils.executorService)

        controller = draweeController
    }

}
