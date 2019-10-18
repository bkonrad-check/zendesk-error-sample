package de.check24.zendeskcrashdemo

import android.content.ComponentName
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.zendesk.logger.Logger

import kotlinx.android.synthetic.main.activity_main.*
import zendesk.core.AnonymousIdentity
import zendesk.core.Zendesk
import zendesk.support.Support
import zendesk.support.guide.HelpCenterActivity
import zendesk.support.guide.ViewArticleActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            if (!Zendesk.INSTANCE.isInitialized) {
                Zendesk.INSTANCE.init(
                    this,
                    "data",
                    "data",
                    "data"
                )
            }

            // enables logging for zendesk if build mode is debug
            Logger.setLoggable(BuildConfig.DEBUG)

            if (Zendesk.INSTANCE.identity != AnonymousIdentity()) {
                Zendesk.INSTANCE.setIdentity(AnonymousIdentity())
            }
            if (!Support.INSTANCE.isInitialized) {
                Support.INSTANCE.init(Zendesk.INSTANCE)
            }
            val config = ViewArticleActivity.builder()
                .withContactUsButtonVisible(false)
                .config()
            val helpCenterIntent = HelpCenterActivity.builder()
                .withContactUsButtonVisible(false)
                .withShowConversationsMenuButton(false)
                .intent(this, config)
            // override this value to avoid hardcoded component
            helpCenterIntent.component =
                ComponentName(this, HelpCenterActivity::class.java)
            startActivity(helpCenterIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
