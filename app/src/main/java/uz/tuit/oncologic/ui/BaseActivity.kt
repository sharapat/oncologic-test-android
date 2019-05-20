package uz.tuit.oncologic.ui

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    fun toast(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    fun toast(messageResId: Int?) {
        messageResId?.let {
            Toast.makeText(this, getString(messageResId), Toast.LENGTH_LONG).show()
        }
    }
}