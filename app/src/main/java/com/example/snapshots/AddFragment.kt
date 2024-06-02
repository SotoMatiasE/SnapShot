package com.example.snapshots

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.snapshots.databinding.FragmentAddBinding

class AddFragment : Fragment() {
    private lateinit var mBinding: FragmentAddBinding

    private val RC_GALERY = 6

    private var mPhotoSelectedUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflar fragemnt_add
        mBinding = FragmentAddBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return mBinding.root
    }
    //configuracion de los botones
    /*Se debe crear una vez inflada la vista usando*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnPost.setOnClickListener {
            //metodo
            postSnapshot()

            mBinding.btnSelect.setOnClickListener {
                openGalery()
            }
        }
    }

    private fun openGalery() {//ABRE LA GALERIA
        //lanzamiento del intent y visualizar img en el imgView
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_GALERY)
    }

    private fun postSnapshot() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Este metodo recibe la respuesta de openGalery
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == RC_GALERY){
                //verificamos que salio bien
                mPhotoSelectedUri = data?.data
                mBinding.imgPhoto.setImageURI(mPhotoSelectedUri)//Asignamos la imagen al imgView
                mBinding.tilTitle.visibility = View.VISIBLE
                mBinding.tvMessage.text = getString(R.string.post_message_valid_title)
            }
        }
    }
}











