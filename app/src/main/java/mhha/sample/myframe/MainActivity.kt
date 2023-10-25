package mhha.sample.myframe

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import mhha.sample.myframe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter
    private var imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            updateImages(it)
        } //registerForActivityResult(ActivityResultContracts.GetMultipleContents())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //tooㅣbar
        binding.toolbar.apply {
            title = "사진 가져오기"
            setSupportActionBar(this)
        }

        initRecyclerView()

        binding.loadImageButton.setOnClickListener {

            checkPermission()

        } //binding.loadImageButton.setOnClickListener

        binding.navigateFrameActivityButton.setOnClickListener{
            navigateToFrameActivity()
        }//binding.navigateFrameActivityButton.setOnClickListener
    } //override fun onCreate(savedInstanceState: Bundle?)

    //메뉴 만들기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    // 메뉴 버튼 기능
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_add ->{
                checkPermission()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }//when(item.itemId)

    }//override fun onOptionsItemSelected(item: MenuItem): Boolean

    private fun navigateToFrameActivity(){
        val images = imageAdapter.currentList.filterIsInstance<ImageItems.Image>().map{ it.uri.toString()}.toTypedArray()
        val intent = Intent(this, FrameActivity::class.java).putExtra("images",images)
        startActivity(intent)

    }//private fun navigateToFrameActivity()


    private fun initRecyclerView() {
        imageAdapter = ImageAdapter(object : ImageAdapter.ItemClickListener {
            override fun onLoadMoreClick() {
                checkPermission()
            } //override fun onLoadMoreClick()
        }) //ImageAdapter(object : ImageAdapter.ItemClickListener

        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2)
        } //binding.imageRecyclerView.apply

    } //private fun initRecyclerView()


    /**
     * Check permission
     * 권한 확인
     */
    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }

            shouldShowRequestPermissionRationale(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionInfoDialog()
            }

            else -> {
                requestReadExternalStorage()
            }
        } //when
    } //private fun checkPermission()

    /**
     * Show permission info dialog
     * 권한 이유 설명 하기
     */
    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 귀해 저장소 읽기 권한이 필요")
            setNegativeButton("취소", null)
            setPositiveButton("동의") { _, _ ->
                requestReadExternalStorage()
            }
        }.show() //AlertDialog.Builder(this).apply
    } //private fun showPermissionInfoDialog()


    /**
     * Request read external storage
     * 권한 요청 하기
     */
    private fun requestReadExternalStorage() {
        //권한 요청
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE
        )
    } //private fun requestReadExternalStorage()

    /**
     * Load image
     * 이미지 로드하기
     */
    private fun loadImage() {
        // image 파일로된 모든 것을 가여온다.
        imageLoadLauncher.launch("image/*")
        Toast.makeText(this, "이미지 로드 버튼", Toast.LENGTH_SHORT).show()

    } //private fun loadImage()

    /**
     * Update images
     * 이미지 가져오기
     * @param uriList
     */
    private fun updateImages(uriList: List<Uri>) {
        Log.i("updateImages", " ${uriList} ")
        val images = uriList.map { ImageItems.Image(it) }
        //원래 이미지 선택에서 추가하기
        val updateImages = imageAdapter.currentList.toMutableList().apply { addAll(images) }
        imageAdapter.submitList(updateImages)

    } //private fun updateImages(uriList : List<Uri>)

    /**
     * On request permissions result
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_GRANTED
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }//REQUEST_READ_EXTERNAL_STORAGE ->
        }//when(requestCode)
    } //override fun onRequestPermissionsResult

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    } //companion object

} //class MainActivity : AppCompatActivity()