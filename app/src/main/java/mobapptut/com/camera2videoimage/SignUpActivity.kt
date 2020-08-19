package mobapptut.com.camera2videoimage


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = Firebase.auth

        signUpBtn.setOnClickListener {
            val emailStr = emailET.text.toString()
            val passStr = passwordET.text.toString()
            val cnfrmPassStr = confirmPassET.text.toString()
            if (emailStr.isEmpty() || passStr.isEmpty() || cnfrmPassStr.isEmpty())
                showMessage("Please provide all the details")
            else if (passStr.length < 8)
                showMessage("Use 8 characters or more for password")
            else if (passStr != cnfrmPassStr)
                showMessage("Both passwords didn't match. Try again")
            else
                signUpUser(emailStr, passStr)

        }
    }

    private fun signUpUser(emailStr: String, passStr: String) {
        val dialog = ProgressDialog(this)
        dialog.setTitle("Registering User...")
        dialog.setMessage("Please wait while we're creating your profile")
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        try {
            val authResult = mAuth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(this) { it ->
                if (it.isSuccessful) {
                    Intent(this@SignUpActivity, Camera2VideoImageActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                } else {
                    Toast.makeText(this, "Already Registered!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        } catch (e: Exception) {
            dialog.dismiss()
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
