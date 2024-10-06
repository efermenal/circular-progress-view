package me.endherson.circularprogressview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var mView: PercentageCircleView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mView = findViewById(R.id.my_circle_view)

        val runButton = findViewById<AppCompatButton>(R.id.run_complete)
        runButton.setOnClickListener {
            mView.startAnimation(100)
        }

        val runInsideBoundButton = findViewById<AppCompatButton>(R.id.run_inside_bound)
        runInsideBoundButton.setOnClickListener {
            mView.startAnimation(0, 50)
        }

        val initialPercentageButton = findViewById<AppCompatButton>(R.id.set_initial_percentage)
        initialPercentageButton.setOnClickListener {
            mView.initialPercentage = 75
        }

        val resumeAnimationButton = findViewById<AppCompatButton>(R.id.resume_run)
        resumeAnimationButton.setOnClickListener {
            mView.resumeAnimation(100)
        }
    }

}