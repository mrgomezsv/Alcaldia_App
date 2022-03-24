package com.chinameca.app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.chinameca.app.databinding.ActivityHomeBinding
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {


    //Variables Privadas de Update
    private var appUpdate : AppUpdateManager? = null

    private val REQUEST_CODE = 100

    private lateinit var binding: ActivityHomeBinding

    private lateinit var firebaseAuth: FirebaseAuth

    /////////////////SECCION 1 PARA NOMBRE EN BARRA/////////////////
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val response = IdpResponse.fromResultIntent(it.data)

        if (it.resultCode == RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null){
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show()
            }
        }else{
            if (response == null){
                Toast.makeText(this, "Hasta Pronto", Toast.LENGTH_LONG).show()
                finish()
            }else{
                response.error?.let {
                    if (it.errorCode == ErrorCodes.NO_NETWORK){
                        Toast.makeText(this, "Sin Red", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Codigo de Error: ${it.errorCode}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    /////////////////SECCION 1 PARA NOMBRE EN BARRA/////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        LogOut() //Cerrar Sesion
        unidad1() // Unidad de Genero

        configAuthName()// Para el Nombre en la barras
        link2()

        appUpdate = AppUpdateManagerFactory.create(this)

    }

    private fun configAuthName(){
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            supportActionBar?.title = auth.currentUser?.displayName
            if (auth.currentUser != null){
                supportActionBar?.title = auth.currentUser?.displayName
            }
        }
    }

    /////////////////SECCION 2 PARA NOMBRE EN BARRA/////////////////
    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    /////////////////SECCION 2 PARA NOMBRE EN BARRA/////////////////

    private fun checkUser(){

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            val email = firebaseUser.email
            val nameUser = firebaseUser.displayName
            binding.nameTv.text = nameUser
            binding.emailTv.text = email
        }
    }

    private fun LogOut(){
        binding.logOutB.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Sesion Cerrada con Exito", Toast.LENGTH_LONG).show()
            checkUser()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun unidad1(){
        val btngenero : ImageView = findViewById(R.id.btnUnidadGenero)
        btngenero.setOnClickListener {
            val intent = Intent(this, UnidadGeneroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun link2() {// Yo
        val txtUrl2: TextView = findViewById(R.id.powerTxt2)
        txtUrl2.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.webforallsv.com/portfolio/buffet-proevent-app/")
            )
            startActivity(intent)
        }
    }
}
