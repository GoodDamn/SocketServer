package good.damn.filesharing.models

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Messenger {

    private val mHandler = Handler(Looper.getMainLooper())
    private var mTextView: TextView? = null
    private var mIndex = 0
    private var messages = ""

    fun addMessage(
        text: String
    ) {
        messages += "${mIndex++}) $text\n"
        mTextView?.let {
            mHandler.post {
                it.text = messages
            }
        }
    }

    fun setTextView(
        textView: TextView?
    ) {
        mTextView = textView
    }

    fun getMessages(): String {
        return messages
    }

}