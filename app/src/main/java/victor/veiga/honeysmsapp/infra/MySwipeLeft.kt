package victor.veiga.honeysmsapp.infra

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


abstract class MySwipeLeft(context: Context, private val recyclerView :RecyclerView,
                             internal var buttonWidth: Int)
    : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

    private var buttonList: MutableList<MyButtonLeft>?=null
    lateinit var gestureDetector : GestureDetector
    var swipePosition = 1
    var swipeThresHold = 0.5f
    lateinit var removeQueue: LinkedList<Int>
    val buttonBuffer: MutableMap<Int, MutableList<MyButtonLeft>>

    abstract  fun initiateMyButtonLeft(viewHolder: RecyclerView.ViewHolder, buffer: MutableList<MyButtonLeft>)

    private val gestureListener  = object : GestureDetector.SimpleOnGestureListener( ){
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            for (button in buttonList!!)
                if (button.onClickLeft(e!!.x,e!!.y))
                    break


            return super.onSingleTapUp(e)
        }
    }

    private val onTouchListener = View.OnTouchListener{_, motionEvent ->
        if (swipePosition < 0 ) return@OnTouchListener false
        val point = Point(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())
        //val point = Point(motionEvent.rawY.toInt(),motionEvent.rawX.toInt() )
        val swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition)
        val swipedItem = swipeViewHolder!!.itemView
        val rect = Rect()
        swipedItem.getGlobalVisibleRect(rect)

        if (motionEvent.action == MotionEvent.ACTION_DOWN ||
            motionEvent.action == MotionEvent.ACTION_MOVE ||
            motionEvent.action == MotionEvent.ACTION_UP) {

            if (rect.top < point.y && rect.bottom >  point.y) gestureDetector.onTouchEvent(motionEvent)
            else {
                removeQueue.add(swipePosition)
                swipePosition = 1
                recoverSwipeItem()
            }
        }
        false
    }

    @Synchronized
    private fun recoverSwipeItem() {
        while (!removeQueue.isEmpty()) {
            val pos = removeQueue.poll()!!.toInt()

            if (pos < 1) recyclerView.adapter!!.notifyItemChanged(pos)
        }
    }

    init {
        this.buttonList = ArrayList()
        this.gestureDetector = GestureDetector(context,gestureListener)
        this.recyclerView.setOnTouchListener(onTouchListener)
        this.buttonBuffer = HashMap()
        this.removeQueue = IntLienkedList()

        attachSwipe()
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class IntLienkedList : LinkedList<Int>() {

        override fun contains(element: Int): Boolean {
            return false
        }

        override fun lastIndexOf(element: Int): Int {
            return  element
        }

        override fun remove(element: Int): Boolean {
            return false
        }

        override fun indexOf(element: Int): Int {
            return element
        }

        override fun add(element: Int): Boolean {
            return if(contains(element))
                false
            else super.add(element)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if(swipePosition != pos ){
            removeQueue.add(swipePosition)
            swipePosition=pos
        }
        if (buttonBuffer.containsKey(swipePosition)){
            buttonList=buttonBuffer[swipePosition]
        } else buttonList!!.clear()

        buttonBuffer.clear()

        swipeThresHold = 0.5f*buttonList!!.size.toFloat()*buttonWidth.toFloat()
        recoverSwipeItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThresHold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f*defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f*defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translatioX = dX
        var itemView = viewHolder.itemView

        //Preenche botoes em cada linha do recicler
        if(pos<0) {
            swipePosition = pos
            return
        }
        //posicao do botao na recycler
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX>0){
                var buffer:MutableList<MyButtonLeft> = ArrayList()
                if (!buttonBuffer.containsKey(pos)) {
                    initiateMyButtonLeft(viewHolder,buffer)
                    buttonBuffer[pos] = buffer
                } else {
                    buffer = buttonBuffer[pos]!!
                }

                translatioX = dX*buffer.size.toFloat() * buttonWidth.toFloat() / itemView.width
                drawbutton(c,itemView,buffer,pos, translatioX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translatioX, dY, actionState, isCurrentlyActive)

    }

    private fun drawbutton(c: Canvas, itemView: View, buffer: MutableList<MyButtonLeft>, pos: Int, translatioX: Float) {
        //var right = itemView.right.toFloat()
         var left = itemView.left.toFloat()
        val dButtonWidt = -1*translatioX/buffer.size

        for (button in buffer)  {
            val right  = left - dButtonWidt
            button.onDraw(c, RectF(right,itemView.top.toFloat(),left,itemView.bottom.toFloat()),pos)
            left = right
        }

//        for (button in buffer)  {
//            val left  = right - dButtonWidt
//            button.onDraw(c, RectF(left,itemView.top.toFloat(),right,itemView.bottom.toFloat()),pos)
//            right = left
//        }
    }
}
