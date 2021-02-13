package kz.kolesateam.confapp.hello.presentation

import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.AbstractTextWatcher
import kz.kolesateam.confapp.di.SHARED_PREFS_DATA_SOURCE
import kz.kolesateam.confapp.common.domain.UserNameDataSource
import kz.kolesateam.confapp.events.presentation.UpcomingEventsRouter
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class HelloActivity : AppCompatActivity() {

    private val userNameSharedPrefsDataSource: UserNameDataSource by inject(named(SHARED_PREFS_DATA_SOURCE))
    private val upcomingEventsRouter: UpcomingEventsRouter by inject()

    private lateinit var editText: EditText
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        checkUsernameAndNavigate()
        initViews()
    }

    private fun checkUsernameAndNavigate() {
        if (userNameSharedPrefsDataSource.isUserNameExists()) {
            startActivity(upcomingEventsRouter.createIntent(this))
            finish()
        }
    }

    private fun initViews() {
        editText = findViewById(R.id.activity_hello_edit_text)
        continueButton = findViewById(R.id.activity_hello_button)

        editText.addTextChangedListener(object : AbstractTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                continueButton.isEnabled = s.toString().isNotBlank()
            }
        })

        continueButton.setOnClickListener {
            saveName(editText.text.toString())
            startActivity(upcomingEventsRouter.createIntent(this))
        }
    }

    private fun saveName(name: String) = userNameSharedPrefsDataSource.saveUserName(name)
}