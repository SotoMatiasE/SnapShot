package com.example.snapshots

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.snapshots.databinding.FragmentHomeBinding
import com.example.snapshots.databinding.ItemSnapshotBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {
    private lateinit var mBinding : FragmentHomeBinding
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<SnapShot, SnapshotHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //comienzo de configuracion del adaptador
        val query = FirebaseDatabase.getInstance().reference.child("snapshots")

        val options = FirebaseRecyclerOptions.
        Builder<SnapShot>().setQuery(query, SnapShot::class.java).build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<SnapShot, SnapshotHolder>(options){
            //rescatar el contexto
            private lateinit var mContext: Context

            //comienzo a sobre escribir los metodos
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapshotHolder {
                mContext = parent.context
                val view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_snapshot, parent, false)

                return SnapshotHolder(view)
            }

            override fun onBindViewHolder(holder: SnapshotHolder, position: Int, model: SnapShot) {
                val snapshot = getItem(position)

                with(holder){
                    setListener(snapshot)

                    binding.tvTitle.text  = snapshot.title

                    Glide.with(mContext)
                        .load(snapshot.photoURL)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.imgPhoto)
                }
            }
            //Aca le podemos poner stop al progrs bar
            override fun onDataChanged() {
                super.onDataChanged()

                mBinding.progressBar.visibility = View.GONE
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class SnapshotHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemSnapshotBinding.bind(view)

        fun setListener(snapshot: SnapShot){

        }
    }
}