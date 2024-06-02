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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddFragment : Fragment() {
    private lateinit var mBinding: FragmentAddBinding

    private val RC_GALERY = 6

    private var mPhotoSelectedUri: Uri? = null

    private val PATH_SNAPSHOT = "snapshots" //para crear una carpeta en el servido y administrar las imagenes

    private lateinit var mStoreageReference: StorageReference

    private lateinit var mDatabaseReference: DatabaseReference//extrar url

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
        }
        mBinding.btnSelect.setOnClickListener {
            openGalery()
        }

        mStoreageReference = FirebaseStorage.getInstance().reference
        //inicialisamos db
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child(PATH_SNAPSHOT)
    }

    private fun openGalery() {//ABRE LA GALERIA
        //lanzamiento del intent y visualizar img en el imgView
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_GALERY)
    }

    private fun postSnapshot() {
        //subir imagen a storage
        mBinding.progressBar.visibility = View.VISIBLE

        val  key = mDatabaseReference.push().key!!
        val storageReference = mStoreageReference.child(PATH_SNAPSHOT).child("my_photo")
        if (mPhotoSelectedUri != null){
            storageReference.putFile(mPhotoSelectedUri!!)
            .addOnProgressListener {
                //pintamos el progres barr que se pinte mientra sube la img
                val progress = (100 * it.bytesTransferred/it.totalByteCount).toDouble()//calcula el porsentaje de byts transferidos
                mBinding.progressBar.progress = progress.toInt()
                mBinding.tvMessage.text = "$progress%"
            }
            .addOnCompleteListener{
                //que hace cuando se completa la subida
                mBinding.progressBar.visibility = View.INVISIBLE
            }
            .addOnSuccessListener {
                //salvar url
                //extraer URL
                it.storage.downloadUrl.addOnSuccessListener {
                    //solo se guarda si se extrajo la url con exito
                    //it.toString seria la URL
                    saveSnapshot(key, it.toString(), mBinding.edTitle.text.toString().trim())
                    mBinding.tilTitle.visibility = View.GONE
                    mBinding.tvMessage.text = getString(R.string.post_message_title)
                }
                Snackbar.make(mBinding.root, "Foto Publicada", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Snackbar.make(mBinding.root, "Error al subir la foto", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveSnapshot(key: String, url: String, title: String){
        //salvar nueva url dentro de elemento tipo snapshot
        val snapshot = SnapShot(title = title, photoUrl = url)
        mDatabaseReference.child(key).setValue(snapshot)
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











