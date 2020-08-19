package mobapptut.com.camera2videoimage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        mAuth = Firebase.auth
        mAuth.currentUser?.also {
            Intent(this, Camera2VideoImageActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        progressBar.visibility = View.GONE

        singUpTV.setOnClickListener {
            Intent(this, SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        loginBtn.setOnClickListener {
            val emailStr = emailET.text.toString()
            val passStr = passwordET.text.toString()
            if (emailStr.isEmpty() || passStr.isEmpty())
                showMessage("Please provide all the details")
            else
                authenticate(emailStr, passStr)
        }
    }

    private fun authenticate(emailStr: String, passStr: String) {
        progressBar.visibility = View.VISIBLE
        try {
            val authResult = mAuth.signInWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(this) { it ->
                if (it.isSuccessful) {
                    progressBar.visibility = View.GONE
                    Intent(this@LoginActivity, Camera2VideoImageActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                } else {
                    Toast.makeText(this, "Wrong Credentials!", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            progressBar.visibility = View.GONE
            showMessage("${e.message}")
        } catch (e: Exception) {
            progressBar.visibility = View.GONE
            showDialog("Error : ${e.message}")
        }
    }


    private fun showDialog(msg: String) {
        AlertDialog.Builder(this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Okay") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    private fun showMessage(msg: String) {
        Snackbar.make(
                findViewById(android.R.id.content),
                msg,
                Snackbar.LENGTH_LONG
        ).show()
    }
}