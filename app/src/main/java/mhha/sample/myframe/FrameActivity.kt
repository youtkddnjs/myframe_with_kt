package mhha.sample.myframe

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import mhha.sample.myframe.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val images = (intent.getStringArrayExtra("images") ?: emptyArray())
            .map{ uriString -> FrameItem(Uri.parse(uriString)) }

            val frameAdapter = FrameAdapter(images)

            binding.viewPager.adapter = frameAdapter

        TabLayoutMediator( binding.tabLayout, binding.viewPager){ tab, position ->
            binding.viewPager.currentItem = tab.position
        }.attach()//TabLayoutMediator( binding.tabLayout, binding.viewPager)

        //toobar
        binding.toolbar.apply {
            title = "사진 가져오기"
            setSupportActionBar(this)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }//onCreate

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> {super.onOptionsItemSelected(item)}
        }//return when(item.itemId)
    } //override fun onOptionsItemSelected(item: MenuItem): Boolean

}//class FrameActivity : AppCompatActivity()