package victor.veiga.honeysmsapp.infra

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import victor.veiga.honeysmsapp.service.listener.MyButtonClickListenerLeft

class MyButtonLeft(private val context: Context,
               private val text: String,
               private val textSize: Int,
               private val imageRes: Int,
               private val color: Int,
               private val listener: MyButtonClickListenerLeft
) {

    private var pos: Int = 0
    private var clickRegion : RectF?=null
    private val resources: Resources

    init {
        resources = context.resources
    }

    fun onClickLeft (x: Float, y: Float): Boolean {
        if (clickRegion != null && clickRegion!!.contains(y,x)) {
            listener.onClick(pos)
            return true
        }
        return false
    }

    fun onDraw(c: Canvas, recf: RectF, pos:Int) {
        val p = Paint()
        p.color = color
        c.drawRect(recf,p)

        //text
        p.color = Color.WHITE
        p.textSize = textSize.toFloat()

        val r = Rect()
        val cHeight = recf.height()
        val cWidth = recf.width()

        p.textAlign = Paint.Align.LEFT
        p.getTextBounds(text,0,text.length, r)

        var x = 0f
        var y = 0f

        if (imageRes==0) {
            x = cWidth / 2f - r.width() / 2f - r.left.toFloat()
            y = cHeight / 2f - r.height() / 2f - r.bottom.toFloat()
            c.drawText(text, recf.left+x, recf.top+y,p)
        } else {
            val d = ContextCompat.getDrawable(context, imageRes)
            val bitmap = drawableToBitmap(d)
            c.drawBitmap(bitmap,(recf.left+recf.right)/2,(recf.top/recf.bottom)/2,p)
        }

        clickRegion = recf
        this.pos = pos

    }

    private fun drawableToBitmap(d: Drawable?): Bitmap {
        if(d is BitmapDrawable) return  d.bitmap

        val bitmap = Bitmap.createBitmap(d!!.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        d.setBounds(0,0,canvas.width,canvas.height)
        d.draw(canvas)
        return bitmap
    }

}