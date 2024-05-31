package com.example.snapshots

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.snapshots.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityMainBinding

    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(mBinding.root)

        setupButtonNav()
    }

    private fun setupButtonNav(){
        /*Utiliza mFragmentManager para iniciar una transacción de fragmentos con beginTransaction().
        * esta función configura la navegación de botones inicializando el administrador
        * de fragmentos y añadiendo un HomeFragment a un contenedor específico en la interfaz de usuario*/
        mFragmentManager = supportFragmentManager

        //instanciar los fragmentos()
        val homeFragment = HomeFragment()
        val addFragment = AddFragment()
        val profileFragment = ProfileFragment()

        //seteo ActivFragment
        mActiveFragment = homeFragment

        //ordenamos los fragemnts para que home sea el primero en mostrar
        /*en este paso añadimos ProfileFragment y con .hint() lo ocultamos*/
        mFragmentManager.beginTransaction().add(R.id.hostFragment,
            profileFragment, ProfileFragment::class.java.name)
            .hide(profileFragment)
            .commit()

        mFragmentManager.beginTransaction().add(R.id.hostFragment,
            addFragment, AddFragment::class.java.name)
            .hide(addFragment)
            .commit()

        //como queremos que HomeFragment se vea del inicio sacamos .hide
        mFragmentManager.beginTransaction().add(R.id.hostFragment,
            homeFragment, HomeFragment::class.java.name)
            .commit()

        mBinding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_home -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment).commit()
                    mActiveFragment = homeFragment
                    true
                }
                R.id.action_add -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(addFragment).commit()
                    mActiveFragment = addFragment
                    true
                }
                R.id.action_profile -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment).commit()
                    mActiveFragment = profileFragment
                    true
                }
                else -> false
            }
        }
    }
}