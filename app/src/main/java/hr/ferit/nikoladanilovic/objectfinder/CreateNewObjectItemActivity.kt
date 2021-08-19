package hr.ferit.nikoladanilovic.objectfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityCreateNewObjectItemBinding

class CreateNewObjectItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCreateNewObjectItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewObjectItemBinding.inflate(layoutInflater)

        binding.captureImageBtn.setOnClickListener { captureImg() }
        binding.createNewObjectBtn.setOnClickListener { createNewObject() }

        setContentView(binding.root)
    }

    private fun createNewObject() {
        startActivity(Intent(this, ObjectListOnLocationActivity::class.java))
        finish()
    }

    private fun captureImg() {
        startActivity(Intent(this, TakeObjectPhotoActivity::class.java))
    }
}