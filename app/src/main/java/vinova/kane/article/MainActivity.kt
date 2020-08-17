package vinova.kane.article.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import vinova.kane.article.R
import vinova.kane.article.ui.IOnBackPressed

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.overviewFragment)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}